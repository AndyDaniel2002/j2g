package cujae.map.j2g.mme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cujae.map.j2g.dgraphm.util.Directions;
import cujae.map.j2g.dgraphm.util.Flags;
import cujae.map.j2g.parser.OneLineParser;
import cujae.map.j2g.parser.OneLineParserFactory;

public abstract class Moled {
	
	public static final int TRY_RENAME_POLICY = 0;
	
	public static final int FORCE_RENAME = 1;
	
	public static interface NodeEntry{
		
		public int getFlags();
		
		public String getName();
		
		public String getDescription();
		
		public boolean isVariable();
		
		public String getWarningMsg();
		
		public String getErrorMsg();
		
	}
	
	public static interface EdgeEntry{
		
		public int getVariableIndex();
		
		public int getRelationIndex();
		
		public int getDirection();
		
		public void setDirection(int direction);
		
	}
	
	public static class MutableEdgeEntry implements EdgeEntry{
		
		private final int variableIndex;
		private final int relationIndex;
		private int direction;

		public MutableEdgeEntry(int variableIndex, int relationIndex,
				int direction) {
			super();
			this.variableIndex = variableIndex;
			this.relationIndex = relationIndex;
			this.direction = direction;
		}

		@Override
		public int getVariableIndex() {
			return variableIndex;
		}

		@Override
		public int getRelationIndex() {
			return relationIndex;
		}

		@Override
		public int getDirection() {
			return direction;
		}

		@Override
		public void setDirection(int direction) {
			if(Directions.isChangeable(this.direction, direction)){
				this.direction = direction;
			}			
		}
		
	}
	
	public static class InmutableEdgeEntry extends MutableEdgeEntry{

		public InmutableEdgeEntry(int variableIndex, int relationIndex, int direction) {
			super(variableIndex, relationIndex, direction);
		}

