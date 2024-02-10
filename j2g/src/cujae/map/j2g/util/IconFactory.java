package cujae.map.j2g.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import cujae.map.j2g.dgraphm.util.Flags;

public abstract class IconFactory {	
	
	public static final String BASE = "/cujae/map/j2g/resource/16";
	
	private static Icon syncIcon;
	
	private static Icon verticalViewIcon;
	
	private static Icon horizontalViewIcon;
	
	private static Icon stackViewIcon;
	
	private static Icon rowViewIcon;
	
	private static Icon verticalOrientationIcon;
	
	private static Icon horizontalOrientationIcon;
	
	private static Icon zoomInIcon;
	
	private static Icon zoomOutIcon;
	
	private static Icon zoom100Icon;
	
	private static Icon hierarchicalLayoutNorthIcon;
	
	private static Icon hierarchicalLayoutWestIcon;
	
	private static Icon circleLayoutIcon;
	
	private static Icon organicLayoutIcon;
	
	private static Icon exportIcon;
	
	private static Icon saveIcon;
	
	private static Icon saveAsIcon;
	
	private static Icon testOkIcon;
	
	private static Icon testFailIcon;
	
	private static Icon testErrorIcon;
	
	private static Icon variableIcon;
	
	private static Icon functionIcon;
	
	private static Icon edgeIcon;
	
	private static Icon newFileIcon;
	
	private static Icon openFileIcon;
	
	private static Icon openFolderAlertIcon;
	
	private static Icon openFolderIcon;
	
	private static Icon closeFolderAlertIcon;
	
	private static Icon closeFolderIcon;
	
	private static Icon addIcon;
	
	private static Icon deleteIcon;
	
	private static Icon upIcon;
	
	private static Icon downIcon;
	
	private static Icon solveIcon;
	
	private static Icon errorIcon;
	
	private static Icon warningIcon;
	
	private static Icon errorWarningIcon;
	
	private static Icon blankIcon;
	
	private static Icon logsIcon;
	
	private static Icon infoIcon;
	
	public static Icon getSyncIcon() {
		if(syncIcon == null){
			syncIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/updatetree.gif", BASE)));
		}
		return syncIcon;
	}

	public static Icon getVerticalViewIcon() {
		if(verticalViewIcon == null){
			verticalViewIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/verticalview.gif", BASE)));
		}
		return verticalViewIcon;
	}

	public static Icon getHorizontalViewIcon() {
		if(horizontalViewIcon == null){
			horizontalViewIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/horizontalview.gif", BASE)));
		}
		return horizontalViewIcon;
	}

	public static Icon getStackViewIcon() {
		if(stackViewIcon == null){
			stackViewIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/stackview.gif", BASE)));
		}
		return stackViewIcon;
	}

	public static Icon getRowViewIcon() {
		if(rowViewIcon == null){
			rowViewIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/rowview.gif", BASE)));
		}
		return rowViewIcon;
	}

	public static Icon getVerticalOrientationIcon() {
		if(verticalOrientationIcon == null){
			verticalOrientationIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/verticalorientation.gif", BASE)));
		}
		return verticalOrientationIcon;
	}

	public static Icon getHorizontalOrientationIcon() {
		if(horizontalOrientationIcon == null){
			horizontalOrientationIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/horizontalorientation.gif", BASE)));
		}
		return horizontalOrientationIcon;
	}

	public static Icon getZoomInIcon() {
		if(zoomInIcon == null){
			zoomInIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/zoomin.gif", BASE)));
		}
		return zoomInIcon;
	}

	public static Icon getZoomOutIcon() {
		if(zoomOutIcon == null){
			zoomOutIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/zoomout.gif", BASE)));
		}
		return zoomOutIcon;
	}

	public static Icon getZoom100Icon() {
		if(zoom100Icon == null){
			zoom100Icon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/zoom100.gif", BASE)));
		}
		return zoom100Icon;
	}

	public static Icon getHierarchicalLayoutNorthIcon() {
		if(hierarchicalLayoutNorthIcon == null){
			hierarchicalLayoutNorthIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/layout_tree_down.gif", BASE)));
		}
		return hierarchicalLayoutNorthIcon;
	}

