package cujae.map.j2g.dgraphm.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BGraphImpl implements BGraph {
	
	private Map<Integer, Map<Integer, Integer>> even;
	private Map<Integer, Map<Integer, Integer>> odd;

	public BGraphImpl() {
		this.even = new HashMap<Integer, Map<Integer,Integer>>();
		this.odd = new HashMap<Integer, Map<Integer,Integer>>();
	}

	@Override
	public int size(boolean isEven) {		
		return (isEven ? even : odd).size();	
	}

	@Override
	public int size(int key) {
		Map<Integer, Integer> m = ((key % 2 == 0) ? even : odd).get(key);
		return m != null ? m.size() : -1;
	}

	@Override
	public boolean contains(int key) {
		return (key % 2 == 0 ? even : odd).containsKey(key);
	}

	@Override
	public boolean add(int key) {
		if(key % 2 == 0){
			if(!even.containsKey(key)){
				even.put(key, new HashMap<Integer, Integer>());
				return true;
			}
		} else{
			if(!odd.containsKey(key)){
				odd.put(key, new HashMap<Integer, Integer>());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean clear(int key) {
		if(key % 2 == 0){
			Map<Integer, Integer> m = even.get(key);
			if(m != null){
				Set<Integer> s = m.keySet();					
				for(Iterator<Integer> it = s.iterator(); it.hasNext();){
					odd.get(it.next()).remove(key);
				}
				even.get(key).clear();
				return true;
			}
			return false;

		} else{
			Map<Integer, Integer> m = odd.get(key);
			if(m != null){
				Set<Integer> s = m.keySet();
				for(Iterator<Integer> it = s.iterator(); it.hasNext();){
					even.get(it.next()).remove(key);
				}
				odd.get(key).clear();
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean remove(int key) {
		if(key % 2 == 0){
			Map<Integer, Integer> m = even.remove(key);
			if(m != null){
				Set<Integer> s = m.keySet();
				for(Iterator<Integer> it = s.iterator(); it.hasNext();){
					odd.get(it.next()).remove(key);
				}
				return true;
			}
			return false;

		} else{
			Map<Integer, Integer> m = odd.remove(key);
			if(m != null){
				Set<Integer> s = m.keySet();
				for(Iterator<Integer> it = s.iterator(); it.hasNext();){
					even.get(it.next()).remove(key);
				}
				return true;	
			}
			return false;
		}
	}

	@Override
	public Integer put(int even, int odd, Integer direction) {
		if(this.even.containsKey(even) && this.odd.containsKey(odd)){
			if(direction == null){
				this.even.get(even).remove(odd);
				this.odd.get(odd).remove(even);
			} else{
				this.even.get(even).put(odd, direction);
				this.odd.get(odd).put(even, direction);
			}
		}
		return null;
	}

	@Override
	public Integer get(int even, int odd) {
		if(this.even.containsKey(even) && this.odd.containsKey(odd)){
			return this.even.get(even).get(odd);
		}
		return null;
	}

	@Override
	public Iterator<Integer> iterator(boolean isEven) {
		final Iterator<Integer> it = (isEven ? even : odd).keySet().iterator();
		
		return new Iterator<Integer>() {

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Integer next() {
				return it.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	private class AdjIterator implements Iterator<BEdge>{
		
		public final int key;
		public final boolean isEven;
		public final Iterator<Map.Entry<Integer, Integer>> it;

		public AdjIterator(int key, Iterator<Entry<Integer, Integer>> it) {
			super();
			this.key = key;
			this.it = it;
			this.isEven = this.key % 2 == 0;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public BEdge next() {
			Map.Entry<Integer, Integer> entry = it.next();
			int even = isEven ? key : entry.getKey();
			int odd = isEven ? entry.getKey() : key;
			return new BEdgeImpl(even, odd, entry.getValue());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public Iterator<BEdge> adjIterator(int key) {		
		Map<Integer, Integer> m = (key % 2 == 0 ? even : odd).get(key);		
		if(m != null){
			return new AdjIterator(key, m.entrySet().iterator());
		}
		return null;
	}

}
