package cujae.map.j2g.dgraphm.util;

import java.util.EventObject;
import java.util.Map;

import cujae.map.j2g.dgraphm.model.BGraph;

public class AnalysisEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1373967419206211495L;
	
	private final int type;
	private final BGraph g;
	private final Map<String, Object> m;
	
	public AnalysisEvent(Object source, int type, BGraph g,
			Map<String, Object> m) {
		super(source);
		this.type = type;
		this.g = g;
		this.m = m;
	}
	
	public int getType() {
		return type;
	}
	
	public BGraph getG() {
		return g;
	}
	
	public Map<String, Object> getM() {
		return m;
	}

}
