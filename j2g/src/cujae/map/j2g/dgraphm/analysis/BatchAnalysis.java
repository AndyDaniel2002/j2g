package cujae.map.j2g.dgraphm.analysis;

import java.util.Map;

import javax.swing.event.EventListenerList;

import cujae.map.j2g.dgraphm.model.BGraph;
import cujae.map.j2g.dgraphm.util.AnalysisEvent;
import cujae.map.j2g.dgraphm.util.AnalysisListener;

public class BatchAnalysis extends AbstractAnalysis {
	
	private static BatchAnalysis instance;
	
	public static BatchAnalysis getInstance(){
		if(instance == null){
			instance = new BatchAnalysis();
		}
		return instance;
	}
	
	private final EventListenerList listenerList = new EventListenerList();
	
	private void fireAnalysisExecuted(BGraph g, Map<String, Object> m, int type){
		AnalysisEvent evt = new AnalysisEvent(this, type, g, m);
		AnalysisListener [] listeners = listenerList.getListeners(AnalysisListener.class);
		for(int i = 0; i < listeners.length; i ++){
			listeners[i].executed(evt);
		}
	}
	
	public void addAnalysisListener(AnalysisListener listener){
		listenerList.add(AnalysisListener.class, listener);
	}
	
	public void removeAnalysisListener(AnalysisListener listener){
		listenerList.remove(AnalysisListener.class, listener);
	}
	
	public AnalysisListener[] getAnalysisListeners(){
		return listenerList.getListeners(AnalysisListener.class);
	}

	@Override
	public void execute(BGraph g, Map<String, Object> m) {
		
		new ModelAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, MODEL_TYPE);
		
		new SituationAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, SITUATION_TYPE);
		
		new ProblemAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, PROBLEM_TYPE);
		
		new MatchingProblemAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, MATCHING_PROBLEM_TYPE);
		
		new ResolventAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, RESOLVENT_TYPE);
		
		new MarkedResolventAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, MARKED_RESOLVENT_TYPE);
		
		new AlgorithmAnalysis().execute(g, m);
		fireAnalysisExecuted(g, m, ALGORITHM_TYPE);

	}

}
