package cujae.map.j2g.mme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cujae.map.j2g.dgraphm.util.Directions;
import cujae.map.j2g.dgraphm.util.Flags;
import cujae.map.j2g.parser.ThokenCollector;
import cujae.map.j2g.parser.impl.ParseException;
import cujae.map.j2g.parser.impl.TokenMgrError;

public class MoledImpl extends Moled {
	
	private class Node{
		
		private String name;
		private String description;
		
		private int flags;
		
		private String warningMsg;
		private String errorMsg;
		
		public final boolean isVariable;
		
		public final NodeEntry entry;

		public Node(String name, String description, int flags,
				boolean isVariable) {
			super();
			this.name = name;
			this.description = description;
			this.flags = flags;
			this.isVariable = isVariable;
			this.entry = new NodeEntry() {
				
				@Override
				public boolean isVariable() {
					return Node.this.isVariable;
				}
				
				@Override
				public String getName() {
					return Node.this.getName();
				}
				
				@Override
				public int getFlags() {
					return Node.this.getFlags();
				}
				
				@Override
				public String getDescription() {
					return Node.this.getDescription();
				}

				@Override
				public String toString() {
					return Node.this.getName();
				}

				@Override
				public String getWarningMsg() {
					return Node.this.getWarningMsg();
				}

				@Override
				public String getErrorMsg() {
					return Node.this.getErrorMsg();
				}
			};
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getFlags() {
			return flags;
		}

		public void setFlags(int flags) {
			this.flags = flags;
		}

		public String getWarningMsg() {
			return warningMsg;
		}

		public void setWarningMsg(String warningMsg) {
			this.warningMsg = warningMsg;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}
	}
	
	private List<Node> variables;
	private List<Node> relations;
	
	private List<List<Integer>> directions;

	public MoledImpl(int variableCapacity, int relationCapacity) {
		super();
		this.variables = new ArrayList<MoledImpl.Node>(variableCapacity);
		this.relations = new ArrayList<MoledImpl.Node>(relationCapacity);
		this.directions = new ArrayList<List<Integer>>(variableCapacity);
	}

	public MoledImpl() {
		this(10, 10);
	}

	@Override
	public int size(boolean isVariable) {
		return (isVariable ? variables : relations).size();
	}
	
	private int insert(int index, String name, String description, int flags, 
			boolean replace, boolean isVariable){
		if(isVariable){
			int indexOf = indexOf(name, isVariable);
			if(indexOf == -1){
				variables.add(index, new Node(name, description, flags, isVariable));
				directions.add(index, new ArrayList<Integer>(relations.size()));
				for(int i = 0; i < relations.size(); i ++){
					directions.get(index).add(null);
				}
			} else{
				if(replace){
					Node n = variables.get(indexOf);
					n.setDescription(description);
					n.setFlags(flags);
				}
				return indexOf;
			}
		} else{
			relations.add(index, new Node(name, description, flags, isVariable));
			for(int i = 0; i < variables.size(); i ++){
				directions.get(i).add(index, null);
			}
		}
		return index;
	}

	@Override
	public void insert(int index, String name, String description, int flags,
			boolean isVariable) {
		insert(index, name, description, flags, true, isVariable);		
	}

	@Override
	public boolean remove(int index, boolean isVariable) {
		if(isVariable){
			if(edges(index, isVariable).length == 0){
				variables.remove(index);
				directions.remove(index);
			} else{
				return false;
			}
		} else{
			relations.remove(index);
			for(int i = 0; i < variables.size(); i ++){
				directions.get(i).remove(index);
			}
			
		}
		return true;
	}

	@Override
	public boolean set(int variableIndex, int relationIndex, int direction) {
		Integer oldDirection = directions.get(variableIndex).get(relationIndex);
		if(Directions.isChangeable(oldDirection, direction)){
			directions.get(variableIndex).set(relationIndex, direction);
			return true;
		}
		return false;
	}

	@Override
	public Integer get(int variableIndex, int relationIndex) {
		return directions.get(variableIndex).get(relationIndex);
	}

	@Override
	public NodeEntry get(int index, boolean isVariable) {
		return (isVariable ? variables : relations).get(index).entry;
	}

	@Override
	public boolean setName(int index, String name, boolean isVariable) {
		if(isVariable){
			//TODO
		} else{
			relations.get(index).setName(name);
		}
		return true;
	}

	@Override
	public boolean setDescription(int index, String description,
			boolean isVariable) {
		(isVariable ? variables : relations).get(index).setDescription(description);
		return true;
	}
	
	@Override
	public boolean setFlags(int index, int flags, boolean isVariable){
		int oldFlags = get(index, isVariable).getFlags();
		if(flags != oldFlags){
			(isVariable ? variables : relations).get(index).setFlags(flags);
			return true;
		}
		return false;
	}

	@Override
	public boolean swap(int i, int j, boolean isVariable) {
		// TODO Auto-generated method stub		
		return false;
	}

	@Override
	public void parse(int index) {
		if(Integer.signum(index) != -1){
			Node n = relations.get(index);			
			int flags = n.getFlags();
			n.setFlags(Flags.setError2(flags));
			for(int i = 0; i < variables.size(); i ++){
				directions.get(i).set(index, null);
			}	
			StringReader stream = new StringReader(String.format("%s", n.getDescription()));
			try {
				ThokenCollector c = getParser().parse(stream);
				
				switch(getRenamePolicy()){
				case TRY_RENAME_POLICY:
					if(c.getName() != null && !c.getName().isEmpty()){
						n.setName(c.getName());
					}
					break;
				case FORCE_RENAME:
					n.setName(c.getName());
					break;
				}
				
				Map<String, Integer> m = c.getPostAnalysis(isLowestSignumPriority());
				
				for(Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator(); it.hasNext();){
					Map.Entry<String, Integer> next = it.next();
					int variableIndex = insert(size(true), next.getKey(), null, 0, false, true);
					directions.get(variableIndex).set(index, next.getValue());
				}
				
				n.setFlags(Flags.unsetError2(flags));
				
			} catch (ParseException e) {
//				n.setFlags(Flags.setError2(flags));
				n.setErrorMsg(e.getMessage());
//				e.printStackTrace();				
				
			} catch(TokenMgrError e){
//				n.setFlags(Flags.setError2(flags));
				n.setErrorMsg(e.getMessage());
//				e.printStackTrace();				
			}
		} else{
			for(int i = 0; i < relations.size(); i ++){
				parse(i);
			}
		}
		
	}
	
}
