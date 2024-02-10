package cujae.map.j2g.util;

import java.util.EventListener;

public interface RowLinkModelListener extends EventListener {
	
	public void modelChanged(RowLinkModelEvent evt);

}
