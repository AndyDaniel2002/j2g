package cujae.map.j2g.ui;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import cujae.map.j2g.dgraphm.analysis.AbstractAnalysis;
import cujae.map.j2g.dgraphm.analysis.AlgorithmAnalysis;
import cujae.map.j2g.dgraphm.analysis.MarkedResolventAnalysis;
import cujae.map.j2g.dgraphm.analysis.MatchingProblemAnalysis;
import cujae.map.j2g.dgraphm.analysis.ModelAnalysis;
import cujae.map.j2g.dgraphm.analysis.ProblemAnalysis;
import cujae.map.j2g.dgraphm.analysis.ResolventAnalysis;
import cujae.map.j2g.dgraphm.analysis.SituationAnalysis;
import cujae.map.j2g.dgraphm.model.BGraph;
import cujae.map.j2g.mme.Moled;
import cujae.map.j2g.mme.Moled.NodeEntry;
import cujae.map.j2g.ui.event.MoledEvent;
import cujae.map.j2g.ui.event.MoledListener;
import cujae.map.j2g.util.Helper;
import cujae.map.j2g.util.IconFactory;
import cujae.map.j2g.util.tree.MyTreeCellRenderer;
import cujae.map.j2g.util.tree.MyTreeNode;
import cujae.map.j2g.util.tree.TreeNodeBuilder;
import sun.nio.ch.SelChImpl;

public class JMainFrame extends JFrame {
	
	public static final String MOLED_VARIABLES = "moled.variables";
	
	public static final String MOLED_RELATIONS = "moled.relations";

	/**
	 * 
	 */
	private static final long serialVersionUID = 22256210376601689L;
	
	private final JMoledInternalFrame moledInternalFrame = new JMoledInternalFrame();
	
	private final JGraphInternalFrame modelInternalFrame = new JGraphInternalFrame();	
	private final JGraphInternalFrame situationInternalFrame = new JGraphInternalFrame();
	private final JGraphInternalFrame problemInternalFrame = new JGraphInternalFrame();
	private final JGraphInternalFrame matchingProblemInternalFrame = new JGraphInternalFrame();	
	private final JGraphInternalFrame resolventInternalFrame = new JGraphInternalFrame();	
	private final JGraphInternalFrame markedResolventInternalFrame = new JGraphInternalFrame();	
	private final JGraphInternalFrame algorithmInternalFrame = new JGraphInternalFrame();	
	
	public final Action syncGraphAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4850972183461769512L;