	public static Icon getHierarchicalLayoutWestIcon() {
		if(hierarchicalLayoutWestIcon == null){
			hierarchicalLayoutWestIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/layout_tree_right.gif", BASE)));
		}
		return hierarchicalLayoutWestIcon;
	}

	public static Icon getCircleLayoutIcon() {
		if(circleLayoutIcon == null){
			circleLayoutIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/circlelayout.gif", BASE)));
		}
		return circleLayoutIcon;
	}

	public static Icon getOrganicLayoutIcon() {
		if(organicLayoutIcon == null){
			organicLayoutIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/organiclayout.gif", BASE)));
		}
		return organicLayoutIcon;
	}

	public static Icon getExportIcon() {
		if(exportIcon == null){
			exportIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/export.gif", BASE)));
		}
		return exportIcon;
	}

	public static Icon getSaveIcon() {
		if(saveIcon == null){
			saveIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/save.gif", BASE)));
		}
		return saveIcon;
	}

	public static Icon getSaveAsIcon() {
		if(saveAsIcon == null){
			saveAsIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/saveas.gif", BASE)));
		}
		return saveAsIcon;
	}

	public static Icon getTestOkIcon() {
		if(testOkIcon == null){
			testOkIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/testok.gif", BASE)));
		}
		return testOkIcon;
	}

	public static Icon getTestFailIcon() {
		if(testFailIcon == null){
			testFailIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/testfail.gif", BASE)));
		}
		return testFailIcon;
	}

	public static Icon getTestErrorIcon() {
		if(testErrorIcon == null){
			testErrorIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/testerror.gif", BASE)));
		}
		return testErrorIcon;
	}

	public static Icon getVariableIcon() {
		if(variableIcon == null){
			variableIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/variable.gif", BASE)));
		}
		return variableIcon;
	}

	public static Icon getFunctionIcon() {
		if(functionIcon == null){
			functionIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/function.gif", BASE)));
		}
		return functionIcon;
	}
	
	public static Icon getEdgeIcon() {
		if(edgeIcon == null){
			edgeIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/edge.gif", BASE)));
		}
		return edgeIcon;
	}

	public static Icon getNewFileIcon() {
		if(newFileIcon == null){
			newFileIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/newfile.gif", BASE)));
		}
		return newFileIcon;
	}

	public static Icon getOpenFileIcon() {
		if(openFileIcon == null){
			openFileIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/projectopened_obj.gif", BASE)));
		}
		return openFileIcon;
	}
	
	public static Icon getOpenFolderIcon() {
		if(openFolderIcon == null){
			openFolderIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/openfolder.gif", BASE)));
		}
		return openFolderIcon;
	}
	
	public static Icon getOpenFolderAlertIcon(){
		if(openFolderAlertIcon == null){
			openFolderAlertIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/openfolderalert.gif", BASE)));
		}
		return openFolderAlertIcon;
	}	
	
	public static Icon getCloseFolderIcon() {
		if(closeFolderIcon == null){
			closeFolderIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/closefolder.gif", BASE)));
		}
		return closeFolderIcon;
	}
	
	public static Icon getCloseFolderAlertIcon(){
		if(closeFolderAlertIcon == null){
			closeFolderAlertIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/closefolderalert.gif", BASE)));
		}
		return closeFolderAlertIcon;
	}	

	public static Icon getAddIcon() {
		if(addIcon == null){
			addIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/add.gif", BASE)));
		}
		return addIcon;
	}

	public static Icon getDeleteIcon() {
		if(deleteIcon == null){
			deleteIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/delete.gif", BASE)));
		}
		return deleteIcon;
	}

	public static Icon getUpIcon() {
		if(upIcon == null){
			upIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/up.gif", BASE)));
		}
		return upIcon;
	}

	public static Icon getDownIcon() {
		if(downIcon == null){
			downIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/down.gif", BASE)));
		}
		return downIcon;
	}
	
	public static Icon getSolveIcon() {
		if(solveIcon == null){
			solveIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/solve.gif", BASE)));
		}
		return solveIcon;
	}
	
	public static Icon getErrorIcon(){
		if(errorIcon == null){
			errorIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/error.gif", BASE)));
		}
		return errorIcon;
	}	

