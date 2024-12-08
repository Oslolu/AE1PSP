package es.florida.ae1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase SimulacioMT simula el proceso de generación de archivos de simulación
 * para diferentes tipos de proteínas utilizando hilos (multithreading). Cada instancia
 * de esta clase representa una simulación de una proteína específica, donde se generará
 * un archivo con los detalles de la simulación, incluyendo marcas de tiempo, duración y
 * datos simulados.
 */
public class SimulacioMT implements Runnable {
    private int tipoProteina;
    private int numeroProteina;
    
    /**
     * Constructor de la clase code SimulacioMT.
     * 
     * @param tipoProteina El tipo de proteína (un número entre 1 y 4).
     * @param numeroProteina El número de la proteína en la simulación.
     */
    public SimulacioMT(int tipoProteina, int numeroProteina) {
        this.tipoProteina = tipoProteina;
        this.numeroProteina = numeroProteina;
    }

    /**
     * Este método es ejecutado por el hilo y realiza la simulación de una proteína.
     * La simulación incluye la generación de marcas de tiempo para el inicio y la
     * finalización del proceso, así como la creación de un archivo que registra estos
     * detalles y un valor aleatorio simulado como parte del resultado.
     */
    @Override
    public void run() {
        System.out.println("Iniciando simulación de proteína MT...");
        System.out.println("Tipo de proteína: " + tipoProteina);

        long startTime = System.currentTimeMillis();
        String startTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SS").format(new Date(startTime));

        try {
            System.out.println("Simulación en proceso...");
            Thread.sleep((int) (Math.random() * 1000)); 
        } catch (InterruptedException e) {
            System.out.println("Error en la simulación: " + e.getMessage());
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        String endTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SS").format(new Date(endTime));
        long duration = endTime - startTime;

        String filename = String.format("./PROT_MT_%d_n%d_%s.sim", tipoProteina, numeroProteina, startTimestamp);
        System.out.println("Creando archivo de salida: " + filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(startTimestamp + "\n");
            writer.write(endTimestamp + "\n");
            writer.write(String.format("%d_%d\n", duration / 1000, duration % 1000));
            writer.write(String.valueOf(Math.random()));
            System.out.println("Archivo " + filename + " escrito con éxito.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Simulación de proteína MT completada en " + duration + " ms.");
    }
}
