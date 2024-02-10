package cujae.map.j2g.edt;

public class MutableEdgeEntry implements EdgeEntry, Cloneable {
	
	public final int variableIndex;
	public final int relationIndex;
	
	private int direction;

	public MutableEdgeEntry(int variableIndex, int relationIndex, int direction) {
		super();
		this.variableIndex = variableIndex;
		this.relationIndex = relationIndex;
		this.direction = direction;
	}

	@Override
	public int getVariableIndex() {
		return variableIndex;
	}

	@Override
	public int getRelationIndex() {
		return relationIndex;
	}

	@Override
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}

}
