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
	private boolean iniciado;
	private String ejeX, ejeY;

	public Kmeans(Set<Punto> puntos, int numCentroides, String ejeX, String ejeY) {
		this.iniciado = false;
		this.datos = puntos; // new HashSet<Punto>();
		this.iteraciones = 0;
		this.finalizado = false;
		this.ejeX = ejeX;
		this.ejeY = ejeY;
		generarCentroides(numCentroides);
	}

	public Kmeans(int numPuntos, int numCentroides, String ejeX, String ejeY) {
		this.iniciado = false;
		this.iteraciones = 0;
		this.finalizado = false;
		this.ejeX = ejeX;
		this.ejeY = ejeY;
		
		generaDatos(numPuntos);
		generarCentroides(numCentroides);
	}

	/**
	 * Realiza todas las iteraciones hasta que los centroides no varien su posicion
	 */
	public void init() {
		while (!finalizado)
			agrupar();
	}

	/**
	 * Va haciendo haciendo cada iteración de Kmeans de forma individual. Un
	 * iteración consta de: 1) Agrupar los puntos en el cluster más cercano. 2)
	 * Calcular el centroie a partir de la agrupación de puntos. 3) Mover el punto
	 * medio del cluster inicial a la nueva posición. 4) Comprobar que no el
	 * centroide no varia de posición, y dar por finalizado el algoritmo.
	 */
	public void iterar() {
		if (!finalizado)
			agrupar();
	}

	/**
	 * Genera aleatoriamente los centroides de las agrupaciones/clusters
	 */
	private void generarCentroides(int numCentroides) {
		centroides = new HashSet<Centroide>();

		for (int i = 0; i < numCentroides; i++) {
			Centroide centroide = new Centroide("Clase_" + i);
			centroides.add(centroide);
		}
	}

	/**
	 * Genera los puntos aleatorios para que sean agrupados.
	 * @param numPuntos
	 */
	private void generaDatos(int numPuntos) {
		this.datos = new HashSet<Punto>();

		for (int i = 0; i < numPuntos; i++) {
			datos.add(new Punto());
		}
	}
	
	/**
	 * Asigna cada punto al centroide más cercano.
	 */
	private void agrupar() {

		if (finalizado)
			return;

		Iterator<Centroide> itC = centroides.iterator();
		while (itC.hasNext()) {
			Centroide centroide = itC.next();
			centroide.vaciar();
		}

		Iterator<Punto> it = datos.iterator();
		while (it.hasNext()) {
			Punto punto = it.next();
			Centroide centroide = centroideCercano(punto);
			centroide.addPunto(punto);
			punto.setClase(centroide.getNombre());
		}

		recalculaCentroide();
		
		iteraciones++;

	}

	/**
	 * Devuelve el centroide más cercano del punto pasado por parámetro.
	 */
	private Centroide centroideCercano(Punto punto) {
		float min = Float.MAX_VALUE;
		float dist;
		Centroide centroideCercano = null;

		Iterator<Centroide> it = centroides.iterator();
		while (it.hasNext()) {
			Centroide centroide = it.next();
			dist = punto.distancia(centroide.getPosicion());

			if (dist < min) {
				min = dist;
				centroideCercano = centroide;
			}
		}

		return centroideCercano;
	}

	/**
	 * Calcula la nueva posición del centroide a partir de los puntos que forman un
	 * mismo cluster.
	 */
	private void recalculaCentroide() {
		boolean desvio = false;

		Iterator<Centroide> itC = centroides.iterator();
		while (itC.hasNext()) {			
			Centroide centroide = itC.next();
			
			// Elimina los centroides que no tienen puntos
//			if(centroide.getPuntos().size() == 0) {
//				//itC.remove();
//				break;
//			}
			
			Punto p = new Punto(centroide.getPosicion().getX(), centroide.getPosicion().getY());
			centroide.recalculaPosicion();

			if (centroide.getPosicion().distancia(p) > 0) {
				desvio = true;
			}
		}

		finalizado = !desvio;
	}

	public String toString() {
		String mensaje = "";

		Iterator<Centroide> it = centroides.iterator();
		while (it.hasNext()) {
			Centroide centroide = it.next();
			mensaje += "\nNombre cluster: " + centroide.getNombre() + "[" + centroide.getPosicion() + "]" + "\n";

			Iterator<Punto> itPunto = centroide.getPuntos().iterator();
			while (itPunto.hasNext()) {
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

	public boolean isIniciado() {
		return iniciado;
	}

	public void setIniciado(boolean iniciado) {
		this.iniciado = iniciado;
	}

	public String getEjeX() {
		return ejeX;
	}

	public void setEjeX(String ejeX) {
		this.ejeX = ejeX;
	}

	public String getEjeY() {
		return ejeY;
	}

	public void setEjeY(String ejeY) {
		this.ejeY = ejeY;
	}

}
