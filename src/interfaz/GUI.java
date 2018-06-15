package interfaz;

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
import org.jfree.ui.NumberCellRenderer;
import org.jfree.ui.RefineryUtilities;

import kmeans.Centroide;
import kmeans.Kmeans;
import kmeans.Punto;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Component;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI {
	private static final int NUM_PUNTOS = 5000;
	private static final int NUM_CLUSTERS = 6;
	private static final String EJE_X = "Tiempo";
	private static final String EJE_Y = "Coste";

	private JFrame frame;
	private static Grafico chart;

	private int numClusters, numPuntos;
	private String ejeX, ejeY;
	private Kmeans kmeans;
	private JPanel panelGrafico;
	private JLabel lbIteraciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					GUI window = new GUI(NUM_PUNTOS, NUM_CLUSTERS, EJE_X, EJE_Y);
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI(int numPuntos, int numClusters, String ejeX, String ejeY) {

		this.numPuntos = numPuntos;
		this.numClusters = numClusters;
		this.ejeX = ejeX;
		this.ejeY = ejeY;

		panelGrafico = null;
		kmeans = new Kmeans(numPuntos, numClusters, ejeX, ejeY);
		chart = new Grafico(kmeans);

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);

		frame.setTitle("Ejemplo K-Means");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setForeground(Color.YELLOW);
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		lbIteraciones = new JLabel("Iteraciones: 0");
		lbIteraciones.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(lbIteraciones);

		JPanel jpControles = new JPanel();
		jpControles.setAlignmentX(Component.RIGHT_ALIGNMENT);
		jpControles.setLayout(null);
		jpControles.setMinimumSize(new Dimension(100, 100));
		jpControles.setBounds(new Rectangle(0, 0, 50, 100));
		jpControles.setBackground(Color.WHITE);
		jpControles.setForeground(Color.RED);
		frame.getContentPane().add(jpControles, BorderLayout.SOUTH);
		jpControles.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblPuntos = new JLabel("Puntos");
		jpControles.add(lblPuntos);

		JSpinner spPuntos = new JSpinner();
		spPuntos.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				numPuntos = (int) s.getValue();
				if(numPuntos < 0) numPuntos = 0;
			}
		});
		spPuntos.setMinimumSize(new Dimension(100, 20));
		spPuntos.setValue(numPuntos);
		jpControles.add(spPuntos);

		JLabel lblClusters = new JLabel("Clusters");
		jpControles.add(lblClusters);

		JSpinner spCluster = new JSpinner();
		spCluster.setSize(new Dimension(100, 40));
		spCluster.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner s = (JSpinner) e.getSource();
				numClusters = (int) s.getValue();
				if(numClusters < 1) numClusters = 1;
			}
		});
		spCluster.setValue(numClusters);
		spCluster.setMinimumSize(new Dimension(40, 20));
		jpControles.add(spCluster);

		JButton jbIterar = new JButton("Iterar");
		jbIterar.setBounds(new Rectangle(0, 0, 80, 20));
		jbIterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kmeans.iterar();
				chart = new Grafico(kmeans);
				pintarGrafico();

			}
		});
		jpControles.add(jbIterar);

		JButton btnResultado = new JButton("Resultado");
		btnResultado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kmeans.init();
				chart = new Grafico(kmeans);
				pintarGrafico();
			}
		});
		jpControles.add(btnResultado);

		JButton btnReiniciar = new JButton("Reiniciar");
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				kmeans = new Kmeans(numPuntos, numClusters, ejeX, ejeY);
				chart = new Grafico(kmeans);
				pintarGrafico();
			}
		});
		jpControles.add(btnReiniciar);

		pintarGrafico();

	}

	private void pintarGrafico() {

		if (panelGrafico != null) {
			frame.remove(panelGrafico);
			panelGrafico.removeAll();
			panelGrafico.revalidate();
		}

		panelGrafico = chart.getPanel(); 

		panelGrafico.revalidate();
		frame.getContentPane().add(panelGrafico, BorderLayout.CENTER);

		lbIteraciones.setText("Iteraciones: " + kmeans.getIteraciones());
		frame.repaint();
		frame.revalidate();

	}

}
