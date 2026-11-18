package modelo;

public class Auditoria {
    private String fechaHora;
    private String usuario;
    private String accion;
    private String detalle;

    public Auditoria(String fechaHora, String usuario, String accion, String detalle) {
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    public String getFechaHora() { return fechaHora; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }
    public String getDetalle() { return detalle; }

    public String toLineaArchivo() {
        return fechaHora + ";" + usuario + ";" + accion + ";" + detalle;
    }
}