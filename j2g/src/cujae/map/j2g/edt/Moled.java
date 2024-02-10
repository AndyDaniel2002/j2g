package cujae.map.j2g.edt;

import java.beans.PropertyChangeListener;

import cujae.map.j2g.parser.OneLineParser;

public interface Moled {	
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	
	public void removePropertyChangeListener(PropertyChangeListener listener);
	
	public void addMoledListener(MoledListener listener);
	
	public void removeMoledListener(MoledListener listener);
	
	
	public boolean isLowestPriority();
	
	public void setLowestPriority(boolean lowestPriority);
	
	public int getRenamePolicy();
	
	public void setRenamePolicy(int renamePolicy);
	
	public boolean isAutoSolve();
	
	public void setAutoSolve(boolean autoSolve);
	
	public OneLineParser getParser();
	
	public void setParser(OneLineParser parser);
	
	
	public int size(boolean isVariable);
	
	public int indexOf(Object o, boolean isVariable);
	
	public void insert(int index, String name, String description, int flags, boolean isVariable);
	
	public void remove(int[] variableIndices, int[] relationIndices);
	
	public NodeEntry get(int index, boolean isVariable);
	
	public NodeEntry[] nodes(boolean isVariable);
	
	public Integer get(int variableIndex, int relationIndex);	
	
	public EdgeEntry[] edges(int index, boolean isVariable);
	
	public void set(EdgeEntry... edges);

	
	public void setName(int index, String name, boolean isVariable);
	
	public void setDescription(int index, String description, boolean isVariable);
	
	public void setFlags(int index, int flags, boolean isVariable);	
	
	
	public void parse(int index);
	
	public void solve();
	
	public void check();
	
	public void clear(boolean includeFlags);
	
	public void swap(int i, int j, boolean isVariable);

}
