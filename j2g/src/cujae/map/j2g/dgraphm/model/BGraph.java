package cujae.map.j2g.dgraphm.model;

import java.util.Iterator;

public interface BGraph {
	
	public int size(boolean isEven);
	
	public int size(int key);

	public boolean contains(int key);
	
	public boolean add(int key);
	
	public boolean clear(int key);
	
	public boolean remove(int key);
	
	public Integer put(int even, int odd, Integer direction);
	
	public Integer get(int even, int odd);
	
	public Iterator<Integer> iterator(boolean isEven);
	
	public Iterator<BEdge> adjIterator(int key);

}
