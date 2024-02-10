package cujae.map.j2g.content.util;

import java.util.Formattable;
import java.util.Formatter;

public class ByteArrayFormattable implements Formattable {
	
	private final byte[] bytes;

	public ByteArrayFormattable(byte[] bytes) {
		super();
		this.bytes = bytes;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width,
			int precision) {
		
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < bytes.length; i ++){
			buffer.append(String.format("%X", bytes[i]));
			buffer.append(",");
		}
		
		formatter.format("%s", buffer.toString());

	}

}