		@Override
		public void actionPerformed(ActionEvent e) {
			updateGraphs(moledInternalFrame.getMoled());
		}
	};
	
	public final Action matchingAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4850972183461769512L;

		@Override
		public void actionPerformed(ActionEvent e) {
			updateMatching();
		}
	};
	
	public final Action horizontalViewAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4883408474182166870L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doHorizontalView();			
		}
	};
	
	public final Action verticalViewAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1420196002735097018L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doVerticalView();			
		}
	};
	
	public final Action stackViewAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2956935050352489657L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doStackView();
		}
	};
	
	public final Action rowViewAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1133972357145304567L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doRowView();
		}
	};
	
	public final Action horizontalOrientationAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1765967081973074233L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doHorizontalOrientation();
		}
	};
	
	public final Action verticalOrientationAction = new AbstractAction() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2504907070004239852L;

		@Override
		public void actionPerformed(ActionEvent e) {
			doVerticalOrientation();
		}
	};

	
	private final JToolBar jToolBar = new JToolBar();
	
	private final JDesktopPane jDesktopPane = new JDesktopPane();
	
	private final MyTreeNode moledNode = new MyTreeNode();	
	private final MyTreeNode modelNode = new MyTreeNode();	
	private final MyTreeNode situationNode = new MyTreeNode();	
	private final MyTreeNode problemNode = new MyTreeNode();	
	private final MyTreeNode matchingProblemNode = new MyTreeNode();	
	private final MyTreeNode resolventNode = new MyTreeNode();	
	private final MyTreeNode markedResolventNode = new MyTreeNode();	
	private final MyTreeNode algorithmNode = new MyTreeNode();	
	private final MyTreeNode rootNode = new MyTreeNode();
	
	private final JTree jTree = new JTree();
	
	private final MouseListener treeMouseListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = jTree.getRowForLocation(x, y);
			TreePath path = jTree.getPathForLocation(x, y);
			if(row !=-1){
				
				if(new TreePath(moledNode.getPath()).isDescendant(path)){
					moledInternalFrame.toFront();
					moledInternalFrame.setVisible(true);
				} else if(new TreePath(modelNode.getPath()).isDescendant(path)){
					modelInternalFrame.toFront();
					modelInternalFrame.setVisible(true);
				} else if(new TreePath(situationNode.getPath()).isDescendant(path)){
					situationInternalFrame.toFront();
					situationInternalFrame.setVisible(true);
				} else if(new TreePath(problemNode.getPath()).isDescendant(path)){
					problemInternalFrame.toFront();
					problemInternalFrame.setVisible(true);
				} else if(new TreePath(matchingProblemNode.getPath()).isDescendant(path)){
					matchingProblemInternalFrame.toFront();
					matchingProblemInternalFrame.setVisible(true);
				} else if(new TreePath(resolventNode.getPath()).isDescendant(path)){
					resolventInternalFrame.toFront();
					resolventInternalFrame.setVisible(true);
				} else if(new TreePath(markedResolventNode.getPath()).isDescendant(path)){
					markedResolventInternalFrame.toFront();
					markedResolventInternalFrame.setVisible(true);
				} else if(new TreePath(algorithmNode.getPath()).isDescendant(path)){
					algorithmInternalFrame.toFront();
					algorithmInternalFrame.setVisible(true);
				}					
			}
		}
		
	};
	
	private final JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	private final MoledListener moledListener = new MoledListener() {
		
		@Override
		public void moledChanged(MoledEvent evt) {
			if((evt.getType() & (MoledEvent.CREATE_TYPE | MoledEvent.DELETE_TYPE)) != 0){
				updateGraphs(null);
			}
			
			if((evt.getType() & MoledEvent.UPDATE_TYPE) != 0){
				JMoledInternalFrame source = (JMoledInternalFrame) evt.getSource();
				Moled moled = source.getMoled();
				
				List<NodeEntry> variables = new ArrayList<Moled.NodeEntry>();
				List<NodeEntry> relations = new ArrayList<Moled.NodeEntry>();
				
				if(moled != null){
					variables = Arrays.asList(moled.nodes(true));
					relations = Arrays.asList(moled.nodes(false));				
				} else{
					moledMap.clear();
				}
				
				moledMap.put(MOLED_VARIABLES, variables);
				moledMap.put(MOLED_RELATIONS, relations);
				TreeNodeBuilder.update(moledNode, true, null, moledLabelMap, moledMap);
				((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(moledNode);
			}
			
		}
	};
	
	private final Map<String, Object> analysisMap = new HashMap<String, Object>();
	private final Map<String, Object> moledMap = new HashMap<String, Object>();	
	
	private final Map<String, String> moledLabelMap = new HashMap<String, String>();	
	private final Map<String, String> modelLabelMap = new HashMap<String, String>();	
	private final Map<String, String> situationLabelMap = new HashMap<String, String>();	
	private final Map<String, String> problemLabelMap = new HashMap<String, String>();	
	private final Map<String, String> matchingProblemLabelMap = new HashMap<String, String>();	
	private final Map<String, String> resolventLabelMap = new HashMap<String, String>();	
	private final Map<String, String> markedResolventLabelMap = new HashMap<String, String>();	
	private final Map<String, String> algorithLabelMap = new HashMap<String, String>();
	
	private void initComponents(){	
		
		syncGraphAction.putValue(Action.NAME, "Actualizar Grafos");
		syncGraphAction.putValue(Action.SHORT_DESCRIPTION, "Actualizar Grafos");
		syncGraphAction.putValue(Action.SMALL_ICON, IconFactory.getSyncIcon());
		
		matchingAction.putValue(Action.NAME, "Selectionar Pareo");
		matchingAction.putValue(Action.SHORT_DESCRIPTION, "Seleccionar Pareo");
		matchingAction.putValue(Action.SMALL_ICON, IconFactory.getEdgeIcon());
		
		horizontalViewAction.putValue(Action.NAME, "Vista horizontal");
		horizontalViewAction.putValue(Action.SHORT_DESCRIPTION, "Vista horizontal");
		horizontalViewAction.putValue(Action.SMALL_ICON, IconFactory.getHorizontalViewIcon());
		
		verticalViewAction.putValue(Action.NAME, "Vista vertical");
		verticalViewAction.putValue(Action.SHORT_DESCRIPTION, "Vista vertical");
		verticalViewAction.putValue(Action.SMALL_ICON, IconFactory.getVerticalViewIcon());
		
		stackViewAction.putValue(Action.NAME, "Vista en cascada");
		stackViewAction.putValue(Action.SHORT_DESCRIPTION, "Vista en cascada");
		stackViewAction.putValue(Action.SMALL_ICON, IconFactory.getStackViewIcon());
		
		rowViewAction.putValue(Action.NAME, "Vista en filas");
		rowViewAction.putValue(Action.SHORT_DESCRIPTION, "Vista en filas");
		rowViewAction.putValue(Action.SMALL_ICON, IconFactory.getRowViewIcon());
		
		horizontalOrientationAction.putValue(Action.NAME, "Orientaci\u00f3n horizontal");
		horizontalOrientationAction.putValue(Action.SHORT_DESCRIPTION, "Orientaci\u00f3n horizontal");
		horizontalOrientationAction.putValue(Action.SMALL_ICON, IconFactory.getHorizontalOrientationIcon());
		
		verticalOrientationAction.putValue(Action.NAME, "Orientaci\u00f3n vertical");
		verticalOrientationAction.putValue(Action.SHORT_DESCRIPTION, "Orientaci\u00f3n vertical");
		verticalOrientationAction.putValue(Action.SMALL_ICON, IconFactory.getVerticalOrientationIcon());
		
		jToolBar.add(syncGraphAction).setBorderPainted(false);
		jToolBar.add(matchingAction).setBorderPainted(false);
		jToolBar.add(horizontalViewAction).setBorderPainted(false);
		jToolBar.add(verticalViewAction).setBorderPainted(false);
		jToolBar.add(stackViewAction).setBorderPainted(false);
		jToolBar.add(rowViewAction).setBorderPainted(false);
		jToolBar.add(horizontalOrientationAction).setBorderPainted(false);
		jToolBar.add(verticalOrientationAction).setBorderPainted(false);
		jToolBar.setFloatable(false);
		
		moledInternalFrame.setTitle("Editor de Problemas");
		moledNode.setUserObject("Editor de Problemas");		
		moledInternalFrame.addMoledListener(moledListener);	
		
		modelInternalFrame.setTitle("Modelo");
		modelNode.setUserObject("Modelo");
		
		situationInternalFrame.setTitle("Situaci\u00f3n");
		situationNode.setUserObject("Situaci\u00f3n");
		
		problemInternalFrame.setTitle("Problema");
		problemNode.setUserObject("Problema");
		
		matchingProblemInternalFrame.setTitle("Problema Pareado");
		matchingProblemNode.setUserObject("Problema Pareado");
		
		resolventInternalFrame.setTitle("Resolvente");
		resolventNode.setUserObject("Resolvente");
		
		markedResolventInternalFrame.setTitle("Resolvente Marcado");
		markedResolventNode.setUserObject("Resolvente Marcado");
		
		algorithmInternalFrame.setTitle("Algoritmo");
		algorithmNode.setUserObject("Algoritmo");
		
		moledLabelMap.put(MOLED_VARIABLES, "Variables");
		moledLabelMap.put(MOLED_RELATIONS, "Relaciones");
		
		modelLabelMap.put(AbstractAnalysis.MODEL_VARIABLES, "Variables");
		modelLabelMap.put(AbstractAnalysis.MODEL_RELATIONS, "Relaciones");
		modelLabelMap.put(AbstractAnalysis.MODEL_COMPONENTS, "Componentes");
		modelLabelMap.put(AbstractAnalysis.MODEL_COMPONENTS, "Componentes");
		
		situationLabelMap.put(AbstractAnalysis.SITUATION_INPUTS, "Entradas");
		situationLabelMap.put(AbstractAnalysis.SITUATION_RELATIONS, "Relaciones");
		situationLabelMap.put(AbstractAnalysis.SITUATION_DEFICIENCES, "Deficiencias");
		situationLabelMap.put(AbstractAnalysis.SITUATION_UNKNOWNS, "Inc\u00f3gnitas");
		situationLabelMap.put(AbstractAnalysis.SITUATION_COMPONENTS, "Componentes");
		situationLabelMap.put(AbstractAnalysis.SITUATION_APPROPRIATE, "Apropiado");
		situationLabelMap.put(AbstractAnalysis.SITUATION_COMPATIBLE, "Compatible");		

		problemLabelMap.put(AbstractAnalysis.PROBLEM_APPROPRIATE, "Apropiado");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_COMPATIBLE, "Compatible");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_COMPONENTS, "Componentes");
//		problemLabelMap.put(AbstractAnalysis.PROBLEM_DEFICIENCES, "Deficiencias");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_FREE_RELATIONS, "Relaciones Libres");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_FREE_UNKNOWNS, "Inc\u00f3gnitas Libres");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_ORIGINAL_MATCHING, "Pareo");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_OUTPUTS, "Salidas");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_REMOVED, "Nodos Eliminados");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_RELATIONS, "Relaciones");
		problemLabelMap.put(AbstractAnalysis.PROBLEM_UNKNOWNS, "Inc\u00f3gnitas");		

		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_COMPATIBLE, "Compatible");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_DETERMINE, "Determinado");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_CONTROL_UNKNOWNS, "Inc\u00f3gnitas de Control");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_DEFICIENCES, "Deficiencias");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_DESIGN_PARAMETERS, "Par\u00e1metro de Dise\u00f1o");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_FREEDOM_DEGREES, "Grados de Libertad");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_MAXIMUM_MATCH, "Pareo M\u00e1ximo");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_REALIZABLE, "Realizable");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_REALIZABLE_DISJOINT, "Disjoint");
		matchingProblemLabelMap.put(AbstractAnalysis.MATCHING_PROBLEM_MATCHING_COUNT, "Pareos disponibles");
		
		resolventLabelMap.put(AbstractAnalysis.RESOLVENT_STRONG_CONNECTED_COMPONENTS, "SCC");
		resolventLabelMap.put(AbstractAnalysis.RESOLVENT_LOOPS, "Ciclos");
		
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_W1, "w1");
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_W2, "w2");
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_W3, "w3");
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_CANDIDATE_DEFICIENCE, "Posibles Deficiencias");
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_CANDIDATE_FREEDOM_DEGREE, "Posibles Libertades");
		markedResolventLabelMap.put(AbstractAnalysis.MARKED_RESOLVENT_MARKED_EDGES, "Arcos Marcados");
		
		algorithLabelMap.put(AbstractAnalysis.ALGORITHM_REMOVED, "Nodos Eliminados");
		algorithLabelMap.put(AbstractAnalysis.ALGORITHM_COMPONENTS, "Componentes");
		algorithLabelMap.put(AbstractAnalysis.ALGORITHM_STRONG_CONNECTED_COMPONENTS, "SCC");
		algorithLabelMap.put(AbstractAnalysis.ALGORITHM_LOOPS, "Ciclos");
		
		jDesktopPane.add(moledInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(modelInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(situationInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(problemInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(matchingProblemInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(resolventInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(markedResolventInternalFrame, JDesktopPane.DEFAULT_LAYER);
		jDesktopPane.add(algorithmInternalFrame, JDesktopPane.DEFAULT_LAYER);

		rootNode.add(moledNode);
		rootNode.add(modelNode);
		rootNode.add(situationNode);
		rootNode.add(problemNode);
		rootNode.add(matchingProblemNode);
		rootNode.add(resolventNode);
		rootNode.add(markedResolventNode);
		rootNode.add(algorithmNode);
		
		DefaultTreeModel tm = (DefaultTreeModel) jTree.getModel();
		tm.setRoot(rootNode);
		
		jTree.setRootVisible(false);
		jTree.setShowsRootHandles(true);		
		
		jTree.addMouseListener(treeMouseListener);
		jTree.setCellRenderer(new MyTreeCellRenderer());
		
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.add(new JScrollPane(jTree));
		jSplitPane.add(jDesktopPane);
		
		getContentPane().add(jToolBar, BorderLayout.NORTH);
		getContentPane().add(jSplitPane, BorderLayout.CENTER);		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}

	protected void updateMatching() {
		// TODO Auto-generated method stub
		
		Vector<Integer> items = new Vector<Integer>();
		
		if (mp != null) {
			for (int i = 0; i < mp.getAvailableMatchings(); i ++) {
				items.add(i);
			}
		}	
		
		JComboBox<Integer> newMessage = new JComboBox<Integer>(items);
		
		JOptionPane pane = new JOptionPane();
		pane.setMessageType(JOptionPane.PLAIN_MESSAGE);
		pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		pane.setMessage(newMessage);
		
		JDialog dialog = pane.createDialog("Seleccionar Pareos");
		dialog.setVisible(true);
		
		Object value = pane.getValue();
		if (value != null && value.equals(JOptionPane.OK_OPTION)) {
			int index = newMessage.getSelectedIndex();	
			
			mp.setMatchingIndex(index);
			
			selectMatching(moledInternalFrame.getMoled());
			
		}
		
		
	}

	public JMainFrame() throws HeadlessException {
		initComponents();
	}

	public JMainFrame(GraphicsConfiguration gc) {
		super(gc);
		initComponents();
	}

	public JMainFrame(String title) throws HeadlessException {
		super(title);
		initComponents();
	}

	public JMainFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		initComponents();
	}
	
	MatchingProblemAnalysis mp = null;
	
	private void selectMatching(Moled moled) {
	
		BGraph g = null;
		Map<Integer, Object> valueMap = new HashMap<Integer, Object>();
		
		analysisMap.clear();
		
		if(moled != null){
			g = Helper.createGraph(moled, valueMap, analysisMap);
		}		
		
		new ModelAnalysis().execute(g, analysisMap);
		modelInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(modelNode, true, valueMap, modelLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(modelNode);
		
		new SituationAnalysis().execute(g, analysisMap);
		situationInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(situationNode, true, valueMap, situationLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(situationNode);
		
		new ProblemAnalysis().execute(g, analysisMap);
		problemInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(problemNode, true, valueMap, problemLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(problemNode);
		
		mp.execute(g, analysisMap);
		matchingProblemInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(matchingProblemNode, true, valueMap, matchingProblemLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(matchingProblemNode);
		
		new ResolventAnalysis().execute(g, analysisMap);
		resolventInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(resolventNode, true, valueMap, resolventLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(resolventNode);
		
		new MarkedResolventAnalysis().execute(g, analysisMap);
		markedResolventInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(markedResolventNode, true, valueMap, markedResolventLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(markedResolventNode);
		
		new AlgorithmAnalysis().execute(g, analysisMap);
		algorithmInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(algorithmNode, true, valueMap, algorithLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(algorithmNode);
	}
	
	private void updateGraphs(Moled moled){
		
		if (mp == null) {
			mp = new MatchingProblemAnalysis();
		}
	
		BGraph g = null;
		Map<Integer, Object> valueMap = new HashMap<Integer, Object>();
		
		analysisMap.clear();
		
		if(moled != null){
			g = Helper.createGraph(moled, valueMap, analysisMap);
		}		
		
		new ModelAnalysis().execute(g, analysisMap);
		modelInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(modelNode, true, valueMap, modelLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(modelNode);
		
		new SituationAnalysis().execute(g, analysisMap);
		situationInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(situationNode, true, valueMap, situationLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(situationNode);
		
		new ProblemAnalysis().execute(g, analysisMap);
		problemInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(problemNode, true, valueMap, problemLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(problemNode);
		
		mp.reset();
		
		mp.execute(g, analysisMap);
		matchingProblemInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(matchingProblemNode, true, valueMap, matchingProblemLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(matchingProblemNode);
		
		new ResolventAnalysis().execute(g, analysisMap);
		resolventInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(resolventNode, true, valueMap, resolventLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(resolventNode);
		
		new MarkedResolventAnalysis().execute(g, analysisMap);
		markedResolventInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(markedResolventNode, true, valueMap, markedResolventLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(markedResolventNode);
		
		new AlgorithmAnalysis().execute(g, analysisMap);
		algorithmInternalFrame.update(g, valueMap, analysisMap);
		TreeNodeBuilder.update(algorithmNode, true, valueMap, algorithLabelMap, analysisMap);
		((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(algorithmNode);
		
	}
	
	public void doSync(){
		updateGraphs(moledInternalFrame.getMoled());
	}
	
	public void doVerticalOrientation() {
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);		
	}

	public void doHorizontalOrientation() {
		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
	}

	private void doVerticalView(List<JInternalFrame> l, int x, int y, int width, int height){
		if(!l.isEmpty()){
			
			int part = height / l.size();
			
			for(int i = 0; i < l.size(); i ++){
				int _x = x;
				int _y = (i * part) + y;
				int _width = width;
				int _height = part;
				
				l.get(i).setBounds(_x, _y, _width, _height);
			}
		}	

	}
	
	public void doVerticalView(){
		JInternalFrame[] frames = jDesktopPane.getAllFrames();
		List<JInternalFrame> visibleFrames = new ArrayList<JInternalFrame>();
		
		for(int i = 0; i < frames.length; i ++){
			if(frames[i].isVisible()){
				visibleFrames.add(frames[i]);
			}
		}
		
		int width = jDesktopPane.getWidth();
		int height = jDesktopPane.getHeight();
		
		doVerticalView(visibleFrames, 0, 0, width, height);

	}
	
	private void doHorizontalView(List<JInternalFrame> l, int x, int y, int width, int height){
		if(!l.isEmpty()){
			
			int part = width / l.size();
			
			for(int i = 0; i < l.size(); i ++){
				int _x = (i * part) + x;
				int _y = y;
				int _width = part;
				int _height = height;
				
				l.get(i).setBounds(_x, _y, _width, _height);
			}
		}
	}
	
	public void doHorizontalView(){	
		
		JInternalFrame[] frames = jDesktopPane.getAllFrames();
		List<JInternalFrame> visibleFrames = new ArrayList<JInternalFrame>();
		
		for(int i = 0; i < frames.length; i ++){
			if(frames[i].isVisible()){
				visibleFrames.add(frames[i]);
			}
		}
		
		int width = jDesktopPane.getWidth();
		int height = jDesktopPane.getHeight();
		
		doHorizontalView(visibleFrames, 0, 0, width, height);

	}	

	public void doRowView() {
		JInternalFrame[] frames = jDesktopPane.getAllFrames();
		List<JInternalFrame> evenVisibleFrames = new ArrayList<JInternalFrame>();
		List<JInternalFrame> oddVisibleFrames = new ArrayList<JInternalFrame>();
		
		for(int i = 0; i < frames.length; i ++){
			if(frames[i].isVisible()){				
				if(i % 2 == 0){
					evenVisibleFrames.add(frames[i]);
				} else{
					oddVisibleFrames.add(frames[i]);
				}
			}
		}
		
		int width = jDesktopPane.getWidth();
		int height = jDesktopPane.getHeight();
		
		int part = height / 2;
		doHorizontalView(evenVisibleFrames, 0, 0, width, part);
		doHorizontalView(oddVisibleFrames, 0, part, width, part);
		
	}

	public void doStackView() {
		JInternalFrame[] frames = jDesktopPane.getAllFrames();
		List<JInternalFrame> visibleFrames = new ArrayList<JInternalFrame>();
		
		for(int i = 0; i < frames.length; i ++){
			if(frames[i].isVisible()){
				visibleFrames.add(frames[i]);
			}
		}
		
//		int width = jDesktopPane.getWidth();
//		int height = jDesktopPane.getHeight();
//		int gap = 9;
		
		if(!visibleFrames.isEmpty()){
			
		}
	}

}
