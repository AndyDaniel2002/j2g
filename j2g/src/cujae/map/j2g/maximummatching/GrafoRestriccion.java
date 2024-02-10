package cujae.map.j2g.maximummatching;

import java.util.Iterator;

import edu.princeton.cs.algs4.Graph;

public class GrafoRestriccion {
	private Controladora controladora;

	
	public GrafoRestriccion() {
		super();
		this.controladora = Controladora.getInstancia();
	}


	public Graph obtenerGrafoRestriccion(){
		Graph grafoRestriccion = new Graph(controladora.getListaNodosSuperiorePareoMax().size());
		int posV = -1;
		int posA = -1;
		for (int i=0; i< controladora.getListaNodosSuperiorePareoMax().size(); i++){
			int v = controladora.getListaNodosSuperiorePareoMax().get(i);
			Iterator <Integer> adj = controladora.getGrafo().adj(v).iterator();
			while(adj.hasNext()){
				int a = adj.next();		
				if(controladora.getListaNodosSuperiorePareoMax().contains(a)){				
					posV = controladora.getListaNodosSuperiorePareoMax().indexOf(v);
					posA = controladora.getListaNodosSuperiorePareoMax().indexOf(a);		
					if(!controladora.buscarAristaGrafo(grafoRestriccion, posV, posA) || !controladora.buscarAristaGrafo(grafoRestriccion,posA, posV) ){
						grafoRestriccion.addEdge(posV, posA);
					}				
				}
			}	
		}
		//esta parte es para facilitar el trabajo en metodo de dirgir el grafo, crear grafo de resrtriccion en grafo originla(solo con aristas y nodo de grafo de restriccion)
		Graph restriccion = new Graph(controladora.getGrafo().V());
		for (int j=0; j< grafoRestriccion.V(); j++){
			Iterator <Integer> ady = grafoRestriccion.adj(j).iterator();
			while(ady.hasNext()){
				int adyacente =ady.next();
				int valorJ = controladora.getListaNodosSuperiorePareoMax().get(j);
				int valorAdy = controladora.getListaNodosSuperiorePareoMax().get(adyacente);
				if(!controladora.buscarAristaGrafo(restriccion, valorJ, valorAdy) || !controladora.buscarAristaGrafo(restriccion, valorJ, valorAdy)){
					restriccion.addEdge(valorJ,valorAdy);
				}
			}
		}
		return restriccion;
	}

}
