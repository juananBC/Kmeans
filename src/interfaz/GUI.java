package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kmeans.Kmeans;
import javax.swing.JButton;
import javax.swing.JSpinner;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.JLabel;

public class GUI {
	private static final int NUM_PUNTOS = 5000;
	private static final int NUM_CLUSTERS = 6;
	private static final String EJE_X = "Tiempo";
	private static final String EJE_Y = "Coste";

	private JFrame frame;

	private int numClusters;
	private int numPuntos;
	private String ejeX;
	private String ejeY;
	private Kmeans kmeans = new Kmeans(numPuntos, numClusters, ejeX, ejeY, false);
	private Grafico chart = new Grafico(kmeans);

	private JPanel panelGrafico;
	private JLabel lbIteraciones;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			GUI window = new GUI(NUM_PUNTOS, NUM_CLUSTERS, EJE_X, EJE_Y);
			window.frame.setVisible(true);
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
		spPuntos.addChangeListener(e -> {
			JSpinner s = (JSpinner) e.getSource();
			numPuntos = (int) s.getValue();
			if (numPuntos < 0)
				numPuntos = 0;
		});
		spPuntos.setMinimumSize(new Dimension(100, 20));
		spPuntos.setValue(numPuntos);
		jpControles.add(spPuntos);

		JLabel lblClusters = new JLabel("Clusters");
		jpControles.add(lblClusters);

		JSpinner spCluster = new JSpinner();
		spCluster.setSize(new Dimension(100, 40));
		spCluster.addChangeListener(e -> {
			JSpinner s = (JSpinner) e.getSource();
			numClusters = (int) s.getValue();
			if (numClusters < 1)
				numClusters = 1;
		});
		spCluster.setValue(numClusters);
		spCluster.setMinimumSize(new Dimension(40, 20));
		jpControles.add(spCluster);

		JButton jbIterar = new JButton("Iterar");
		jbIterar.setBounds(new Rectangle(0, 0, 80, 20));
		jbIterar.addActionListener(e -> {
			kmeans.iterar();
			chart = new Grafico(kmeans);
			pintarGrafico();
		});
		jpControles.add(jbIterar);

		JButton btnResultado = new JButton("Resultado");
		btnResultado.addActionListener(e -> {
			kmeans.init();
			chart = new Grafico(kmeans);
			pintarGrafico();
		});
		jpControles.add(btnResultado);

		JButton btnReiniciar = new JButton("Iniciar");
		btnReiniciar.addActionListener(e -> {
			kmeans = new Kmeans(numPuntos, numClusters, ejeX, ejeY, false);
			chart = new Grafico(kmeans);
			btnReiniciar.setText("Reiniciar");
			pintarGrafico();
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
