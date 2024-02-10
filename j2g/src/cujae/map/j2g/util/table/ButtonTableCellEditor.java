package cujae.map.j2g.util.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public abstract class ButtonTableCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -932022506440003588L;
	
	private JButton delegate;	
	
	private Object cellEditorValue;
	
	private int row;
	private int column;
	private boolean selected;


	public ButtonTableCellEditor() {
		super();
		
		delegate = new JButton("...");
		delegate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonTableCellEditor.this.actionPerformed(e);
			}
		});
	}

	@Override
	public Object getCellEditorValue() {
		return cellEditorValue;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setCellEditorValue(Object cellEditorValue) {
		this.cellEditorValue = cellEditorValue;
	}

	@Override
	public boolean isCellEditable(EventObject e) {		
		if(e instanceof MouseEvent){
			MouseEvent e1 = (MouseEvent)e;
			return e1.getClickCount() != 1;
		}		
		return super.isCellEditable(e);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.cellEditorValue = value;
		this.row = row;
		this.column = column;
		this.selected = isSelected;
		
		return delegate;
	}
	
	public abstract void actionPerformed(ActionEvent e);

}
