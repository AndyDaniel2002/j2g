package cujae.map.j2g.maximummatching;

import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.TarjanSCC;

public class GestorCFC {
	private Controladora controladora; 
	 
	
	
	public GestorCFC() {
		super();
		this.controladora = Controladora.getInstancia();
	}



	/**
	 * MÈtodo encargado de encontrar las aristas pertenecientes a los componentes fuertemente conectados de un grafo
	 * dirigido y marcarlas como m√°ximamente matcheables
	 * @param grafoDirigido Grafo dirigido
	 */
	public void componentesFuertementeConectados(Digraph grafoDirigido){
		TarjanSCC tarjan = new TarjanSCC(grafoDirigido);
		for (int v=0; v < grafoDirigido.V(); v++){
			Iterator<Integer> iter = grafoDirigido.adj(v).iterator();
			while(iter.hasNext()){
				int a = iter.next();
				if(tarjan.stronglyConnected(v, a)){
					//para cuando el pareo no es perfecto
					int nodoV = controladora.getListaVerticesPareoIzq().get(v);
					int nodoA = controladora.getListaVerticesPareoDer().get(a);
					Arista arista = controladora.buscarAristaGrafoOriginal(nodoV, nodoA);
					if(arista != null){
						arista.setMaximamenteMatcheable(0);
					}					
				}
			}
		}
	}

}
