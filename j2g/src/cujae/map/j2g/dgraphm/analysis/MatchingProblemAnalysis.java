package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;
import cujae.map.j2g.maximummatching.Arista;
import cujae.map.j2g.maximummatching.Controladora;
import cujae.map.j2g.maximummatching.PareoMaximo;
import edu.princeton.cs.algs4.Graph;

public class MatchingProblemAnalysis extends AbstractAnalysis {
	
	private int matchingIndex = -1;
	
	private List<PareoMaximo> pareos = null;
	
	public int getAvailableMatchings() {
		if (pareos != null) {			
			return pareos.size();
		}
		return -1;
	}
	
	private static HashMap<Integer, ArrayList<Integer>> prepareInput(BGraph g, Set<Integer> ignore){
		HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();
		
		for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
			Integer next = it.next();
			
			if(!ignore.contains(next)){
				List<BEdge> eq0 = new ArrayList<BEdge>();
				List<BEdge> gt0 = new ArrayList<BEdge>();
				
				splitEdges(g, next, null, eq0, gt0);
				
				if(gt0.isEmpty()){
					ArrayList<Integer> l = new ArrayList<Integer>();
					for(Iterator<BEdge> it2 = eq0.iterator(); it2.hasNext();){
						BEdge next2 = it2.next();						
						
						if(!ignore.contains(next2.even())){
							List<BEdge> gt02 = new ArrayList<BEdge>();
							splitEdges(g, next2.even(), null, null, gt02);
							if(gt02.isEmpty()){
								l.add(next2.even());
							}
						}
					}
					
					graph.put(next, l);
				}	
			}			
		}
		
