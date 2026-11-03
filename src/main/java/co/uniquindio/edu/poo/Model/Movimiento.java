package co.uniquindio.edu.poo.Model;

import java.time.LocalDateTime;

public class Movimiento {


    private String id;
    private double valor;
    private LocalDateTime fecha;
    private String descripcion;
    private TipoMovimiento tipo;


    /**
     * Crea un movimiento con la fecha y hora actuales.
     *
     * @param id          identificador del movimiento
     * @param valor       monto de la operación
     * @param tipo        tipo de operación
     * @param descripcion detalle legible de la operación
     * @param fecha       hora y dia del evento
     */
    public Movimiento(String id, double valor, LocalDateTime fecha, String descripcion, TipoMovimiento tipo) {
        this.id = id;
        this.valor = valor;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "valor=" + valor +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}

