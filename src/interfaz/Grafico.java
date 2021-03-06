package interfaz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

import kmeans.Centroide;
import kmeans.Kmeans;
import kmeans.Punto;

public class Grafico {


	private static final float TAM_PUNTO_VALOR = (float) 2;
	private static final float TAM_PUNTO_CLUSTER = (float) 6;
	
	private Kmeans kmeans;
	private JPanel panel;
	
	public Grafico(Kmeans kmeans) {
		this.kmeans = kmeans;
		this.panel = createPanel();
	}
	
	public Grafico() {
		this.kmeans = null;
		this.panel = createPanel();
	}
	
	

	public  XYZDataset datosPintar() {
		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
		
		pintarClusters(defaultxyzdataset);
		pintarPuntosSinAgrupar(defaultxyzdataset);
		
		return defaultxyzdataset;
	}
	
	
	private void pintarClusters(DefaultXYZDataset defaultxyzdataset) {
		if(kmeans == null) return;
		
		List<Float> coordX = new ArrayList<>();
		List<Float> coordY = new ArrayList<>();
		List<Float> size = new ArrayList<>();
		
		Set<Centroide> centroides = kmeans.getCentroides();
		Iterator<Centroide> itC = centroides.iterator();
		while (itC.hasNext()) {
			coordX.clear();
			coordY.clear();
			size.clear();
			
			Centroide centroide = itC.next();

			Set<Punto> puntos = centroide.getPuntos();
			Iterator<Punto> itP = puntos.iterator();
			while (itP.hasNext()) {
				Punto punto = itP.next();
				coordX.add(punto.getX());
				coordY.add(punto.getY());
				size.add(TAM_PUNTO_VALOR);				
			}

			coordX.add(centroide.getPosicion().getX());
			coordY.add(centroide.getPosicion().getY());
			size.add(TAM_PUNTO_CLUSTER);
			
			double[][] valores = new double[][] {toArray(coordX), toArray(coordY), toArray(size)};
			defaultxyzdataset.addSeries(centroide.getNombre(), valores );
		}
	}
	
	private void pintarPuntosSinAgrupar(DefaultXYZDataset defaultxyzdataset) {
		if(kmeans == null) return;
		
		List<Float> coordX = new ArrayList<>();
		List<Float> coordY = new ArrayList<>();
		List<Float> size = new ArrayList<>();
		
		// Pintar los puntos sin agrupar
		if(!kmeans.isIniciado()) {		
			
			Set<Punto> puntos = kmeans.getDatos();
			Iterator<Punto> itP = puntos.iterator();
			while (itP.hasNext()) {
				Punto punto = itP.next();
				coordX.add(punto.getX());
				coordY.add(punto.getY());
				size.add(TAM_PUNTO_VALOR);				
			}
			
			double[][] valores = new double[][] {toArray(coordX), toArray(coordY), toArray(size)};
			defaultxyzdataset.addSeries("Sin agrupar", valores );
		}
	}
	
	private JFreeChart crearGrafico() {
		String ejeX = (kmeans != null)?  kmeans.getEjeX(): "";
		String ejeY = (kmeans != null)?  kmeans.getEjeY(): "";
		
		XYZDataset xyzdataset = datosPintar();
		JFreeChart jfreechart = ChartFactory.createBubbleChart("K-means",ejeX, ejeY,
				xyzdataset,	PlotOrientation.VERTICAL, true, true, false);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setForegroundAlpha(0.95F);

		NumberAxis range = (NumberAxis) xyplot.getRangeAxis();
        range.setRange(Kmeans.MIN, Kmeans.MAX);
        xyplot.getDomainAxis().setRange(0, 100);
        range.setTickUnit(new NumberTickUnit(0.1));
		return jfreechart;
	}
	
	public JPanel createPanel() {
		JFreeChart jfreechart = crearGrafico();
		ChartPanel chartpanel = new ChartPanel(jfreechart);

		chartpanel.setDomainZoomable(false);
		chartpanel.setRangeZoomable(false);

		return chartpanel;
	}
	

	private static double[] toArray(List<Float> arr) {
		double[] resultado = new double[arr.size()];
		
		for(int i = 0; i < arr.size(); i++) {
			resultado[i] = arr.get(i);
		}
		return resultado;
	}



	public JPanel getPanel() {
		return panel;
	}



	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	
	
}
