package cujae.map.j2g.util;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

public final class UmodifiableAction implements Action {
	
	private final Action delegate;

	public UmodifiableAction(Action delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		delegate.actionPerformed(e);
	}

	@Override
	public Object getValue(String key) {
		return delegate.getValue(key);
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		delegate.removePropertyChangeListener(listener);
	}

}
