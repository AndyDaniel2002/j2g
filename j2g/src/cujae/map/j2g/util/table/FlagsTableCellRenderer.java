package cujae.map.j2g.util.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import cujae.map.j2g.dgraphm.util.Flags;
import cujae.map.j2g.util.IconFactory;

public class FlagsTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2134101858989191697L;
	
	private final boolean isVariable;

	public FlagsTableCellRenderer(boolean isVariable) {
		this.isVariable = isVariable;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {	
		
		Integer flags = (Integer)value;
		
		setIcon(IconFactory.getBlankIcon());
		
//		if(Flags.isWarning(flags)){
//			setIcon(IconFactory.getWarningIcon());
//		} 
		
//		if(Flags.isError(flags)){
//			setIcon(IconFactory.getErrorIcon());
//		} 
		
//		if(Flags.isWarningAndError(flags)){
//			setIcon(IconFactory.getErrorWarningIcon());
//		} 
		
		if(Flags.isError0(flags)){
//			System.out.println("error0");
			setIcon(IconFactory.getWarningIcon());
		}
		
		if(Flags.isError1(flags)){
//			System.out.println("error1");
			setIcon(IconFactory.getErrorWarningIcon());
		}
		
		if(Flags.isError2(flags)){
//			System.out.println("error2");
			setIcon(IconFactory.getErrorIcon());
		}
		
		StringBuffer buffer = new StringBuffer();
		
		if(isVariable){
			if(Flags.isInput(flags)){
				buffer.append("[E]");
			}
				
			if(Flags.isOutput(flags)){
				buffer.append("[S]");
			}
				
			if(Flags.isStateVariable(flags)){
				buffer.append("[Ve]");
			}
				
			if(Flags.isControlUnknown(flags)){
				buffer.append("[Y]");
			}
				
			if(Flags.isParametricVariable(flags)){
				buffer.append("[Vp]");
			}
				
			if(Flags.isDesignParameter(flags)){
				buffer.append("[Z]");
			}
				
			if(Flags.isFreedomDegree(flags)){
				buffer.append("[L]");
			}
		} else{
			if(Flags.isDeficience(flags)){
				buffer.append("[D]");
			}
		}	
		
		return super.getTableCellRendererComponent(table, String.format("%s", buffer), 
				isSelected, hasFocus, row, column);
	}

}
