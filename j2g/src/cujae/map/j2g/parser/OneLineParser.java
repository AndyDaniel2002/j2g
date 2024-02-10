package cujae.map.j2g.parser;

import java.io.InputStream;
import java.io.Reader;

import cujae.map.j2g.parser.impl.ParseException;

public interface OneLineParser {
	
	public String getName();
	
	public ThokenCollector parse(InputStream stream) throws ParseException;
	
	public ThokenCollector parse(InputStream stream, String encoding) throws ParseException;
	
	public ThokenCollector parse(Reader stream) throws ParseException;

}
