package cujae.map.j2g.content.util;

public class TreeEvent {

	private String input;
	private int lineNumber;
	
	public TreeEvent(String input, int lineNumber) {
		super();
		this.input = input;
		this.lineNumber = lineNumber;
	}

	public String getInput() {
		return input;
	}

	public int getLineNumber() {
		return lineNumber;
	}

}
