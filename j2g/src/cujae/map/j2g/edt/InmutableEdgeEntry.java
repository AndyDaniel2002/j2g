package cujae.map.j2g.edt;

public class InmutableEdgeEntry extends MutableEdgeEntry {

	public InmutableEdgeEntry(int variableIndex, int relationIndex,
			int direction) {
		super(variableIndex, relationIndex, direction);
	}

	@Override
	public void setDirection(int direction) {
		throw new UnsupportedOperationException();
	}
	
	

}