	public static Icon getWarningIcon() {
		if(warningIcon == null){
			warningIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/warning.gif", BASE)));
		}
		return warningIcon;
	}

	public static Icon getErrorWarningIcon() {
		if(errorWarningIcon == null){
			errorWarningIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/errorwarning.gif", BASE)));
		}
		return errorWarningIcon;
	}
	
	public static Icon getBlankIcon() {
		if(blankIcon == null){
			blankIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/blank.gif", BASE)));
		}
		return blankIcon;
	}

	public static Icon getLogsIcon(){
		if(logsIcon == null){
			logsIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/problems_view.gif", BASE)));
		}
		return logsIcon;
	}
	
	public static Icon getInfoIcon(){
		if(infoIcon == null){
			infoIcon = new ImageIcon(IconFactory.class.getResource(
					String.format("%s/info.gif", BASE)));
		}
		return infoIcon;
	}
	
	public static final int SQUARE_TYPE = 0;
	
	public static final int DIAMOND_TYPE = 1;
	
	public static final int CIRCLE_TYPE = 2;
	
	public static final int MASK = SQUARE_TYPE | DIAMOND_TYPE | CIRCLE_TYPE;
	
	public static void paintSquareIcon(Component c, Graphics g, int x, int y, int width, int height, int flags){
		g.setColor(c.getBackground());
		g.fillRect(x, y, width, height);				
		
		if(Flags.isError(flags)){
			g.setColor(Color.RED);
			g.fillRect(x, y, width / 2, height);
		}								
		
//		if(Flags.isWarning(flags)){
//			g.setColor(Color.YELLOW);
//			g.fillRect( x + (width / 2), y, width / 2, height);
//		}
	}
	
	public static void paintDiamondIcon(Component c, Graphics g, int x, int y, int width, int height, int flags){
		g.setColor(c.getBackground());
		g.fillRect(x, y, width, height);				
		
		if(Flags.isError(flags)){
			g.setColor(Color.RED);
			g.fillRect(x, y, width / 2, height);
		}								
		
//		if(Flags.isWarning(flags)){
//			g.setColor(Color.YELLOW);
//			g.fillRect( x + (width / 2), y, width / 2, width);
//		}
	}
	
	public static void paintCircleIcon(Component c, Graphics g, int x, int y, int width, int height, int flags){
		g.setColor(c.getBackground());
		g.fillRect(x, y, width, height);				
		
		if(Flags.isError(flags)){
			g.setColor(Color.RED);
			g.fillRect(x, y, width / 2, height);
		}								
		
//		if(Flags.isWarning(flags)){
//			g.setColor(Color.YELLOW);
//			g.fillRect( x + (width / 2), y, width / 2, width);
//		}
	}
	
	public static void paintIcon(Component c, Graphics g, int x, int y, int width, int height, int flags, int type){
		switch(type & MASK){
		case SQUARE_TYPE: 
			paintSquareIcon(c, g, x, type, width, height, flags); 
			break;
		case DIAMOND_TYPE: 
			paintDiamondIcon(c, g, x, type, width, height, flags); 
			break;
		case CIRCLE_TYPE: 
			paintCircleIcon(c, g, x, type, width, height, flags); 
			break;
		}
	}
	
	public static class DefaultIcon implements Icon{
		
		private final int iconWidth;
		private final int iconHeight;
		private final int flags;
		private final int type;

		public DefaultIcon(int iconWidth, int iconHeight, int flags, int type) {
			super();
			this.iconWidth = iconWidth;
			this.iconHeight = iconHeight;
			this.flags = flags;
			this.type = type;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			IconFactory.paintIcon(c, g, x, y, iconWidth, iconHeight, flags, type);
		}

		@Override
		public int getIconWidth() {
			return iconWidth;
		}

		@Override
		public int getIconHeight() {
			return iconHeight;
		}		

		public int getFlags() {
			return flags;
		}

		public int getType() {
			return type;
		}		
		
	}
	
	public static Icon createIcon(int iconWidth, int iconHeight, int flags, int type){
		return new DefaultIcon(iconWidth, iconHeight, flags, type); 				
	}

}
