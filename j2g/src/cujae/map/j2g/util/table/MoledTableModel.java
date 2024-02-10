package cujae.map.j2g.util.table;

import javax.swing.table.AbstractTableModel;

import cujae.map.j2g.mme.Moled;
import cujae.map.j2g.mme.Moled.EdgeEntry;
import cujae.map.j2g.mme.Moled.NodeEntry;

public class MoledTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1107027699662612693L;
	
	private final String[] columnNames = new String[]{
			"Estado", "Nombre", "Descripción", "Referencias"
	};
	
	private final Class<?>[] columnClasses = new Class<?>[]{
			Integer.class, Object.class, Object.class, EdgeEntry[].class
	};
	
	private final Moled moled;
	private final boolean isVariable;

	public MoledTableModel(Moled moled, boolean isVariable) {
		super();
		this.moled = moled;
		this.isVariable = isVariable;
	}

	@Override
	public int getRowCount() {
		return moled.size(isVariable);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object valueAt = null;
		
		NodeEntry entry = moled.get(rowIndex, isVariable);
		
		switch(columnIndex){
		case 0: valueAt = entry.getFlags(); break;
		case 1: valueAt = entry.getName(); break;
		case 2: valueAt = entry.getDescription(); break;
		case 3: valueAt = moled.edges(rowIndex, isVariable); break;
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
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch(columnIndex){
		case 0: 
			moled.setFlags(rowIndex, (Integer) aValue, isVariable);
			break;
		case 1: 
			moled.setName(rowIndex, (String) aValue, isVariable);
			break;
		case 2: 
			moled.setDescription(rowIndex, (String) aValue, isVariable);
			if(!isVariable){
				moled.parse(rowIndex);
			}
			break;
		case 3:				
			moled.set(isVariable, (EdgeEntry[]) aValue);
			break;
		}
	}

}
