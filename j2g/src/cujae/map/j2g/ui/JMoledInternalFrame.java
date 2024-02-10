package cujae.map.j2g.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import cujae.map.j2g.content.MDocument;
import cujae.map.j2g.content.MDocumentReader;
import cujae.map.j2g.content.MDocumentReaderFactory;
import cujae.map.j2g.content.MDocumentWriter;
import cujae.map.j2g.dgraphm.util.Flags;
import cujae.map.j2g.mme.Moled;
import cujae.map.j2g.mme.Moled.EdgeEntry;
import cujae.map.j2g.mme.Moled.MutableEdgeEntry;
import cujae.map.j2g.mme.Moled.NodeEntry;
import cujae.map.j2g.mme.MoledImpl;
import cujae.map.j2g.parser.OneLineParserFactory;
import cujae.map.j2g.ui.event.MoledEvent;
import cujae.map.j2g.ui.event.MoledListener;
import cujae.map.j2g.util.AbstractRowLinkModel;
import cujae.map.j2g.util.EdgesFormattable;
import cujae.map.j2g.util.Helper;
import cujae.map.j2g.util.IconFactory;
import cujae.map.j2g.util.JRowLinkPanel;
import cujae.map.j2g.util.RowLinkModel;
import cujae.map.j2g.util.table.ButtonTableCellEditor;
import cujae.map.j2g.util.table.DirectionTableCellEditor;
import cujae.map.j2g.util.table.DirectionTableCellRenderer;
import cujae.map.j2g.util.table.EdgesTableModel;
import cujae.map.j2g.util.table.FlagsTableCellEditor;
import cujae.map.j2g.util.table.FlagsTableCellRenderer;
import cujae.map.j2g.util.tree.TreeNodeBuilder;

public class JMoledInternalFrame extends JInternalFrame {	

	public static final String MOLED = "moled";

	/**
	 * 
	 */
	private static final long serialVersionUID = -308692497708179699L;
	
	private static final Class<?>[] moledColumnClasses = new Class<?>[]{
		Integer.class, Object.class, Object.class, EdgeEntry[].class
	};
	
	private static final String[] moledColumnNames = new String[]{
			"Estado", "Nombre", "Descripci\u00f3n", "Referencias"
	};
	
	private class MoledTableModel extends AbstractTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8089229641709347324L;	
		
		public final boolean isVariable;

		public MoledTableModel(boolean isVariable) {
			super();
			this.isVariable = isVariable;
		}

		@Override
		public int getRowCount() {
			return moled != null ? moled.size(isVariable) : 0;
		}

		@Override
		public int getColumnCount() {
			return moledColumnNames.length;
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
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			boolean changed = false;
			switch(columnIndex){
			case 0: 
				changed = moled.setFlags(rowIndex, (Integer) aValue, isVariable);
				break;
			case 1: 
				changed = moled.setName(rowIndex, (String) aValue, isVariable);
				break;
			case 2: 
				changed = moled.setDescription(rowIndex, (String) aValue, isVariable);
				if(!isVariable){
					moled.parse(rowIndex);
				}
				break;
			case 3:				
				changed = moled.set(isVariable, (EdgeEntry[]) aValue);
				break;
			}
			if(changed){
				moled.solve(!solveCheckBox.isSelected());
				variablesTableModel.fireTableDataChanged();
				relationsTableModel.fireTableDataChanged();
				fireMoledChanged(MoledEvent.UPDATE_TYPE);
			}
			
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return moledColumnClasses[columnIndex];
		}

		@Override
		public String getColumnName(int column) {
			return moledColumnNames[column];
		}
		
