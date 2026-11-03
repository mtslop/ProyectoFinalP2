package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;

public class RegistroPuntos {

    private int cantidad;
    private int cantidadDisponible;
    private LocalDate fechaObtencion;
    private LocalDate fechaVencimiento;

    /**
     * Crea un lote de puntos con vencimiento a un año.
     *
     * @param cantidad       puntos obtenidos
     * @param fechaObtencion fecha en que se obtuvieron los puntos
     * @throws IllegalArgumentException si la cantidad no es mayor que cero
     */
    public RegistroPuntos(int cantidad, LocalDate fechaObtencion) {
        if (cantidad <= 0) { // Valida que el lote tenga puntos; no se permiten lotes de 0 o con cantidad negativa
            throw new IllegalArgumentException("La cantidad de puntos debe ser mayor que cero");
        }
        this.cantidad = cantidad;
        this.cantidadDisponible = cantidad;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaObtencion.plusYears(1);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDate fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "RegistroPuntos{" +
                "cantidad=" + cantidad +
                ", cantidadDisponible=" + cantidadDisponible +
                ", fechaObtencion=" + fechaObtencion +
                ", fechaVencimiento=" + fechaVencimiento +
                '}';
    }

    /**
     * Indica si el lote ya venció en una fecha dada.
     *
     * @param hoy fecha de referencia
     * @return {@code true} si la fecha de referencia es posterior a la fecha de vencimiento
     */
    public boolean estaVencido(LocalDate hoy) {
        return hoy.isAfter(fechaVencimiento); // Devuelve true si la fecha de hoy ya pasó la fecha de vencimiento (los puntos duran un año).
    }

    /**
     * Descuenta de este lote la mayor cantidad posible de puntos.
     *
     * @param cantidad puntos que se desean descontar
     * @return puntos que no pudieron descontarse de este lote
     */
    public int descontar(int cantidad) {
        int usados = Math.min(cantidad, cantidadDisponible);
        cantidadDisponible -= usados;
        return cantidad - usados;
    }

    /**
     * Calcula los puntos del lote que ya se redimieron o se perdieron por vencimiento.
     *
     * @return puntos usados del lote
     */
    public int calcularCantidadUsada() {
        return cantidad - cantidadDisponible;
    }
}
