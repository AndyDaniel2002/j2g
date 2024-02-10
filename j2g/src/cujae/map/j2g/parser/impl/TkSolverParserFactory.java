package cujae.map.j2g.parser.impl;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import cujae.map.j2g.parser.OneLineParser;
import cujae.map.j2g.parser.OneLineParserFactory;
import cujae.map.j2g.parser.ThokenCollector;

public class TkSolverParserFactory extends OneLineParserFactory {
	
	private final String NAME = "tksolver";

	@Override
	public boolean accept(File pathname) {
		if(pathname != null && pathname.isFile()){
			String name = pathname.getName().toLowerCase();
			return name.endsWith(".tkw") || name.endsWith(".tkt");
		}
		return false;
	}	

	@Override
	public boolean accept(String name) {
		return NAME.equals(name);
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
				TkSolver parser = new TkSolver(stream);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;
				
			}
			
			@Override
			public ThokenCollector parse(InputStream stream, String encoding)
					throws ParseException {
				TkSolver parser = new TkSolver(stream, encoding);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;

			}
			
			@Override
			public ThokenCollector parse(InputStream stream) throws ParseException {
				TkSolver parser = new TkSolver(stream);
				
				ThokenCollector c = new ThokenCollector();
				parser.input(c);
				return c;

			}
		};
	}

}
