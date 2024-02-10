package cujae.map.j2g.maximummatching;

import java.util.ArrayList;



import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BipartiteX;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.HopcroftKarp;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TarjanSCC;

/** Clase controladora del proyecto
 * @author Thalía Rodríguez
 * @version 1.0
 * @since 1.0
 */
public class Controladora {
	private Graph grafo;
	private HopcroftKarp hk;
	private ArrayList<PareoMaximo> listaPareos;
	private ArrayList<Integer> listaVerticesPareoIzq;
	private ArrayList<Integer> listaVerticesPareoDer;// nodos del pareo a la derecha
	private ArrayList<Arista> listaAristasGrafoB;
	private ArrayList<Integer> listaNodosSuperiorePareoMax;
	private ArrayList<Integer> listaVerticesParticionIzq;
	private ArrayList<Integer> listaVerticesParticionDer;

	private GrafoRestriccion grafoRestriccion;
	private GestorGrafoDirigido gestorGrafoDirigido;
	private GestorCFC gestorCFC;
	private static Controladora controladora;


	private Controladora() {
		super();

	}

	public void inicializar(Graph grafo) {

		this.grafo = grafo;
		this.listaNodosSuperiorePareoMax = new ArrayList<>();
		this.hk = new HopcroftKarp(grafo);
		inicializarListaAristasGrafoB();
		this.listaPareos = new ArrayList<>();
		encontrarPrimerPareoMaximo();
		definirParticionesNodosDelPareo();
		inicializarParticiones();
		this.gestorGrafoDirigido = new GestorGrafoDirigido();
		this.gestorCFC = new GestorCFC();
		this.grafoRestriccion = new GrafoRestriccion();

	}

