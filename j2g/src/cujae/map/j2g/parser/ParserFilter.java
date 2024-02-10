package cujae.map.j2g.parser;

import java.io.FileFilter;

public interface ParserFilter extends FileFilter {
	
	public boolean accept(String name);	

}
