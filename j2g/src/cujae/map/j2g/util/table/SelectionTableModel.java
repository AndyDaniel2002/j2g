package cujae.map.j2g.util.table;

import javax.swing.table.AbstractTableModel;

public class SelectionTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8501137118419151951L;
	
	private static final Class<?>[] columnClasses = new Class<?>[]{
		Boolean.class, Object.class
	};	
	
	private final Object[] values;
	private final Boolean[] selected;

	public SelectionTableModel(Object[] values, Boolean[] selected) {
		super();
		this.values = values;
		this.selected = selected;
	}

	@Override
	public int getRowCount() {
		return Math.min(values.length, selected.length);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object valueAt = null;
		switch(columnIndex){
		case 0: valueAt = selected[rowIndex]; break;
		case 1: valueAt = values[rowIndex]; break;
		}
		return valueAt;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(columnIndex == 0){
			selected[rowIndex] = (Boolean) aValue;
		}
	}

}
