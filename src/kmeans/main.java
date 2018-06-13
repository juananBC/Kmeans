package kmeans;

import java.util.HashSet;
import java.util.Set;

public class main {

	private static final int NUM_PUNTOS = 30;
	
	public static void main(String[] args) {

		Set<Punto> puntos = new HashSet<Punto>();
		for(int i = 0; i < NUM_PUNTOS; i++) {
			puntos.add(new Punto());
		}
		
		Kmeans kmeans = new Kmeans(puntos, 6);

		kmeans.init();
		
		System.out.println(kmeans.toString());
	}

}
