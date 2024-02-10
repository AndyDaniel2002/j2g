package cujae.map.j2g.parser;

import java.io.File;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import cujae.map.j2g.parser.impl.PlainParserFactory;

public abstract class OneLineParserFactory implements ParserFilter{
	
	public static final OneLineParserFactory DEFAULT_PARSER_FACTORY = new PlainParserFactory();
	
	public static OneLineParserFactory getInstance(File pathname){
		OneLineParserFactory factory = null;
		ServiceLoader<OneLineParserFactory> serviceLoader = ServiceLoader.load(OneLineParserFactory.class);
		try {
			for(Iterator<OneLineParserFactory> it = serviceLoader.iterator(); 
					factory == null && it.hasNext();){
				OneLineParserFactory next = it.next();
				if(next.accept(pathname)){
					factory = next;
				}
			}
		} catch (ServiceConfigurationError e) {
			// TODO: handle exception
		}
		
		if(factory == null){			
			factory = DEFAULT_PARSER_FACTORY;
		}
		
		return factory;		
	}
	
	public static OneLineParserFactory getInstance(String name){
		OneLineParserFactory factory = null;
		ServiceLoader<OneLineParserFactory> serviceLoader = ServiceLoader.load(OneLineParserFactory.class);
		try {
			for(Iterator<OneLineParserFactory> it = serviceLoader.iterator(); 
					factory == null && it.hasNext();){
				OneLineParserFactory next = it.next();
				if(next.accept(name)){
					factory = next;
				}
			}
		} catch (ServiceConfigurationError e) {
			// TODO: handle exception
		}
		
		if(factory == null){
			factory = DEFAULT_PARSER_FACTORY;
		}
		
		return factory;
	}
	
	public abstract OneLineParser newOneLineParser();	

}