	public static Controladora getInstancia(){
		if(controladora == null){
			controladora = new Controladora();
		}
		return controladora;
	}
	public Graph getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph grafo) {
		this.grafo = grafo;
	}

	public HopcroftKarp getHk() {
		return hk;
	}

	public void setHk(HopcroftKarp hk) {
		this.hk = hk;
	}

	public ArrayList<PareoMaximo> getListaPareos() {
		return listaPareos;
	}

	public void setListaPareos(ArrayList<PareoMaximo> listaPareos) {
		this.listaPareos = listaPareos;
	}
	public ArrayList<Integer> getListaVerticesParticionIzq() {
		return listaVerticesParticionIzq;
	}

	public void setListaVerticesParticionIzq(
			ArrayList<Integer> listaVerticesParticionIzq) {
		this.listaVerticesParticionIzq = listaVerticesParticionIzq;
	}

	public ArrayList<Integer> getListaVerticesParticionDer() {
		return listaVerticesParticionDer;
	}

	public void setListaVerticesParticionDer(
			ArrayList<Integer> listaVerticesParticionDer) {
		this.listaVerticesParticionDer = listaVerticesParticionDer;
	}
	public ArrayList<Integer> getListaVerticesPareoIzq() {
		return listaVerticesPareoIzq;
	}

	public void setListaVerticesPareoIzq(ArrayList<Integer> listaVerticesPareoIzq) {
		this.listaVerticesPareoIzq = listaVerticesPareoIzq;
	}

	public ArrayList<Integer> getListaVerticesPareoDer() {
		return listaVerticesPareoDer;
	}

	public void setListaVerticesPareoDer(ArrayList<Integer> listaVerticesPareoDer) {
		this.listaVerticesPareoDer = listaVerticesPareoDer;
	}

	public ArrayList<Arista> getListaAristasGrafoB() {
		return listaAristasGrafoB;
	}

	public void setListaAristasGrafoB(ArrayList<Arista> listaAristasGrafoB) {
		this.listaAristasGrafoB = listaAristasGrafoB;
	}

	public ArrayList<Integer> getListaNodosSuperiorePareoMax() {
		return listaNodosSuperiorePareoMax;
	}

	public void setListaNodosSuperiorePareoMax(
			ArrayList<Integer> listaNodosSuperiorePareoMax) {
		this.listaNodosSuperiorePareoMax = listaNodosSuperiorePareoMax;
	}

	/**
	 * Método encargado de encontrar un pareo mÃ¡ximo M a partir de un grafo bipartito
	 * @param
	 */
	private void encontrarPrimerPareoMaximo(){
		//Mostrar pareo maximo encontrado
		ArrayList<Arista> aristas = new ArrayList<>();
		/*StdOut.print("Max matching: ");
		System.out.println("Perfecto :"+this.hk.isPerfect());*/
		for (int v = 0; v < grafo.V(); v++) {
			int w = this.hk.mate(v);
			if (this.hk.isMatched(v) && v < w){// v < w para evitar que se repitan las aristas)
				Arista arista = buscarAristaGrafoOriginal(v, w);
				arista.setMaximamenteMatcheable(0);
				aristas.add(arista);
				listaNodosSuperiorePareoMax.add(v);
				listaNodosSuperiorePareoMax.add(w);
			}	
		}
		PareoMaximo pareo = new PareoMaximo(aristas);
		listaPareos.add(pareo);
	}

	/**
	 * Método encargado de llenar la lista de aristas pertenecientes al grafo bipartito original
	 */
	private void inicializarListaAristasGrafoB(){
		listaAristasGrafoB = new ArrayList<>();
		int tamannoPareo = hk.size();
		for(int v=0; v< grafo.V(); v++){
			Iterator<Integer> iter = grafo.adj(v).iterator();
			while(iter.hasNext()){
				int w = iter.next();

				if(buscarAristaGrafoOriginal(v,w) == null){
					Arista a = new Arista(v, w);
					if( v <= tamannoPareo && w <= tamannoPareo){
					}
					listaAristasGrafoB.add(a);
				}
			}
		}
	}

	/**
	 * Método encargado de encontrar una arista en el grafo bipartito original
	 * @param nodoE nodo de entrada de la arista
	 * @param nodoS nodo de salida de la arista
	 * @return La aritsa formada por nodoE y nodoS, null en caso contrario
	 */
	public Arista buscarAristaGrafoOriginal (int nodoE, int nodoS){
		Arista a = null;
		boolean existe = false;
		int i=0;
		while(!existe && i<listaAristasGrafoB.size()){
			int v = listaAristasGrafoB.get(i).getNodoEntrada();
			int w = listaAristasGrafoB.get(i).getNodoSalida();
			if((nodoE == v && nodoS == w) ||(nodoE == w && nodoS == v)){
				existe = true;
				a = listaAristasGrafoB.get(i);		
			}
			else{
				i++;
			}
		}
		return a;
	}


	/**
	 * Método encargado de buscar una arista en un grafo bipartito dado
	 * @param grafo Grafo bipartito
	 * @param nodoE nodo de entrada de la arista
	 * @param nodoS nodo de salidad de la arista
	 * @return true si existe l arista en el grafo, false en caso contrario
	 */
	public boolean buscarAristaGrafo(Graph grafo,int nodoE, int nodoS){
		boolean existe = false;
		Iterator <Integer> ady = grafo.adj(nodoE).iterator();
		while(!existe && ady.hasNext() ){
			if(ady.next() == nodoS){
				existe = true;
			}		
		}
		return existe;
	}



	/**
	 * Método encargado de marcar como máximamente matcheable las aristas visitadas a partir de un nodo K en un
	 * grafo dirigido
	 * @param grafoD Grafo dirigido
	 * @return lista de aristas maximamente matcheables
	 */
	public void marcarAristaMaxMatchConBFS(Digraph grafoD){
		BreadthFirstDirectedPaths bdfs = new  BreadthFirstDirectedPaths(grafoD, grafoD.V()-1);
		//ArrayList<Arista> visitadas = new ArrayList<>();
		for (int v =0; v<grafoD.V()-1; v++){
			if(bdfs.hasPathTo(v)){
				Stack<Integer> camino = (Stack<Integer>) bdfs.pathTo(v); //Pila
				camino.pop();
				while(!camino.isEmpty()){
					int primero = camino.pop();
					if(!camino.isEmpty()){
						int segundo = camino.peek();
						if(primero < listaVerticesParticionIzq.size() && segundo < listaVerticesParticionDer.size()){
							int posPrimeroGrafoOriginal = this.listaVerticesParticionIzq.get(primero);
							int posSegundoGrafoOriginal = this.listaVerticesParticionDer.get(segundo);
							Arista arista = buscarAristaGrafoOriginal(posPrimeroGrafoOriginal,posSegundoGrafoOriginal);
							if(arista != null){
								arista.setMaximamenteMatcheable(0);

							}
							else {
								posPrimeroGrafoOriginal = this.listaVerticesParticionDer.get(primero);
								posSegundoGrafoOriginal = this.listaVerticesParticionIzq.get(segundo);
								arista = buscarAristaGrafoOriginal(posPrimeroGrafoOriginal,posSegundoGrafoOriginal);
								if(arista != null){
									arista.setMaximamenteMatcheable(0);								
								}
							}
						}

					}				
				}
			}
		}
	}

	

	/**
	 * Método encargado de clasificar una arista en superior como No MÃ¡ximamente Matcheable e inferior como
	 * MÃ¡ximanete Matcheable en un grafo bipartito
	 * @param a Arista a clasificar
	 */
	public void clasificarAristaEnSuperiorOInferior(Arista a){
		if((hk.isMatched(a.getNodoSalida()) && hk.isMatched(a.getNodoEntrada()))
				&& !buscarAristaListaPareoMax(a.getNodoEntrada(),a.getNodoSalida())){
			a.setMaximamenteMatcheable(1);
		}
		else{
			a.setMaximamenteMatcheable(0);
		}
	}

	/**
	 * Método encargado de buscar una arista en el pareo mÃ¡ximo M
	 * @param entrada nodo de entrada de la arista
	 * @param salida nodo de saldiad e la arista
	 * @return true si existe la arista, false en caso contrario
	 */
	public boolean  buscarAristaListaPareoMax(int entrada, int salida){
		boolean existe = false;
		ArrayList <Arista> listaArista = this.listaPareos.get(0).getAristas();
		int i=0;
		while(!existe && i<listaArista.size()){
			int nodoEntList =  listaArista.get(i).getNodoEntrada();
			int nodoSalList = listaArista.get(i).getNodoSalida();
			if((entrada == nodoEntList && salida == nodoSalList) || (salida == nodoEntList && entrada == nodoSalList)){
				existe = true;
			}
			else{
				i++;
			}
		}
		return existe;
	}

	/**
	 * Método encargado de dividir los nodos pertenecientes a un pareo mÃ¡ximo en dos particiones, izquierda y derecha
	 */
	private void definirParticionesNodosDelPareo(){
		listaVerticesPareoDer = new ArrayList<>();
		listaVerticesPareoIzq = new ArrayList<>();

		for (int v=0; v<this.listaNodosSuperiorePareoMax.size(); v++){
			if(v % 2 ==0){
				listaVerticesPareoIzq.add(listaNodosSuperiorePareoMax.get(v));
			}
			else
				listaVerticesPareoDer.add(listaNodosSuperiorePareoMax.get(v));

		}
	}

	/**
	 * Método encargado de devolver una lista con todos los pareos mÃ¡ximos presentes en un grafo bipartito
	 */
	public void devolverListaConTodosPareos(){
		this.listaPareos.clear();
		ArrayList<Arista> listaAristasMaxMatch = new ArrayList<>();
		for(Arista arista:listaAristasGrafoB){
			if(arista.getMaximamenteMatcheable() == 0){
				listaAristasMaxMatch.add(arista);
			} 
		}
		System.out.println("Aristas "+listaAristasMaxMatch.size());
		System.out.println("Cantidad a elegir "+hk.size());
		Set<Set<Arista>> combinacionesPareo = Sets.combinations(ImmutableSet.copyOf(listaAristasMaxMatch), hk.size()); //hace copia y convierte lista en set
		System.out.println("Combinaciones "+combinacionesPareo.size());
		for(Set<Arista> set: combinacionesPareo){
			ArrayList<Arista> aristas = new ArrayList<>(set);
			PareoMaximo pareo = new PareoMaximo(aristas);
			if(verificarPareo(pareo)){
				listaPareos.add(pareo);
			}	
		}	
	}

	/**
	 * Método encargado de eliminar los Pareos MÃ¡ximos que presenten nodos repetidos
	 * @param pareo Pareo MÃ¡ximo
	 * @return true si existen nodos repetidos en el pareo, false en caso contrario
	 */
	private boolean verificarPareo(PareoMaximo pareo){
		boolean verificar = true;
		ArrayList<Integer> nodosVisitados = new ArrayList<>();
		int i=0;
		while(i< pareo.getAristas().size() && verificar){
			Arista a = pareo.getAristas().get(i);
			if(nodosVisitados.contains(a.getNodoEntrada())|| nodosVisitados.contains(a.getNodoSalida())){
				verificar = false;
			}
			else{
				nodosVisitados.add(a.getNodoEntrada());
				nodosVisitados.add(a.getNodoSalida());
				i++;
			}
		}
		return verificar;
	}

	/**
	 * Método encargado de inicializar las particiones(izquierda y derecha)
	 */
	public void inicializarParticiones(){
		listaVerticesParticionIzq = new ArrayList<>();
		listaVerticesParticionDer = new ArrayList<>();
		BipartiteX grafoB = new BipartiteX(this.grafo);
		for(int v=0; v < this.grafo.V(); v++){
			if(!grafoB.color(v)){
				listaVerticesParticionIzq.add(v);
			}
			else{
				listaVerticesParticionDer.add(v);
			}
		}
	}

	/**
	 * Método encargado de realizar el procedimiento correspondient epara un grafo con pareo perfecto
	 * @param grafo Grafo bipartito
	 */
	private void procedimientoPareoPerfecto(Graph grafo){ //algoritmo2
		Digraph grafoDirigido = gestorGrafoDirigido.devolverGrafoDirigido(grafo);
		gestorCFC.componentesFuertementeConectados(grafoDirigido);
	}

	/**
	 * Método encargado de ejecutar el algoritmo 1
	 * @return lista con todos los pareos mÃ¡ximos
	 */
	public ArrayList<PareoMaximo> devolverPareosMaximos(){ //algoritmo1
		if(hk.isPerfect()){
			procedimientoPareoPerfecto(this.grafo);//algoritmo 2
		}
		else{
			encontrarAristasMaxMatchUtilizandoPareoMaximo(this.grafo);//algoritmo3
		}
		devolverListaConTodosPareos();
		return this.listaPareos;
	}

	/**
	 * Método encargado de aplicar el algoritmo 3 a un grafo bipartito
	 * @param grafo Grafo Bipartito
	 */
	public void encontrarAristasMaxMatchUtilizandoPareoMaximo(Graph grafo){ //algoritmo3
		for(int i=0; i<this.listaAristasGrafoB.size(); i++){
			clasificarAristaEnSuperiorOInferior(listaAristasGrafoB.get(i));
		}	

		Graph grafoRestriccionObtenido = grafoRestriccion.obtenerGrafoRestriccion();
		procedimientoPareoPerfecto(grafoRestriccionObtenido);

		Digraph grafoDirigidoIzq = gestorGrafoDirigido.dirigirGrafoIzquierdaDerecha();
		Digraph grafoDirigidoDer = grafoDirigidoIzq.reverse();

		Digraph grafoDirigidoIzqK = gestorGrafoDirigido.conectarNodoFuenteANodaK(grafoDirigidoIzq);
		Digraph grafoDirigidoDerK = gestorGrafoDirigido.conectarNodoFuenteANodaK(grafoDirigidoDer);

		marcarAristaMaxMatchConBFS(grafoDirigidoIzqK);
		marcarAristaMaxMatchConBFS(grafoDirigidoDerK);


	}


}
