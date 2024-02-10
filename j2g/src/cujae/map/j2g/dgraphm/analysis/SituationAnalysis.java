package cujae.map.j2g.dgraphm.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cujae.map.j2g.dgraphm.model.BGraph;

public class SituationAnalysis extends AbstractAnalysis{

	public void execute(BGraph g, Map<String, Object> m) {
		
		if(g != null && g.size(true) != 0 && g.size(false) != 0){
			Collection<Integer> inputs = tryExtract(g, SITUATION_INPUTS, m, true);
			
			for(Iterator<Integer> it = inputs.iterator(); it.hasNext();){
				g.remove(it.next());
			}
			
			if(m != null){
				Set<Integer> deficiences = nodesWithEdgeSizeUnder(g, 1, false);
				
				Collection<Integer> unknowns = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
					unknowns.add(it.next());
				}
				
				Collection<Integer> relations = new ArrayList<Integer>();
				for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
					relations.add(it.next());
				}

				m.put(SITUATION_INPUTS, inputs);
				m.put(SITUATION_UNKNOWNS, unknowns);
				m.put(SITUATION_RELATIONS, relations);
				m.put(SITUATION_DEFICIENCES, deficiences);
				m.put(SITUATION_COMPATIBLE, deficiences.isEmpty());
				m.put(SITUATION_APPROPRIATE, g.size(true) != 0);
				m.put(SITUATION_COMPONENTS, components(components(g)).values());
			}	
		} else{
			if(m != null){
				m.remove(SITUATION_INPUTS);
				m.remove(SITUATION_UNKNOWNS);
				m.remove(SITUATION_RELATIONS);
				m.remove(SITUATION_DEFICIENCES);
				m.remove(SITUATION_COMPATIBLE);
				m.remove(SITUATION_APPROPRIATE);
				m.remove(SITUATION_COMPONENTS);
			}
		}
		
	}

}
