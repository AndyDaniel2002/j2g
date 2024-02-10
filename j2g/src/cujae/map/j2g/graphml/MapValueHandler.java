package cujae.map.j2g.graphml;

import java.util.Map;

public class MapValueHandler implements ValueHandler {

	@Override
	public String getName(Object value) {
		Map<?, ?> m = (Map<?, ?>)value;
		return String.format("%s", m.get(AbbGraphmlConstants.LABEL));
	}

	@Override
	public String getDescription(Object value) {
		Map<?, ?> m = (Map<?, ?>)value;
		return String.format("%s", m.get(AbbGraphmlConstants.DESCRIPTION));
	}

	@Override
	public String getURL(Object value) {
		Map<?, ?> m = (Map<?, ?>)value;
		return String.format("%s", m.get(AbbGraphmlConstants.URL));
	}

}
