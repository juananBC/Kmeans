package interfaz;

import java.util.ArrayList;
import java.util.HashSet;
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
	
	public Grafico(Kmeans kmeans) {
		this.kmeans = kmeans;
	}
	
	

	public  XYZDataset datosPintar() {
		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
		
		pintarClusters(defaultxyzdataset);
		pintarPuntosSinAgrupar(defaultxyzdataset);
		
		return defaultxyzdataset;
	}
	
	
	private void pintarClusters(DefaultXYZDataset defaultxyzdataset) {
		List<Float> X = new ArrayList<Float>();
		List<Float> Y = new ArrayList<Float>();
		List<Float> size = new ArrayList<Float>();
		
		Set<Centroide> centroides = kmeans.getCentroides();
		Iterator<Centroide> itC = centroides.iterator();
		while (itC.hasNext()) {
			X.clear();
			Y.clear();
			size.clear();
			
			Centroide centroide = itC.next();
			X.add(centroide.getPosicion().getX());
			Y.add(centroide.getPosicion().getY());
			size.add(TAM_PUNTO_CLUSTER);

			Set<Punto> puntos = centroide.getPuntos();
			Iterator<Punto> itP = puntos.iterator();
			while (itP.hasNext()) {
				Punto punto = itP.next();
				X.add(punto.getX());
				Y.add(punto.getY());
				size.add(TAM_PUNTO_VALOR);				
			}
			
	
			double[][] valores = new double[][] {toArray(X), toArray(Y), toArray(size)};
			defaultxyzdataset.addSeries(centroide.getNombre(), valores );
		}
	}
	
	private void pintarPuntosSinAgrupar(DefaultXYZDataset defaultxyzdataset) {
		List<Float> X = new ArrayList<Float>();
		List<Float> Y = new ArrayList<Float>();
		List<Float> size = new ArrayList<Float>();
		
		// Pintar los puntos sin agrupar
		if(!kmeans.isIniciado()) {		
			
			Set<Punto> puntos = kmeans.getDatos();
			Iterator<Punto> itP = puntos.iterator();
			while (itP.hasNext()) {
				Punto punto = itP.next();
				X.add(punto.getX());
				Y.add(punto.getY());
				size.add(TAM_PUNTO_VALOR);				
			}
			
			double[][] valores = new double[][] {toArray(X), toArray(Y), toArray(size)};
			defaultxyzdataset.addSeries("Sin agrupar", valores );
		}
	}
	
	private JFreeChart crearGrafico() {
		XYZDataset xyzdataset = datosPintar();
		JFreeChart jfreechart = ChartFactory.createBubbleChart("K-means", kmeans.getEjeX(), kmeans.getEjeY(),
				xyzdataset,	PlotOrientation.VERTICAL, true, true, false);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setForegroundAlpha(0.65F);

		NumberAxis range = (NumberAxis) xyplot.getRangeAxis();
        range.setRange(Kmeans.MIN, Kmeans.MAX+ Kmeans.MAX*0.1);
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
	
}
