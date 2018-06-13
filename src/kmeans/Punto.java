package kmeans;

public class Punto {


	private String clase;
	private float x, y;
	
	public Punto() {
		x = (float) (Math.random() * Kmeans.MAX - Kmeans.MIN);
		y = (float) (Math.random() * Kmeans.MAX - Kmeans.MIN);
	}
	
	public Punto(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float distancia(Punto p) {
		return (float) Math.sqrt( Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
	}

	public void sumar(Punto p) {
		System.out.print(x + ", " + y + "    ");
		x += p.x;
		y += p.y;
		System.out.println(x + ", " + y);
	}
	
	public void dividir(int n) {
		if(n == 0) return;
		
		System.out.print(x + ", " + y + "    ");
		x = x / n;
		y = y / n;
		System.out.println(x + ", " + y);

	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String toString() {
		return "("+x + ", "+y + ")";
	}

}
