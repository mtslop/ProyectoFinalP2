package co.uniquindio.edu.poo.Model;

import java.time.LocalDateTime;

public class SolicitudRecarga {

    private String id;
    private Cliente cliente;
    private double monto;
    private LocalDateTime fecha;
    private EstadoSolicitud estado;

    /**
     * Crea una solicitud en estado pendiente.
     *
     * @param id      identificador de la solicitud
     * @param cliente cliente que solicita la recarga
     * @param monto   valor que desea recargar
     * @throws IllegalArgumentException si el monto no es mayor que cero
     */
    public SolicitudRecarga(String id, Cliente cliente, double monto) {
        if (monto <= 0) { // Valida que el monto sea mayor que cero; no se permiten recargas de 0 ni negativas.
            throw new IllegalArgumentException("El monto de la recarga debe ser mayor que cero");
        }
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "SolicitudRecarga{" +
                "id='" + id + '\'' +
                ", monto=" + monto +
                ", fecha=" + fecha +
                ", estado=" + estado +
                '}';
    }

    /**
     * Marca la solicitud como aprobada. La recarga de la tarjeta la realiza el servicio
     * del administrador antes de invocar este método.
     *
     * @throws IllegalStateException si la solicitud ya fue atendida
     */
    public void aprobar() {
        validarPendiente();
        estado = EstadoSolicitud.APROBADA;
    }

    /**
     * Marca la solicitud como rechazada sin modificar la tarjeta.
     *
     * @throws IllegalStateException si la solicitud ya fue atendida
     */
    public void rechazar() {
        validarPendiente();
        estado = EstadoSolicitud.RECHAZADA;
    }

    /**
     * Verifica que la solicitud no haya sido atendida.
     *
     * @throws IllegalStateException si el estado no es {@link EstadoSolicitud#PENDIENTE}
     */
    private void validarPendiente() {
        if (estado != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud " + id + " ya fue atendida (" + estado + ")");
        }
    }
}
