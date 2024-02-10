package cujae.map.j2g.content.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cujae.map.j2g.content.MAsymmetry;
import cujae.map.j2g.content.MCell;
import cujae.map.j2g.content.MDocument;
import cujae.map.j2g.content.MDocumentReader;
import cujae.map.j2g.content.MDocumentReaderFactory;
import cujae.map.j2g.content.MDocumentWriter;
import cujae.map.j2g.content.MVertex;
import cujae.map.j2g.content.util.MDocumentConstants;
import cujae.map.j2g.content.util.TreeEvent;
import cujae.map.j2g.content.util.TreeNode;

public class J2FReaderFactory extends MDocumentReaderFactory {
	
	public static final String PARSER_NAME_REGEX = "parser[\\.:;](.*)";
	
	public static final String AUTO_SOLVE_REGEX = "autosolve[\\.:;](.*)";
	
	public static final String LOWEST_PRIORITY_REGEX = "lowestpriority[\\.:;](.*)";
	
	public static final String RENAME_POLICY_REGEX = "renamepolicy[\\.:;](.*)";
	
	public static final String VARIABLE_NAME_REGEX = "variable[\\.:;]([-]?\\d+)[\\.:;]name[\\.:;](.*)";
	
	public static final String VARIABLE_DESCRIPTION_REGEX = "variable[\\.:;]([-]?\\d+)[\\.:;]description[\\.:;](.*)";
	
	public static final String VARIABLE_STATUS_REGEX = "variable[\\.:;]([-]?\\d+)[\\.:;]status[\\.:;](.*)";
	
	public static final String RELATION_NAME_REGEX = "relation[\\.:;]([-]?\\d+)[\\.:;]name[\\.:;](.*)";
	
	public static final String RELATION_DESCRIPTION_REGEX = "relation[\\.:;]([-]?\\d+)[\\.:;](?:description|image)[\\.:;](.*)";
	
	public static final String RELATION_IMAGE_REGEX = "relation\\.([-]?\\d+)\\.image\\.(.*)";
	
	public static final String RELATION_STATUS_REGEX = "relation[\\.:;]([-]?\\d+)[\\.:;]status[\\.:;](.*)";

	public static final String ASYMMETRY_REGEX = "(?:asymmetry|edge)[\\.:;]([-]?\\d+)[\\.:;]variable[\\.:;]([-]?\\d+)[\\.:;]relation[\\.:;]([-]?\\d+)[\\.:;]direction[\\.:;]([-]?\\d+)";	
	
	
	private static final TreeNode root;
	
	static{
		
		root = new TreeNode(0, "", Pattern.CASE_INSENSITIVE);
		
		TreeNode parserName = new TreeNode(1, PARSER_NAME_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				document.setParserName(getMatcher().group(1));
			}
			
		};
		
		TreeNode autoSolve = new TreeNode(2, AUTO_SOLVE_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				document.setAutoSolve(getMatcher().group(1));
			}
			
		};
		
		TreeNode lowestPriority = new TreeNode(3, LOWEST_PRIORITY_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				document.setLowestPriority(getMatcher().group(1));
			}
			
		};	
		
		TreeNode renamePolicy = new TreeNode(4, RENAME_POLICY_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				document.setRenamePolicy(getMatcher().group(1));
			}
			
		};
		
		TreeNode variableName = new TreeNode(5, VARIABLE_NAME_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String name = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getVariables());
					if(cell != null){
						((MVertex)cell).setName(name);
					} else{
						document.getVariables().add(new MVertex(id, name, null, null));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};
		
		TreeNode variableDescription = new TreeNode(6, VARIABLE_DESCRIPTION_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String description = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getVariables());
					if(cell != null){
						((MVertex)cell).setDescription(description);
					} else{
						document.getVariables().add(new MVertex(id, null, description, null));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};
		
		TreeNode variableStatus = new TreeNode(7, VARIABLE_STATUS_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String status = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getVariables());
					if(cell != null){
						((MVertex)cell).setStatus(status);
					} else{
						document.getVariables().add(new MVertex(id, null, null, status));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};
		
		TreeNode relationName = new TreeNode(8, RELATION_NAME_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String name = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getRelations());
					if(cell != null){
						((MVertex)cell).setName(name);
					} else{
						document.getRelations().add(new MVertex(id, name, null, null));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};
		
		TreeNode relationDescription = new TreeNode(9, RELATION_DESCRIPTION_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String image = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getRelations());
					if(cell != null){
						((MVertex)cell).setDescription(image);
					} else{
						document.getRelations().add(new MVertex(id, null, image, null));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};		
		
		TreeNode relationStatus = new TreeNode(10, RELATION_STATUS_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					String status = getMatcher().group(2);
					MCell cell = MDocument.getById(id, document.getRelations());
					if(cell != null){
						((MVertex)cell).setStatus(status);
					} else{
						document.getRelations().add(new MVertex(id, null, null, status));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};		
		
		TreeNode asymmetry = new TreeNode(11, ASYMMETRY_REGEX, Pattern.CASE_INSENSITIVE){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				
				try {
					int id = Integer.parseInt(getMatcher().group(1));
					int variable = Integer.parseInt(getMatcher().group(2));
					int relation = Integer.parseInt(getMatcher().group(3));
					int direction = Integer.parseInt(getMatcher().group(4));
					MCell cell = MDocument.getById(id, document.getAsymmetry());
					if(cell != null){
						MAsymmetry edge = (MAsymmetry)cell;
						edge.setVariable(variable);
						edge.setRelation(relation);
						edge.setDirection(direction);
					} else{
						document.getAsymmetry().add(new MAsymmetry(id, variable, relation, direction));
					}
					
				} catch (NumberFormatException e) {
					if(logger != null){
						logger.log(Level.WARNING, e.getMessage());
					}
				}
				
			}
			
		};
		
		root.addChild(parserName);
		root.addChild(autoSolve);
		root.addChild(lowestPriority);
		root.addChild(renamePolicy);
		root.addChild(variableName);
		root.addChild(variableDescription);
		root.addChild(variableStatus);
		root.addChild(relationName);
		root.addChild(relationDescription);
		root.addChild(relationStatus);
		root.addChild(asymmetry);
	}

	@Override
	public boolean accept(File pathname) {
		if(pathname != null && pathname.isFile()){
			String name = pathname.getName().toLowerCase();
			return name.endsWith(MDocumentWriter.SUFFIX) || name.endsWith(".original");
		}
		return false;
	}

	@Override
	public MDocumentReader newDocumentReader() {
		return new MDocumentReader() {
			
			@Override
			public MDocument read(InputStream in, String charsetName) throws IOException {
				return read(new LineNumberReader(new InputStreamReader(in, charsetName)));
			}

			@Override
			public MDocument read(InputStream in, Charset cs) throws IOException {
				return read(new LineNumberReader(new InputStreamReader(in, cs)));
			}

			@Override
			public MDocument read(InputStream in) throws IOException {
				return read(in, MDocumentConstants.DEFAULT_CHARSET_NAME);
			}
			
			@Override
			public MDocument read(Reader reader) throws IOException {
				
				Logger logger = Logger.getLogger(cujae.map.j2g.content.MDocumentReader.class.getName());
				
				LineNumberReader lnr = new LineNumberReader(reader);
				MDocument document = new MDocument();
				String line = null;
				while((line = lnr.readLine()) != null){
					TreeNode next = root.next(line);
					if(next != null){
						next.action(new TreeEvent(line, lnr.getLineNumber()), document, logger);
					}
				}
				
				return document;
			}
		};
	}

}
