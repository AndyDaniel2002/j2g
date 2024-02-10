package cujae.map.j2g.dgraphm.model;

public class BEdgeImpl implements BEdge {
	
	private final int even;
	private final int odd;
	private final int direction;

	public BEdgeImpl(int even, int odd, int direction) {
		super();
		this.even = even;
		this.odd = odd;
		this.direction = direction;
	}

	@Override
	public int even() {
		return even;
	}

	@Override
	public int odd() {
		return odd;
	}

	@Override
	public int direction() {
		return direction;
	}



}