		return graph;
	}
	
	private static void freeVertices(BGraph g, Set<Integer> ignore, List<Integer> vertices, boolean isVariable) {
		for (Iterator<Integer> it = g.iterator(isVariable); it.hasNext();) {
			Integer next = it.next();
			
			if (!ignore.contains(next)) {
				List<BEdge> gt0 = new ArrayList<BEdge>();
				
				splitEdges(g, next, null, null, gt0);
				
				if (gt0.isEmpty()) {
					vertices.add(next);
				}
			}
		}
	}
	
	private static Graph transform(BGraph g, Set<Integer> ignore, List<Integer> mapping) {
		
		// Calculate free vertices, a.k.a (vertices to be matched)
		freeVertices(g, ignore, mapping, true);
		freeVertices(g, ignore, mapping, false);
		
		// Initialize target graph
		Graph h = new Graph(mapping.size());
		
		// Insert edges into target graph
		for (Iterator<Integer> vIt = mapping.iterator(); vIt.hasNext();) {
			Integer next = vIt.next();
			
			// Get undirected edges, a.k.a (direction equal to zero)
			List<BEdge> eq0 = new ArrayList<BEdge>();
			splitEdges(g,  next,  null, eq0, null);
			
			for (Iterator<BEdge> adjIt = eq0.iterator(); adjIt.hasNext();) {
				BEdge edge = adjIt.next();
				
				int even = edge.even();
				int odd = edge.odd();
				
				// Transform into index based vertex
				int v = mapping.indexOf(even);
				int w = mapping.indexOf(odd);
				
				// There may be some inexistent vertices in the list, check it.
				if (v != -1 && w != -1) {
					h.addEdge(v, w);
				}				
			}			
		}
		
		return h;
	}
	
	public void reset() {
		pareos = null;
		matchingIndex = -1;
	}

	public int getMatchingIndex() {
		return matchingIndex;
	}

	public void setMatchingIndex(int matchingIndex) {
		this.matchingIndex = matchingIndex;
	}

	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0) {
			
			Set<Integer> controlUnknowns = tryExtract(g, MATCHING_PROBLEM_CONTROL_UNKNOWNS, m, true);		
			Set<Integer> designParameters = tryExtract(g, MATCHING_PROBLEM_DESIGN_PARAMETERS, m, true);
			Set<Integer> freedomDegrees = tryExtract(g, MATCHING_PROBLEM_FREEDOM_DEGREES, m, true);
			Set<Integer> deficiences = tryExtract(g, MATCHING_PROBLEM_DEFICIENCES, m, false);
			
			Set<Integer> ignore = new HashSet<Integer>();
			ignore.addAll(controlUnknowns);
			ignore.addAll(designParameters);
			ignore.addAll(freedomDegrees);
			ignore.addAll(deficiences);
			
			System.out.println("Ignore" + ignore);
			
			// Initialize the mapping, must be empty
			List<Integer> mapping = new ArrayList<Integer>();

			// Transform into the new graph
			Graph h = transform(g, ignore, mapping);
			System.out.println(mapping);
			
			/**
			 * 
			 */
			
			if (pareos == null) {
				Controladora controladora = Controladora.getInstancia();
				controladora.inicializar(h);			
				pareos = controladora.devolverPareosMaximos();	
			}	
			
			/**
			 * 
			 */			
			
			if (matchingIndex < 0 || matchingIndex > pareos.size() - 1) {
				matchingIndex = 0;
			}
			
			PareoMaximo primerPareo = pareos.get(matchingIndex);
			
			System.out.println(String.format("Matching Index = %d", matchingIndex));
			
			Map<Integer, Integer> matching = new HashMap<Integer, Integer>();			
			
			for(Arista arista: primerPareo.getAristas()){
				
				int nodoSalida = arista.getNodoSalida();
				int nodoEntrada = arista.getNodoEntrada();
				
				System.out.println(String.format("entrada: %d, salida :%d", nodoSalida, nodoEntrada));
				
				Integer key = mapping.get(nodoSalida);
				Integer value = mapping.get(nodoEntrada);
				
				matching.put(key, value);
			}
			
			/**
			 * 
			 */
			
			// Update graph to the newly calculated matching
			for(Iterator<Map.Entry<Integer, Integer>> it = matching.entrySet().iterator(); it.hasNext();){
				Map.Entry<Integer, Integer> next = it.next();
				g.put(next.getKey(), next.getValue(), 2);
			}			
			
			Collection<BEdge> maximumMatch = new ArrayList<BEdge>();
			splitEdges(g, null, null, maximumMatch);
			
			freedomDegrees.addAll(nodesWithoutEdgeDirection(g, 1, true));
			deficiences.addAll(nodesWithoutEdgeDirection(g, 1, false));
			
			Set<Integer> disjoint = new HashSet<Integer>();
			disjoint.addAll(freedomDegrees);
			disjoint.removeAll(controlUnknowns);
			disjoint.removeAll(designParameters);		
			
			
			if(m != null){
				m.put(MATCHING_PROBLEM_CONTROL_UNKNOWNS, controlUnknowns);
				m.put(MATCHING_PROBLEM_DESIGN_PARAMETERS, designParameters);
				m.put(MATCHING_PROBLEM_MAXIMUM_MATCH, maximumMatch);		
				m.put(MATCHING_PROBLEM_DEFICIENCES, deficiences);
				m.put(MATCHING_PROBLEM_COMPATIBLE, deficiences.isEmpty());
				m.put(MATCHING_PROBLEM_FREEDOM_DEGREES, freedomDegrees);
				m.put(MATCHING_PROBLEM_DETERMINE, freedomDegrees.isEmpty());
				m.put(MATCHING_PROBLEM_REALIZABLE_DISJOINT, disjoint);
				m.put(MATCHING_PROBLEM_REALIZABLE, disjoint.isEmpty());
			}
		} else{
			if(m != null){
				m.remove(MATCHING_PROBLEM_CONTROL_UNKNOWNS);
				m.remove(MATCHING_PROBLEM_DESIGN_PARAMETERS);
				m.remove(MATCHING_PROBLEM_MAXIMUM_MATCH);		
				m.remove(MATCHING_PROBLEM_DEFICIENCES);
				m.remove(MATCHING_PROBLEM_COMPATIBLE);
				m.remove(MATCHING_PROBLEM_FREEDOM_DEGREES);
				m.remove(MATCHING_PROBLEM_DETERMINE);
				m.remove(MATCHING_PROBLEM_REALIZABLE_DISJOINT);
				m.remove(MATCHING_PROBLEM_REALIZABLE);
				m.remove(MATCHING_PROBLEM_MATCHING_COUNT);
			}
		}

	}

}