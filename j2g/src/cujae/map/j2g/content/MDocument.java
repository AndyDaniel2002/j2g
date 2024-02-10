package cujae.map.j2g.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MDocument {
	
	public static MCell getById(int id, Collection<? extends MCell> c){
		MCell found = null;
		
		for(Iterator<? extends MCell> it = c.iterator(); found == null && it.hasNext();){
			MCell next = it.next();
			if(id == next.getId()){
				found = next;
			}
		}
		
		return found;
	}
	
	private String parserName;
	private String autoSolve;
	private String lowestPriority;
	private String renamePolicy;
	
	private List<MVertex> variables;
	private List<MVertex> relations;
	private List<MAsymmetry> asymmetry;	

	public String getParserName() {
		return parserName;
	}

	public void setParserName(String parserName) {
		this.parserName = parserName;
	}

	public String getAutoSolve() {
		return autoSolve;
	}

	public void setAutoSolve(String autoSolve) {
		this.autoSolve = autoSolve;
	}

	public String getLowestPriority() {
		return lowestPriority;
	}

	public void setLowestPriority(String lowestPriority) {
		this.lowestPriority = lowestPriority;
	}

	public String getRenamePolicy() {
		return renamePolicy;
	}

	public void setRenamePolicy(String renamePolicy) {
		this.renamePolicy = renamePolicy;
	}

	public List<MVertex> getVariables() {
		if(variables == null){
			variables = new ArrayList<MVertex>();
		}
		return variables;
	}

	public List<MVertex> getRelations() {
		if(relations == null){
			relations = new ArrayList<MVertex>();
		}
		return relations;
	}

	public List<MAsymmetry> getAsymmetry() {
		if(asymmetry == null){
			asymmetry = new ArrayList<MAsymmetry>();
		}
		return asymmetry;
	}	

}
