package cujae.map.j2g.dgraphm.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class Tarjan {

	private Map<Integer, Integer> indexMap;
	private Map<Integer, Integer> lowLinkMap;
	private Stack<Integer> s;
	int index;
	
	private Collection<Collection<Integer>> scc;
	
	private void strongConnect(BGraph g, int v){
		indexMap.put(v, index);
		lowLinkMap.put(v, index);
		index ++;
		s.push(v);
		
		boolean even = v % 2 == 0;
		for(Iterator<BEdge> it = g.adjIterator(v); it.hasNext();){
			BEdge next = it.next();
			int directionSignum = Integer.signum(next.direction());
			if(even){
				if(directionSignum == -1){
					int w = next.odd();
					if(!indexMap.containsKey(w)){
						strongConnect(g, w);
						int min = Math.min(lowLinkMap.get(v), 
								lowLinkMap.get(w));
						lowLinkMap.put(v, min);
					} else{
						if(s.contains(w)){
							int min = Math.min(lowLinkMap.get(v), 
									indexMap.get(w));
							lowLinkMap.put(v, min);
						}
					}
				}
			} else{
				if(directionSignum == 1){
					int w = next.even();
					if(!indexMap.containsKey(w)){
						strongConnect(g, w);
						int min = Math.min(lowLinkMap.get(v), 
								lowLinkMap.get(w));
						lowLinkMap.put(v, min);
					} else{
						if(s.contains(w)){
							int min = Math.min(lowLinkMap.get(v), 
									indexMap.get(w));
							lowLinkMap.put(v, min);
						}
					}
				}
			}
		}
		
		if(lowLinkMap.get(v) == indexMap.get(v)){
			Collection<Integer> c = new ArrayList<Integer>();
			int w;			
			do{
				w = s.pop();
				c.add(w);
			} while(w != v);
			
			/**
			 * Original implementation according to the article
			 */
//			scc.add(c);
			
			/**
			 * Additional filter imposed due to our current scenario,
			 * because the minimum cycle form in our bipartite graph is
			 * of length 4, witch means 2 relations and 2 variables. Or 
			 * simple put, just ignore components lower than 4.
			 */
			if(c.size() >= 4){
				scc.add(c);
			}			
			
		}
		
	}
	
	public synchronized Collection<Collection<Integer>> execute(BGraph g){
		
		indexMap = new HashMap<Integer, Integer>();
		lowLinkMap = new HashMap<Integer, Integer>();
		s = new Stack<Integer>();
		index = 0;
		scc = new ArrayList<Collection<Integer>>();
		
		for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
			Integer next = it.next();
			if(!indexMap.containsKey(next)){
				strongConnect(g, next);
			}
		}
		
		for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
			Integer next = it.next();
			if(!indexMap.containsKey(next)){
				strongConnect(g, next);
			}
		}
		
		return scc;		
				
	}

}
