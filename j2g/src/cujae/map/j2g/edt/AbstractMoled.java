package cujae.map.j2g.edt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.EventListenerList;

import cujae.map.j2g.parser.OneLineParser;
import cujae.map.j2g.parser.OneLineParserFactory;

public abstract class AbstractMoled implements Moled {
	
	public static final String LOWEST_PRIORITY_PROPERTY = "lowestPriority";
	
	public static final String RENAME_POLICY_PROPERTY = "renamePolicy";
	
	public static final String AUTO_SOLVE_PROPERTY = "autoSolve";
	
	public static final String PARSER_PROPERTY = "parser";
	
	public static final int TRY_RENAME_POLICY = 1;
	
	public static final int RENAME_IF_EMPTY_POLICY = 2;
	
	public static final int ALLWAYS_RENAME_POLICY = 4;
	
	private static class UnmodifiableMoled implements Moled{
		
		private final Moled moled;

		public UnmodifiableMoled(Moled moled) {
			super();
			this.moled = moled;
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addMoledListener(MoledListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void removeMoledListener(MoledListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isLowestPriority() {
			return moled.isLowestPriority();
		}

		@Override
		public void setLowestPriority(boolean lowestPriority) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getRenamePolicy() {
			return moled.getRenamePolicy();
		}

		@Override
		public void setRenamePolicy(int renamePolicy) {
			throw new UnsupportedOperationException();
		}		

		@Override
		public boolean isAutoSolve() {
			return moled.isAutoSolve();
		}

		@Override
		public void setAutoSolve(boolean autoSolve) {
			throw new UnsupportedOperationException();
		}

		@Override
		public OneLineParser getParser() {
			return moled.getParser();
		}

		@Override
		public void setParser(OneLineParser parser) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size(boolean isVariable) {
			return moled.size(isVariable);
		}

		@Override
		public int indexOf(Object o, boolean isVariable) {
			return moled.indexOf(o, isVariable);
		}

		@Override
		public void insert(int index, String name, String description,
				int flags, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove(int[] variableIndices, int[] relationIndices) {
			throw new UnsupportedOperationException();
		}

		@Override
		public NodeEntry get(int index, boolean isVariable) {
			return moled.get(index, isVariable);
		}

		@Override
		public NodeEntry[] nodes(boolean isVariable) {
			return moled.nodes(isVariable);
		}

		@Override
		public Integer get(int variableIndex, int relationIndex) {
			return moled.get(variableIndex, relationIndex);
		}

		@Override
		public EdgeEntry[] edges(int index, boolean isVariable) {
			return moled.edges(index, isVariable);
		}

		@Override
		public void set(EdgeEntry... edges) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(int index, String name, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDescription(int index, String description,
				boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlags(int index, int flags, boolean isVariable) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void parse(int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void solve() {
			throw new UnsupportedOperationException();
		}		

		@Override
		public void check() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear(boolean includeFlags) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void swap(int i, int j, boolean isVariable) {
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
	
	public static int check(int flags, boolean isVariable, Collection<EdgeEntry> lt0, 
			Collection<EdgeEntry> eq0, Collection<EdgeEntry> gt0){
		
		int newFlags = flags;
		
		int size = lt0.size() + eq0.size() + gt0.size();
		
		if(isVariable){
			
			if(((Flags.isInput(flags) || Flags.isFreedomDegree(flags)) && !gt0.isEmpty()) || gt0.size() >= 2){
				newFlags = Flags.set(newFlags, Flags.ERROR23_FLAG);
			} else{
				newFlags = Flags.unset(newFlags, Flags.ERROR23_FLAG);
			}
			
			if(size == 0 ){
				newFlags = Flags.set(newFlags, Flags.ERROR15_FLAG);
			} else{
				newFlags = Flags.unset(newFlags, Flags.ERROR15_FLAG);
			}
			
		} else{
			if((eq0.isEmpty() && gt0.isEmpty() && !lt0.isEmpty())){
				newFlags = Flags.set(newFlags, Flags.ERROR23_FLAG);
			} else{
				newFlags = Flags.unset(newFlags, Flags.ERROR23_FLAG);
			}
			
			if(gt0.size() > 1 || size < 2 ||
					(!lt0.isEmpty()) && eq0.isEmpty() && gt0.isEmpty()){
				newFlags = Flags.set(newFlags, Flags.ERROR15_FLAG);
			} else{
				newFlags = Flags.unset(newFlags, Flags.ERROR15_FLAG);
			}
		}
		
		return newFlags;
	}
	
	private PropertyChangeSupport propertyChangeSupport;
	private EventListenerList eventListenerList;
	
	private boolean lowestPriority;
	private int renamePolicy;
	private boolean autoSolve;
	private OneLineParser parser;
	
	protected void fireMoledChanged(){
		MoledListener[] listener = eventListenerList.getListeners(MoledListener.class);
		for(int i = 0; i < listener.length; i ++){
			MoledEvent evt = new MoledEvent(unmodifiableMoled(this));
			listener[i].moledChanged(evt);
		}
	}

	public AbstractMoled() {
		propertyChangeSupport = new PropertyChangeSupport(unmodifiableMoled(this));
		eventListenerList = new EventListenerList();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void addMoledListener(MoledListener listener) {
		eventListenerList.add(MoledListener.class, listener);
	}

	@Override
	public void removeMoledListener(MoledListener listener) {
		eventListenerList.remove(MoledListener.class, listener);
	}

	@Override
	public boolean isLowestPriority() {
		return lowestPriority;
	}

	@Override
	public void setLowestPriority(boolean lowestPriority) {
		Object oldValue = this.lowestPriority;
		this.lowestPriority = lowestPriority;		
		propertyChangeSupport.firePropertyChange(LOWEST_PRIORITY_PROPERTY, oldValue, this.lowestPriority);
	}

	@Override
	public int getRenamePolicy() {
		return renamePolicy;
	}

	@Override
	public void setRenamePolicy(int renamePolicy) {
		Object oldValue = this.renamePolicy;
		this.renamePolicy = renamePolicy;
		propertyChangeSupport.firePropertyChange(RENAME_POLICY_PROPERTY, oldValue, this.renamePolicy);
	}	

	@Override
	public boolean isAutoSolve() {
		return autoSolve;
	}

	@Override
	public void setAutoSolve(boolean autoSolve) {
		Object oldValue = this.autoSolve;
		this.autoSolve = autoSolve;
		propertyChangeSupport.firePropertyChange(AUTO_SOLVE_PROPERTY, oldValue, this.autoSolve);
	}

	@Override
	public OneLineParser getParser() {
		if(parser == null){
			parser = OneLineParserFactory.DEFAULT_PARSER_FACTORY.newOneLineParser();
		}
		return parser;
	}

	@Override
	public void setParser(OneLineParser parser) {
		Object oldValue = this.parser;		
		this.parser = parser;
		propertyChangeSupport.firePropertyChange(PARSER_PROPERTY, oldValue, this.parser);		
	}

	@Override
	public int indexOf(Object o, boolean isVariable) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeEntry[] nodes(boolean isVariable) {
		NodeEntry[] a = new NodeEntry[size(isVariable)];
		for(int i = 0; i < a.length; i ++){
			a[i] = get(i, isVariable);
		}
		return a;
	}

	@Override
	public EdgeEntry[] edges(int index, boolean isVariable) {
		Collection<EdgeEntry> c = new ArrayList<EdgeEntry>();
		for(int i = 0; i < size(!isVariable); i ++){
			int variableIndex = isVariable ? index : i;
			int relationIndex = isVariable ? i : index;
			Integer direction = get(variableIndex, relationIndex);
			if(direction != null){
				c.add(new MutableEdgeEntry(variableIndex, relationIndex, direction));
			}
		}
		return c.toArray(new EdgeEntry[0]);
	}

}
