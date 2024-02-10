package cujae.map.j2g.maximummatching;

import java.util.ArrayList;

public class PareoMaximo {
	private ArrayList<Arista> aristas;
	
	public PareoMaximo(ArrayList<Arista> aristas){
		this.aristas = aristas;
	}
	
	public PareoMaximo(){
		this.aristas = new ArrayList<>();
	}
	
	public int tamanoPareo(){
		return this.aristas.size();
	}

	public ArrayList<Arista> getAristas() {
		return aristas;
	}

	public void setAristas(ArrayList<Arista> aristas) {
		this.aristas = aristas;
	}

	@Override
	public String toString() {
		
		return "Aristas ->"+ aristas;
	}
	
	
	
	
	
	

}
