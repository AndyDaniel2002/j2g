package cujae.map.j2g.content.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import cujae.map.j2g.content.MDocument;
import cujae.map.j2g.content.MDocumentReader;
import cujae.map.j2g.content.MDocumentReaderFactory;
import cujae.map.j2g.content.MVertex;
import cujae.map.j2g.content.util.TreeEvent;
import cujae.map.j2g.content.util.TreeNode;

public class TkReaderFactory extends MDocumentReaderFactory {
	
	public static final String ISO_8859_7_CHARSET_NAME = "ISO-8859-7";
	
	private static final String SEQUENCE_REGEX = "#(\\d+)";
	
	private static final String ANY_REGEX = ".*";
	
	private static TreeNode root;
	
	static{
		root = new TreeNode(0, "", 0);
		
		TreeNode header = new TreeNode(1, "TK((WNT\\+\\d+)|(\\+\\d+))", 0);
		
		TreeNode variable = new TreeNode(2, "=v", 0);
		
		TreeNode variableSequence = new TreeNode(3, SEQUENCE_REGEX, 0);
		
		TreeNode variableName = new TreeNode(4, ":n", 0);
		
		TreeNode variableNameAny = new TreeNode(5, ANY_REGEX, 0){

			@Override
			public void action(TreeEvent event, MDocument document, Logger logger) {
				super.action(event, document, logger);
				MVertex variable = new MVertex(document.getVariables().size(), event.getInput(), null, null);
				document.getVariables().add(variable);				
			}
			
		};	
		
		TreeNode variableValue = new TreeNode(6, ":v", 0);
		
		TreeNode variableValueAny = new TreeNode(7, ANY_REGEX, 0);			
		
		TreeNode variableStatus = new TreeNode(8, ":s", 0);
		
		TreeNode variableStatusAny = new TreeNode(9, ANY_REGEX, 0){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				if(!document.getVariables().isEmpty()){
					MVertex variable = document.getVariables().get(document.getVariables().size() - 1);
					variable.setStatus(event.getInput());
				}
			}
			
		};
		
		TreeNode variableAttribute = new TreeNode(10, ":[ud]", 0);
		
		TreeNode variableAttributeAny = new TreeNode(11, ANY_REGEX, 0);
		
		TreeNode variableComment = new TreeNode(12, ":c", 0);
		
		TreeNode variableCommentAny = new TreeNode(13, ANY_REGEX, 0){

			@Override
			public void action(TreeEvent event, MDocument document, Logger logger) {
				super.action(event, document, logger);
				if(!document.getVariables().isEmpty()){
					MVertex variable = document.getVariables().get(document.getVariables().size() - 1);
					variable.setDescription(event.getInput());
				}
			}
			
		};
		
		TreeNode variableAttribute2 = new TreeNode(14, ":[af#]", 0);
		
		TreeNode variableAttribute2Any = new TreeNode(15, ANY_REGEX, 0);
		
		TreeNode unit = new TreeNode(16, "=u", 0);
		
		TreeNode unitSequence = new TreeNode(17, SEQUENCE_REGEX, 0);
		
		TreeNode unitAttribute = new TreeNode(18, ":[fmtac]", 0);
		
		TreeNode unitAttributeAny = new TreeNode(19, ANY_REGEX, 0);
		
		TreeNode list = new TreeNode(20, "=l", 0);
		
		TreeNode listSequence = new TreeNode(21, SEQUENCE_REGEX, 0);
		
		TreeNode listAttribute = new TreeNode(22, ":[nduc#]", 0);
		
		TreeNode listAttributeAny = new TreeNode(23, ANY_REGEX, 0);
		
		TreeNode listValues = new TreeNode(24, ":v", 0);
		
		TreeNode listValuesNotE = new TreeNode(25, "[^E]|(.{2,})?", 0);
		
		TreeNode listValuesE = new TreeNode(26, "E", 0);
		
