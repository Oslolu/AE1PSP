package es.florida.ae1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * La clase SimulacioMP simula el proceso de generación de archivos de simulación
 * para diferentes tipos de proteínas.
 */
public class SimulacioMP {
	
	/**
	 * Clase principal donde se crea un archivo de salida con el nombre que incluye 
	 * el tipo de proteína, el número de simulaciones y las marcas de tiempo
	 * de inicio y finalización de la simulación.
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: Se deben especificar el tipo de proteina y el número de simulaciones.");
            return;
        }

        int tipoProteina;
        int numProteinas;
        try {
            tipoProteina = Integer.parseInt(args[0]);
            numProteinas = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Ambos argumentos deben ser numeros enteros.");
            return;
        }

        if (tipoProteina < 1 || tipoProteina > 4) {
            System.out.println("Error: El tipo de proteina debe estar entre 1 y 4.");
            return;
        }

        if (numProteinas <= 0) {
            System.out.println("Error: El numero de proteinas debe ser mayor que 0.");
            return;
        }

        System.out.println("Iniciando simulacion de proteinas MP...");
        System.out.println("Tipo de proteina: " + tipoProteina + ", Numero de simulaciones: " + numProteinas);

        long startTime = System.currentTimeMillis();
        String startTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SS").format(new Date(startTime));

        long endTime = System.currentTimeMillis();
        String endTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SS").format(new Date(endTime));
        long duration = endTime - startTime;

        String filename = String.format("./PROT_MP_%d_n%d_%s.sim", tipoProteina, numProteinas, startTimestamp);
        System.out.println("Creando archivo de salida: " + filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(startTimestamp + "\n");
            writer.write(endTimestamp + "\n");
            writer.write(String.format("%d_%d\n", duration / 1000, duration % 1000));
            writer.write(String.valueOf(Math.random()));
            System.out.println("Archivo " + filename + " escrito con exito.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Simulacion de proteínas MP completada.");
    }
}
