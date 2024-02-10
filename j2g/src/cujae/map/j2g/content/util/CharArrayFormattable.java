package cujae.map.j2g.content.util;

import java.util.Formattable;
import java.util.Formatter;

public class CharArrayFormattable implements Formattable {
	
	private final char[] chars;

	public CharArrayFormattable(char[] chars) {
		super();
		this.chars = chars;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width,
			int precision) {
		
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < chars.length; i ++){
			buffer.append(String.format("%c:%04X", chars[i], (int)chars[i]));
			buffer.append(',');
		}
		
		formatter.format("%s", buffer.toString());

	}

}
