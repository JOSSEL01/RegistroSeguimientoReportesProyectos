package modelo;

public class Presupuesto {
    private double montoAsignado;
    private double montoEjecutado;
    private int gestion;

    public Presupuesto(double montoAsignado, double montoEjecutado, int gestion) {
        if (montoAsignado < 0) {
            throw new IllegalArgumentException("El monto asignado no puede ser negativo.");
        }
        if (montoEjecutado < 0 || montoEjecutado > montoAsignado) {
            throw new IllegalArgumentException("El monto ejecutado debe estar entre 0 y el monto asignado.");
        }
        this.montoAsignado = montoAsignado;
        this.montoEjecutado = montoEjecutado;
        this.gestion = gestion;
    }

    public double getMontoAsignado() { return montoAsignado; }
    public void setMontoAsignado(double montoAsignado) {
        if (montoAsignado < 0) {
            throw new IllegalArgumentException("El monto asignado no puede ser negativo.");
        }
        this.montoAsignado = montoAsignado;
    }

    public double getMontoEjecutado() { return montoEjecutado; }
    public void setMontoEjecutado(double montoEjecutado) {
        if (montoEjecutado < 0 || montoEjecutado > this.montoAsignado) {
            throw new IllegalArgumentException("Monto ejecutado inválido.");
        }
        this.montoEjecutado = montoEjecutado;
    }

    public int getGestion() { return gestion; }
    public void setGestion(int gestion) { this.gestion = gestion; }

    public double calcularPorcentajeEjecucion() {
        if (montoAsignado == 0) return 0;
        return (montoEjecutado / montoAsignado) * 100.0;
    }
}