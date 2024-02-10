package cujae.map.j2g.util.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.util.IconFactory;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6496041547008754311L;
	
	private void updateIcons(boolean leaf, Object userValue, boolean alert){
		if(leaf){
			if(userValue instanceof Integer){
				if(((Integer)userValue) % 2 == 0){
					setLeafIcon(IconFactory.getVariableIcon());
				} else{
					setLeafIcon(IconFactory.getFunctionIcon());
				}
			} else if(userValue instanceof Boolean){
				if((Boolean)userValue){
					setLeafIcon(IconFactory.getTestOkIcon());
				} else{
					setLeafIcon(IconFactory.getTestErrorIcon());
				}
			} else if(userValue instanceof BEdge){
				setLeafIcon(IconFactory.getEdgeIcon());
			} else{
				setLeafIcon(getDefaultLeafIcon());
			}
		} else{		
			if(alert){
				setOpenIcon(IconFactory.getOpenFolderAlertIcon());
				setClosedIcon(IconFactory.getCloseFolderAlertIcon());
			} else{
				setOpenIcon(IconFactory.getOpenFolderIcon());
				setClosedIcon(IconFactory.getCloseFolderIcon());
			}
		}
	}
	
	public MyTreeCellRenderer() {
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		if(value instanceof MyTreeNode){
			MyTreeNode n = (MyTreeNode) value;
			updateIcons(leaf, n.getUserValue(), n.isAlert());		
		} else{
			updateIcons(leaf, null, false);
		}	

		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
	}
}
