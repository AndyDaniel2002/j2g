package cujae.map.j2g.graphx;

import java.util.EventObject;
import java.util.Map;

import com.mxgraph.view.mxGraph;

public abstract class CellEvent extends EventObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2379587576136952593L;
	
	private final Object cellX;
	private final mxGraph graphX;
	private final Map<String, Object> analysisMap;

	public CellEvent(Object source, Object cellX, mxGraph graphX, Map<String, Object> analysisMap) {
		super(source);
		this.cellX = cellX;
		this.graphX = graphX;
		this.analysisMap = analysisMap;
	}

	public Object getCell() {
		return cellX;
	}

	public mxGraph getGraphX() {
		return graphX;
	}

	public Map<String, Object> getAnalysisMap() {
		return analysisMap;
	}


}
