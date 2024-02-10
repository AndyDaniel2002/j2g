package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class MarkedResolventAnalysis extends AbstractAnalysis {

	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){			
			
			Map<Integer, Integer> preCanonical = canonical(g);
			Collection<BEdge> markedEdges = new ArrayList<BEdge>();
			for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
				Integer next = it.next();
				for(Iterator<BEdge> it2 = g.adjIterator(next); it2.hasNext();){
					BEdge next2 = it2.next();
					Integer evenValue = preCanonical.get(next2.even());
					Integer oddValue = preCanonical.get(next2.odd());
					
					if(evenValue != oddValue){
						markedEdges.add(next2);
					}
				}
			}

			Map<Integer, Collection<Integer>> postCanonical = components(canonical(g));
			
			Collection<Integer> candidateFreedomDegrees = new ArrayList<Integer>();		
			Collection<Integer> w1 = postCanonical.get(1);
			if(w1 == null){
				w1 = new ArrayList<Integer>();
			} 
			for(Iterator<Integer> it = w1.iterator(); it.hasNext();){
				Integer next = it.next();
				if(next != null){
					if(next % 2 == 0){
						candidateFreedomDegrees.add(next);
					} 
				}
			}
			
			Collection<Integer> candidateDeficiences = new ArrayList<Integer>();
			Collection<Integer> w3 = postCanonical.get(3);
			if(w3 == null){
				w3 = new ArrayList<Integer>();
			}
			for(Iterator<Integer> it = w3.iterator(); it.hasNext();){
				Integer next = it.next();
				if(next != null){
					if(next % 2 == 1){
						candidateDeficiences.add(next);
					}
				}
			}
			
			Collection<Integer> w2 = postCanonical.get(2);
			if(w2 == null){
				w2 = new ArrayList<Integer>();
			}
			
			if(m != null){
				m.put(MARKED_RESOLVENT_W1, w1);
				m.put(MARKED_RESOLVENT_W2, w2);
				m.put(MARKED_RESOLVENT_W3, w3);
				m.put(MARKED_RESOLVENT_CANDIDATE_FREEDOM_DEGREE, candidateFreedomDegrees);
				m.put(MARKED_RESOLVENT_CANDIDATE_DEFICIENCE, candidateDeficiences);
				m.put(MARKED_RESOLVENT_MARKED_EDGES, markedEdges);
			}
		} else{
			if(m != null){
				m.remove(MARKED_RESOLVENT_W1);
				m.remove(MARKED_RESOLVENT_W2);
				m.remove(MARKED_RESOLVENT_W3);
				m.remove(MARKED_RESOLVENT_CANDIDATE_FREEDOM_DEGREE);
				m.remove(MARKED_RESOLVENT_CANDIDATE_DEFICIENCE);
				m.remove(MARKED_RESOLVENT_MARKED_EDGES);
			}
		}


	}

}
