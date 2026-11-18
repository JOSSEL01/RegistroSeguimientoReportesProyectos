package modelo;

public class ProyectoSocial extends Proyecto {
    private int beneficiariosDirectos; // Atributo específico de proyectos sociales

    public ProyectoSocial(String codigo, String nombre, UnidadEjecutora unidadEjecutora,
                          FuenteFinanciamiento fuenteFinanciamiento, Presupuesto presupuesto,
                          EstadoProyecto estado, double avanceFisico, boolean activo,
                          int beneficiariosDirectos) {
        super(codigo, nombre, unidadEjecutora, fuenteFinanciamiento, presupuesto, estado, avanceFisico, activo);
        this.beneficiariosDirectos = beneficiariosDirectos;
    }

    public int getBeneficiariosDirectos() { return beneficiariosDirectos; }
    public void setBeneficiariosDirectos(int beneficiariosDirectos) { this.beneficiariosDirectos = beneficiariosDirectos; }

    @Override
    public String getTipoProyecto() {
        return "SOCIAL";
    }

    @Override
    public String getDetalleEspecifico() {
        return "Beneficiarios directos: " + beneficiariosDirectos + " personas";
    }

    @Override
    public String toLineaArchivo() {
        return "SOCIAL;" + getCodigo() + ";" + getNombre() + ";"
                + getUnidadEjecutora().getCodigo() + ";" + getUnidadEjecutora().getNombre() + ";" + getUnidadEjecutora().getResponsable() + ";"
                + getFuenteFinanciamiento().getSigla() + ";" + getFuenteFinanciamiento().getDescripcion() + ";"
                + getPresupuesto().getMontoAsignado() + ";" + getPresupuesto().getMontoEjecutado() + ";" + getPresupuesto().getGestion() + ";"
                + getEstado().name() + ";" + getAvanceFisico() + ";" + isActivo() + ";" + beneficiariosDirectos;
    }
}