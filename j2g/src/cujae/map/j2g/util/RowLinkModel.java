package cujae.map.j2g.util;

public interface RowLinkModel {
	
	public void addModelListener(RowLinkModelListener listener);
	
	public void removeModelListener(RowLinkModelListener listener);
	
	public RowLinkModelListener[] getListeners();
	
	public int getSize();
	
	public int getValue(int index);

}
