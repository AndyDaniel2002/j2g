package cujae.map.j2g.ui.event;

import java.util.EventObject;

public class MoledEvent extends EventObject {	
	
	public static final int UPDATE_TYPE = 1;
	
	public static final int CREATE_TYPE = 2;
	
	public static final int DELETE_TYPE = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4279509236427722137L;

	private final int type;

	public MoledEvent(Object source, int type) {
		super(source);
		this.type = type;
	}

	public int getType() {
		return type;
	}


}
