package cujae.map.j2g.edt;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cujae.map.j2g.parser.ThokenCollector;
import cujae.map.j2g.parser.impl.ParseException;
import cujae.map.j2g.parser.impl.TokenMgrError;

public class DefaultMoled extends AbstractMoled {
	
	public static boolean equals(Object oldValue, Object newValue){
		if(oldValue == null){
			return newValue == null;
		} else{
			return oldValue.equals(newValue);
		}
	}
	
	private class Node{
		
		private int flags;
		private String name;
		private String description;
		
		public final boolean isVariable;
		public final NodeEntry entry;

		public Node(int flags, String name, String description, boolean isVariable) {
			super();
			this.flags = flags;
			this.name = name;
			this.description = description;
			this.isVariable = isVariable;
			
			this.entry = new NodeEntry() {
				
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
				public boolean isVariable() {
					return Node.this.isVariable;
				}
			};
		}

		public int getFlags() {
			return flags;
		}

		public void setFlags(int flags) {
			this.flags = flags;
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
		
	}
	
	private List<Node> variables;
	private List<Node> relations;
	private List<List<Integer>> directions;

	public DefaultMoled(int variableInitialCapacity, int relationInitialCapacity) {
		variables = new ArrayList<DefaultMoled.Node>(variableInitialCapacity);
		relations = new ArrayList<DefaultMoled.Node>(relationInitialCapacity);
		directions = new ArrayList<List<Integer>>(variableInitialCapacity);
	}	

	public DefaultMoled() {
		this(10, 10);
	}

	@Override
	public int size(boolean isVariable) {
		return (isVariable ? variables : relations).size();
	}
	
	private int _indexOfByName(String name, boolean isVariable){
		int index = -1;
		
		for(int i = 0; i < size(isVariable); i ++){
			String otherName = get(i, isVariable).getName();
			if(otherName == null){
				if(name == null){
					index = i;
				} 
			} else{
				if(otherName.equals(name)){
					index = i;
				}
			}
		}
		
		return index;
	}
	
	private int _insert(int index, String name, String description,
			int flags, boolean isVariable){
		
		if(isVariable){
			int indexOf = _indexOfByName(name, isVariable);
			if(indexOf == -1){
				variables.add(index, new Node(flags, name, description, isVariable));
				directions.add(index, new ArrayList<Integer>());
				for(int i = 0; i < relations.size(); i ++){
					directions.get(index).add(null);
				}				
			}
			return indexOf;
			
		} else{
			relations.add(index, new Node(flags, name, description, isVariable));
			for(int i = 0; i < variables.size(); i ++){
				directions.get(index).add(i, null);
			}			
		}
		
		return index;
	}	

	@Override
	public void insert(int index, String name, String description,
			int flags, boolean isVariable) {
		if(!isVariable || index != _insert(index, name, description, flags, isVariable)){
			fireMoledChanged();
		}
	}
	
	private int _remove(int index, boolean isVariable){
		int count = 0;
		
		if(isVariable){
			if(edges(index, isVariable).length == 0){
				variables.remove(index);
				directions.remove(index);
				count |= 1;
			}
		} else{
			relations.remove(index);
			for(int i = 0; i < directions.size(); i ++){
				directions.get(i).remove(index);
			}
			count |= 1; 
		}
	
		return count;
	}
	
	private int _remove(int[] variableIndices, int[] relationIndices){
		
		int count = 0;
		
		if(relationIndices != null){
			Arrays.sort(relationIndices);
			for(int i = relationIndices.length - 1; i > -1; i--){
				count |= _remove(i, false);
			}
		}
		
		if(variableIndices != null){
			Arrays.sort(variableIndices);
			for(int i = variableIndices.length -1; i > -1; i --){
				count |=_remove(i, true);
			}
		}
		
		return count;

	}

	@Override
	public void remove(int[] variableIndices, int[] relationIndices) {
		if(_remove(variableIndices, relationIndices) != 0){
			if(isAutoSolve()){
				_solve();
			}
			_check();
			fireMoledChanged();
		}

	}

	@Override
	public NodeEntry get(int index, boolean isVariable) {
		return (isVariable ? variables : relations).get(index).entry;
	}

	@Override
	public Integer get(int variableIndex, int relationIndex) {
		return directions.get(variableIndex).get(relationIndex);
	}
	
