package cujae.map.j2g.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Graphics;

public class JInsertPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3248356542530779969L;
	
	private final ComboBoxModel variablesModel;
	private final ComboBoxModel relationsModel;
	private final ComboBoxModel emptyModel = new DefaultComboBoxModel();
	
	private final JToggleButton variableToggleButton = new JToggleButton("Variable", true);
	private final JToggleButton relationToggleButton = new JToggleButton("Relaci\u00f3n");
	
	private final JTextField nameTextField = new JTextField();
	private final JTextField descriptionTextField = new JTextField();
	
	public final TitledBorder nameTitledBorder = new TitledBorder("Nombre");
	public final TitledBorder descriptionTitledBorder = new TitledBorder("Descripci\u00f3n");
	
	private final JRadioButton beginingRadioButton = new JRadioButton("Al principio");
	private final JRadioButton beforeRadioButton = new JRadioButton("Antes");
	private final JComboBox jComboBox = new JComboBox(emptyModel);
	private final JRadioButton afterRadioButton = new JRadioButton("Despu\u00e9s");
	private final JRadioButton endRadioButton = new JRadioButton("Al final", true);
	
	private final JPanel selectPanel = new JPanel();
	private final JPanel dataPanel = new JPanel();
	private final JPanel optionsPanel = new JPanel();
	
	private final ItemListener listener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(beginingRadioButton.isSelected() || endRadioButton.isSelected()){
				jComboBox.setModel(emptyModel);
			} else{
				if(variableToggleButton.isSelected()){
					jComboBox.setModel(variablesModel);
				}
				
				if(relationToggleButton.isSelected()){
					jComboBox.setModel(relationsModel);
				}
			}
			
		}
	};
	private final JTextField rangeTextField = new JTextField();

	public JInsertPanel(Object[] variables, Object[] relations) {
		super();
		rangeTextField.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Rango", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Rango", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		variablesModel = new DefaultComboBoxModel(variables);
		relationsModel = new DefaultComboBoxModel(relations);
		
		variableToggleButton.addItemListener(listener);
//Mostrar u ocultar el rangeTextField
		relationToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rangeTextField.setVisible(false);
			}
		});
		variableToggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rangeTextField.setVisible(true);
			}
		});
		beginingRadioButton.addItemListener(listener);
		beforeRadioButton.addItemListener(listener);
		afterRadioButton.addItemListener(listener);
		endRadioButton.addItemListener(listener);
		
		ButtonGroup selectGroup = new ButtonGroup();
		selectGroup.add(variableToggleButton);
		selectGroup.add(relationToggleButton);
		
		ButtonGroup optionsGroup = new ButtonGroup();
		optionsGroup.add(beginingRadioButton);
		optionsGroup.add(beforeRadioButton);
		optionsGroup.add(afterRadioButton);
		optionsGroup.add(endRadioButton);
		
		selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.X_AXIS));
		selectPanel.add(variableToggleButton);
		selectPanel.add(relationToggleButton);
		
		nameTextField.setBorder(nameTitledBorder);
		descriptionTextField.setBorder(descriptionTitledBorder);
		
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.add(nameTextField);
		dataPanel.add(descriptionTextField);
		
		optionsPanel.setLayout(new GridLayout(5, 1));
		optionsPanel.add(beginingRadioButton);
		optionsPanel.add(beforeRadioButton);
		optionsPanel.add(jComboBox);
		optionsPanel.add(afterRadioButton);
		optionsPanel.add(endRadioButton);
		
		setBorder(new EtchedBorder());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(selectPanel);
		add(dataPanel);
		
		dataPanel.add(rangeTextField);
		add(optionsPanel);
	}
	
	public int getIndex(){
		
		int index = isVariable() ? variablesModel.getSize() : relationsModel.getSize();
		
		if(beforeRadioButton.isSelected()){
			index = jComboBox.getSelectedIndex();
		} else if(afterRadioButton.isSelected()){
			index = jComboBox.getSelectedIndex() + 1;
		} else if(beginingRadioButton.isSelected()){
			index = 0;
		} 
		
		return index;
	}
	
	public String getName(){
		return nameTextField.getText();
	}
	
	public String getDescription(){
		return descriptionTextField.getText();
	}
	
	public String getRange() {
		return rangeTextField.getText();
	}
	
	public boolean isVariable(){
		return variableToggleButton.isSelected() || 
				!relationToggleButton.isSelected();
	}

}
