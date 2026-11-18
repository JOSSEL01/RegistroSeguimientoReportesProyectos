package negocio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Auditoria;
import modelo.EstadoProyecto;
import modelo.Proyecto;
import persistencia.AuditoriaDAO;
import persistencia.ProyectoDAO;

public class ProyectoService {
    private Proyecto[] proyectos;
    private int totalProyectos;
    private ProyectoDAO proyectoDAO;
    private AuditoriaDAO auditoriaDAO;

    public ProyectoService() throws ProyectoException {
        this.proyectoDAO = new ProyectoDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        this.proyectos = new Proyecto[ProyectoDAO.MAX_PROYECTOS];
        try {
            this.totalProyectos = proyectoDAO.cargarTodos(proyectos);
        } catch (IOException e) {
            throw new ProyectoException("No se pudieron cargar los proyectos desde el archivo: " + e.getMessage());
        }
    }

    public void registrarProyecto(Proyecto nuevo, String usuarioActual) throws ProyectoException {
        if (totalProyectos >= proyectos.length) {
            throw new ProyectoException("Capacidad máxima de proyectos alcanzada (" + proyectos.length + ").");
        }
        if (nuevo.getCodigo().trim().isEmpty() || nuevo.getNombre().trim().isEmpty()) {
            throw new ProyectoException("El código y el nombre del proyecto son obligatorios.");
        }
        if (nuevo.getCodigo().contains(";") || nuevo.getNombre().contains(";")) {
            throw new ProyectoException("No se permite el carácter ';' en los textos.");
        }
        if (buscarIndicePorCodigo(nuevo.getCodigo()) != -1) {
            throw new ProyectoException("Ya existe un proyecto registrado con el código: " + nuevo.getCodigo());
        }

        proyectos[totalProyectos] = nuevo;
        totalProyectos++;
        guardarCambios();
        registrarAuditoria(usuarioActual, "REGISTRO", "Registró proyecto " + nuevo.getCodigo() + " - " + nuevo.getNombre());
    }

    public void modificarProyecto(Proyecto actualizado, String usuarioActual) throws ProyectoException {
        int idx = buscarIndicePorCodigo(actualizado.getCodigo());
        if (idx == -1 || !proyectos[idx].isActivo()) {
            throw new ProyectoException("No se encontró un proyecto activo con código: " + actualizado.getCodigo());
        }
        if (actualizado.getNombre().trim().isEmpty()) {
            throw new ProyectoException("El nombre del proyecto no puede quedar vacío.");
        }
        if (actualizado.getNombre().contains(";")) {
            throw new ProyectoException("No se permite el carácter ';' en el nombre.");
        }

        proyectos[idx] = actualizado;
        guardarCambios();
        registrarAuditoria(usuarioActual, "MODIFICACIÓN", "Modificó datos del proyecto " + actualizado.getCodigo());
    }

    public void darDeBajaLogica(String codigo, String usuarioActual) throws ProyectoException {
        int idx = buscarIndicePorCodigo(codigo);
        if (idx == -1 || !proyectos[idx].isActivo()) {
            throw new ProyectoException("Proyecto activo no encontrado: " + codigo);
        }
        proyectos[idx].setActivo(false);
        guardarCambios();
        registrarAuditoria(usuarioActual, "BAJA LÓGICA", "Dio de baja lógica al proyecto " + codigo);
    }

    public void actualizarSeguimiento(String codigo, EstadoProyecto nuevoEstado, double montoEjecutado,
                                      double avanceFisico, String usuarioActual) throws ProyectoException {
        int idx = buscarIndicePorCodigo(codigo);
        if (idx == -1 || !proyectos[idx].isActivo()) {
            throw new ProyectoException("Proyecto activo no encontrado: " + codigo);
        }
        Proyecto p = proyectos[idx];

        if (nuevoEstado.ordinal() < p.getEstado().ordinal()) {
            throw new ProyectoException("No se puede retroceder de estado (" + p.getEstado() + " a " + nuevoEstado + ").");
        }
        if (montoEjecutado < 0 || montoEjecutado > p.getPresupuesto().getMontoAsignado()) {
            throw new ProyectoException("El monto ejecutado debe estar entre 0 y Bs. " + p.getPresupuesto().getMontoAsignado());
        }
        if (avanceFisico < 0 || avanceFisico > 100) {
            throw new ProyectoException("El avance físico debe estar entre 0% y 100%.");
        }

        p.setEstado(nuevoEstado);
        p.getPresupuesto().setMontoEjecutado(montoEjecutado);
        p.setAvanceFisico(avanceFisico);

        guardarCambios();
        registrarAuditoria(usuarioActual, "SEGUIMIENTO",
                "Proyecto " + codigo + " -> Estado: " + nuevoEstado + ", Avance: " + avanceFisico + "%");
    }

    public int buscarIndicePorCodigo(String codigo) {
        for (int i = 0; i < totalProyectos; i++) {
            if (proyectos[i] != null && proyectos[i].getCodigo().equalsIgnoreCase(codigo.trim())) {
                return i;
            }
        }
        return -1;
    }

    public Proyecto buscarProyectoActivo(String codigo) {
        int idx = buscarIndicePorCodigo(codigo);
        if (idx != -1 && proyectos[idx].isActivo()) {
            return proyectos[idx];
        }
        return null;
    }

    public Proyecto[] obtenerProyectosActivos() {
        int activos = 0;
        for (int i = 0; i < totalProyectos; i++) {
            if (proyectos[i] != null && proyectos[i].isActivo()) {
                activos++;
            }
        }
        Proyecto[] resultado = new Proyecto[activos];
        int k = 0;
        for (int i = 0; i < totalProyectos; i++) {
            if (proyectos[i] != null && proyectos[i].isActivo()) {
                resultado[k] = proyectos[i];
                k++;
            }
        }
        return resultado;
    }

    private void guardarCambios() throws ProyectoException {
        try {
            proyectoDAO.guardarTodos(proyectos, totalProyectos);
        } catch (IOException e) {
            throw new ProyectoException("Error al guardar en archivo: " + e.getMessage());
        }
    }

    private void registrarAuditoria(String usuario, String accion, String detalle) {
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        auditoriaDAO.registrarEvento(new Auditoria(fecha, usuario, accion, detalle));
    }

    public Auditoria[] obtenerHistorialAuditoria() {
        Auditoria[] temp = new Auditoria[AuditoriaDAO.MAX_AUDITORIA];
        int n = auditoriaDAO.cargarTodos(temp);
        Auditoria[] resultado = new Auditoria[n];
        for (int i = 0; i < n; i++) {
            resultado[i] = temp[i];
        }
        return resultado;
    }
}