package cujae.map.j2g.content;

public abstract class MCell {
	
	private int id;

	public MCell(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
