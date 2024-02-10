package cujae.map.j2g.graphx;

import java.util.Map;

import com.mxgraph.view.mxGraph;

import cujae.map.j2g.dgraphm.model.BEdge;

public class CellEdgeEvent extends CellEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4330696640981543730L;
	
	private final BEdge edge;
	
	public CellEdgeEvent(Object source, Object cellX, mxGraph graphX,
			Map<String, Object> analysisMap, BEdge edge) {
		super(source, cellX, graphX, analysisMap);
		this.edge = edge;
	}

	public BEdge getEdge() {
		return edge;
	}

}
