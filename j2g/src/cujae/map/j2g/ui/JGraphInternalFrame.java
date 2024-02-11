package cujae.map.j2g.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import cujae.map.j2g.dgraphm.model.BGraph;
import cujae.map.j2g.graphml.AbbGraphmlCodec;
import cujae.map.j2g.graphml.ValueHandler;
import cujae.map.j2g.graphx.GraphBuilder;
import cujae.map.j2g.util.CellValueHandler;
import cujae.map.j2g.util.Helper;
import cujae.map.j2g.util.IconFactory;

public class JGraphInternalFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -750284278174804063L;		
	
	private final mxGraphComponent graphComponent = new mxGraphComponent(new mxGraph());
	
	private final GraphBuilder graphBuilder = new GraphBuilder();
	
	public final Map<Class<?>, ValueHandler> valueHandlerProvider = new HashMap<Class<?>, ValueHandler>();
	
	public final CellValueHandler cellValueHandler = new CellValueHandler();
	
	private final JToolBar jToolBar = new JToolBar();
	
	public final TitledBorder layoutOrientationTitledBorder = new TitledBorder("Orientaci\u00f3n");
	
	public final Action exportAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8106918777597481118L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(currentDirectory);
			chooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
			if(chooser.showSaveDialog(JGraphInternalFrame.this) == JFileChooser.APPROVE_OPTION){
				
				File selectedFile = chooser.getSelectedFile();
				
				try {
					doExport(graphComponent.getGraph(), valueHandlerProvider, 
							selectedFile, AbbGraphmlCodec.SUFFIX);
				} catch (ClassCastException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				} catch (InstantiationException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					JOptionPane.showMessageDialog(JGraphInternalFrame.this, e1.getMessage(), 
							"Error", JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
				}
			}
		}
	};
	
	public final Action hierarchicalLayoutNorthAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4090272059927547345L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doHierarchicalLayout(SwingConstants.NORTH);
			defaultLayoutAction = hierarchicalLayoutNorthAction;
		}
	};
	
	public final Action hierarchicalLayoutWestAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3246205945549783291L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doHierarchicalLayout(SwingConstants.WEST);
			defaultLayoutAction = hierarchicalLayoutWestAction;
		}
	};
	
	public final Action organicLayoutAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3621949568548636259L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doOrganicLayout();
			defaultLayoutAction = organicLayoutAction;
		}
	};
	
	public final Action circleLayoutAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6872892382981103894L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doCircleLayout();
			defaultLayoutAction = circleLayoutAction;
		}
	};
	
	public final Action zoomInAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4180297853631897608L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doZoomIn();
		}
	};
	
	public final Action zoomOutAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1560681296355723435L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doZoomOut();
		}
	};
	
	public final Action zoomResetAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4847759659078407395L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doZoomReset();
		}
	};
	
	private final JCheckBox autoLayoutCheckBox = new JCheckBox();
	
	private Action defaultLayoutAction = null;
	
	private File currentDirectory;
	
	private void initComponents(){
		
		jToolBar.setFloatable(false);
		
		valueHandlerProvider.put(Helper.CellValue.class, cellValueHandler);
		
		graphBuilder.addDefaultListener();
		
		graphComponent.setEnabled(false);	
		graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {
			
			private int sx;
			private int sy;

			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.isControlDown()){
					int tx = e.getX();
					int ty = e.getY();
					
					mxGraph graph = graphComponent.getGraph();
					Object [] cells = graph.getChildCells(graph.getDefaultParent());
					double scale = graph.getView().getScale();
					
					double dx = (tx - sx) / scale;
					double dy = (ty - sy) / scale;
					
					graph.moveCells(cells, dx, dy);
					
					sx = tx;
					sy = ty;
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {

				sx = e.getX();
				sy = e.getY();
			}
			
		});
		graphComponent.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
			
			// hace que se aumento o disminuya el zoom  ...Andy
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
					if(e.getWheelRotation() <= -1){
						doZoomIn();
					} else {
						doZoomOut();
					}
				
			}
		});
		
		exportAction.putValue(Action.NAME, "Exportar");
		exportAction.putValue(Action.SHORT_DESCRIPTION, "Exportar");
		exportAction.putValue(Action.SMALL_ICON, IconFactory.getExportIcon());
		
		hierarchicalLayoutNorthAction.putValue(Action.NAME, "Hierarchical Layout (Norte)");
		hierarchicalLayoutNorthAction.putValue(Action.SHORT_DESCRIPTION, "Hierarchical Layout (Norte)");
		hierarchicalLayoutNorthAction.putValue(Action.SMALL_ICON, IconFactory.getHierarchicalLayoutNorthIcon());

		hierarchicalLayoutWestAction.putValue(Action.NAME, "Hierarchical Layout (Oeste)");
		hierarchicalLayoutWestAction.putValue(Action.SHORT_DESCRIPTION, "Hierarchical Layout (Oeste)");
		hierarchicalLayoutWestAction.putValue(Action.SMALL_ICON, IconFactory.getHierarchicalLayoutWestIcon());
		
		organicLayoutAction.putValue(Action.NAME, "Organic Layout");
		organicLayoutAction.putValue(Action.SHORT_DESCRIPTION, "Organic Layout");
		organicLayoutAction.putValue(Action.SMALL_ICON, IconFactory.getOrganicLayoutIcon());
		
		circleLayoutAction.putValue(Action.NAME, "Circle Layout");
		circleLayoutAction.putValue(Action.SHORT_DESCRIPTION, "Circle Layout");
		circleLayoutAction.putValue(Action.SMALL_ICON, IconFactory.getCircleLayoutIcon());
		
		zoomInAction.putValue(Action.NAME, "Zoom In");
		zoomInAction.putValue(Action.SHORT_DESCRIPTION, "Zoom In");
		zoomInAction.putValue(Action.SMALL_ICON, IconFactory.getZoomInIcon());
		
		zoomOutAction.putValue(Action.NAME, "Zoom Out");
		zoomOutAction.putValue(Action.SHORT_DESCRIPTION, "Zoom Out");
		zoomOutAction.putValue(Action.SMALL_ICON, IconFactory.getZoomOutIcon());
		
		zoomResetAction.putValue(Action.NAME, "Zoom Reset");
		zoomResetAction.putValue(Action.SHORT_DESCRIPTION, "Zoom Reset");
		zoomResetAction.putValue(Action.SMALL_ICON, IconFactory.getZoom100Icon());
		
		autoLayoutCheckBox.setText("Auto Layout");
		autoLayoutCheckBox.setSelected(true);
		autoLayoutCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				doAutoLayout(e.getStateChange() == ItemEvent.SELECTED);
			}
		});		
		
		jToolBar.add(exportAction).setBorderPainted(false);
		jToolBar.add(hierarchicalLayoutNorthAction).setBorderPainted(false);
		jToolBar.add(hierarchicalLayoutWestAction).setBorderPainted(false);
		jToolBar.add(organicLayoutAction).setBorderPainted(false);
		jToolBar.add(circleLayoutAction).setBorderPainted(false);
		jToolBar.add(zoomInAction).setBorderPainted(false);
		jToolBar.add(zoomOutAction).setBorderPainted(false);
		jToolBar.add(zoomResetAction).setBorderPainted(false);
		jToolBar.add(autoLayoutCheckBox);
		
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setIconifiable(true);
		
		getContentPane().add(jToolBar, BorderLayout.NORTH);
		getContentPane().add(graphComponent, BorderLayout.CENTER);
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setBounds(0, 0, 400, 400);
		
	}

	public JGraphInternalFrame() {
		initComponents();
	}

	public JGraphInternalFrame(String title) {
		super(title);
		initComponents();
	}

	public JGraphInternalFrame(String title, boolean resizable) {
		super(title, resizable);
		initComponents();
	}

	public JGraphInternalFrame(String title, boolean resizable,
			boolean closable) {
		super(title, resizable, closable);
		initComponents();
	}

	public JGraphInternalFrame(String title, boolean resizable,
			boolean closable, boolean maximizable) {
		super(title, resizable, closable, maximizable);
		initComponents();
	}

	public JGraphInternalFrame(String title, boolean resizable,
			boolean closable, boolean maximizable, boolean iconifiable) {
		super(title, resizable, closable, maximizable, iconifiable);
		initComponents();
	}
	
	public void doExport(mxGraph graph, Map<Class<?>, ValueHandler> valueHandlerProvider, 
			File pathname, String suffix) throws FileNotFoundException, ClassCastException, 
			ClassNotFoundException, InstantiationException, IllegalAccessException, 
			ParserConfigurationException{
		Helper.exportGraph(graph, valueHandlerProvider, pathname, suffix);
	}
	
	public void doHierarchicalLayout(int orientation){
		Object parent = graphComponent.getGraph().getDefaultParent();
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graphComponent.getGraph(), orientation);
		layout.setUseBoundingBox(true);
		layout.execute(parent);
	}
	
	public void doOrganicLayout(){
		Object parent = graphComponent.getGraph().getDefaultParent();
		mxOrganicLayout layout = new mxOrganicLayout(graphComponent.getGraph());		
		layout.setUseBoundingBox(true);
		layout.setResetEdges(true);
		layout.setFineTuning(true);
		layout.execute(parent);
	}
	
	public void doCircleLayout(){
		Object parent = graphComponent.getGraph().getDefaultParent();
		mxCircleLayout layout = new mxCircleLayout(graphComponent.getGraph());
		layout.setResetEdges(true);
		layout.setUseBoundingBox(true);
		layout.execute(parent);
	}
	
	public void doZoomIn(){
		graphComponent.zoomIn();
	}
	
	public void doZoomOut(){
		graphComponent.zoomOut();
	}
	
	public void doZoomReset(){
		graphComponent.zoom(true, true);
		graphComponent.zoomActual();
	}
	
	public void doAutoLayout(boolean selected){
		if(selected){
			if(defaultLayoutAction == null){
				defaultLayoutAction = hierarchicalLayoutNorthAction;
			}
			defaultLayoutAction.actionPerformed(null);
		}
	}
	
	public void setToolBarName(String name){
		jToolBar.setName(name);
	}
	
	public void update(BGraph graph, Map<Integer, Object> valueMap, Map<String, Object> analysisMap){		
		graphBuilder.update(graphComponent.getGraph(), true, graph, valueMap, analysisMap);		
		doAutoLayout(autoLayoutCheckBox.isSelected());
	}

}