		TreeNode function = new TreeNode(27, "=f", 0);
		
		TreeNode functionSequence = new TreeNode(28, SEQUENCE_REGEX, 0);
		
		TreeNode functionName = new TreeNode(29, ":n", 0);
		
		TreeNode functionNameAny = new TreeNode(30, ANY_REGEX, 0);
		
		TreeNode functionAttribute = new TreeNode(31, ":[ctdmrpavio]", 0);
		
		TreeNode functionAttributeAny = new TreeNode(32, ANY_REGEX, 0);
		
		TreeNode functionValues = new TreeNode(33, ":z", 0);
		
		TreeNode functionValuesNotSolidusE = new TreeNode(34, "([^/].*)?", 0);
		
		TreeNode functionValuesSolidusE = new TreeNode(35, "/E", 0);
		
		TreeNode functionValuesE = new TreeNode(36, "E", 0);
		
		TreeNode rule = new TreeNode(37, "=r", 0);
		
		TreeNode ruleSequence = new TreeNode(38, SEQUENCE_REGEX, 0){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				String name = getMatcher().group(1);
				int size = document.getRelations().size();
				MVertex r = new MVertex(size, name, event.getInput(), null);
				document.getRelations().add(r);
			}
			
		};
		
		TreeNode ruleRule = new TreeNode(39, ":r", 0);
		
		TreeNode ruleRuleAny = new TreeNode(40, ANY_REGEX, 0){

			@Override
			public void action(TreeEvent event, MDocument document, Logger logger) {
				super.action(event, document, logger);
				if(!document.getRelations().isEmpty()){
					MVertex relation = document.getRelations().get(document.getRelations().size() - 1);
					relation.setDescription(event.getInput());
				}
			}
			
		};
		
		TreeNode ruleStatus = new TreeNode(41, ":s", 0);
		
		TreeNode ruleStatusAny = new TreeNode(42, ANY_REGEX, 0);
		
		TreeNode plot = new TreeNode(43, "=p", 0);		
		
		root.addChild(header);
		
		header.addChild(variable);
		
		variable.addChild(variableSequence);
		variable.addChild(unit);
		
		variableSequence.addChild(variableName);
		variableSequence.addChild(variableValue);
		variableSequence.addChild(variableStatus);
		variableSequence.addChild(variableAttribute);
		variableSequence.addChild(variableComment);
		variableSequence.addChild(variableAttribute2);
		
		variableName.addChild(variableNameAny);		

		variableNameAny.addChild(variableValue);
		variableNameAny.addChild(variableStatus);
		variableNameAny.addChild(variableAttribute);
		variableNameAny.addChild(variableComment);
		variableNameAny.addChild(variableAttribute2);
		variableNameAny.addChild(variableSequence);
		variableNameAny.addChild(unit);
		
		variableValue.addChild(variableValueAny);
		
		variableValueAny.addChild(variableStatus);
		variableValueAny.addChild(variableAttribute);
		variableValueAny.addChild(variableComment);
		variableValueAny.addChild(variableAttribute2);
		variableValueAny.addChild(variableSequence);
		variableValueAny.addChild(unit);
		
		variableStatus.addChild(variableStatusAny);
		
		variableStatusAny.addChild(variableAttribute);
		variableStatusAny.addChild(variableComment);
		variableStatusAny.addChild(variableAttribute2);
		variableStatusAny.addChild(variableSequence);
		variableStatusAny.addChild(unit);
		
		variableAttribute.addChild(variableAttributeAny);		

		variableAttributeAny.addChild(variableAttribute);
		variableAttributeAny.addChild(variableComment);
		variableAttributeAny.addChild(variableAttribute2);
		variableAttributeAny.addChild(variableSequence);
		variableAttributeAny.addChild(unit);
		
		variableComment.addChild(variableCommentAny);
		
		variableCommentAny.addChild(variableAttribute2);
		variableCommentAny.addChild(variableSequence);
		variableCommentAny.addChild(unit);
		
		variableAttribute2.addChild(variableAttribute2Any);
		
		variableAttribute2Any.addChild(variableAttribute2);
		variableAttribute2Any.addChild(variableSequence);
		variableAttribute2Any.addChild(unit);
		
		unit.addChild(unitSequence);
		unit.addChild(list);
		
		unitSequence.addChild(unitAttribute);
		
		unitAttribute.addChild(unitAttributeAny);		

		unitAttributeAny.addChild(unitAttribute);
		unitAttributeAny.addChild(unitSequence);
		unitAttributeAny.addChild(list);
		
		list.addChild(listSequence);
		list.addChild(function);
		
		listSequence.addChild(listAttribute);
		listSequence.addChild(listValues);
		
		listAttribute.addChild(listAttributeAny);		

		listAttributeAny.addChild(listAttribute);
		listAttributeAny.addChild(listValues);
		listAttributeAny.addChild(listSequence);
		listAttributeAny.addChild(function);
		
		listValues.addChild(listValuesNotE);
		listValues.addChild(listValuesE);
		
		listValuesNotE.addChild(listValuesNotE); // LOOP
		listValuesNotE.addChild(listValuesE);
		
		listValuesE.addChild(listSequence);
		listValuesE.addChild(function);
		
		function.addChild(functionSequence);
		function.addChild(rule);
		
		functionSequence.addChild(functionName);
		functionSequence.addChild(functionAttribute);
		functionSequence.addChild(functionValues);
		
		functionName.addChild(functionNameAny);		

		functionNameAny.addChild(functionAttribute);
		functionNameAny.addChild(functionValues);
		functionNameAny.addChild(functionSequence);
		functionNameAny.addChild(rule);
		
		functionAttribute.addChild(functionAttributeAny);		

		functionAttributeAny.addChild(functionAttribute);
		functionAttributeAny.addChild(functionSequence);
		functionAttributeAny.addChild(rule);
		
		functionValues.addChild(functionValuesNotSolidusE);
		functionValues.addChild(functionValuesSolidusE);
		
		functionValuesNotSolidusE.addChild(functionValuesNotSolidusE); // LOOP
		functionValuesNotSolidusE.addChild(functionValuesSolidusE);
		
		functionValuesSolidusE.addChild(functionValuesE);
		
		functionValuesE.addChild(functionSequence);
		functionValuesE.addChild(rule);
		
		rule.addChild(ruleSequence);
		rule.addChild(plot);
		
		ruleSequence.addChild(ruleRule);
		ruleSequence.addChild(ruleStatus);
		
		ruleRule.addChild(ruleRuleAny);		

		ruleRuleAny.addChild(ruleStatus);
		ruleRuleAny.addChild(ruleSequence);
		ruleRuleAny.addChild(plot);
		
		ruleStatus.addChild(ruleStatusAny);
		
		ruleStatusAny.addChild(ruleSequence);
		ruleStatusAny.addChild(plot);
	}

	@Override
	public boolean accept(File pathname) {
		if(pathname != null && pathname.isFile()){
			String name = pathname.getName().toLowerCase();
			return name.endsWith(".tkw") || name.endsWith(".tkt");
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
				return read(new LineNumberReader(new InputStreamReader(in, ISO_8859_7_CHARSET_NAME)));
			}
			
			@Override
			public MDocument read(Reader in) throws IOException {
				
				Logger logger = Logger.getLogger(cujae.map.j2g.content.MDocumentReader.class.getName());				
				
				LineNumberReader lnr = new LineNumberReader(in);
				MDocument document = new MDocument();
				document.setParserName("tksolver");
				String line = null;
				TreeNode current = root;				
				while(current != null && (line = lnr.readLine()) != null){						
					current = current.next(line);					
					if(current != null){
						current.action(new TreeEvent(line, lnr.getLineNumber()), document, logger);
					}
				}
				
				return document;
			}


		};
	}

}
