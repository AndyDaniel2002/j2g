package cujae.map.j2g.parser.impl;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import cujae.map.j2g.parser.OneLineParser;
import cujae.map.j2g.parser.OneLineParserFactory;
import cujae.map.j2g.parser.ThokenCollector;

public class PlainParserFactory extends OneLineParserFactory {
	
	private final String NAME = null;

	@Override
	public boolean accept(File pathname) {
		return pathname != null && pathname.isFile();
	}
	

	@Override
	public boolean accept(String name) {
		return true;
	}

	@Override
	public OneLineParser newOneLineParser() {		
		return new OneLineParser() {

			@Override
			public String getName() {
				return NAME;
			}

			@Override
			public ThokenCollector parse(Reader stream) throws ParseException {
			
				Plain parser = new Plain(stream);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;
				
			}
			
			@Override
			public ThokenCollector parse(InputStream stream, String encoding)
					throws ParseException {
				
				Plain parser = new Plain(stream, encoding);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;

			}
			
			@Override
			public ThokenCollector parse(InputStream stream) throws ParseException {
				
				Plain parser = new Plain(stream);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;

			}
		};
	}

}
