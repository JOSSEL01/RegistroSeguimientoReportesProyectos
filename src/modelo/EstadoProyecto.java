package modelo;

public enum EstadoProyecto {
    FORMULACION("Formulación"),
    EVALUACION("Evaluación"),
    APROBACION("Aprobación"),
    EJECUCION("Ejecución"),
    CIERRE("Cierre");

    private final String descripcion;

    EstadoProyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}