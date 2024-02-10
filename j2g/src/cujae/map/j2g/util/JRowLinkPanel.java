package cujae.map.j2g.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

public class JRowLinkPanel extends JPanel {
	
	public static final int MAX_DY = 16;
	
	public static final int ROW_LINK_HEIGHT = 4;
	
	public static final String MAX_DY_PROPERTY = "maxDy";
	
	public static final String ROW_LINK_HEIGHT_PROPERTY = "rowLinkHeight";
	
	public static final String MODEL_PROPERTY = "model";

	/**
	 * 
	 */
	private static final long serialVersionUID = -383328286035168132L;
	
	private final PropertyChangeListener maxDyPropertyChangeListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			repaint();
		}
	};
	
	private final PropertyChangeListener rowLinkHeightPropertyChangeListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			repaint();
		}
	};
	
	private final PropertyChangeListener modelPropertyChangeListener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			repaint();
		}
	};
	
	private final RowLinkModelListener modelListener = new RowLinkModelListener() {
		
		@Override
		public void modelChanged(RowLinkModelEvent evt) {
			repaint();
		}
	};
	
	private int maxDy = MAX_DY;
	private int rowLinkHeight = ROW_LINK_HEIGHT;
	private RowLinkModel model;
	
	private void registerPropertyChangeListeners(){
		addPropertyChangeListener(MAX_DY_PROPERTY, maxDyPropertyChangeListener);
		addPropertyChangeListener(ROW_LINK_HEIGHT_PROPERTY, rowLinkHeightPropertyChangeListener);
		addPropertyChangeListener(MODEL_PROPERTY, modelPropertyChangeListener);
	}
	
	private void initComponents(){
		registerPropertyChangeListeners();
	}

	public JRowLinkPanel() {
		initComponents();
	}

	public JRowLinkPanel(LayoutManager layout) {
		super(layout);
		initComponents();
	}

	public JRowLinkPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initComponents();
	}

	public JRowLinkPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initComponents();
	}

	public int getMaxDy() {
		return maxDy;
	}

	public void setMaxDy(int maxDy) {
		int oldValue = this.maxDy;
		this.maxDy = Math.abs(maxDy);
		firePropertyChange(MAX_DY_PROPERTY, oldValue, this.maxDy);
	}

	public int getRowLinkHeight() {
		return rowLinkHeight;
	}

	public void setRowLinkHeight(int rowLinkHeight) {
		int oldValue = this.rowLinkHeight;
		this.rowLinkHeight = Math.abs(rowLinkHeight);
		firePropertyChange(ROW_LINK_HEIGHT_PROPERTY, oldValue, this.rowLinkHeight);
	}

	public RowLinkModel getModel() {
		return model;
	}

	public void setModel(RowLinkModel model) {
		RowLinkModel oldValue = this.model;
		if(oldValue != null){
			oldValue.removeModelListener(modelListener);
		}
		this.model = model;
		if(this.model != null){
			this.model.addModelListener(modelListener);
		}
		firePropertyChange(MODEL_PROPERTY, oldValue, this.model);
	}
	
	public int getRow(int x, int y){
        int row = -1;
        if(model != null){
        	int size = model.getSize();
        	if(size != 0){
        		Graphics2D g2d = (Graphics2D) getGraphics();        		
        		AffineTransform aff = g2d.getTransform(); 
        		double sy = (double)getHeight() / (size * maxDy);
	            if(sy >= 1){
	            	sy = 1;	            	
	            }
        		aff.setToScale(1, sy);
      		
        		for(int i = 0; row == -1 && i < size; i ++){
        			Point a = new Point(0, i * maxDy);
        			Point2D b = aff.transform(a, null);
        			int Y = (int) b.getY();
        			int value = model.getValue(i);            			
        			if(value != 0){
        				if(y >= Y && y <= (Y + rowLinkHeight)){
        					row = i;
        				}
        			}        			
        		}
        	} 
        }
        return row;
	}
	
	private void paintElement1(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		if(model != null){
			int size = model.getSize();
			if(size != 0){    
	            double sy = (double)getHeight() / (size * maxDy);	            
	            if(sy >= 1){
	            	sy = 1;	            	
	            }
	            
	            AffineTransform aff0 = g2d.getTransform();
	            aff0.setToScale(1, sy);	            
	            
	            for(int i = 0; i < size; i ++){ 
	            	Point a = new Point(0, i * maxDy);	            	
	            	Point2D b = aff0.transform(a, null);
	            	int y = (int) b.getY();
	                switch(model.getValue(i)){                
                    case -1:                    	
                        g.setColor(Color.RED);  
                        g.fillRect(0, y, getWidth(), rowLinkHeight);
                        break;
                    case 1:                     	
                    	g.setColor(Color.ORANGE);    
                    	g.fillRect(0, y, getWidth(), rowLinkHeight);
                        break;
	                }
	            }	            
			}			
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintElement1(g);
	}

}
