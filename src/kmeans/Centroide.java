package kmeans;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Centroide {

	private String nombre;
	private Set<Punto> puntos;
	private Punto posicion;
	
	public Centroide(String nombre) {
		this.nombre= nombre;
		this.puntos = new HashSet<>();
		this.posicion = new Punto();
	}
	
	/**
	 * Devuelve verdadero si el punto añadido al cluster no existia
	 * @param punto
	 * @return
	 */
	public boolean addPunto(Punto punto) {
		String clase = punto.getClase();
		punto.setClase(nombre);
		puntos.add(punto);
		
		return nombre != null && !nombre.equals(clase);
	}
	
	public void recalculaPosicion() {
		if(puntos.isEmpty()) return;
			
		Punto puntoMedio = new Punto(0,0); 
		
		Iterator<Punto> it = puntos.iterator();
		while(it.hasNext()){
			Punto p = it.next();
			puntoMedio.sumar(p);
		}

		puntoMedio.dividir(puntos.size());
		
		posicion = puntoMedio;
		
	}
	
	public void vaciar() {
		puntos.clear();
	}
	
	public Punto getPosicion() {
		return posicion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Punto> getPuntos() {
		return puntos;
	}

	public void setPuntos(Set<Punto> puntos) {
		this.puntos = puntos;
	}

	public void setPosicion(Punto posicion) {
		this.posicion = posicion;
	}
	
	public String toString() {
		return nombre + " " + posicion + "[" + puntos.size() +"]";
	}
}
