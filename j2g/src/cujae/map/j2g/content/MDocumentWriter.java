package cujae.map.j2g.content;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

import cujae.map.j2g.content.util.MDocumentConstants;

public class MDocumentWriter {
	
	public static final String SUFFIX = ".j2f";
	
	public static final String PARSER_NAME_FORMAT = "parser:%s";
	
	public static final String AUTO_SOLVE_FORMAT = "autosolve:%s";
	
	public static final String LOWEST_PRIORITY_FORMAT = "lowestpriority:%s";

	public static final String RENAME_POLICY_FORMAT = "renamepolicy:%s";
	
	public static final String VARIABLE_NAME_FORMAT = "variable:%d:name:%s";
	
	public static final String VARIABLE_DESCRIPTION_FORMAT = "variable:%d:description:%s";
	
	public static final String VARIABLE_STATUS_FORMAT = "variable:%d:status:%s";
	
	public static final String RELATION_NAME_FORMAT = "relation:%d:name:%s";
	
	public static final String RELATION_DESCRIPTION_FORMAT = "relation:%d:description:%s";
	
	public static final String RELATION_STATUS_FORMAT = "relation:%d:status:%s";
	
	public static final String ASYMMETRY_FORMAT = "asymmetry:%d:variable:%d:relation:%d:direction:%d";
	
	public void write(MDocument d, File file) throws IOException{
		write(d, file, MDocumentConstants.DEFAULT_CHARSET_NAME);
	}
	
	public void write(MDocument d, File file, String csn) throws IOException{
		write(d, new PrintWriter(file, csn));
	}
	
	public void write(MDocument d, String fileName) throws IOException{
		write(d, fileName, MDocumentConstants.DEFAULT_CHARSET_NAME);
	}
	
	public void write(MDocument d, String fileName, String csn) throws IOException{
		write(d, new File(fileName), csn);
	}
	
	public void write(MDocument d, OutputStream out) throws IOException{
		write(d, new PrintWriter(out));
	}
	
	public void write(MDocument d, Writer out) throws IOException{
		
		PrintWriter pw = new PrintWriter(out);
		pw.println(String.format(PARSER_NAME_FORMAT, d.getParserName()));
		pw.println(String.format(AUTO_SOLVE_FORMAT, d.getAutoSolve()));
		pw.println(String.format(LOWEST_PRIORITY_FORMAT, d.getLowestPriority()));
		pw.println(String.format(RENAME_POLICY_FORMAT, d.getRenamePolicy()));
		
		pw.println();		
		
		for(Iterator<MVertex> it = d.getVariables().iterator(); it.hasNext();){
			MVertex next = it.next();
			
			if(next.getName() != null){
				pw.println(String.format(VARIABLE_NAME_FORMAT, next.getId(), 
						next.getName()));
			}			
			if(next.getDescription() != null){
				pw.println(String.format(VARIABLE_DESCRIPTION_FORMAT, next.getId(), 
						next.getDescription()));
				
			}			
			if(next.getStatus() != null){
				pw.println(String.format(VARIABLE_STATUS_FORMAT, next.getId(), 
						next.getStatus()));
				
			}
		}		
		pw.println();
		
		for(Iterator<MVertex> it = d.getRelations().iterator(); it.hasNext();){
			MVertex next = it.next();
			
			if(next.getName() != null){
				pw.println(String.format(RELATION_NAME_FORMAT, next.getId(), 
						next.getName()));				
			}			
			if(next.getDescription() != null){
				pw.println(String.format(RELATION_DESCRIPTION_FORMAT, next.getId(), 
						next.getDescription()));				
			}	
			if(next.getStatus() != null){
				pw.println(String.format(RELATION_STATUS_FORMAT, next.getId(), 
						next.getStatus()));
				
			}
		}		
		pw.println();
		
		for(Iterator<MAsymmetry> it = d.getAsymmetry().iterator(); it.hasNext();){
			MAsymmetry next = it.next();
			
			pw.println(String.format(ASYMMETRY_FORMAT, next.getId(), 
					next.getVariable(), next.getRelation(), next.getDirection()));
			
		}
		pw.println();
		out.close();
		
	}

}
