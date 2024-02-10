package cujae.map.j2g.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.mxgraph.view.mxGraph;

import cujae.map.j2g.content.MAsymmetry;
import cujae.map.j2g.content.MDocument;
import cujae.map.j2g.content.MDocumentReader;
import cujae.map.j2g.content.MDocumentReaderFactory;
import cujae.map.j2g.content.MVertex;
import cujae.map.j2g.dgraphm.analysis.AbstractAnalysis;
import cujae.map.j2g.dgraphm.model.BGraph;
import cujae.map.j2g.dgraphm.model.BGraphImpl;
import cujae.map.j2g.dgraphm.util.Flags;
import cujae.map.j2g.graphml.AbbGraphmlCodec;
import cujae.map.j2g.graphml.ValueHandler;
import cujae.map.j2g.mme.Moled;
import cujae.map.j2g.mme.Moled.EdgeEntry;
import cujae.map.j2g.mme.Moled.NodeEntry;
import cujae.map.j2g.mme.MoledImpl;
import cujae.map.j2g.parser.OneLineParserFactory;

public abstract class Helper {
	
	public static class CellValue{
		
		private final String label;
		private final String description;
		
		public CellValue(String label, String description) {
			super();
			this.label = label;
			this.description = description;
		}

		public String getLabel() {
			return label;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return label;
		}
	}
	
	public static BGraph createGraph(Moled m, Map<Integer, Object> valueMap, Map<String, Object> analysisMap){
		BGraph g = new BGraphImpl();
		
		Map<Integer, Integer> variableIndexMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> relationIndexMap = new HashMap<Integer, Integer>();
		
		Collection<Integer> inputs = new ArrayList<Integer>();
		Collection<Integer> outputs = new ArrayList<Integer>();
		Collection<Integer> freedomDegrees = new ArrayList<Integer>();
		Collection<Integer> controlUnknowns = new ArrayList<Integer>();
		Collection<Integer> desingParameteres = new ArrayList<Integer>();
		
		for(int i = 0; i < m.size(true); i ++){
			NodeEntry nodeEntry = m.get(i, true);
			CellValue value = new CellValue(nodeEntry.getName(), nodeEntry.getDescription());
			int key = 2 * i;
			g.add(key);
			valueMap.put(key, value);
			variableIndexMap.put(i, key);
			
			int flags = nodeEntry.getFlags();
			
			if(Flags.isInput(flags)){
				inputs.add(key);
			}
			
			if(Flags.isOutput(flags)){
				outputs.add(key);
			}
			
			if(Flags.isFreedomDegree(flags)){
				freedomDegrees.add(key);
			}
			
			if(Flags.isControlUnknown(flags)){
				controlUnknowns.add(key);				
			}
			
			if(Flags.isDesignParameter(flags)){
				desingParameteres.add(key);
			}
		}
		
		for(int i = 0; i < m.size(false); i ++){
			NodeEntry nodeEntry = m.get(i, false);
			CellValue value = new CellValue(nodeEntry.getName(), nodeEntry.getDescription());			
			int key = (i * 2) + 1;
			g.add(key);
			valueMap.put(key, value);
			relationIndexMap.put(i, key);
			
			EdgeEntry[] edges = m.edges(i, false);
			for(int j = 0; j < edges.length; j ++){
				EdgeEntry edgeEntry = edges[j];
				g.put(variableIndexMap.get(edgeEntry.getVariableIndex()),
						relationIndexMap.get(edgeEntry.getRelationIndex()),
						Integer.signum(edgeEntry.getDirection()));
			}
		}
		
		analysisMap.put(AbstractAnalysis.SITUATION_INPUTS, inputs);
		analysisMap.put(AbstractAnalysis.PROBLEM_OUTPUTS, outputs);
		analysisMap.put(AbstractAnalysis.MATCHING_PROBLEM_FREEDOM_DEGREES, freedomDegrees);
		analysisMap.put(AbstractAnalysis.MATCHING_PROBLEM_DESIGN_PARAMETERS, desingParameteres);
		analysisMap.put(AbstractAnalysis.MATCHING_PROBLEM_CONTROL_UNKNOWNS, controlUnknowns);
		
		return g;
	}
	
	public static BGraph createGraph(Moled m, Map<Integer, Object> valueMap, 
			Collection<Integer> inputs, Collection<Integer> outputs){
		BGraph g = new BGraphImpl();
		
		Map<Integer, Integer> variableIndexMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> relationIndexMap = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < m.size(true); i ++){
			NodeEntry nodeEntry = m.get(i, true);
			CellValue value = new CellValue(nodeEntry.getName(), nodeEntry.getDescription());
			int key = 2 * i;
			g.add(key);
			valueMap.put(key, value);
			variableIndexMap.put(i, key);
			if(Flags.isInput(nodeEntry.getFlags())){
				inputs.add(key);
			}
			
			if(Flags.isOutput(nodeEntry.getFlags())){
				outputs.add(key);
			}
		}
		
