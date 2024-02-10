package cujae.map.j2g.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.UIManager;

public final class Locales{
	
	public static final String LOCALE_PROPERTY = "locale";

	public static final String OK_BUTTON_TEXT = "okButtonText";

	public static final String CANCEL_BUTTON_TEXT = "cancelButtonText";

	public static final String YES_BUTTON_TEXT = "yesButtonText";

	public static final String NO_BUTTON_TEXT = "noButtonText";	
	
	private static final PropertyChangeListener l = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			
			ResourceBundle bundle = ResourceBundle.getBundle(Locales.class.getName(), (Locale)evt.getNewValue());
			
			UIManager.put("OptionPane.okButtonText", bundle.getString(OK_BUTTON_TEXT));
			UIManager.put("OptionPane.cancelButtonText", bundle.getString(CANCEL_BUTTON_TEXT));
			UIManager.put("OptionPane.yesButtonText", bundle.getString(YES_BUTTON_TEXT));
			UIManager.put("OptionPane.noButtonText", bundle.getString(NO_BUTTON_TEXT));
		}
	};
	
	public static Locales getInstance(){
		if(instance == null){
			instance = new Locales();
		}
		return instance;
	}	
	
	private static Locales instance;
	
	private PropertyChangeSupport support;
	private Locale locale;
	
	private Locales(){		
		support = new PropertyChangeSupport(this);
		support.addPropertyChangeListener(LOCALE_PROPERTY, l);
		locale = Locale.getDefault();		
	}

	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		if(locale != null){
			Object oldValue = this.locale;
			this.locale = locale;
			support.firePropertyChange(LOCALE_PROPERTY, oldValue, this.locale);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		support.removePropertyChangeListener(propertyName, listener);
	}	

}
