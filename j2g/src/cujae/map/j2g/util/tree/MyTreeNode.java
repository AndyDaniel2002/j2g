package cujae.map.j2g.util.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7125779319191795400L;
	
	private Object userValue;
	
	private boolean alert;	

	public MyTreeNode() {
	}

	public MyTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public Object getUserValue() {
		return userValue;
	}

	public void setUserValue(Object userValue) {
		this.userValue = userValue;
	}

}
