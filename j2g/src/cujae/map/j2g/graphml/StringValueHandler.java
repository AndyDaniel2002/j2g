package cujae.map.j2g.graphml;


public class StringValueHandler implements ValueHandler {

	@Override
	public String getName(Object value) {
		return value.toString();
	}

	@Override
	public String getDescription(Object value) {
		return null;
	}

	@Override
	public String getURL(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
