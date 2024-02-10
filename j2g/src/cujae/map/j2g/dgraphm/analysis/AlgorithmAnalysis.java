package cujae.map.j2g.dgraphm.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.algo.Tarjan;
import cujae.map.j2g.dgraphm.model.BGraph;

public class AlgorithmAnalysis extends AbstractAnalysis {


	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){
			
			Set<Integer> outputs = tryExtract(g, PROBLEM_OUTPUTS, m, true);
			
			Set<Integer> s = followDirection(g, 1, outputs);
			
			Set<Integer> removed = new HashSet<Integer>();
			for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
				Integer next = it.next();
				if(!s.contains(next)){
					removed.add(next);				
				}
			}
			
			for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
				Integer next = it.next();
				if(!s.contains(next)){
					removed.add(next);				
				}
			}
			
			for(Iterator<Integer> it = removed.iterator(); it.hasNext();){
				g.remove(it.next());
			}
			
			if(m != null){
				m.put(ALGORITHM_REMOVED, removed);
				m.put(ALGORITHM_STRONG_CONNECTED_COMPONENTS, new Tarjan().execute(g));
				m.put(ALGORITHM_COMPONENTS, components(components(g)).values());
			}	

		} else{
			if(m != null){
				m.remove(ALGORITHM_REMOVED);
				m.remove(ALGORITHM_STRONG_CONNECTED_COMPONENTS);
				m.remove(ALGORITHM_COMPONENTS);
			}
		}
		
	}

}
