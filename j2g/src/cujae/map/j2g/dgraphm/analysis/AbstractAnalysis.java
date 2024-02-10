package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public abstract class AbstractAnalysis {
	
	public static final int MODEL_TYPE = 1;
	
	public static final String MODEL = "model";
	
	public static final String MODEL_REMOVED = "model.removed";
	
	public static final String MODEL_VARIABLES = "model.variables";
	
	public static final String MODEL_RELATIONS = "model.relations";
	
	public static final String MODEL_COMPONENTS = "model.components";

	public static final int SITUATION_TYPE = 2;
	
	public static final String SITUATION = "situation";
	
	public static final String SITUATION_INPUTS = "situation.inputs";
	
	public static final String SITUATION_UNKNOWNS = "situation.unknowns";
	
	public static final String SITUATION_RELATIONS = "situation.relations";
	
	public static final String SITUATION_DEFICIENCES = "situation.deficiences";
	
	public static final String SITUATION_COMPONENTS = "situation.components";
	
	public static final String SITUATION_APPROPRIATE = "situation.appropriate";
	
	public static final String SITUATION_COMPATIBLE = "situation.compatible";
	
	
	public static final int PROBLEM_TYPE = 4;
	
	public static final String PROBLEM = "problem";
	
	public static final String PROBLEM_OUTPUTS = "problem.outputs";
	
	public static final String PROBLEM_REMOVED = "problem.removed";
	
	public static final String PROBLEM_UNKNOWNS = "problem.unknowns";
	 
	public static final String PROBLEM_FREE_UNKNOWNS = "problem.freeUnknowns";

	public static final String PROBLEM_RELATIONS = "problem.relations";
	
	public static final String PROBLEM_FREE_RELATIONS = "problem.freeRelations";
	
	public static final String PROBLEM_DEFICIENCES = "problem.deficiences";
	
	public static final String PROBLEM_ORIGINAL_MATCHING = "problem.originalMatching";
	
	public static final String PROBLEM_COMPONENTS = "problem.components";
	
	public static final String PROBLEM_APPROPRIATE = "problem.appropriate";
	
	public static final String PROBLEM_COMPATIBLE = "problem.compatible";
	
	
	public static final int MATCHING_PROBLEM_TYPE = 8;
	
	public static final String MATCHING_PROBLEM = "matchingProblem";
	
	public static final String MATCHING_PROBLEM_DEFICIENCES = "matchingProblem.deficiences";
	
	public static final String MATCHING_PROBLEM_FREEDOM_DEGREES = "matchingProblem.freedomDegree";
	
	public static final String MATCHING_PROBLEM_CONTROL_UNKNOWNS = "matchingProblem.controlUnknowns";
	
	public static final String MATCHING_PROBLEM_DESIGN_PARAMETERS = "matchingProblem.designParametes";
	
	public static final String MATCHING_PROBLEM_COMPATIBLE = "matchingProblem.compatible";
	
	public static final String MATCHING_PROBLEM_DETERMINE = "matchingProblem.determine";
	
	public static final String MATCHING_PROBLEM_REALIZABLE = "matchingProblem.realizable";
	
	public static final String MATCHING_PROBLEM_REALIZABLE_DISJOINT = "matchingProblem.realizableDisjoint";
	
	public static final String MATCHING_PROBLEM_MAXIMUM_MATCH = "matchingProblem.maximumMatch";
	
	public static final String MATCHING_PROBLEM_MATCHING_COUNT = "matchingProblem.matchingCount";
	
	
	public static final int RESOLVENT_TYPE = 16;
	
	public static final String RESOLVENT = "resolvent";
	
	public static final String RESOLVENT_STRONG_CONNECTED_COMPONENTS = "resolvent.strongConnectedComponents";
	
	public static final String RESOLVENT_LOOPS = "resolvent.loops";
	
	
	public static final int MARKED_RESOLVENT_TYPE = 32;
	
	public static final String MARKED_RESOLVENT = "markedResolvent";
	
	public static final String MARKED_RESOLVENT_W1 = "markedResolvent.w1";
	
	public static final String MARKED_RESOLVENT_W2 = "markedResolvent.w2";
	
	public static final String MARKED_RESOLVENT_W3 = "markedResolvent.w3";
	
	public static final String MARKED_RESOLVENT_CANDIDATE_FREEDOM_DEGREE = "markedResolvent.candidateFreedomDegree";
	
	public static final String MARKED_RESOLVENT_CANDIDATE_DEFICIENCE = "markedResolvent.candidateDeficience";
	
	public static final String MARKED_RESOLVENT_MARKED_EDGES = "markedResolvent.markedEdges";
	
	public static final String MARKED_RESOLVENT_COMPONENTS = "markedResolvent.components";
	
	
	public static final int ALGORITHM_TYPE = 64;
	
	public static final String ALGORITHM = "algorithm";
	
	public static final String ALGORITHM_REMOVED = "algorithm.removed";
	
	public static final String ALGORITHM_COMPONENTS = "algorithm.components";
	
	public static final String ALGORITHM_STRONG_CONNECTED_COMPONENTS = "algorithm.strongConnectedComponents";
	
	public static final String ALGORITHM_LOOPS = "algorithm.loops";
	
	
	public static Set<Integer> tryExtract(BGraph g, String key, Map<String, Object> m, boolean isEven){
		Set<Integer> s = new HashSet<Integer>();
		
		if(m != null){
			Object value = m.get(key);
			if(value instanceof Collection<?>){
				Collection<?> proposed = (Collection<?>) value;
				for(Iterator<?> it = proposed.iterator(); it.hasNext();){
					Object next = it.next();
					if(next instanceof Integer){					
						Integer nextInt = (Integer) next;					
						if(g.contains(nextInt)){						
							if(isEven){
								if(nextInt % 2 == 0){
									s.add(nextInt);
								}
							} else{
								if(nextInt % 2 == 1){
									s.add(nextInt);
								}
							}
						}
					}				
				}
			}	
		}
		
		return s;
	}	
	
	public static void splitEdges(BGraph g, Integer key, Collection<BEdge> lt0,
			Collection<BEdge> eq0, Collection<BEdge> gt0){
		
		Iterator<BEdge> it = g.adjIterator(key);
		if(it != null){
			while(it.hasNext()){
				BEdge next = it.next();
				
				switch(Integer.signum(next.direction())){
				case -1: if(lt0 != null) lt0.add(next); break;
				case  0: if(eq0 != null) eq0.add(next); break;
				case  1: if(gt0 != null) gt0.add(next); break;
				
				}
			}
		}
	}
	
	public static void splitEdges(BGraph g, Collection<BEdge> lt0,
			Collection<BEdge> eq0, Collection<BEdge> gt0){
		
		for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
			splitEdges(g, it.next(), lt0, eq0, gt0);
		}
	}
	
	public static boolean deepFirstSearch(BGraph g, Integer key, int direction, 
			boolean ignoreDirection, int value, Map<Integer, Integer> visited){
		
		final boolean b = !visited.containsKey(key);		
		
		if(b){
			final boolean isEven = key % 2 == 0;
			visited.put(key, value);
			for(Iterator<BEdge> it = g.adjIterator(key); it.hasNext();){
				BEdge next = it.next();
				int nextSignum = Integer.signum(next.direction());
				if(nextSignum == Integer.signum(direction) || ignoreDirection){			
					deepFirstSearch(g, isEven ? next.odd() : next.even(), 
							nextSignum * -1, ignoreDirection, value, visited);
				}
			}
		}
		
		return b;
	}
	
	public static Map<Integer, Integer> components(BGraph g){
		
		int value = 0;
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();		
		
		boolean isEven = true;
		for(Iterator<Integer> it = g.iterator(isEven); it.hasNext();){
			if(deepFirstSearch(g, it.next(), 0, true, value, m)){
				value ++;
			}
		}
		
		isEven = !isEven;
		for(Iterator<Integer> it = g.iterator(isEven); it.hasNext();){
			if(deepFirstSearch(g, it.next(), 0, true, value, m)){
				value ++;
			}
		}
		
		return m;
	}
	
	public static Map<Integer, Collection<Integer>> components(Map<Integer, Integer> m){
		
		Map<Integer, Collection<Integer>> n = new HashMap<Integer, Collection<Integer>>();
		
		for(Iterator<Map.Entry<Integer, Integer>> it = m.entrySet().iterator(); it.hasNext();){
			Map.Entry<Integer, Integer> next = it.next();
			
			if(!n.containsKey(next.getValue())){
				n.put(next.getValue(), new ArrayList<Integer>());
			}
			n.get(next.getValue()).add(next.getKey());
		}
		
		return n;
	}
	
	public static Set<Integer> nodesWithEdgeSizeUnder(BGraph g, int size, boolean isEven){
		Set<Integer> s = new HashSet<Integer>();
		
		for(Iterator<Integer> it = g.iterator(isEven); it.hasNext();){
			Integer next = it.next();
			if(g.size(next) < size){
				s.add(next);				
			}			
		}
		
		return s;
	}
	
	public static boolean isNodeWithoutEdgeDirection(BGraph g, Integer key, int direction){
		Collection<BEdge> lt0 = new ArrayList<BEdge>();
		Collection<BEdge> eq0 = new ArrayList<BEdge>();
		Collection<BEdge> gt0 = new ArrayList<BEdge>();
		
		splitEdges(g, key, lt0, eq0, gt0);
		
		boolean b = false;
		
		switch(Integer.signum(direction)){
		case -1: b = lt0.isEmpty(); break;
		case  0: b = eq0.isEmpty(); break;
		case  1: b = gt0.isEmpty(); break;
		}		
		
		return b;
	}
	
	public static Set<Integer> nodesWithoutEdgeDirection(BGraph g, int direction, boolean isEven){
		
		Set<Integer> s = new HashSet<Integer>();
		
		for(Iterator<Integer> it = g.iterator(isEven); it.hasNext();){
			Integer next = it.next();
			
			if(isNodeWithoutEdgeDirection(g, next, direction)){
				s.add(next);
			}
			
		}
		
		return s;
	}
	
	public static Map<Integer, Integer> canonical(BGraph g){
		
		
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		
		/*
		 * Canonical Component 1
		 */
		Set<Integer> s = nodesWithoutEdgeDirection(g, 1, true);
		for(Iterator<Integer> it = s.iterator(); it.hasNext();){
			deepFirstSearch(g, it.next(), -1, false, 1, m);
		}
		
		/*
		 * Canonical Component 3
		 */
		s = nodesWithoutEdgeDirection(g, 1, false);
		m.keySet().removeAll(s);
		for(Iterator<Integer> it = s.iterator(); it.hasNext();){
			deepFirstSearch(g, it.next(), -1, false, 3, m);
		}
		
		/*
		 * Canonical Component 2
		 */
		for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
			Integer next = it.next();
			if(!m.containsKey(next)){
				m.put(next, 2);
			}
		}
		for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
			Integer next = it.next();
			if(!m.containsKey(next)){
				m.put(next, 2);
			}
		}
		
		return m;
	}

	public static Set<Integer> followDirection(BGraph g, int direction, Set<Integer> s){
		
		Map<Integer, Integer> t = new HashMap<Integer, Integer>();
		
		for(Iterator<Integer> it = s.iterator(); it.hasNext();){
			deepFirstSearch(g, it.next(), 1, false, direction, t);
		}
		
		return t.keySet();
		
	}
	
	public abstract void execute(BGraph g, Map<String, Object> m);

}