	private int _set(EdgeEntry... edges){
		int count = 0;
		
		for(int i = 0; i < edges.length; i ++){
			EdgeEntry e = edges[i];
			Integer oldDirection = get(e.getVariableIndex(), e.getRelationIndex());
			if(oldDirection != null){
				int newDirection = e.getDirection();
				if(!equals(oldDirection, newDirection)){
					directions.get(e.getVariableIndex()).set(e.getRelationIndex(), newDirection);
					count |= 1;
				}
			}
		}
		
		return count;
	}

	@Override
	public void set(EdgeEntry... edges) {		
		if((_set(edges) | _solve() | _check()) != 0){
			fireMoledChanged();
		}
	}

	@Override
	public void setName(int index, String name, boolean isVariable) {
		Node n = (isVariable ? variables : relations).get(index);
		String oldName = n.getName();
		if(isVariable){
			// TODO Not implemented yet.
		} else{
			if(!equals(oldName, name)){
				n.setName(name);
				fireMoledChanged();
			}
		}

	}

	@Override
	public void setDescription(int index, String description, boolean isVariable) {
		Node n = (isVariable ? variables : relations).get(index);
		String oldDescription = n.getDescription();
		int count = 0;
		
		if(!equals(oldDescription, description)){
			n.setDescription(description);
			count |= 1;
		}		
		if(!isVariable){
			count |= _parse(index);
		}
		
		if(count != 0){
			if(!isVariable){
				if(isAutoSolve()){
					_solve();
				}
				_check();
			}
			fireMoledChanged();
		}
		
	}

	@Override
	public void setFlags(int index, int flags, boolean isVariable) {
		
		Node n = (isVariable ? variables : relations).get(index);
		int oldFlags = n.getFlags() & Flags.MASK;
		int errorFlags = oldFlags & ~Flags.MASK;
		int newFlags = flags & Flags.MASK;
		
		if(!equals(oldFlags, newFlags)){
			n.setFlags(newFlags | errorFlags);
			if(isAutoSolve()){
				_solve();
			}
			_check();
			fireMoledChanged();			
		}

	}
	
	private int _parse(int index){
		int count = 0;
		
		if(index != -1){
			Node n = relations.get(index);
			int flags = n.getFlags();
			flags = Flags.set(flags, Flags.ERROR31_FLAG);
			for(Iterator<List<Integer>> it = directions.iterator(); it.hasNext();){
				List<Integer> next = it.next();
				next.set(index, null);
				count |= 1;
			}
			
			String s = String.format("%s", n.getDescription());
			StringReader stream = new StringReader(s);
			
			try {
				ThokenCollector collector = getParser().parse(stream);
				
				switch(getRenamePolicy()){
				case TRY_RENAME_POLICY:
					break;
				case RENAME_IF_EMPTY_POLICY:
					break;
				case ALLWAYS_RENAME_POLICY:
					break;
				}
				
				Map<String, Integer> m = collector.getPostAnalysis(isLowestPriority());
				for(Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator(); it.hasNext();){
					Map.Entry<String, Integer> next = it.next();
					int variableIndex = _insert(0, next.getKey(), null, 0, true);
					directions.get(variableIndex).set(index, next.getValue());
					count |= 1;
				}
				
				flags = Flags.unset(flags, Flags.ERROR31_FLAG);
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (TokenMgrError e){
				e.printStackTrace();
			}
			
		} else{
			for(int i = 0; i < relations.size(); i ++){
				parse(i);
			}
		}
		
		return count;
	}

	@Override
	public void parse(int index) {
		int count = 0;
		
		count |= _parse(index);
		if(isAutoSolve()){
			count |= _solve();
		}
		count |= _check();
		
		if(count != 0){
			fireMoledChanged();
		}
	}	
	
	private int _solve(int index, boolean isVariable){
		
		Node n = (isVariable ? variables : relations).get(index);
		int flags = n.getFlags();
		Collection<EdgeEntry> lt0 = new ArrayList<EdgeEntry>();
		Collection<EdgeEntry> eq0 = new ArrayList<EdgeEntry>();
		Collection<EdgeEntry> gt0 = new ArrayList<EdgeEntry>();
		splitEdges(edges(index, isVariable), lt0, eq0, gt0);
		
		int count = 0;
		
		if(isVariable){
			if(Flags.isFreedomDegree(flags) || !gt0.isEmpty()){
				for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
					MutableEdgeEntry next = (MutableEdgeEntry) it.next();
					next.setDirection(-2);
					count |= _set(next);
				}
				
			}
			
		} else{
			if(!gt0.isEmpty()){
				for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
					MutableEdgeEntry next = (MutableEdgeEntry) it.next();
					count |= _set(next);
				}
			} else if(!lt0.isEmpty() && eq0.size() == 1){
				for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
					MutableEdgeEntry next = (MutableEdgeEntry) it.next();
					next.setDirection(-2);
					count |= _set(next);				
				}
			}
		}
		
