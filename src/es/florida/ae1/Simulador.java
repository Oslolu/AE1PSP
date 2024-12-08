package es.florida.ae1;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase Simulador representa una interfaz gráfica que permite al usuario
 * ejecutar simulaciones de proteínas utilizando dos métodos diferentes: multiproceso y multifil.
 * La interfaz incluye campos de texto para ingresar la cantidad de proteínas de cada tipo
 * y un botón para iniciar la simulación. Al hacer clic en el botón, se ejecutaran las simulaciones.
 */
public class Simulador extends JFrame{

	private static final long serialVersionUID = 1L;
	
	/**
     * El punto de entrada principal del programa. Inicializa la ventana de la interfaz gráfica
     * y permite que el usuario inicie la simulación de proteínas.
     *
     * @param args Los argumentos de línea de comandos (no se utilizan en este caso).
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Simulador frame = new Simulador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 /**
     * Constructor de la clase Simulador. Configura la ventana principal, los componentes
     * de la interfaz gráfica y los eventos que se producen al interactuar con ellos.
     */
	public Simulador() {
        JFrame frame = new JFrame("Simulador de Proteínas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setForeground(Color.CYAN);
        panel.setLayout(new GridLayout(6, 2));

        JLabel label1 = new JLabel("Proteínas tipo 1:");
        JTextField input1 = new JTextField("");
        JLabel label2 = new JLabel("Proteínas tipo 2:");
        JTextField input2 = new JTextField("");
        JLabel label3 = new JLabel("Proteínas tipo 3:");
        JTextField input3 = new JTextField("");
        JLabel label4 = new JLabel("Proteínas tipo 4:");
        JTextField input4 = new JTextField("");

        JButton simulateButton = new JButton("Iniciar Simulación");
        simulateButton.setBackground(Color.CYAN);
        simulateButton.setForeground(Color.BLACK);
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(label1);
        panel.add(input1);
        panel.add(label2);
        panel.add(input2);
        panel.add(label3);
        panel.add(input3);
        panel.add(label4);
        panel.add(input4);
        panel.add(simulateButton);

        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(resultArea), BorderLayout.CENTER);

        simulateButton.addActionListener(e -> {
            int[] proteinCounts = new int[4];
            proteinCounts[0] = Integer.parseInt(input1.getText());
            proteinCounts[1] = Integer.parseInt(input2.getText());
            proteinCounts[2] = Integer.parseInt(input3.getText());
            proteinCounts[3] = Integer.parseInt(input4.getText());

            // MULTIPROCESO
            resultArea.append("Iniciando simulación multiproceso...\n");
            long multiProcessStart = System.currentTimeMillis();
            try {
                executeMultiProcessSimulation(proteinCounts, resultArea);
            } catch (Exception ex) {
                resultArea.append("Error en la simulación multiproceso: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }
            long multiProcessEnd = System.currentTimeMillis();
            double multiProcessDuration = (multiProcessEnd - multiProcessStart) / 1000.0;
            resultArea.append("Simulación multiproceso completada en " + multiProcessDuration + " segundos.\n");

            // MULTIFIL
            resultArea.append("Iniciando simulación multifil...\n");
            long multiThreadStart = System.currentTimeMillis();
            executeMultiThreadSimulation(proteinCounts, resultArea);
            long multiThreadEnd = System.currentTimeMillis();
            double multiThreadDuration = (multiThreadEnd - multiThreadStart) / 1000.0;
            resultArea.append("Simulación multifil completada en " + multiThreadDuration + " segundos.\n");
            
            resultArea.append("Revise que los archivos se encuentren creados correctamente.");
        });

        frame.setVisible(true);
    }
	
	/**
     * Ejecuta la simulación de proteínas utilizando multiproceso.
     * Cada simulación se ejecuta en un proceso separado, lanzando instancias de la clase
     * SimulacioMP para cada tipo de proteína.
     * 
     * @param proteinCounts Un arreglo que contiene la cantidad de proteínas para cada tipo (de 1 a 4).
     * @param resultArea El área de texto donde se muestran los resultados de la simulación.
     * @throws Exception Si ocurre un error al ejecutar alguno de los procesos.
     */	
    private static void executeMultiProcessSimulation(int[] proteinCounts, JTextArea resultArea) throws Exception {
        List<Process> processes = new ArrayList<>();
        int proteinNumber = 1;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < proteinCounts[i]; j++) {
            	String clase = "es.florida.ae1.SimulacioMP";
    			String javaHome = System.getProperty("java.home");
    			String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
    			String classpath = System.getProperty("java.class.path");
    			String className = clase;
    			
    			List<String> command = new ArrayList<>();
    			command.add(javaBin);
    			command.add("-cp");
    			command.add(classpath);
    			command.add(className);
    			command.add(String.valueOf(i + 1));
    			command.add(String.valueOf(proteinCounts[i]));
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.start();
                processes.add(process);
            }
        }

        for (Process process : processes) {
            process.waitFor();
        }
    }
    
    /**
     * Ejecuta la simulación de proteínas utilizando multifil.
     * Cada simulación se ejecuta en un hilo separado, utilizando la clase SimulacioMT.
     * 
     * @param proteinCounts Array que contiene la cantidad de proteínas para cada tipo (de 1 a 4).
     * @param resultArea Area de texto donde se muestran los resultados de la simulación.
     */
    private static void executeMultiThreadSimulation(int[] proteinCounts, JTextArea resultArea) {
        List<Thread> threads = new ArrayList<>();
        int proteinNumber = 1;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < proteinCounts[i]; j++) {
                Thread thread = new Thread(new SimulacioMT(i + 1, proteinNumber++));
                threads.add(thread);
                thread.start();
            }
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                resultArea.append("Error en la simulación multifil: " + ex.getMessage() + "\n");
                ex.printStackTrace();
            }
        }
    }
}