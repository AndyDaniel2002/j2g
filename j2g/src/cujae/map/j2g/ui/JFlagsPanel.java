package cujae.map.j2g.ui;

import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cujae.map.j2g.dgraphm.util.Flags;

public class JFlagsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1978672354238415895L;
	
	private final JCheckBox inputCheckBox = new JCheckBox("Variable de Entrada [E]");
	private final JCheckBox outputCheckBox = new JCheckBox("Variable de Salida [S]");
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private final JRadioButton stateVariableRadioButton = new JRadioButton("Variable de Estado [Ve]");
	private final JRadioButton controlUnknownRadioButton = new JRadioButton("Inc\u00f3gnita de Control [Y]");
	private final JRadioButton parametricVariableRadioButton = new JRadioButton("Variable Param\u00e9trica [Vp]");
	private final JRadioButton designParameterRadioButton = new JRadioButton("Par\u00e1metro de Dise\u00f1o [Z]");
	
	private final JCheckBox freedomDegreeCheckBox = new JCheckBox("Grado de Libertad [L]");
	private final JCheckBox deficienceCheckBox = new JCheckBox("Deficiencia [D]");
	
	public final boolean isVariable;
	
	private void initComponents(){
		
		inputCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					outputCheckBox.setSelected(false);
				}
			}
		});
		
		outputCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					inputCheckBox.setSelected(false);
				} 
			}
		});
		
		buttonGroup.add(stateVariableRadioButton);
		buttonGroup.add(controlUnknownRadioButton);
		buttonGroup.add(parametricVariableRadioButton);
		buttonGroup.add(designParameterRadioButton);
		
		if(isVariable){
			add(inputCheckBox);
			add(outputCheckBox);
			add(stateVariableRadioButton);
			add(controlUnknownRadioButton);
			add(parametricVariableRadioButton);
			add(designParameterRadioButton);
			add(freedomDegreeCheckBox);
		} else{
			add(deficienceCheckBox);
		}
		
	}
	
	private void resetComponents(int flags){
		
		inputCheckBox.setSelected(Flags.isInput(flags));
		outputCheckBox.setSelected(Flags.isOutput(flags));
		freedomDegreeCheckBox.setSelected(Flags.isFreedomDegree(flags));
		deficienceCheckBox.setSelected(Flags.isDeficience(flags));
		
		buttonGroup.clearSelection();
		
		if(Flags.isStateVariable(flags)){
			stateVariableRadioButton.doClick();
		}		
		if(Flags.isControlUnknown(flags)){
			controlUnknownRadioButton.doClick();
		}		
		if(Flags.isParametricVariable(flags)){
			parametricVariableRadioButton.doClick();
		}		
		if(Flags.isDesignParameter(flags)){
			designParameterRadioButton.doClick();
		}

	}

	public JFlagsPanel(int flags, boolean isVariable) {
		this.isVariable = isVariable;
		initComponents();		
		resetComponents(flags);
	}

	public JFlagsPanel(int flags, boolean isVariable, LayoutManager layout) {
		super(layout);
		this.isVariable = isVariable;
		initComponents();		
		resetComponents(flags);
	}

	public JFlagsPanel(int flags, boolean isVariable, boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		this.isVariable = isVariable;
		initComponents();		
		resetComponents(flags);
	}

	public JFlagsPanel(int flags, boolean isVariable, LayoutManager layout, 
			boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		this.isVariable = isVariable;
		initComponents();		
		resetComponents(flags);
	}

	public int getFlags(){
		
		int flags = 0;
		
		if(inputCheckBox.isSelected()){
			flags = Flags.setInput(flags);
		} else if(outputCheckBox.isSelected()){
			flags = Flags.setOutput(flags);
		} else{
			flags = Flags.clearInputOutput(flags);			
		}
		
		if(stateVariableRadioButton.isSelected()){
			flags = Flags.setStateVariable(flags);
		}
		
		if(controlUnknownRadioButton.isSelected()){
			flags = Flags.setControlUnknown(flags);
		}
		
		if(parametricVariableRadioButton.isSelected()){
			flags = Flags.setParametricVariable(flags);
		}
		
		if(designParameterRadioButton.isSelected()){
			flags = Flags.setDesignParameter(flags);
		}
		
		flags = freedomDegreeCheckBox.isSelected() 
				? Flags.setFreedomDegree(flags) : Flags.unsetFreedomDegree(flags);
		flags = deficienceCheckBox.isSelected() 
				? Flags.setDeficience(flags) : Flags.unsetDeficience(flags);
		
		return flags;
	}

}
