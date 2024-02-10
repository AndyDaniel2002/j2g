package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.model.BGraph;

public class ModelAnalysis extends AbstractAnalysis {


	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){
			
			Set<Integer> s = nodesWithEdgeSizeUnder(g, 2, false);
			for(Iterator<Integer> it = s.iterator(); it.hasNext();){		
				g.remove(it.next());
			}
			
			Set<Integer> t = nodesWithEdgeSizeUnder(g, 1, true);
			for(Iterator<Integer> it = t.iterator(); it.hasNext();){
				g.remove(it.next());
			}
			
			s.addAll(t);
			
			if(m != null){
				
				Collection<Integer> variables = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
					variables.add(it.next());
				}
				
				Collection<Integer> relations = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
					relations.add(it.next());
				}
				
				m.put(MODEL_VARIABLES, variables);
				m.put(MODEL_RELATIONS, relations);
				m.put(MODEL_REMOVED, s);
				m.put(MODEL_COMPONENTS, components(components(g)).values());
			}
		} else{
			m.remove(MODEL_VARIABLES);
			m.remove(MODEL_RELATIONS);
			m.remove(MODEL_REMOVED);
			m.remove(MODEL_COMPONENTS);
		}
		
	}

}
