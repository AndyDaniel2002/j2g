package cujae.map.j2g.graphx;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import cujae.map.j2g.dgraphm.analysis.AbstractAnalysis;
import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class GraphBuilder {
	
	public static final String COLOR_STRING_FORMAT_SMALL = "#%02X%02X%02X";
	
	public static final CellListener defaultCellListener = new CellListener() {
		
		@Override
		public void cellInserted(CellEvent evt) {
			
			mxCell cell = (mxCell) evt.getCell();
			
			if(evt instanceof CellNodeEvent){
				CellNodeEvent otherEvt = (CellNodeEvent) evt;
				
				String style = createDefaultNodeStyleName(otherEvt.getKey());
				cell.setStyle(style);
				
				if(!evt.getGraphX().getStylesheet().getStyles().containsKey(style)){
					evt.getGraphX().getStylesheet().putCellStyle(style, 
							GraphBuilder.createDefaultNodeStyle(otherEvt.getKey()));
				}
				
			} else if(evt instanceof CellEdgeEvent){
				CellEdgeEvent otherEvt = (CellEdgeEvent) evt;
				
				boolean isScc = isScc(otherEvt.getEdge(), otherEvt.getAnalysisMap());
				boolean isMarked = isMarked(otherEvt.getEdge(), otherEvt.getAnalysisMap());
				
				String style = GraphBuilder.createDefaultEdgeStyleName(otherEvt.getEdge(), isScc, isMarked);
				cell.setStyle(style);				
				
				if(!evt.getGraphX().getStylesheet().getStyles().containsKey(style)){
					evt.getGraphX().getStylesheet().putCellStyle(style, 
							GraphBuilder.createDefaultEdgeStyle(otherEvt.getEdge(), isScc, isMarked));
				}
			}
		}
	};
	
	public static String color2String(Color c){
		return String.format(COLOR_STRING_FORMAT_SMALL, c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public static String createDefaultNodeStyleName(Integer key){
		StringBuffer buffer = new StringBuffer("n");
		buffer.append(key % 2 == 0 ? "v" : "r");
		return buffer.toString();
	}
	
	public static Map<String, Object> createDefaultNodeStyle(Integer key){
		Map<String, Object> style = new HashMap<String, Object>();	
		
		style.put(mxConstants.STYLE_FILLCOLOR, color2String(key % 2 == 0 ? Color.YELLOW : Color.GREEN));
		style.put(mxConstants.STYLE_ROUNDED, Boolean.TRUE);
		style.put(mxConstants.STYLE_STROKECOLOR, color2String(Color.BLACK));
		style.put(mxConstants.STYLE_STROKEWIDTH, 1.0);
		
		return style;
	}
	
	public static String createDefaultEdgeStyleName(BEdge edge, boolean scc, boolean isMarked){
		StringBuffer buffer = new StringBuffer("e");
	
		int direction = edge.direction();		
		int signum = Integer.signum(direction);
		boolean even = direction % 2 == 0;		
		
		buffer.append(signum != 0 ? 0 : 1);
		buffer.append(!even ? 2 : 3);
		buffer.append(signum == 1 && even ? 4 : 5);
		
		if(scc){
			buffer.append("scc");
		}
		
		if(isMarked){
			buffer.append("marked");
		}
				
		return buffer.toString();
	}
	
	public static Map<String, Object> createDefaultEdgeStyle(BEdge edge, boolean isScc, boolean isMarked){
		Map<String, Object> style = new HashMap<String, Object>();	
		
		style.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
		style.put(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
		style.put(mxConstants.STYLE_STROKECOLOR, color2String(Color.BLACK));
		style.put(mxConstants.STYLE_STROKEWIDTH, 1.0);
		
		int direction = edge.direction();		
		int signum = Integer.signum(direction);
		boolean even = direction % 2 == 0;
				
		if(signum != 0){
			style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		}
		
		if(!even){
			style.put(mxConstants.STYLE_STROKEWIDTH, 2.0);
		} 

		if(signum == 1 && even){
			style.put(mxConstants.STYLE_STROKECOLOR, color2String(Color.RED));
		} 
		
		if(isScc){
			style.put(mxConstants.STYLE_DASHED, Boolean.TRUE);
		} 
		
		if(isMarked){
			style.put(mxConstants.STYLE_STROKECOLOR, color2String(Color.BLUE));
		}
		
		return style;
	}

	private final EventListenerList listenerList = new EventListenerList();
	
	private void dispatchEvent(CellEvent evt){
		CellListener[] listeners = listenerList.getListeners(CellListener.class);
		for(int i = 0; i < listeners.length; i ++){
			CellListener listener = listeners[i];
			listener.cellInserted(evt);
		}
	}
	
	private static boolean isScc(BEdge e, Map<String, Object> analysisMap){
		boolean b = false;
		
		Object value = analysisMap.get(AbstractAnalysis.RESOLVENT_STRONG_CONNECTED_COMPONENTS);
		if(value instanceof Collection<?>){
			Collection<?> c = (Collection<?>) value;
			for(Iterator<?> it = c.iterator(); !b && it.hasNext();){
				Object next = it.next();
				if(next instanceof Collection<?>){
					Collection<?> c2 = (Collection<?>) next;
					b = (c2.contains(e.even()) && c2.contains(e.odd()));
				}
			}
		}
		
		return b;
	}
	
	public static boolean isMarked(BEdge e, Map<String, Object> analysisMap){
		boolean b = false;
		
		Object value = analysisMap.get(AbstractAnalysis.MARKED_RESOLVENT_MARKED_EDGES);
		if(value instanceof Collection<?>){
			Collection<?> c = (Collection<?>) value;
			for(Iterator<?> it = c.iterator(); !b && it.hasNext();){
				Object next = it.next();
				if(next instanceof BEdge){
					BEdge otherE = (BEdge) next;
					b = (otherE.even() == e.even() && otherE.odd() == e.odd());
				}
			}
		}
		
		return b;
	}
	
	public static boolean isMatched(BEdge e, Map<String, Object> analysisMap){
		boolean b = false;
		
		Object value = analysisMap.get(AbstractAnalysis.MATCHING_PROBLEM_MAXIMUM_MATCH);
		if(value instanceof Collection<?>){
			Collection<?> c = (Collection<?>) value;
			for(Iterator<?> it = c.iterator(); !b && it.hasNext();){
				Object next = it.next();
				if(next instanceof BEdge){
					BEdge otherE = (BEdge) next;
					b = (otherE.even() == e.even() && otherE.odd() == e.odd());
				}
			}
		}
		
		return b;
	}
	
	public void addCellListener(CellListener l){
		listenerList.add(CellListener.class, l);
	}
	
	public void removeCellListener(CellListener l){
		listenerList.remove(CellListener.class, l);
	}	

	public void update(mxGraph graphX, boolean clearGraphX, BGraph graph, 
			Map<Integer, Object> valueMap, Map<String, Object> analysisMap){
		if(clearGraphX){
			graphX.selectAll();
			graphX.removeCells();
		}
		
		if(graph != null){
			graphX.getModel().beginUpdate();
			try{
				
				Object parent = graphX.getDefaultParent();
				
				Map<Integer, Object> variables = new HashMap<Integer, Object>();
				Map<Integer, Object> relations = new HashMap<Integer, Object>();
				
				boolean isEven = true;
				for(Iterator<Integer> it = graph.iterator(isEven); it.hasNext();){
					Integer next = it.next();
					Object value = null;
					if(valueMap != null){
						value = valueMap.get(next);
					}
					Object cellX = graphX.insertVertex(parent, null, value, 0, 0, 30, 30);
					variables.put(next, cellX);
					CellEvent evt = new CellNodeEvent(this, cellX, graphX, analysisMap, next);
					dispatchEvent(evt);
				}
				
				isEven = !isEven;
				for(Iterator<Integer> it = graph.iterator(isEven); it.hasNext();){
					Integer next = it.next();
					Object value = null;
					if(valueMap != null){
						value = valueMap.get(next);
					}							
					Object cellX = graphX.insertVertex(parent, null, value, 0, 0, 30, 30);
					relations.put(next, cellX);
					CellEvent evt = new CellNodeEvent(this, cellX, graphX,analysisMap, next);
					dispatchEvent(evt);
					
					for(Iterator<BEdge> it2 = graph.adjIterator(next); it2.hasNext();){
						BEdge next2 = it2.next();
						
						Object source = relations.get(next2.odd());
						Object target = variables.get(next2.even());
						
						if(Integer.signum(next2.direction()) < 0){
							Object temp = source;
							source = target;
							target = temp;
						}
						
						Object cellX2 = graphX.insertEdge(parent, null, null, source, target);
						CellEvent evt2 = new CellEdgeEvent(this, cellX2, graphX, analysisMap, next2);
						dispatchEvent(evt2);
					}
				}
				
			} finally{
				graphX.getModel().endUpdate();
			}
		}
	}

	public void addDefaultListener() {
		listenerList.add(CellListener.class, defaultCellListener);		
	}

}
