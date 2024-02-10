package cujae.map.j2g.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public final class JAccessoryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5633809759693383397L;
	
	public final TitledBorder titleBorder = new TitledBorder("Opciones");
	
	public final TitledBorder charsetTitledBorder = new TitledBorder("Codificaci\u00f3n");
	
	public final Action autoAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1529959355342947198L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private final BasicComboBoxRenderer listRenderer = new BasicComboBoxRenderer(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 8164909784809857216L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			Object realValue = ((Charset)value).displayName();
			
			return super.getListCellRendererComponent(list, realValue, index, isSelected,
					cellHasFocus);
		}
		
	};
	
	private final ItemListener itemListener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {			
			charsetComboBox.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
			charsetComboBox.setSelectedItem(Charset.defaultCharset());
		}
	};
	
	private final JCheckBox autoCheckBox = new JCheckBox(autoAction);
	
	private final JComboBox<?> charsetComboBox = new JComboBox(Charset.availableCharsets().values().toArray());
	
	private final JPanel innerPanel = new JPanel(new GridLayout(2, 1));
	
	private void initComponents(){
		
		autoAction.putValue(Action.NAME, "Auto");
		autoAction.putValue(Action.SHORT_DESCRIPTION, "Auto");
		
		autoCheckBox.addItemListener(itemListener);
		
		charsetComboBox.setEditable(false);
		charsetComboBox.setRenderer(listRenderer);
		
		setBorder(titleBorder);
		
		innerPanel.setBorder(new TitledBorder("Codificaci\u00f3n"));
		
		innerPanel.add(charsetComboBox);
		innerPanel.add(autoCheckBox);
		
		add(innerPanel);
		
		autoCheckBox.doClick();
		
	}

	public JAccessoryPanel() {
		initComponents();
	}

	public JAccessoryPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initComponents();
	}

	public JAccessoryPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initComponents();
	}

	public JAccessoryPanel(LayoutManager layout) {
		super(layout);
		initComponents();
	}
	
	public boolean isAuto(){
		return autoCheckBox.isSelected();
	}
	
	public Charset getSelectedCharset(){
		return (Charset) charsetComboBox.getSelectedItem();
	}

}
