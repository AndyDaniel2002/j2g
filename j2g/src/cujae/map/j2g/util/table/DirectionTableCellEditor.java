package cujae.map.j2g.util.table;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class DirectionTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3010683249974544835L;
	
	
	private final JComboBox<?> delegate = new JComboBox(new Object[]{
			"entrada", "no", "salida"
	});
	
	private final ItemListener itemListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED){
				stopCellEditing();
			} 
		}
	};

	public DirectionTableCellEditor() {
		delegate.setEditable(false);
		delegate.addItemListener(itemListener);
	}

	@Override
	public Object getCellEditorValue() {
		Object cellEditorValue = null;
		switch(delegate.getSelectedIndex()){
		case 0: cellEditorValue = -2; break;
		case 1: cellEditorValue =  0; break;
		case 2: cellEditorValue =  2; break;
		}
		return cellEditorValue;
	}
	
	public void setCellEditorValue(Object value){
		int direction = (Integer) value;
		switch(Integer.signum(direction)){
		case -1: delegate.setSelectedIndex(0); break;
		case  0: delegate.setSelectedIndex(1); break;
		case  1: delegate.setSelectedIndex(2); break;
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		setCellEditorValue(value);
//		table.setRowHeight(delegate.getPreferredSize().height);
		return delegate;
	}

}
