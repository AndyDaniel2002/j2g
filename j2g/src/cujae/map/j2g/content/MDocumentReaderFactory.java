package cujae.map.j2g.content;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import cujae.map.j2g.content.impl.PlainReaderFactory;

public abstract class MDocumentReaderFactory implements FileFilter{
	
	public static final MDocumentReaderFactory DEFAULT_READER_FACTORY = new PlainReaderFactory();
	
	public static MDocumentReaderFactory getInstance(File pathname){
		MDocumentReaderFactory factory = null;
		ServiceLoader<MDocumentReaderFactory> serviceLoader = ServiceLoader.load(MDocumentReaderFactory.class);
		try {
			for(Iterator<MDocumentReaderFactory> it = serviceLoader.iterator(); 
					factory == null && it.hasNext();){
				MDocumentReaderFactory next = it.next();
				if(next.accept(pathname)){
					factory = next;
				}
			}
		} catch (ServiceConfigurationError e) {
			// TODO: handle exception
		}
		
		if(factory == null){
			factory = DEFAULT_READER_FACTORY;
		}
		
		return factory;		
	}
	
	public abstract MDocumentReader newDocumentReader();

}
