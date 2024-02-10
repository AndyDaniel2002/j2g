package cujae.map.j2g.dgraphm.algo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.dgraphm.model.BGraph;

public class KosarajuSharir {
	
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
	
	private int h;
	
	private boolean execute(BGraph g, Integer key, int direction, Map<Integer, Integer> visited){
		
		final boolean b = !visited.containsKey(key);
		
		if(b){
			final boolean isEven = key % 2 == 0;
			visited.put(key, null);
			for(Iterator<BEdge> it = g.adjIterator(key); it.hasNext();){
				BEdge next = it.next();
				int nextSignum = Integer.signum(next.direction());
				if(nextSignum == Integer.signum(next.direction())){
					execute(g, isEven ? next.odd() : next.even(), nextSignum * -1, visited);
				}
			}
			visited.put(key, h++);
		}
		
		return b;
	}
	
	public static Integer getKey4MaxValue(Map<Integer, Integer> visited){
		
		Integer key = null;
		Integer maxValue = Integer.MIN_VALUE;
		for(Iterator<Map.Entry<Integer, Integer>> it = visited.entrySet().iterator(); it.hasNext();){
			Map.Entry<Integer, Integer> next = it.next();
			if(next.getValue() > maxValue){
				maxValue = next.getValue();
				key = next.getKey();
			}
		}
		
		return key;
	}
	
	public synchronized Map<Integer, Integer> execute(BGraph g){
		
		h = 1;
		
		Map<Integer, Integer> visited = new HashMap<Integer, Integer>();
		
		for(Iterator<Integer> it = g.iterator(true); it.hasNext();){
			Integer next = it.next();
			execute(g, next, -1, visited);
		}
		
		for(Iterator<Integer> it = g.iterator(false); it.hasNext();){
			Integer next = it.next();
			execute(g, next, 1, visited);
		}
		
		Map<Integer, Integer> visited2 = new HashMap<Integer, Integer>();
		
		int value = 0;
		while(!visited.isEmpty()){
			Integer key = getKey4MaxValue(visited);
			visited.remove(key);
			
			int direction = key % 2 == 0 ? 1 : -1;
			
			if(deepFirstSearch(g, key, direction, false, value, visited2)){
				value++;
			}
		}
		
		return visited2;
		
	}

}
