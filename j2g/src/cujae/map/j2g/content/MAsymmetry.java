package cujae.map.j2g.content;

public class MAsymmetry extends MCell{
	
	private int variable;
	private int relation;
	private int direction;
	
	public MAsymmetry(int id, int variable, int relation, int direction) {
		super(id);
		this.variable = variable;
		this.relation = relation;
		this.direction = direction;
	}

	public int getVariable() {
		return variable;
	}

	public void setVariable(int variable) {
		this.variable = variable;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
