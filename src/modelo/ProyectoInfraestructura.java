package modelo;

public class ProyectoInfraestructura extends Proyecto {
    private double longitudKm; // Atributo específico de infraestructura

    public ProyectoInfraestructura(String codigo, String nombre, UnidadEjecutora unidadEjecutora,
                                 FuenteFinanciamiento fuenteFinanciamiento, Presupuesto presupuesto,
                                 EstadoProyecto estado, double avanceFisico, boolean activo,
                                 double longitudKm) {
        super(codigo, nombre, unidadEjecutora, fuenteFinanciamiento, presupuesto, estado, avanceFisico, activo);
        this.longitudKm = longitudKm;
    }

    public double getLongitudKm() { return longitudKm; }
    public void setLongitudKm(double longitudKm) { this.longitudKm = longitudKm; }

    @Override
    public String getTipoProyecto() {
        return "INFRAESTRUCTURA";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Longitud/Magnitud: " + longitudKm + " Km";
    }

    @Override
    public String toLineaArchivo() {
        return "INFRA;" + getCodigo() + ";" + getNombre() + ";"
                + getUnidadEjecutora().getCodigo() + ";" + getUnidadEjecutora().getNombre() + ";" + getUnidadEjecutora().getResponsable() + ";"
                + getFuenteFinanciamiento().getSigla() + ";" + getFuenteFinanciamiento().getDescripcion() + ";"
                + getPresupuesto().getMontoAsignado() + ";" + getPresupuesto().getMontoEjecutado() + ";" + getPresupuesto().getGestion() + ";"
                + getEstado().name() + ";" + getAvanceFisico() + ";" + isActivo() + ";" + longitudKm;
    }
}