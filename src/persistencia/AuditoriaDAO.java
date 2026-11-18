package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Auditoria;

public class AuditoriaDAO {
    private static final String CARPETA = "datos";
    private static final String RUTA_ARCHIVO = "datos/auditoria.txt";
    public static final int MAX_AUDITORIA = 1000;

    public AuditoriaDAO() {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void registrarEvento(Auditoria evento) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true)); // true = agrega al final sin borrar lo anterior
            bw.write(evento.toLineaArchivo());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir auditoría: " + e.getMessage());
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException ex) {
                System.err.println("Error al cerrar auditoría: " + ex.getMessage());
            }
        }
    }

    public int cargarTodos(Auditoria[] destino) {
        int contador = 0;
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return 0;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null && contador < destino.length) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    destino[contador] = new Auditoria(partes[0], partes[1], partes[2], partes[3]);
                    contador++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer auditoría: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                System.err.println("Error al cerrar lectura de auditoría: " + ex.getMessage());
            }
        }
        return contador;
    }
}