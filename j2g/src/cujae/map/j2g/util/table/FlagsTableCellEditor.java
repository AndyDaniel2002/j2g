package cujae.map.j2g.util.table;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import cujae.map.j2g.ui.JFlagsPanel;

public class FlagsTableCellEditor extends ButtonTableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7721044689508627152L;
	
	private final Component parentComponent;
	private final boolean isVariable;

	public FlagsTableCellEditor(Component parentComponent, boolean isVariable) {
		super();
		this.parentComponent = parentComponent;
		this.isVariable = isVariable;	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		JFlagsPanel message = new JFlagsPanel((Integer) getCellEditorValue(), 
				isVariable);
		message.setBorder(new EtchedBorder());
		BoxLayout boxLayout = new BoxLayout(message, BoxLayout.Y_AXIS);
		message.setLayout(boxLayout);
		
		if(JOptionPane.showConfirmDialog(parentComponent, message, 
				"Seleccionar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
				== JOptionPane.OK_OPTION){
			setCellEditorValue(message.getFlags());
			stopCellEditing();
		} else{
			cancelCellEditing();
		}

	}

}
