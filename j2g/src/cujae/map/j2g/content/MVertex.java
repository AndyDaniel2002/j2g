package cujae.map.j2g.content;

public class MVertex extends MCell {
	
	private String name;
	private String description;
	private String status;

	public MVertex(int id, String name, String description, String status) {
		super(id);
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
