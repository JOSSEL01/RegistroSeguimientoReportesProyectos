package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.EstadoProyecto;
import modelo.FuenteFinanciamiento;
import modelo.Presupuesto;
import modelo.Proyecto;
import modelo.ProyectoInfraestructura;
import modelo.ProyectoSocial;
import modelo.UnidadEjecutora;

public class ProyectoDAO {
    private static final String CARPETA = "datos";
    private static final String RUTA_ARCHIVO = "datos/proyectos.txt";
    public static final int MAX_PROYECTOS = 500;

    public ProyectoDAO() {
        File dir = new File(CARPETA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void guardarTodos(Proyecto[] proyectos, int cantidad) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false));
            for (int i = 0; i < cantidad; i++) {
                if (proyectos[i] != null) {
                    bw.write(proyectos[i].toLineaArchivo());
                    bw.newLine();
                }
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public int cargarTodos(Proyecto[] destino) throws IOException {
        int contador = 0;
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return 0;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null && contador < destino.length) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length == 15) {
                    String tipo = p[0];
                    String codigo = p[1];
                    String nombre = p[2];
                    UnidadEjecutora ue = new UnidadEjecutora(p[3], p[4], p[5]);
                    FuenteFinanciamiento ff = new FuenteFinanciamiento(p[6], p[7]);
                    double asignado = Double.parseDouble(p[8]);
                    double ejecutado = Double.parseDouble(p[9]);
                    int gestion = Integer.parseInt(p[10]);
                    Presupuesto pres = new Presupuesto(asignado, ejecutado, gestion);
                    EstadoProyecto estado = EstadoProyecto.valueOf(p[11]);
                    double avance = Double.parseDouble(p[12]);
                    boolean activo = Boolean.parseBoolean(p[13]);

                    if (tipo.equals("INFRA")) {
                        double km = Double.parseDouble(p[14]);
                        destino[contador] = new ProyectoInfraestructura(
                                codigo, nombre, ue, ff, pres, estado, avance, activo, km);
                        contador++;
                    } else if (tipo.equals("SOCIAL")) {
                        int benef = Integer.parseInt(p[14]);
                        destino[contador] = new ProyectoSocial(
                                codigo, nombre, ue, ff, pres, estado, avance, activo, benef);
                        contador++;
                    }
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return contador;
    }
}