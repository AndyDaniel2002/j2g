package cujae.map.j2g.graphx;

import java.util.Map;

import com.mxgraph.view.mxGraph;

public class CellNodeEvent extends CellEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5075478506364422891L;
	
	private final Integer key;

	public CellNodeEvent(Object source, Object cellX, mxGraph graphX,
			Map<String, Object> analysisMap, Integer key) {
		super(source, cellX, graphX, analysisMap);
		this.key = key;
	}

	public Integer getKey() {
		return key;
	}


}
