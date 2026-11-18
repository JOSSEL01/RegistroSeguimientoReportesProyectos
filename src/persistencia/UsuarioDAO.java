package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Usuario;

public class UsuarioDAO {
    private static final String CARPETA = "datos";
    private static final String RUTA_ARCHIVO = "datos/usuarios.txt";
    public static final int MAX_USUARIOS = 100;

    public UsuarioDAO() {
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            Usuario[] iniciales = new Usuario[3];
            iniciales[0] = new Usuario("admin", "1234", "Administrador General", "ADMINISTRADOR");
            iniciales[1] = new Usuario("operador", "1234", "Técnico de Proyectos", "OPERADOR");
            iniciales[2] = new Usuario("consulta", "1234", "Auditor / Consulta", "CONSULTA");
            guardarTodos(iniciales, 3);
        }
    }

    public void guardarTodos(Usuario[] usuarios, int cantidad) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false));
            for (int i = 0; i < cantidad; i++) {
                if (usuarios[i] != null) {
                    bw.write(usuarios[i].toLineaArchivo());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException ex) {
                System.err.println("Error al cerrar archivo de usuarios: " + ex.getMessage());
            }
        }
    }

    public int cargarTodos(Usuario[] destino) {
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
                    destino[contador] = new Usuario(partes[0], partes[1], partes[2], partes[3]);
                    contador++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios: " + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                System.err.println("Error al cerrar lectura de usuarios: " + ex.getMessage());
            }
        }
        return contador;
    }
}