package cujae.map.j2g.util.table;

import javax.swing.table.AbstractTableModel;

import cujae.map.j2g.dgraphm.util.Directions;
import cujae.map.j2g.mme.Moled.EdgeEntry;

public class EdgesTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022105695883040335L;
	
	public static final String[] columnNames = new String[]{
		"Nombre", "Dirección"		
	};
	
	public static final Class<?>[] columnClasses = new Class<?>[]{
		String.class, Integer.class
	};
	
	private final String[] names;
	private final EdgeEntry[] edges;
	
	public EdgesTableModel(String[] names, EdgeEntry[] edges) {
		super();
		this.names = names;
		this.edges = edges;
	}

	@Override
	public int getRowCount() {
		return Math.min(names.length, edges.length);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object valueAt = null;
		switch(columnIndex){
		case 0: valueAt = names[rowIndex]; break;
		case 1: valueAt = edges[rowIndex].getDirection(); break;
		}
		return valueAt;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1 && Directions.isChangeable(edges[rowIndex].getDirection(), 0);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		edges[rowIndex].setDirection((Integer) aValue);
	}

}
