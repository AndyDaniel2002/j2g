package cujae.map.j2g.dgraphm.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class AnalysisPrinter {
	
	public static final int DEFAULT_GAP = 2;
	
	public static final int DEFAULT_WIDTH = 80;
	
	public static String gap2whitespace(int gap){
		StringBuffer s = new StringBuffer();
		for(int i = 0; i < gap; i ++){
			s.append(" ");
		}
		return s.toString();
	}
	
	private static void printBoolean(OutputStream os, String key, Boolean value, int gap){
		PrintWriter pw = new PrintWriter(os);
		pw.println(String.format("%s%s:%b", gap2whitespace(gap), key, value));
	}
	
	private static void printCollection(OutputStream os, String key, Collection<?> c, 
			Map<Integer, String> labelMap, int gap, int width){
		
	}
	
	private static void printMap(OutputStream os, String key, Map<?, ?> m, 
			Map<Integer, String> labelMap, int gap, int width){
		
	}
	
	public static void print(OutputStream os, Map<String, Object> analysisMap, 
			Map<Integer, String> labelMap, int gap, int width){
		
		for(Iterator<Map.Entry<String, Object>> it = analysisMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<String, ?> next = it.next();
			
			String key = next.getKey();
			Object value = next.getValue();
			
			if(value instanceof Boolean){
				printBoolean(os, key, (Boolean) value, gap);
			} else if(value instanceof Collection<?>){
				printCollection(os, key, (Collection<?>)value, labelMap, gap, width);
			} else if(value instanceof Map<?, ?>){
				printMap(os, key, (Map<?, ?>) value, labelMap, gap, width);
			}
		}
	}

}
