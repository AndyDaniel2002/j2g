package cujae.map.j2g.maximummatching;

public class Arista {

	private int nodoSalida;
	private int nodoEntrada;
	private int maximamenteMatcheable; // si -1 no tiene definici�n  si 0 maximamente matcheable  si 1 no es maximamente Matc
	private String tipoArista;

	public Arista(int nodoSalida, int nodoEntrada, int maximamenteMatcheable) {
		super();
		this.nodoSalida = nodoSalida;
		this.nodoEntrada = nodoEntrada;
		this.maximamenteMatcheable = maximamenteMatcheable;
		this.tipoArista = "No clasificada";
	}

	
	public Arista(int nodoSalida, int nodoEntrada){
		this.nodoSalida = nodoSalida;
		this.nodoEntrada = nodoEntrada;
		this.maximamenteMatcheable = -1;
		this.tipoArista = "No clasificada";
	}
	

	public Arista() {
		super();
	}


	public int getNodoSalida() {
		return nodoSalida;
	}

	public void setNodoSalida(int nodoSalida) {
		this.nodoSalida = nodoSalida;
	}

	public int getNodoEntrada() {
		return nodoEntrada;
	}

	public void setNodoEntrada(int nodoEntrada) {
		this.nodoEntrada = nodoEntrada;
	}
	public String getTipoArista() {
		return tipoArista;
	}

	public void setTipoArista(String tipoArista) {
		this.tipoArista = tipoArista;
	}

	@Override
	public String toString() {
		String tipoA = "No catalogada";
		if(maximamenteMatcheable == 0){
			tipoA ="MaxMatch";
		}
		else if (maximamenteMatcheable == 1) {
			tipoA ="No Max Match";
			
		}
		return nodoSalida +"-" + nodoEntrada/* + "- Tipo de max match:" + tipoA*/;
	}
	public int getMaximamenteMatcheable() {
		return maximamenteMatcheable;
	}

	public void setMaximamenteMatcheable(int maximamenteMatcheable) {
		this.maximamenteMatcheable = maximamenteMatcheable;
	}
	
}
