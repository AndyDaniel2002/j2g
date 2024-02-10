package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cujae.map.j2g.dgraphm.algo.Tarjan;
import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class ResolventAnalysis extends AbstractAnalysis {

	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){
			
			Collection<BEdge> eq0 = new ArrayList<BEdge>();
			splitEdges(g, null, eq0, null);
			
			for(Iterator<BEdge> it = eq0.iterator(); it.hasNext();){
				BEdge next = it.next();
				g.put(next.even(), next.odd(), -2);
			}
			
			Collection<Collection<Integer>> scc = new Tarjan().execute(g);
			
			m.put(RESOLVENT_STRONG_CONNECTED_COMPONENTS, scc);
			
		} else{
			m.remove(RESOLVENT_STRONG_CONNECTED_COMPONENTS);
		}
		
		

	}

}
