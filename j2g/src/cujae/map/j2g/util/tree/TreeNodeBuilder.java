package cujae.map.j2g.util.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cujae.map.j2g.dgraphm.model.BEdge;
import cujae.map.j2g.mme.Moled.NodeEntry;

public class TreeNodeBuilder {
	
	private static void update(MyTreeNode root, Object value, Map<Integer, Object> valueMap){
		if(value instanceof Collection<?>){
			Collection<?> c = (Collection<?>) value;
			for(Iterator<?> it = c.iterator(); it.hasNext();){
				Object next = it.next();
				if(next instanceof Integer){
					MyTreeNode newChild = new MyTreeNode();
					newChild.setUserObject(next);
					newChild.setUserValue(next);
					
					if(valueMap != null){
						newChild.setUserObject(valueMap.get(next));
					}
					root.add(newChild);
				} else if(next instanceof BEdge){
					BEdge e = (BEdge) next;
					MyTreeNode newChild = new MyTreeNode();
					newChild.setUserObject(next);
					newChild.setUserValue(next);
					
					if(valueMap != null){
						newChild.setUserObject(String.format("%s,%s", 
								valueMap.get(e.even()), valueMap.get(e.odd())));
					}
					root.add(newChild);
				} else if(next instanceof NodeEntry){
					NodeEntry n = (NodeEntry) next;
					MyTreeNode newChild = new MyTreeNode();
					newChild.setUserObject(n);
					newChild.setUserValue(n.isVariable() ? 0 : 1);
					root.add(newChild);
				} else if(next instanceof Collection<?>){
					MyTreeNode newChild = new MyTreeNode();
					newChild.setUserObject(((Collection<?>)next).size());
					update(newChild, next, valueMap);					
					root.add(newChild);					
				}
			}
		} 
	}
	
	public static void update(MyTreeNode root, boolean clearFirst, 
			Map<Integer, Object> valueMap, 
			Map<String, String> labelMap,
			Map<String, Object> analysisMap){
		
		if(clearFirst){
			root.removeAllChildren();
			root.setAlert(false);
			
			for(Iterator<Map.Entry<String, String>> it = labelMap.entrySet().iterator(); it.hasNext();){
				Map.Entry<String, String> next = it.next();
				if(analysisMap.containsKey(next.getKey())){
					String label = labelMap.get(next.getKey());
					if(label == null){
						label = next.getKey();
					}
					
					Object value = analysisMap.get(next.getKey());					
					
					MyTreeNode newChild = new MyTreeNode(label);
					if(value instanceof Boolean){
						Boolean b = (Boolean) value;
						newChild.setUserValue(b);						
						newChild.setAlert(!b);
						
						if(newChild.isAlert()){
							root.setAlert(true);
						}
					}
					else{
						update(newChild, value, valueMap);
					}
					
					root.add(newChild);	
									
				}
			}			
		}		
	}
}
