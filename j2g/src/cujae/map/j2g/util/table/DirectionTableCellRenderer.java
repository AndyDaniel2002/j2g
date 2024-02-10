package cujae.map.j2g.util.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DirectionTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2879947700050935417L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		String s = "";
		switch(Integer.signum((Integer) value)){
		case -1: s = "entrada"; break;
		case  0: s = "no"; break;
		case  1: s = "salida"; break;
		}
		
		return super.getTableCellRendererComponent(table, s, isSelected, hasFocus,
				row, column);
	}

}
