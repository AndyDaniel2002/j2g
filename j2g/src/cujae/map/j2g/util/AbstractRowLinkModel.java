package cujae.map.j2g.util;

import javax.swing.event.EventListenerList;

public abstract class AbstractRowLinkModel implements RowLinkModel {

	private final EventListenerList listenerList = new EventListenerList();
	
	protected void fireModelChanged(){
		RowLinkModelListener[] listeners = getListeners();
		RowLinkModelEvent evt = new RowLinkModelEvent(this);
		for(int i = 0; i < listeners.length; i ++){
			listeners[i].modelChanged(evt);
		}
	}

	@Override
	public void addModelListener(RowLinkModelListener listener) {
		listenerList.add(RowLinkModelListener.class, listener);
	}

	@Override
	public void removeModelListener(RowLinkModelListener listener) {
		listenerList.remove(RowLinkModelListener.class, listener);
	}

	@Override
	public RowLinkModelListener[] getListeners() {
		return listenerList.getListeners(RowLinkModelListener.class);
	}

}