		return count;
	}
	
	private int _solve(boolean isVariable){
		int count = 0;
		
		for(int i = 0; i < size(isVariable); i ++){
			count |= _solve(i, isVariable);
		}
		
		return count;
	}
	
	private int _solve(){
		
		/*
		 * CRITICAL
		 * 
		 * The algorithm MUST start on RELATIONS,
		 * otherwise bad behavior would be experimented.
		 * 
		 * Description:
		 * 
		 * Suppose that an unrelated variable 'a' exist with the
		 * FREEDOM_DEGREE_FLAG activated.
		 * 
		 * Then insert a new relation containing a variable 'a':
		 * a = abs(b). The parser will produce a:0, b:-1.
		 * 
		 * In this scenario the algorithm change all
		 * edges directions of 'a' to -2 because the 
		 * FREEDOM_DEGREE_FLAG is activated. And then it
		 * would try to adjust values from the relation.
		 * And because the algorithm only change edges
		 * with 0 value directions, -2 remains.
		 * 
		 * If the algorithm is executed on relations first
		 * the direction value will be 2 and that, is the 
		 * desired behavior.
		 *   
		 */	
		
		int i = 0;
		int j = 0;
		
		do{
			i = j;
			if(_solve(false) != 0){
				j ++;
			}
			if(_solve(true) != 0){
				j ++;
			}
			
		} while(i != j);
		
		return i;

	}

	@Override
	public void solve() {		
		if((_solve() | _check()) != 0){
			fireMoledChanged();
		}
	}
	
	private int _check(int index, boolean isVariable){
		int count = 0;
		
		Node n = (isVariable ? variables : relations).get(index);
		int oldFlags = n.getFlags();
		Collection<EdgeEntry> lt0 = new ArrayList<EdgeEntry>();
		Collection<EdgeEntry> eq0 = new ArrayList<EdgeEntry>();
		Collection<EdgeEntry> gt0 = new ArrayList<EdgeEntry>();
		int newFlags = check(oldFlags, isVariable, lt0, eq0, gt0);
		
		if(oldFlags != newFlags){
			n.setFlags(newFlags);
			count = 1;
		}
		
		return count;
	}
	
	private int _check(boolean isVariable){
		int count = 0;
		
		for(int i = 0; i < size(isVariable); i ++){
			count |= _check(i, isVariable);
		}
		
		return count;
	}
	
	private int _check(){
		return _check(true) | _check(false);
	}
	
	@Override
	public void check() {
		if(_check() != 0){
			fireMoledChanged();
		}
		
	}

	private int _clear(int index, boolean isVariable, boolean includeFlags){
		
		int count = 0;
		
		EdgeEntry[] a = edges(index, isVariable);
		
		for(int i = 0; i < a.length; i ++){
			MutableEdgeEntry e = (MutableEdgeEntry) a[i];
			e.setDirection(0);			
		}
		count |= _set(a);
		
		if(includeFlags){
			Node n = (isVariable ? variables : relations).get(index);
			int oldFlags = n.getFlags();
			if(oldFlags != 0){
				n.setFlags(0);
				count |= 1;
			}
		}
		
		return count;
	}
	
	private int _clear(boolean isVariable, boolean includeFlags){
		int count = 0;
		
		for(int i = 0; i < size(isVariable); i ++){
			count |= _clear(i, isVariable, includeFlags);
		}
		
		return count;
	}
	
	private int _clear(boolean includeFlags){
		return _clear(true, includeFlags) | _clear(false, includeFlags);
	}

	@Override
	public void clear(boolean includeFlags) {
		int count = 0;
		
		count |= _clear(includeFlags);
		
		if(isAutoSolve()){
			count |= _solve();
		}
		
		count |= _check();
		
		if(count != 0){
			fireMoledChanged();
		}
	}

	@Override
	public void swap(int i, int j, boolean isVariable) {
		// TODO Auto-generated method stub

	}

}
