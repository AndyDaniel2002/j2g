package cujae.map.j2g.util;

import cujae.map.j2g.graphml.ValueHandler;
import cujae.map.j2g.util.Helper.CellValue;

public class CellValueHandler implements ValueHandler {

	@Override
	public String getName(Object value) {
		return ((CellValue)value).getLabel();
	}

	@Override
	public String getDescription(Object value) {
		return ((CellValue)value).getDescription();
	}

	@Override
	public String getURL(Object value) {
		return null;
	}

}
