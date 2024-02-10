package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class ProblemAnalysis extends AbstractAnalysis {

	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){
			
			Map<Integer, Integer> components = components(g);
			Set<Integer> outputs = tryExtract(g, PROBLEM_OUTPUTS, m, true);
			Set<Integer> s = new HashSet<Integer>();
			
			for(Iterator<Integer> it = outputs.iterator(); it.hasNext();){
				s.add(components.get(it.next()));		
			}
			
			Set<Integer> removed = new HashSet<Integer>();
			
			for(Iterator<Map.Entry<Integer, Integer>> it = components.entrySet().iterator(); it.hasNext();){
				Map.Entry<Integer, Integer> next = it.next();
				if(!s.contains(next.getValue())){
					g.remove(next.getKey());
					removed.add(next.getKey());
				}
			}
			
			if(m != null){
				
				Collection<BEdge> originalMatch = new ArrayList<BEdge>();
				splitEdges(g, null, null, originalMatch);
				
				Collection<Integer> unknowns = new ArrayList<Integer>();
				Collection<Integer> freeUnknowns = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
					Integer next = it.next();
					if(isNodeWithoutEdgeDirection(g, next, 1)){
						freeUnknowns.add(next);
					}
					unknowns.add(next);
				}
				
				Collection<Integer> relations = new ArrayList<Integer>();
				Collection<Integer> freeRelations = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
					Integer next = it.next();
					if(isNodeWithoutEdgeDirection(g, next, 1)){
						freeRelations.add(next);
					}
					relations.add(next);
				}
				
				m.put(PROBLEM_UNKNOWNS, unknowns);
				m.put(PROBLEM_FREE_UNKNOWNS, freeUnknowns);
				m.put(PROBLEM_RELATIONS, relations);
				m.put(PROBLEM_FREE_RELATIONS, freeRelations);
				m.put(PROBLEM_OUTPUTS, outputs);
				m.put(PROBLEM_REMOVED, removed);
				m.put(PROBLEM_ORIGINAL_MATCHING, originalMatch);
				m.put(PROBLEM_COMPONENTS, components(components(g)).values());
				m.put(PROBLEM_APPROPRIATE, (g.size(true) + g.size(false)) != 0);
			}
		} else{
			if(m != null){
				m.remove(PROBLEM_UNKNOWNS);
				m.remove(PROBLEM_FREE_UNKNOWNS);
				m.remove(PROBLEM_RELATIONS);
				m.remove(PROBLEM_FREE_RELATIONS);
				m.remove(PROBLEM_OUTPUTS);
				m.remove(PROBLEM_REMOVED);
				m.remove(PROBLEM_ORIGINAL_MATCHING);
				m.remove(PROBLEM_COMPONENTS);
				m.remove(PROBLEM_APPROPRIATE);
			}
		}
		
	}

}
