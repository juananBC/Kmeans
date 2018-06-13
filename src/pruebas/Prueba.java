package pruebas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RefineryUtilities;

import kmeans.Centroide;
import kmeans.Kmeans;
import kmeans.Punto;

public class Prueba {
	private static final int NUM_PUNTOS = 500;
	private static final int NUM_CENTROIDES = 6;
	private static final float TAM_PUNTO_VALOR = (float) 2;
	private static final float TAM_PUNTO_CLUSTER = (float) 6;

	private JFrame frame;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Prueba window = new Prueba();
					window.frame.setVisible(true);
					
					JPanel panel = new JPanel();
					panel.setBackground(new Color(102, 205, 170));
					
					JPanel jpanel = createDemoPanel();					
					window.frame.getContentPane().add(jpanel, BorderLayout.CENTER);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Prueba() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		
		frame.setTitle("Ejemplo K-Means");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.YELLOW);
		panel.setForeground(Color.YELLOW);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		
		JPanel jpControles = new JPanel();
		jpControles.setBackground(Color.RED);
		jpControles.setForeground(Color.RED);
		frame.getContentPane().add(jpControles, BorderLayout.SOUTH);
	}

	private static JFreeChart createChart(XYZDataset xyzdataset) {
		JFreeChart jfreechart = ChartFactory.createBubbleChart("Kmeans", "X", "Y", xyzdataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setForegroundAlpha(0.65F);
//		NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
//		numberaxis.setLowerMargin(0.2);
//		numberaxis.setUpperMargin(0.5);
//		NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
//		numberaxis1.setLowerMargin(0.8);
//		numberaxis1.setUpperMargin(0.9);
		
		NumberAxis range = (NumberAxis) xyplot.getRangeAxis();
        range.setRange(Kmeans.MIN, Kmeans.MAX+ Kmeans.MAX*0.1);
        range.setTickUnit(new NumberTickUnit(0.1));
		return jfreechart;
	}

	public static XYZDataset createDataset() {

		Set<Punto> puntos = new HashSet<Punto>();
		for (int i = 0; i < NUM_PUNTOS; i++) {
			puntos.add(new Punto());
		}

		Kmeans kmeans = new Kmeans(puntos, NUM_CENTROIDES);
		kmeans.init();

		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();


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

			puntos = centroide.getPuntos();
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
		
		return defaultxyzdataset;
	}

	private static double[] toArray(List<Float> arr) {
		double[] resultado = new double[arr.size()];
		
		for(int i = 0; i < arr.size(); i++) {
			resultado[i] = arr.get(i);
		}
		return resultado;
	}
	
	
	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);

		chartpanel.setDomainZoomable(true);
		chartpanel.setRangeZoomable(true);

		return chartpanel;
	}

}