		for(int i = 0; i < m.size(false); i ++){
			NodeEntry nodeEntry = m.get(i, false);
			CellValue value = new CellValue(nodeEntry.getName(), nodeEntry.getDescription());			
			int key = (i * 2) + 1;
			g.add(key);
			valueMap.put(key, value);
			relationIndexMap.put(i, key);
			
			EdgeEntry[] edges = m.edges(i, false);
			for(int j = 0; j < edges.length; j ++){
				EdgeEntry edgeEntry = edges[j];
				g.put(variableIndexMap.get(edgeEntry.getVariableIndex()),
						relationIndexMap.get(edgeEntry.getRelationIndex()),
						Integer.signum(edgeEntry.getDirection()));
			}
		}
		
		return g;
	}
	
	public static MDocument buildDocument(Moled moled){
		
		MDocument document = new MDocument();
		document.setParserName(moled.getParser().getName());
		
		for(int i = 0; i < moled.size(true); i ++){
			NodeEntry n = moled.get(i, true);
			document.getVariables().add(new MVertex(i, n.getName(), 
					n.getDescription(), String.valueOf(n.getFlags())));
		}
		
		
		int eid = 0;
		for(int i = 0; i < moled.size(false); i ++){
			NodeEntry n = moled.get(i, false);
			document.getRelations().add(new MVertex(i, n.getName(), 
					n.getDescription(), null));
			
			EdgeEntry[] edges = moled.edges(i, false);
			for(int j = 0; j < edges.length; j ++){
				EdgeEntry e = edges[j];
				document.getAsymmetry().add(new MAsymmetry(eid, e.getVariableIndex(), 
						e.getRelationIndex(), e.getDirection()));
				eid ++;
			}
			
    	}
		
		return document;
	}
	
	public static final String INPUT_STATUS_REGEX = "[Ii]";
	
	public static final String OUPUT_STATUS_REGEX = "[Oo]";
	
	public static Moled buildMoled(MDocument document){
		
		Moled moled = new MoledImpl(document.getVariables().size(), 
				document.getRelations().size());
		
		OneLineParserFactory factory = 
				OneLineParserFactory.getInstance(document.getParserName());
		
		moled.setParser(factory.newOneLineParser());
		
		Map<Integer, Integer> variables = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < document.getVariables().size(); i ++){
			MVertex e = document.getVariables().get(i);
			int flags = 0;
			String status = e.getStatus();
			
			if(status != null){				
				if(status.matches(INPUT_STATUS_REGEX)){
					flags = Flags.INPUT_FLAG;
				} else if(status.matches(OUPUT_STATUS_REGEX)){
					flags = Flags.OUTPUT_FLAG;
				} else{
					try{
						flags = Integer.valueOf(e.getStatus());
					} catch(NumberFormatException ex){						
						//ignore...
					}
				}
			}			
			
			variables.put(e.getId(), moled.insert(e.getName(), e.getDescription(), flags, true));
		}
		
		Map<Integer, Integer> relations = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < document.getRelations().size(); i ++){
			MVertex e = document.getRelations().get(i);
			relations.put(e.getId(), moled.insert(e.getName(), e.getDescription(), 0, false));
		}
		
		moled.parse(-1);
		
		for(int i = 0; i < document.getAsymmetry().size(); i ++){
			MAsymmetry e = document.getAsymmetry().get(i);
			moled.set(variables.get(e.getVariable()), relations.get(e.getRelation()), e.getDirection());
		}
		
		return moled;
	}
	
	public static File fixFile(File pathname, String suffix){
		String absolutePath = pathname.getAbsolutePath();
		if(!absolutePath.endsWith(suffix)){
			pathname = new File(pathname.getParentFile(), 
					pathname.getName().concat(suffix));
		}
		
		return pathname;
	}
	
	public static Moled readMoled(File pathname, String charsetName) throws FileNotFoundException, IOException{
		MDocumentReaderFactory factory = MDocumentReaderFactory.getInstance(pathname);
		MDocumentReader reader = factory.newDocumentReader();		
		MDocument document = reader.read(new FileInputStream(pathname), charsetName);		
		return buildMoled(document);
	}
	
	public static Moled readMoled(File pathname, Charset cs) throws FileNotFoundException, IOException{
		MDocumentReaderFactory factory = MDocumentReaderFactory.getInstance(pathname);
		MDocumentReader reader = factory.newDocumentReader();		
		MDocument document = reader.read(new FileInputStream(pathname), cs);		
		return buildMoled(document);
	}
	
	public static Moled readMoled(File pathname) throws FileNotFoundException, IOException{
		MDocumentReaderFactory factory = MDocumentReaderFactory.getInstance(pathname);
		MDocumentReader reader = factory.newDocumentReader();		
		MDocument document = reader.read(new FileInputStream(pathname));		
		return buildMoled(document);
	}
	
	public static void exportGraph(mxGraph graph, Map<Class<?>, ValueHandler> valueHandlerProvider, 
			File pathname, String suffix) throws FileNotFoundException, ClassCastException, 
			ClassNotFoundException, InstantiationException, IllegalAccessException, 
			ParserConfigurationException{
		File fixFile = (suffix != null) ? fixFile(pathname, suffix) : pathname;
		FileOutputStream os = new FileOutputStream(fixFile);
		AbbGraphmlCodec.serialize(graph, os, valueHandlerProvider);
	}	
	
}
