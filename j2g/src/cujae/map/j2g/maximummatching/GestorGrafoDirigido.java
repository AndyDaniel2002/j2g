package cujae.map.j2g.maximummatching;

import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;

public class GestorGrafoDirigido {
	private Controladora controladora;

	public GestorGrafoDirigido() {
		super();
		this.controladora = Controladora.getInstancia();
		
	}
	/**
	 * Método encargado de devolver un grafo dirigido a partir de un grafo bipartito
	 * @param grafo Grafo Bipartito
	 * @return Grafo Dirigido H
	 */
	public Digraph devolverGrafoDirigido(Graph grafo){
		Digraph gd = new Digraph(grafo.V()/2); 
		for (int v=0; v<=gd.V(); v++){
			Iterator<Integer> ady = grafo.adj(v).iterator();
			while (ady.hasNext()){
				int a = ady.next();
				if(!controladora.buscarAristaListaPareoMax(v, a)){	
					int valorVEnGD = controladora.getListaVerticesPareoIzq().indexOf(v);
					int valorAEnGD = controladora.getListaVerticesPareoDer().indexOf(a);
					if(valorVEnGD != -1 && valorAEnGD !=-1)
						gd.addEdge(valorVEnGD, valorAEnGD);			
				}
			}
		}
		return gd;
	}
	/**
	 * Método encargado de devolver un grafo dirigido de izquierda a derecha Hlr a partir de un grafo bipartito
	 * @param grafo Grafo Bipartito
	 * @return Grafo Dirigido H
	 */
	public Digraph dirigirGrafoIzquierdaDerecha(){
		int cantV=0;
		if(controladora.getListaVerticesParticionIzq().size() >= controladora.getListaVerticesParticionDer().size()){
			cantV  = controladora.getListaVerticesParticionIzq().size();	
		}
		else{
			cantV  =controladora.getListaVerticesParticionDer().size();
		}
		Digraph gd = new Digraph(cantV);
		for(int v=0; v < controladora.getListaVerticesParticionIzq().size(); v++){
			Iterator <Integer> ady = controladora.getGrafo().adj(v).iterator();
			while(ady.hasNext()){
				int a = ady.next();
				if(!controladora.buscarAristaListaPareoMax(v, a)){	
					int valorVEnGD = controladora.getListaVerticesParticionIzq().indexOf(v);
					int valorAEnGD = controladora.getListaVerticesParticionDer().indexOf(a);
					if(valorVEnGD != -1 && valorAEnGD !=-1 && valorVEnGD != valorAEnGD)
						gd.addEdge(valorVEnGD, valorAEnGD);			

				}
			}
		}
		return gd;
	}
	
	/**
	 * Método encargado de conectar un nuevo nodo K a cada uno de los nodos de un grafo dirigido
	 * @param original Grafo dirigido al cual se va a conectar el nuevo nodo K
	 * @return Un nuevo grafo dirigido con un nodo K conectado a cda uno de los nodos del grafo
	 */
	public Digraph conectarNodoFuenteANodaK(Digraph original){
		int tamannoPareo = controladora.getHk().size();
		Digraph nuevo = new Digraph(original.V()+1);
		for(int i=0; i< nuevo.V()-1; i++){
			Iterator<Integer> iter = original.adj(i).iterator();
			while(iter.hasNext()){
				int w = iter.next();
				nuevo.addEdge(i,w);
			}
			if(i >= tamannoPareo){
				nuevo.addEdge(nuevo.V()-1,i);
			}

		}
		/*	System.out.println(original.toString());
		System.out.println("------");
		System.out.println(nuevo.toString());*/
		return nuevo;
	}

}
