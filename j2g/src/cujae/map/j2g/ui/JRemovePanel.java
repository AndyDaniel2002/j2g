package cujae.map.j2g.ui;

import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class JRemovePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164877066376963627L;
	
	public final TitledBorder variablesTitledBorder = new TitledBorder("Variables");
	public final TitledBorder relationsTitledBorder = new TitledBorder("Relaciones");
	
	private final JScrollPane variablesScrollPane;
	private final JScrollPane relationsScrollPane;
	
	private final JList variableList;
	private final JList relationList;
	
	public JRemovePanel(Object[] variables, Object[] relations, int[] variableIndices, int[] relationIndices){
		
		variableList = new JList(variables);		
		variableList.setSelectedIndices(variableIndices);
		
		relationList = new JList(relations);
		relationList.setSelectedIndices(relationIndices);
		
		variablesScrollPane = new JScrollPane(variableList);
		variablesScrollPane.setBorder(variablesTitledBorder);
		
		relationsScrollPane = new JScrollPane(relationList);
		relationsScrollPane.setBorder(relationsTitledBorder);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(variablesScrollPane);
		add(relationsScrollPane);		

	}
	
	public int[] getSelectedVariableIndices(){
		return variableList.getSelectedIndices();
	}
	
	public int[] getSelectedRelationIndices(){
		return relationList.getSelectedIndices();
	}

}