		public void setColumnName(int column, String name){
			moledColumnNames[column] = name;
		}	

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
	}
	
	private class EdgesTableCellRenderer extends DefaultTableCellRenderer{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8463169433741122107L;
		
		private final boolean isVariable;

		public EdgesTableCellRenderer(boolean isVariable) {
			super();
			this.isVariable = isVariable;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			EdgeEntry[] edges = (EdgeEntry[]) value;
			
			EdgesFormattable f = new EdgesFormattable(moled, edges, isVariable);
			
			return super.getTableCellRendererComponent(table, String.format("%s", f), 
					isSelected, hasFocus, row, column);
		}
		
	}
	
	private class EdgesTableCellEditor extends ButtonTableCellEditor{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8647764880917006346L;
		
		public final boolean isVariable;

		public EdgesTableCellEditor(boolean isVariable) {
			super();
			this.isVariable = isVariable;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EdgeEntry[] edges = (EdgeEntry[]) getCellEditorValue();
			EdgeEntry[] _edges = new EdgeEntry[edges.length];
			String[] names = new String[edges.length];
			for(int i = 0; i < edges.length; i ++){
				EdgeEntry entry = edges[i];				
				_edges[i] = new MutableEdgeEntry(entry.getVariableIndex(), 
						entry.getRelationIndex(), entry.getDirection());
				int index = isVariable ? entry.getRelationIndex() : entry.getVariableIndex();
				names[i] = moled.get(index, !isVariable).getName();
			}
						
			JTable t = new JTable(new EdgesTableModel(names, _edges));
			t.setDefaultRenderer(Integer.class, new DirectionTableCellRenderer());
			t.setDefaultEditor(Integer.class, new DirectionTableCellEditor());
			JScrollPane message = new JScrollPane(t);
			
			if(JOptionPane.showConfirmDialog(JMoledInternalFrame.this, message, "Referencias", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
					== JOptionPane.OK_OPTION){
				this.setCellEditorValue(_edges);
				stopCellEditing();
			} else{
				cancelCellEditing();
			}
			
		}
		
	}
	
	private final RowLinkModel variableRowLinkModel = new AbstractRowLinkModel() {
		
		@Override
		public int getValue(int index) {
			int value = 0;
			
			if(moled != null){
				NodeEntry e = moled.get(index, true);
				
				int flags = e.getFlags();
				
				if(Flags.isError1(flags) || Flags.isError2(flags)){
					value = -1;
				} else if(Flags.isError0(flags)){
					value = 1;
				} else{
					value = 0;
				}
				
//				if(Flags.isError(flags)){
//					value = -1;
//				} else if(Flags.isWarning(flags)){
//					value = 1;
//				} else{
//					value = 0;
//				}
				
			}
			return value;
		}
		
		@Override
		public int getSize() {
			return moled != null ? moled.size(true) : 0;
		}
	};
	
	private final RowLinkModel relationRowLinkModel = new AbstractRowLinkModel() {
		
		@Override
		public int getValue(int index) {
			int value = 0;
			
			if(moled != null){
				NodeEntry e = moled.get(index, false);
				
				int flags = e.getFlags();	
				
				if(Flags.isError1(flags) || Flags.isError2(flags)){
					value = -1;
				} else if(Flags.isError0(flags)){
					value = 1;
				} else{
					value = 0;
				}
				
//				if(Flags.isError(flags)){
//					value = -1;
//				} else if(Flags.isWarning(flags)){
//					value = 1;
//				} else{
//					value = 0;
//				}
				
			}			
			return value;
		}
		
		@Override
		public int getSize() {
			return moled != null ? moled.size(false) : 0;
		}
	};
	
	private Moled moled;	
	
	private File openFile;
	private File saveFile;
	
	private final MoledTableModel variablesTableModel = new MoledTableModel(true);
	private final MoledTableModel relationsTableModel = new MoledTableModel(false);
	
	private final JTable variablesTable = new JTable(variablesTableModel);
	private final JTable relationsTable = new JTable(relationsTableModel);
	
	private final MutableTreeNode treeNode = new DefaultMutableTreeNode();
	
	private final JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
	
	private final JScrollPane variablesScrollPane = new JScrollPane(variablesTable);
	private final JScrollPane relationsScrollPane = new JScrollPane(relationsTable);
	
	private final JRowLinkPanel variableRowLinkPanel = new JRowLinkPanel();
	private final JRowLinkPanel relationRowLinkPanel = new JRowLinkPanel();
	
	private final JPanel variablesPanel = new JPanel();
	private final JPanel relationsPanel = new JPanel();
	
	public final TitledBorder variablesBorder = new TitledBorder("Variables");
	public final TitledBorder relationsBorder = new TitledBorder("Relaciones");	
	
	public final TreeNodeBuilder nodeBuilder = new TreeNodeBuilder();
	
	public final Action openNewAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6180664438856807174L;

		@Override
		public void actionPerformed(ActionEvent e) {

			int option = JOptionPane.YES_OPTION;
			
			if(moled != null){				
				boolean retry = true;				
				while(retry && option == JOptionPane.YES_OPTION){					
					option = JOptionPane.showConfirmDialog(JMoledInternalFrame.this, 
							"Desea guardar el Modelo actual?");					
					if(option == JOptionPane.YES_OPTION){
						JFileChooser chooser = new JFileChooser();						
						chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						if(chooser.showSaveDialog(JMoledInternalFrame.this) 
								== JFileChooser.APPROVE_OPTION){
							try {
								MDocument d = Helper.buildDocument(moled);
								MDocumentWriter w = new MDocumentWriter();
								File file = Helper.fixFile(chooser.getSelectedFile(), MDocumentWriter.SUFFIX);						
								w.write(d, file);
								retry = false;
								saveFile = null;								
								moled = null;
								
								
			
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(JMoledInternalFrame.this, 
										e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						} else{
							option = JOptionPane.CANCEL_OPTION;
						}
					}
				}
			}
			
			if(option != JOptionPane.CANCEL_OPTION){
				
				moled = new MoledImpl();
				moled.setParser(OneLineParserFactory.DEFAULT_PARSER_FACTORY.newOneLineParser());
				
				variablesTableModel.fireTableDataChanged();
				relationsTableModel.fireTableDataChanged();
				
				fireMoledChanged(MoledEvent.CREATE_TYPE | MoledEvent.UPDATE_TYPE);
			}
			
		}
	};
	
	public final Action openAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6180664438856807174L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			int option = JOptionPane.YES_OPTION;
			
			if(moled != null){				
				boolean retry = true;				
				while(retry && option == JOptionPane.YES_OPTION){					
					option = JOptionPane.showConfirmDialog(JMoledInternalFrame.this, 
							"Desea guardar el Modelo actual?");					
					if(option == JOptionPane.YES_OPTION){
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);						
						if(chooser.showSaveDialog(JMoledInternalFrame.this) 
								== JFileChooser.APPROVE_OPTION){
							try {
								
								MDocument d = Helper.buildDocument(moled);
								MDocumentWriter w = new MDocumentWriter();
								File file = Helper.fixFile(chooser.getSelectedFile(), MDocumentWriter.SUFFIX);						
								w.write(d, file);
								retry = false;
								saveFile = null;
								
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(JMoledInternalFrame.this, 
										e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						} else{
							option = JOptionPane.CANCEL_OPTION;
						}
					}
				}
			}
			
			if(option != JOptionPane.CANCEL_OPTION){
				JFileChooser chooser = new JFileChooser(openFile);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//				chooser.setAcceptAllFileFilterUsed(false);
//				chooser.addChoosableFileFilter(new FileNameExtensionFilter("TKW Document", "tkt", "tkw"));
				JAccessoryPanel jAccessoryPanel = new JAccessoryPanel();
				chooser.setAccessory(jAccessoryPanel);
				
				if(chooser.showOpenDialog(JMoledInternalFrame.this) == JFileChooser.APPROVE_OPTION){
					try {						
						
						File file = chooser.getSelectedFile();
						
						MDocumentReaderFactory factory = MDocumentReaderFactory.getInstance(file);
						MDocumentReader reader = factory.newDocumentReader();
						
						FileInputStream stream = new FileInputStream(file);
						
						MDocument d = null;
						
						if(jAccessoryPanel.isAuto()){
							d = reader.read(stream);
						} else{
							d = reader.read(stream, jAccessoryPanel.getSelectedCharset());
						}						
						
						moled = Helper.buildMoled(d);
						
						moled.solve(!solveCheckBox.isSelected());
						
						variablesTableModel.fireTableDataChanged();
						relationsTableModel.fireTableDataChanged();
						
						openFile = file;
												
						fireMoledChanged(MoledEvent.CREATE_TYPE | MoledEvent.UPDATE_TYPE);
						
					} catch (IOException e1) {						
						JOptionPane.showMessageDialog(JMoledInternalFrame.this, e1.getMessage(), 
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	};	
	
	public final Action saveAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 691510254009620697L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(moled != null){
				JFileChooser chooser = new JFileChooser(saveFile);
				chooser.setSelectedFile(saveFile);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				JAccessoryPanel jAccessoryPanel = new JAccessoryPanel();
				chooser.setAccessory(jAccessoryPanel);
				if(chooser.showSaveDialog(JMoledInternalFrame.this) == JFileChooser.APPROVE_OPTION){					
					try {
						
						MDocument d = Helper.buildDocument(moled);
						MDocumentWriter w = new MDocumentWriter();
						File file = Helper.fixFile(chooser.getSelectedFile(), MDocumentWriter.SUFFIX);						
						w.write(d, file);						
						saveFile = file;

					} catch (IOException e1) {
						JOptionPane.showMessageDialog(JMoledInternalFrame.this, e1.getMessage(), 
								"Error", JOptionPane.ERROR_MESSAGE);
					} 

				}
			}
			
		}
	};
	
	public final Action insertAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -434031358718677602L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(moled != null){
				JInsertPanel message = new JInsertPanel(moled.nodes(true), moled.nodes(false));
				
				if(JOptionPane.showConfirmDialog(JMoledInternalFrame.this, message, 
						"Insertar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
						== JOptionPane.OK_OPTION){
					
					int index = message.getIndex();
					
					moled.insert(index, message.getName(), 
							message.getDescription(), 0, message.isVariable());
					
					if(!message.isVariable()){
						moled.parse(index);
					}
					
					moled.solve(!solveCheckBox.isSelected());					
					variablesTableModel.fireTableDataChanged();
					relationsTableModel.fireTableDataChanged();
					fireMoledChanged(MoledEvent.UPDATE_TYPE);				
				}
			}
			
		}
	};
	
	public final Action removeAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5527181878965075657L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(moled != null){
				JRemovePanel message = new JRemovePanel(
						moled.nodes(true), moled.nodes(false), 
						variablesTable.getSelectedRows(), relationsTable.getSelectedRows());
				
				if(JOptionPane.showConfirmDialog(JMoledInternalFrame.this, message, 
						"Eliminar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
						== JOptionPane.OK_OPTION){
					
					int[] selectedVariableIndices = message.getSelectedVariableIndices();
					int[] selectedRelationIndices = message.getSelectedRelationIndices();
					
					if(moled.remove(selectedVariableIndices, selectedRelationIndices)){
						
						moled.solve(!solveCheckBox.isSelected());
						variablesTableModel.fireTableDataChanged();
						relationsTableModel.fireTableDataChanged();
						fireMoledChanged(MoledEvent.UPDATE_TYPE);
					}
				
				}
			}		
			
		}
	};	
	
	public final Action moveUpAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8764149570101970172L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub			
			
		}
	};
	
	public final Action moveDownAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7850595579375242810L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public final Action horizontalOrientationAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 9150240374250583926L;

		@Override
		public void actionPerformed(ActionEvent e) {
			jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);			
		}
	};
	
	public final Action verticalOrientationAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -337901555077920702L;

		@Override
		public void actionPerformed(ActionEvent e) {
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			
		}
	};	
	
	public final Action solveAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4709193517212638122L;

		@Override
		public void actionPerformed(ActionEvent e) {			
			if(moled != null){
				moled.solve(!solveCheckBox.isSelected());
			}
			variablesTableModel.fireTableDataChanged();
			relationsTableModel.fireTableDataChanged();
			fireMoledChanged(MoledEvent.UPDATE_TYPE);
		}
	};
	
	private final JCheckBox solveCheckBox = new JCheckBox(solveAction);
	
	public final Action flagsAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4709193517212638122L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private final JToolBar jToolBar = new JToolBar();
	
	private final EventListenerList moledListenerList = new EventListenerList();
	
	private void initComponents(){		
		
		variablesTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3){
					int[] selectedRows = variablesTable.getSelectedRows();	
					
					if(selectedRows.length != 0){
						int flags = moled.get(selectedRows[0], true).getFlags();						
						
						for(int i = 1; i < selectedRows.length; i ++){
							flags &= moled.get(selectedRows[i], true).getFlags();
						}
						
						JFlagsPanel message = new JFlagsPanel(flags, true);
						BoxLayout layout = new BoxLayout(message, BoxLayout.Y_AXIS);
						message.setLayout(layout);
						if(JOptionPane.showConfirmDialog(JMoledInternalFrame.this, message, 
								"Estado", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
								== JOptionPane.OK_OPTION){
							moled.setFlags(message.getFlags(), true, selectedRows);
							moled.solve(!solveCheckBox.isSelected());
							variablesTableModel.fireTableDataChanged();
							relationsTableModel.fireTableDataChanged();
							fireMoledChanged(MoledEvent.UPDATE_TYPE);
						}	
					}

				}
				super.mouseClicked(e);
			}
			
		});
		
		treeNode.setUserObject(getTitle());
		
		variablesTable.setDefaultRenderer(Integer.class, new FlagsTableCellRenderer(true));
		variablesTable.setDefaultRenderer(EdgeEntry[].class, new EdgesTableCellRenderer(true));
		relationsTable.setDefaultRenderer(Integer.class, new FlagsTableCellRenderer(false));
		relationsTable.setDefaultRenderer(EdgeEntry[].class, new EdgesTableCellRenderer(false));
		
		variablesTable.setDefaultEditor(Integer.class, new FlagsTableCellEditor(JMoledInternalFrame.this, true));
		variablesTable.setDefaultEditor(EdgeEntry[].class, new EdgesTableCellEditor(true));
		relationsTable.setDefaultEditor(Integer.class, new FlagsTableCellEditor(JMoledInternalFrame.this, false));
		relationsTable.setDefaultEditor(EdgeEntry[].class, new EdgesTableCellEditor(false));
		
		variablesPanel.setBorder(variablesBorder);
		variablesPanel.setLayout(new BorderLayout());
		variablesPanel.add(variablesScrollPane, BorderLayout.CENTER);
		variablesPanel.add(variableRowLinkPanel, BorderLayout.EAST);
		variableRowLinkPanel.setModel(variableRowLinkModel);
		variableRowLinkPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JRowLinkPanel source = (JRowLinkPanel) e.getSource();
				int row = source.getRow(e.getX(), e.getY());
				
				if(row != -1){
					variablesTable.changeSelection(row, -1, false, false);
				}
				
			}

			
		});
		
		relationsPanel.setBorder(relationsBorder);
		relationsPanel.setLayout(new BorderLayout());
		relationsPanel.add(relationsScrollPane, BorderLayout.CENTER);
		relationsPanel.add(relationRowLinkPanel, BorderLayout.EAST);
		relationRowLinkPanel.setModel(relationRowLinkModel);
		relationRowLinkPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JRowLinkPanel source = (JRowLinkPanel) e.getSource();
				int row = source.getRow(e.getX(), e.getY());
				
				if(row != -1){
					relationsTable.changeSelection(row, -1, false, false);
				}
				
			}			
			
		});
		
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.add(variablesPanel);
		jSplitPane.add(relationsPanel);
		
		openNewAction.putValue(Action.NAME, "Nuevo");
		openNewAction.putValue(Action.SHORT_DESCRIPTION, "Nuevo");
		openNewAction.putValue(Action.SMALL_ICON, IconFactory.getNewFileIcon());
		
		openAction.putValue(Action.NAME, "Abrir");
		openAction.putValue(Action.SHORT_DESCRIPTION, "Abrir");
		openAction.putValue(Action.SMALL_ICON, IconFactory.getOpenFileIcon());
		
		saveAction.putValue(Action.NAME, "Guardar");
		saveAction.putValue(Action.SHORT_DESCRIPTION, "Guardar");
		saveAction.putValue(Action.SMALL_ICON, IconFactory.getSaveIcon());
		
		insertAction.putValue(Action.NAME, "Insertar");
		insertAction.putValue(Action.SHORT_DESCRIPTION, "Insertar");
		insertAction.putValue(Action.SMALL_ICON, IconFactory.getAddIcon());
		
		removeAction.putValue(Action.NAME, "Eliminar");
		removeAction.putValue(Action.SHORT_DESCRIPTION, "Eliminar");
		removeAction.putValue(Action.SMALL_ICON, IconFactory.getDeleteIcon());
		
		moveDownAction.putValue(Action.NAME, "Down");
		moveDownAction.putValue(Action.SMALL_ICON, IconFactory.getDownIcon());
		
		moveUpAction.putValue(Action.NAME, "Up");
		moveUpAction.putValue(Action.SMALL_ICON, IconFactory.getUpIcon());
		
		horizontalOrientationAction.putValue(Action.NAME, "Orientaci\u00f3n horizontal");
		horizontalOrientationAction.putValue(Action.SHORT_DESCRIPTION, "Orientaci\u00f3n horizontal");
		horizontalOrientationAction.putValue(Action.SMALL_ICON, IconFactory.getHorizontalOrientationIcon());
		
		verticalOrientationAction.putValue(Action.NAME, "Orientaci\u00f3n vertical");
		verticalOrientationAction.putValue(Action.SHORT_DESCRIPTION, "Orientaci\u00f3n vertical");
		verticalOrientationAction.putValue(Action.SMALL_ICON, IconFactory.getVerticalOrientationIcon());	
		
		solveAction.putValue(Action.NAME, "Resolver asimetr\u00edas");
		solveAction.putValue(Action.SHORT_DESCRIPTION, "Resolver asimetr\u00edas");
		solveAction.putValue(Action.SMALL_ICON, IconFactory.getSolveIcon());
		
		solveCheckBox.setSelected(true);
		
		flagsAction.putValue(Action.NAME, "Estado");
		flagsAction.putValue(Action.SHORT_DESCRIPTION, "Estado");
		
		jToolBar.setFloatable(false);
		jToolBar.add(openNewAction).setBorderPainted(false);
		jToolBar.add(openAction).setBorderPainted(false);
		jToolBar.add(saveAction).setBorderPainted(false);
		jToolBar.add(insertAction).setBorderPainted(false);
		jToolBar.add(removeAction).setBorderPainted(false);
		jToolBar.add(moveDownAction).setBorderPainted(false);
		jToolBar.add(moveUpAction).setBorderPainted(false);
		jToolBar.add(horizontalOrientationAction).setBorderPainted(false);
		jToolBar.add(verticalOrientationAction).setBorderPainted(false);
		jToolBar.add(solveCheckBox);
		
		getContentPane().add(jToolBar, BorderLayout.NORTH);
		getContentPane().add(jSplitPane, BorderLayout.CENTER);	
	
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setBounds(0, 0, 600, 400);
		setVisible(true);
		
	}
	
	private void fireMoledChanged(int type){		
		MoledEvent evt = new MoledEvent(this, type);
		MoledListener[] listeners = moledListenerList.getListeners(MoledListener.class);
		for(int i = 0; i < listeners.length; i ++){
			MoledListener listener = listeners[i];
			listener.moledChanged(evt);
		}	

		variableRowLinkPanel.repaint();
		relationRowLinkPanel.repaint();

	}

	public JMoledInternalFrame() {
		initComponents();		
	}

	public JMoledInternalFrame(String title, boolean resizable,
			boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		initComponents();
	}

	public JMoledInternalFrame(String title, boolean resizable,
			boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		initComponents();
	}

	public JMoledInternalFrame(String title, boolean resizable, boolean closable) {
		super(title, resizable, closable);
		initComponents();
	}

	public JMoledInternalFrame(String title, boolean resizable) {
		super(title, resizable);
		initComponents();
	}

	public JMoledInternalFrame(String title) {
		super(title);
		initComponents();
	}
	
	public void setTableColumnName(int column, String name, boolean isVariable){
		(isVariable ? variablesTableModel : relationsTableModel).setColumnName(column, name);		
	}	
	
	public void addMoledListener(MoledListener listener){
		moledListenerList.add(MoledListener.class, listener);
	}
	
	public void removeMoledListener(MoledListener listener){
		moledListenerList.remove(MoledListener.class, listener);		
	}
	
	public Moled getMoled(){		
		return (moled != null) ? MoledImpl.unmodifiableMoled(moled) : null;
	}
	
}
