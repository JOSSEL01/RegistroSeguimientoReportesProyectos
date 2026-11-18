package negocio;

import modelo.EstadoProyecto;
import modelo.Proyecto;

public class ReporteService {

    // Devuelve matriz [filas][4 columnas: Estado, Cantidad, Asignado, Ejecutado]
    public Object[][] generarResumenPorEstado(Proyecto[] activos) {
        EstadoProyecto[] estados = EstadoProyecto.values();
        Object[][] tabla = new Object[estados.length][4];

        for (int i = 0; i < estados.length; i++) {
            int conteo = 0;
            double sumaAsignado = 0;
            double sumaEjecutado = 0;

            for (int j = 0; j < activos.length; j++) {
                if (activos[j].getEstado() == estados[i]) {
                    conteo++;
                    sumaAsignado += activos[j].getPresupuesto().getMontoAsignado();
                    sumaEjecutado += activos[j].getPresupuesto().getMontoEjecutado();
                }
            }
            tabla[i][0] = estados[i].getDescripcion();
            tabla[i][1] = conteo;
            tabla[i][2] = String.format("%.2f", sumaAsignado);
            tabla[i][3] = String.format("%.2f", sumaEjecutado);
        }
        return tabla;
    }

    // Devuelve matriz [filas][4 columnas: Unidad, Cantidad, Asignado, Ejecutado]
    public Object[][] generarResumenPorUnidad(Proyecto[] activos) {
        String[] unidadesUnicas = new String[activos.length];
        int totalUnicas = 0;

        for (int i = 0; i < activos.length; i++) {
            String nombreUnidad = activos[i].getUnidadEjecutora().toString();
            boolean existe = false;
            for (int k = 0; k < totalUnicas; k++) {
                if (unidadesUnicas[k].equals(nombreUnidad)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                unidadesUnicas[totalUnicas] = nombreUnidad;
                totalUnicas++;
            }
        }

        Object[][] tabla = new Object[totalUnicas][4];
        for (int i = 0; i < totalUnicas; i++) {
            int conteo = 0;
            double sumaAsignado = 0;
            double sumaEjecutado = 0;

            for (int j = 0; j < activos.length; j++) {
                if (activos[j].getUnidadEjecutora().toString().equals(unidadesUnicas[i])) {
                    conteo++;
                    sumaAsignado += activos[j].getPresupuesto().getMontoAsignado();
                    sumaEjecutado += activos[j].getPresupuesto().getMontoEjecutado();
                }
            }
            tabla[i][0] = unidadesUnicas[i];
            tabla[i][1] = conteo;
            tabla[i][2] = String.format("%.2f", sumaAsignado);
            tabla[i][3] = String.format("%.2f", sumaEjecutado);
        }
        return tabla;
    }

    // Devuelve matriz [filas][4 columnas: Fuente, Cantidad, Asignado, Ejecutado]
    public Object[][] generarResumenPorFuente(Proyecto[] activos) {
        String[] fuentesUnicas = new String[activos.length];
        int totalUnicas = 0;

        for (int i = 0; i < activos.length; i++) {
            String nombreFuente = activos[i].getFuenteFinanciamiento().toString();
            boolean existe = false;
            for (int k = 0; k < totalUnicas; k++) {
                if (fuentesUnicas[k].equals(nombreFuente)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                fuentesUnicas[totalUnicas] = nombreFuente;
                totalUnicas++;
            }
        }

        Object[][] tabla = new Object[totalUnicas][4];
        for (int i = 0; i < totalUnicas; i++) {
            int conteo = 0;
            double sumaAsignado = 0;
            double sumaEjecutado = 0;

            for (int j = 0; j < activos.length; j++) {
                if (activos[j].getFuenteFinanciamiento().toString().equals(fuentesUnicas[i])) {
                    conteo++;
                    sumaAsignado += activos[j].getPresupuesto().getMontoAsignado();
                    sumaEjecutado += activos[j].getPresupuesto().getMontoEjecutado();
                }
            }
            tabla[i][0] = fuentesUnicas[i];
            tabla[i][1] = conteo;
            tabla[i][2] = String.format("%.2f", sumaAsignado);
            tabla[i][3] = String.format("%.2f", sumaEjecutado);
        }
        return tabla;
    }
}