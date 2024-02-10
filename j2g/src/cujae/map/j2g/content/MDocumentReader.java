package cujae.map.j2g.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public interface MDocumentReader {
	
	public MDocument read(InputStream in, String charsetName) throws IOException;
	
	public MDocument read(InputStream in, Charset cs) throws IOException;
	
	public MDocument read(InputStream in) throws IOException;
	
	public MDocument read(Reader in) throws IOException;

}
