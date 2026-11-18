package modelo;

public class FuenteFinanciamiento {
    private String sigla;
    private String descripcion;

    public FuenteFinanciamiento(String sigla, String descripcion) {
        this.sigla = sigla;
        this.descripcion = descripcion;
    }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return sigla + " (" + descripcion + ")";
    }
}