		@Override
		public void setDirection(int direction) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private static class UnmodifiableMoled extends Moled{
		
		private final Moled moled;

		public UnmodifiableMoled(Moled moled) {
			super();
			this.moled = moled;
		}

		@Override
		public void setParser(OneLineParser parser) {
			throw new UnsupportedOperationException();
		}


		@Override
		public void setRenamePolicy(int renamePolicy) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size(boolean isVariable) {
			return moled.size(isVariable);
		}

		@Override
		public void insert(int index, String name, String description,
				int flags, boolean isVariable) {
			throw new UnsupportedOperationException();
			
		}

		@Override
		public boolean remove(int index, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean set(int variableIndex, int relationIndex, int direction) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Integer get(int variableIndex, int relationIndex) {
			return moled.get(variableIndex, relationIndex);
		}

		@Override
		public NodeEntry get(int index, boolean isVariable) {
			return moled.get(index, isVariable);
		}

		@Override
		public boolean setName(int index, String name, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean setDescription(int index, String description,
				boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean setFlags(int index, int flags , boolean isVariable) {
			throw new UnsupportedOperationException();
		}	
		

		@Override
		public boolean swap(int i, int j, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void parse(int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean solve(boolean checkOnly) {
			throw new UnsupportedOperationException();
		}			
		
	}
	
	public static Moled unmodifiableMoled(Moled moled){
		return new UnmodifiableMoled(moled);
	}
	
	public static void splitEdges(EdgeEntry[] entries, Collection<EdgeEntry> lt0, 
			Collection<EdgeEntry> eq0, Collection<EdgeEntry> gt0){
		for(int i = 0; i < entries.length; i ++){
			EdgeEntry entry = entries[i];
			switch(Integer.signum(entry.getDirection())){
			case -1: if(lt0 != null) lt0.add(entry); break;
			case  0: if(eq0 != null) eq0.add(entry); break;
			case  1: if(gt0 != null) gt0.add(entry); break;
			}
		}
	}

	private OneLineParser parser;	
	private boolean lowestSignumPriority;
	private int renamePolicy;	

	public OneLineParser getParser(){
		if(parser == null){
			parser = OneLineParserFactory.DEFAULT_PARSER_FACTORY.newOneLineParser();
		}
		return parser;
	}
	
	public void setParser(OneLineParser parser){
		this.parser = parser;
	}	

	public boolean isLowestSignumPriority() {
		return lowestSignumPriority;
	}

	public void setLowestSignumPriority(boolean lowestSignumPriority) {
		this.lowestSignumPriority = lowestSignumPriority;
	}

	public int getRenamePolicy() {
		return renamePolicy;
	}

	public void setRenamePolicy(int renamePolicy) {
		this.renamePolicy = renamePolicy;
	}

	public abstract int size(boolean isVariable);
	
	public int indexOf(String name, boolean isVariable){
		int index = -1;
		for(int i = 0; index == -1 && i < size(isVariable); i ++){
			String getName = get(i, isVariable).getName();
			if(name == null){
				if(getName == null){
					index = i;
				}
			} else{
				if(name.equals(getName)){
					index = i;
				}
			}
		}
		return index;
	}
	
	public abstract void insert(int index, String name, String description, int flags, boolean isVariable);
	
	public int insert(String name, String description, int flags, boolean isVariable){
		insert(size(isVariable), name, description, flags, isVariable);
		return size(isVariable) - 1;
	}
	
	public abstract boolean remove(int index, boolean isVariable);
	
	public boolean remove(boolean isVariable, int... indices){
		int count = 0;
		int[] _indices = Arrays.copyOf(indices, indices.length);
		Arrays.sort(_indices);
		int i = _indices.length - 1;
		for(; i > -1; i --){
			if(remove(_indices[i], isVariable)){
				count ++;
			}
		}
		return count != 0;
	}
	
	public boolean remove(int[] variableIndices, int[] relationIndices){
		int count = 0;
		if(remove(false, relationIndices)){
			count ++;
		}
		if(remove(true, variableIndices)){
			count ++;
		}
		return count != 0;
	}
	
	public abstract boolean set(int variableIndex, int relationIndex, int direction);
	
	public boolean set(boolean isVariable, EdgeEntry... edges){
		int count = 0;
		for(int i = 0; i < edges.length; i ++){
			EdgeEntry e = edges[i];
			if(set(e.getVariableIndex(), e.getRelationIndex(), e.getDirection())){
				count ++;
			}
		}
		return count != 0;
	}
	
	public abstract Integer get(int variableIndex, int relationIndex);
	
	public abstract NodeEntry get(int index, boolean isVariable);
	
	public NodeEntry[] nodes(boolean isVariable){
		NodeEntry[] arr = new NodeEntry[size(isVariable)];
		for(int i = 0; i < size(isVariable); i ++){
			arr[i] = get(i, isVariable);
		}
		return arr;
	}
	
	public EdgeEntry[] edges(int index, boolean isVariable){
		List<EdgeEntry> l = new ArrayList<Moled.EdgeEntry>();
		for(int i = 0; i < size(!isVariable); i ++){
			int variableIndex = isVariable ? index : i;
			int relationIndex = isVariable ? i : index;
			Integer direction = get(variableIndex, relationIndex);
			if(direction != null){
				l.add(new InmutableEdgeEntry(variableIndex, relationIndex, direction));
			}
		}
		
		return l.toArray(new EdgeEntry[0]);
	}
	
	public abstract boolean setName(int index, String name, boolean isVariable);
	
	public abstract boolean setDescription(int index, String description, boolean isVariable);
	
	public abstract boolean setFlags(int index, int flags, boolean isVariable);
	
	public boolean setFlags(int flags, boolean isVariable, int... indices){
		int count = 0;
		for(int i = 0; i < indices.length; i ++){
			if(setFlags(indices[i], flags, isVariable)){
				count ++;
			}
		}
		return count != 0;
	}
	
	public abstract void parse(int index);
	
	public boolean solve(boolean checkOnly){
		int count = 0;
		int prevCount = 0;
		
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
		 * FREEDOM_DEGREE_FLAG is activated. And then
		 * would try to adjust values from the relation.
		 * And because the algorithm only change edges
		 * with 0 value directions, -2 remains.
		 * 
		 * If the algorithm is executed on relations first
		 * the direction value will be 2 and that is the 
		 * desired behavior.
		 *  
		 * 
		 */
		
		do{
			prevCount = count;
			if(solve(false, checkOnly)){
				count ++;
			}
			
			if(solve(true, checkOnly)){
				count ++;
			}
			
		} while(count != prevCount);
		
		return count != 0;
	}
	
	private boolean solve(int index, boolean isVariable, boolean checkOnly){
		
		NodeEntry n = get(index, isVariable);
		int flags = n.getFlags();		
		Collection<EdgeEntry> lt0 = new ArrayList<Moled.EdgeEntry>();
		Collection<EdgeEntry> eq0 = new ArrayList<Moled.EdgeEntry>();
		Collection<EdgeEntry> gt0 = new ArrayList<Moled.EdgeEntry>();
		EdgeEntry[] edges = edges(index, isVariable);
		splitEdges(edges, lt0, eq0, gt0);
		int size = lt0.size() + eq0.size() + gt0.size();
		int count = 0;
		
		if(isVariable){
			
			/*
			 * TRANSFORM
			 */
			if(!checkOnly){
				if(Flags.isFreedomDegree(flags) || !gt0.isEmpty()){
					for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
						EdgeEntry next = it.next();
						if(set(next.getVariableIndex(), next.getRelationIndex(), -2)){
							count ++;
						}
					}
				}
			}
			
			/*
			 * CHECK
			 */
			
			if(((Flags.isInput(flags) || Flags.isFreedomDegree(flags)) && !gt0.isEmpty()) || gt0.size() >= 2){
//				flags = Flags.setError(flags);	
				flags = Flags.setError1(flags);
			} else{
//				flags = Flags.unsetError(flags);
				flags = Flags.unsetError1(flags);
			}
			
			if(size == 0 ){
//				flags = Flags.setWarning(flags);
				flags = Flags.setError0(flags);
			} else{
//				flags = Flags.unsetWarning(flags);
				flags = Flags.unsetError0(flags);
			}		
			
			
		} else{
			
			/*
			 * TRANSFORM
			 */
			if(!checkOnly){
				if(!gt0.isEmpty()){
					for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
						EdgeEntry next = it.next();
						if(set(next.getVariableIndex(), next.getRelationIndex(), -2)){
							count ++;
						}
					}
				} else if(!lt0.isEmpty() && eq0.size() == 1){
					for(Iterator<EdgeEntry> it = eq0.iterator(); it.hasNext();){
						EdgeEntry next = it.next();
						if(set(next.getVariableIndex(), next.getRelationIndex(), 2)){
							count ++;
						}					
					}
				}
			}
			
			/*
			 * CHECK
			 */
			
			if((eq0.isEmpty() && gt0.isEmpty() && !lt0.isEmpty())){
//				flags = Flags.setError(flags);
				flags = Flags.setError1(flags);				
			} else{
//				flags = Flags.unsetError(flags);
				flags = Flags.unsetError1(flags);
			}
			
			if(gt0.size() > 1 || size < 2 ||
					(!lt0.isEmpty()) && eq0.isEmpty() && gt0.isEmpty()){
//				flags = Flags.setWarning(flags);
				flags = Flags.setError0(flags);
			} else{
//				flags = Flags.unsetWarning(flags);
				flags = Flags.unsetError0(flags);
			}			
			

		}		
		
		if(setFlags(index, flags, isVariable)){
			count ++;
		}
		
		return count != 0;
	}
	
	private boolean solve(boolean isVariable, boolean checkOnly){
		int count = 0;
		for(int i = 0; i < size(isVariable); i ++){
			if(solve(i, isVariable, checkOnly)){
				count ++;
			}
		}		
		return count != 0;
	}
	
	public abstract boolean swap(int i, int j, boolean isVariable);

}
