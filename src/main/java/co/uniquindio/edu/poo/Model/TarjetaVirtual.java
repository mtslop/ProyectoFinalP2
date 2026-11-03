package co.uniquindio.edu.poo.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarjetaVirtual {

    private String numero;
    private double saldo;
    private boolean activa;
    private List<Movimiento> movimientos;

    /**
     * Crea una tarjeta virtual activa, con saldo cero y sin movimientos.
     *
     * @param numero número que identifica la tarjeta
     */
    public TarjetaVirtual(String numero) {
        this.numero = numero;
        this.saldo = 0;
        this.activa = true;
        this.movimientos = new ArrayList<>();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @Override
    public String toString() {
        return "TarjetaVirtual{" +
                "numero='" + numero + '\'' +
                ", saldo=" + saldo +
                ", activa=" + activa +
                ", movimientos=" + movimientos.size() +
                '}';
    }

    /**
     * Indica si el saldo alcanza para cubrir un monto.
     *
     * @param monto monto a comparar
     * @return {@code true} si el saldo es mayor o igual al monto
     */
    public boolean tieneSaldo(double monto) {
        return saldo >= monto;
    }

    /**
     * Descuenta el valor de un pago y registra un movimiento de tipo {@link TipoMovimiento#PAGO}.
     *
     * @param monto valor a pagar
     * @throws IllegalArgumentException si el monto no es mayor que cero
     * @throws IllegalStateException    si la tarjeta está inactiva o el saldo no alcanza para el pago
     */
    public void descontar(double monto) {
        validarMonto(monto);
        if (!activa) {
            throw new IllegalStateException("La tarjeta " + numero + " está inactiva");
        }
        if (!tieneSaldo(monto)) {
            throw new IllegalStateException(String.format(
                    "Saldo insuficiente: tiene $%,.0f y el pago es de $%,.0f", saldo, monto));
        }
        saldo -= monto;
        crearMovimiento(monto, TipoMovimiento.PAGO, "Pago de compra");
    }

    /**
     * Suma saldo a la tarjeta y registra un movimiento de tipo {@link TipoMovimiento#RECARGA}.
     * Solo debe invocarse desde las operaciones del administrador.
     *
     * @param monto valor a recargar
     * @throws IllegalArgumentException si el monto no es mayor que cero
     */
    public void recargar(double monto) {
        validarMonto(monto);
        saldo += monto;
        crearMovimiento(monto, TipoMovimiento.RECARGA, "Recarga aprobada por el administrador");
    }

    /**
     * Devuelve dinero a la tarjeta por la cancelación de una compra y registra un movimiento
     * de tipo {@link TipoMovimiento#REEMBOLSO}. El reembolso se aplica aunque la tarjeta esté inactiva.
     *
     * @param monto valor a reembolsar
     * @throws IllegalArgumentException si el monto no es mayor que cero
     */
    public void reembolsar(double monto) {
        validarMonto(monto);
        saldo += monto;
        crearMovimiento(monto, TipoMovimiento.REEMBOLSO, "Reembolso de compra cancelada");
    }

    /**
     * Agrega un movimiento al historial de la tarjeta.
     *
     * @param m movimiento a registrar
     */
    public void registrarMovimiento(Movimiento m) {
        movimientos.add(m);
    }

    /**
     * Crea un movimiento con la fecha y hora actuales y lo agrega al historial.
     * El id se arma con el número de la tarjeta y la posición del movimiento (por ejemplo, T-1001-3).
     *
     * @param monto       valor de la operación
     * @param tipo        tipo de operación
     * @param descripcion detalle legible de la operación
     */
    private void crearMovimiento(double monto, TipoMovimiento tipo, String descripcion) {
        String id = numero + "-" + (movimientos.size() + 1);
        registrarMovimiento(new Movimiento(id, monto, LocalDateTime.now(), descripcion, tipo));
    }

    /**
     * Verifica que un monto sea positivo.
     *
     * @param monto monto a validar
     * @throws IllegalArgumentException si el monto no es mayor que cero
     */
    private void validarMonto(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }
}


