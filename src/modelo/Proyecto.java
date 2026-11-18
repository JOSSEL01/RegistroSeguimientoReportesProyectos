package modelo;

public abstract class Proyecto implements IReportable {
    private String codigo;
    private String nombre;
    private UnidadEjecutora unidadEjecutora;
    private FuenteFinanciamiento fuenteFinanciamiento;
    private Presupuesto presupuesto;
    private EstadoProyecto estado;
    private double avanceFisico;
    private boolean activo; // Para baja lógica (true = activo, false = dado de baja)

    public Proyecto(String codigo, String nombre, UnidadEjecutora unidadEjecutora,
                    FuenteFinanciamiento fuenteFinanciamiento, Presupuesto presupuesto,
                    EstadoProyecto estado, double avanceFisico, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidadEjecutora = unidadEjecutora;
        this.fuenteFinanciamiento = fuenteFinanciamiento;
        this.presupuesto = presupuesto;
        this.estado = estado;
        this.avanceFisico = avanceFisico;
        this.activo = activo;
    }

    // Métodos abstractos que obligan a las subclases a aplicar Polimorfismo
    public abstract String getTipoProyecto();
    public abstract String getDetalleEspecifico();
    public abstract String toLineaArchivo();

    @Override
    public double obtenerMontoComprometido() {
        return presupuesto.getMontoAsignado();
    }

    @Override
    public String generarResumenReporte() {
        return "[" + codigo + "] " + nombre + " | Tipo: " + getTipoProyecto()
                + " | Estado: " + estado.getDescripcion()
                + " | Presupuesto: Bs. " + String.format("%.2f", presupuesto.getMontoAsignado());
    }

    // Getters y Setters (Encapsulamiento)
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public UnidadEjecutora getUnidadEjecutora() { return unidadEjecutora; }
    public void setUnidadEjecutora(UnidadEjecutora unidadEjecutora) { this.unidadEjecutora = unidadEjecutora; }

    public FuenteFinanciamiento getFuenteFinanciamiento() { return fuenteFinanciamiento; }
    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) { this.fuenteFinanciamiento = fuenteFinanciamiento; }

    public Presupuesto getPresupuesto() { return presupuesto; }
    public void setPresupuesto(Presupuesto presupuesto) { this.presupuesto = presupuesto; }

    public EstadoProyecto getEstado() { return estado; }
    public void setEstado(EstadoProyecto estado) { this.estado = estado; }

    public double getAvanceFisico() { return avanceFisico; }
    public void setAvanceFisico(double avanceFisico) {
        if (avanceFisico < 0 || avanceFisico > 100) {
            throw new IllegalArgumentException("El avance físico debe estar entre 0% y 100%.");
        }
        this.avanceFisico = avanceFisico;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}