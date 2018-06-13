package kmeans;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Kmeans {

	public static final int MAX = 100;
	public static final int MIN = 0;
	
	private Set<Punto> datos;
	private Set<Centroide> centroides;
	private int iteraciones;
	private boolean finalizado;
	
	public Kmeans(Set<Punto> puntos, int numCentroides) {
		this.datos = puntos; //new HashSet<Punto>();
		this.iteraciones = 0;
		finalizado = false;
		generarCentroides(numCentroides);
	}
	
	public void init() {		
		while(!finalizado){			
			agrupar();
		};
		
		
		System.out.println("ITERACIONES: " + iteraciones);
	}
	
	public void iterar() {
		if(!finalizado) agrupar();
	}
	
	/**
	 * Crea centroides en Puntos aleatorios.
	 */
	private void generarCentroides(int numCentroides) {
		centroides = new HashSet<Centroide>();		
		
		for(int i = 0; i < numCentroides; i++) {
			Centroide centroide = new Centroide("Clase_"+ i );
			centroides.add(centroide);
		}		
	}	
	
	private void agrupar() {	
		
		if(finalizado) return;
		
		
		Iterator<Centroide> itC = centroides.iterator();
		while(itC.hasNext()){
			Centroide centroide = itC.next();			
			centroide.vaciar();
			System.out.println("Centroide: " +centroide);
		}
		
		Iterator<Punto> it = datos.iterator();
		while(it.hasNext()){
			Punto punto = it.next();			
			Centroide centroide = centroideCercano(punto);
			centroide.addPunto(punto);
			punto.setClase(centroide.getNombre());
		}
		
		
		recalculaCentroide();
		iteraciones++;

	}
	
	
	private Centroide centroideCercano(Punto punto) {		
		float min = Float.MAX_VALUE;
		float dist;
		Centroide centroideCercano = null;
		
		Iterator<Centroide> it = centroides.iterator();
		while(it.hasNext()){
			Centroide centroide = it.next();			
			dist = punto.distancia(centroide.getPosicion());
			
			if(dist < min) {
				min = dist;
				centroideCercano = centroide;
			}
		}
		
		return centroideCercano;
	}
	
	
	private void recalculaCentroide() {
		boolean desvio = false;

		Iterator<Centroide> itC = centroides.iterator();
		while(itC.hasNext()){
			Centroide centroide = itC.next();		
			Punto p = new Punto(centroide.getPosicion().getX(),centroide.getPosicion().getY());
			centroide.recalculaPosicion();
			 
			if(centroide.getPosicion().distancia(p) > 0) {
				desvio = true;
			} 
		}
		
		finalizado = !desvio ;
	}
	
	public String toString() {
		String mensaje = "";
		
		Iterator<Centroide> it = centroides.iterator();
		while(it.hasNext()){
			Centroide centroide = it.next();	
			mensaje += "\nNombre cluster: " + centroide.getNombre() + "["+centroide.getPosicion()+"]" +"\n";
			

			Iterator<Punto> itPunto = centroide.getPuntos().iterator();
			while(itPunto.hasNext()){
				Punto punto = itPunto.next();
				mensaje += punto + ", ";
			}
			
			mensaje += "\n";
			
		}
		
		return mensaje;
	}

	public Set<Punto> getDatos() {
		return datos;
	}

	public void setDatos(Set<Punto> datos) {
		this.datos = datos;
	}

	public Set<Centroide> getCentroides() {
		return centroides;
	}

	public void setCentroides(Set<Centroide> centroides) {
		this.centroides = centroides;
	}

	public int getIteraciones() {
		return iteraciones;
	}

	public void setIteraciones(int iteraciones) {
		this.iteraciones = iteraciones;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	
}
