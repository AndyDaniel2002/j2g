package cujae.map.j2g.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Iterator;

import cujae.map.j2g.mme.Moled;
import cujae.map.j2g.mme.Moled.EdgeEntry;

public class EdgesFormattable implements Formattable {
	
	private final Moled moled;
	private final EdgeEntry[] edges;
	private final boolean isVariable;
	
	private StringBuffer format(Collection<EdgeEntry> c, StringBuffer buffer) {

		for(Iterator<EdgeEntry> it = c.iterator(); it.hasNext();){
			EdgeEntry next = it.next();
			
			if(buffer.length() != 0){
				buffer.append(",");
			}
			
			String name = moled.get(isVariable ? next.getRelationIndex() 
					: next.getVariableIndex(), !isVariable).getName();
			
			buffer.append(name);			
			
		}
		
		return buffer;
	}

	public EdgesFormattable(Moled moled, EdgeEntry[] edges, boolean isVariable) {
		super();
		this.moled = moled;
		this.edges = edges;
		this.isVariable = isVariable;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width,
			int precision) {
		
		Collection<EdgeEntry> lt0 = new ArrayList<Moled.EdgeEntry>();
		Collection<EdgeEntry> eq0 = new ArrayList<Moled.EdgeEntry>();
		Collection<EdgeEntry> gt0 = new ArrayList<Moled.EdgeEntry>();
		
		Moled.splitEdges(edges, lt0, eq0, gt0);
		
		StringBuffer lt0buffer = format(lt0, new StringBuffer());
		StringBuffer eq0buffer = format(eq0, new StringBuffer());
		StringBuffer gt0buffer = format(gt0, new StringBuffer());
		
		formatter.format("%s;%s;%s", lt0buffer, eq0buffer, gt0buffer);

	}

}
