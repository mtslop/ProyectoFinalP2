package co.uniquindio.edu.poo.Model;

import java.util.Objects;
import java.util.UUID;

/**
 * Fachada que centraliza las operaciones principales del proceso de compra:
 * pago, cancelación, reembolso y redención de puntos.
 * De esta forma, el cliente de la aplicación no necesita coordinar directamente
 * todas las clases involucradas en esas operaciones.
 */
public class CompraFacade {

    private final CinemaUQ cinema;
    private final int puntosPorEntrada;

    /**
     * Crea la fachada de compras y configura la regla de acumulación de puntos.
     *
     * @param cinema instancia del sistema donde se registran las compras.
     * @param puntosPorEntrada puntos que se acumulan por cada entrada pagada.
     * @throws NullPointerException si cinema es null.
     * @throws IllegalArgumentException si la cantidad de puntos es negativa.
     */
    public CompraFacade(CinemaUQ cinema, int puntosPorEntrada) {
        this.cinema = Objects.requireNonNull(cinema, "CinemaUQ no puede ser null.");
        if (puntosPorEntrada < 0) {
            throw new IllegalArgumentException("Los puntos por entrada no pueden ser negativos.");
        }
        this.puntosPorEntrada = puntosPorEntrada;
    }

    /**
     * Valida la compra, verifica la tarjeta, descuenta el total, cambia el estado
     * a pagado, acumula los puntos correspondientes y registra la compra en CinemaUQ.
     *
     * @param compra compra que se desea procesar.
     * @return compra después de completar el pago.
     * @throws NullPointerException si la compra es null.
     * @throws IllegalStateException si la tarjeta no está activa o no tiene saldo suficiente.
     */
    public Compra realizarCompra(Compra compra) {
        Objects.requireNonNull(compra, "La compra no puede ser null.");
        compra.build();

        TarjetaVirtual tarjeta = compra.getTarjeta();
        if (!tarjeta.estaActiva()) {
            throw new IllegalStateException("La tarjeta virtual no está activa.");
        }
        if (!tarjeta.tieneSaldo(compra.getTotal())) {
            throw new IllegalStateException("La tarjeta virtual no tiene saldo suficiente.");
        }

        tarjeta.descontar(compra.getTotal());
        try {
            compra.pagar();
        } catch (RuntimeException excepcion) {
            tarjeta.reembolsar(compra.getTotal());
            throw excepcion;
        }

        int puntos = compra.getEntradas().size() * puntosPorEntrada;
        compra.setPuntosGanados(puntos);
        if (puntos > 0) {
            compra.getCliente().getCuentaPuntos().acumularCantidad(puntos);
        }
        cinema.agregarCompra(compra);
        return compra;
    }

    /**
     * Procesa una cancelación solicitada por el cliente.
     * Si la compra está pendiente, simplemente la cancela. Si ya fue pagada,
     * aplica la política del proyecto: 100 % cuando faltan más de 24 horas,
     * 80 % cuando faltan entre 2 y 24 horas, y no permite cancelar con menos de 2 horas.
     *
     * @param compra compra que se desea cancelar.
     * @return monto devuelto a la tarjeta; cero cuando la compra estaba pendiente.
     * @throws NullPointerException si la compra es null.
     * @throws IllegalStateException si el estado o el tiempo restante no permiten cancelar.
     */
    public double cancelarCompra(Compra compra) {
        Objects.requireNonNull(compra, "La compra no puede ser null.");
        if ("PENDIENTE".equals(compra.getEstado().nombre())) {
            compra.cancelar();
            return 0;
        }
        double monto = calcularReembolso(compra, false);
        ejecutarReembolso(compra, monto);
        return monto;
    }

    /**
     * Devuelve el 100 % del valor pagado cuando el cine cancela la función.
     * Una compra pendiente se cancela sin generar un movimiento de reembolso.
     *
     * @param compra compra asociada a la función cancelada por el cine.
     * @return monto devuelto a la tarjeta; cero si la compra estaba pendiente.
     * @throws NullPointerException si la compra es null.
     * @throws IllegalStateException si la compra no se puede reembolsar.
     */
    public double reembolsarPorCine(Compra compra) {
        Objects.requireNonNull(compra, "La compra no puede ser null.");
        if (compra.getFuncion() == null || !compra.getFuncion().estaCancelada()) {
            throw new IllegalStateException("El reembolso total requiere que el cine haya cancelado la función.");
        }
        if ("PENDIENTE".equals(compra.getEstado().nombre())) {
            compra.cancelar();
            return 0;
        }
        double monto = calcularReembolso(compra, true);
        ejecutarReembolso(compra, monto);
        return monto;
    }

    /**
     * Redime los puntos necesarios para emitir una entrada sin costo monetario.
     * El asiento queda ocupado en la función para evitar que se venda nuevamente.
     *
     * @param cliente cliente que utiliza sus puntos.
     * @param funcion función para la que se solicita la entrada.
     * @param asiento asiento que se desea asignar.
     * @return entrada emitida con precio cero.
     * @throws NullPointerException si alguno de los parámetros es null.
     * @throws IllegalStateException si la función está cancelada o no hay puntos suficientes.
     */
    public Entrada redimirEntrada(Cliente cliente, Funcion funcion, Asiento asiento) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null.");
        Objects.requireNonNull(funcion, "La función no puede ser null.");
        Objects.requireNonNull(asiento, "El asiento no puede ser null.");
        if (funcion.estaCancelada()) {
            throw new IllegalStateException("No se puede redimir una entrada para una función cancelada.");
        }
        verificarPuntos(cliente, puntosPorEntrada);
        funcion.ocuparAsiento(asiento);
        try {
            cliente.getCuentaPuntos().redimir(puntosPorEntrada);
        } catch (RuntimeException excepcion) {
            funcion.liberarAsiento(asiento);
            throw excepcion;
        }
        return new Entrada(UUID.randomUUID().toString(), 0, funcion, asiento);
    }

    /**
     * Redime los puntos requeridos por un producto de confitería y la cantidad solicitada.
     * El detalle se devuelve con precio unitario cero porque el producto se pagó con puntos.
     *
     * @param cliente cliente que utiliza sus puntos.
     * @param item producto que se desea redimir.
     * @param cantidad unidades que se desean redimir.
     * @return detalle de confitería emitido con precio unitario cero.
     * @throws NullPointerException si el cliente o el producto son null.
     * @throws IllegalArgumentException si la cantidad no es positiva.
     * @throws IllegalStateException si no hay puntos suficientes.
     */
    public DetalleConfiteria redimirItemConfiteria(Cliente cliente, ItemConfiteria item, int cantidad) {
        Objects.requireNonNull(cliente, "El cliente no puede ser null.");
        Objects.requireNonNull(item, "El producto no puede ser null.");
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        int puntosNecesarios = Math.multiplyExact(item.puntosRedencion(), cantidad);
        verificarPuntos(cliente, puntosNecesarios);
        cliente.getCuentaPuntos().redimir(puntosNecesarios);
        return new DetalleConfiteria(cantidad, 0, item);
    }

    /**
     * Calcula el valor que corresponde devolver según la política de cancelación.
     *
     * @param compra compra que se está cancelando.
     * @param cancelacionPorCine true cuando la cancelación la realiza el cine.
     * @return valor del reembolso.
     * @throws IllegalStateException si la compra no está pagada, ya fue reembolsada
     *         o el tiempo restante no permite cancelar.
     */
    private double calcularReembolso(Compra compra, boolean cancelacionPorCine) {
        if (!"PAGADA".equals(compra.getEstado().nombre())) {
            throw new IllegalStateException("Solo se puede reembolsar una compra pagada.");
        }
        if (compra.getMontoReembolsado() > 0) {
            throw new IllegalStateException("Esta compra ya tiene un reembolso registrado.");
        }
        if (cancelacionPorCine) {
            return compra.getTotal();
        }

        if (compra.getFuncion() == null) {
            throw new IllegalStateException("No se puede calcular la cancelación sin una función asociada.");
        }
        double horasRestantes = compra.getFuncion().horasRestantes();
        if (horasRestantes > 24) {
            return compra.getTotal();
        }
        if (horasRestantes >= 2) {
            return compra.getTotal() * 0.80;
        }
        throw new IllegalStateException("No se permite cancelar cuando faltan menos de 2 horas para la función.");
    }

    /**
     * Devuelve el dinero a la tarjeta y registra el estado reembolsado en la compra.
     *
     * @param compra compra que será reembolsada.
     * @param monto cantidad que se devolverá.
     */
    private void ejecutarReembolso(Compra compra, double monto) {
        compra.cancelar();
        compra.getTarjeta().reembolsar(monto);
        compra.registrarReembolso(monto);
    }

    /**
     * Comprueba que el cliente tenga los puntos necesarios para una redención.
     *
     * @param cliente cliente cuyos puntos serán consultados.
     * @param puntosNecesarios cantidad requerida.
     * @throws IllegalArgumentException si la cantidad solicitada es negativa.
     * @throws IllegalStateException si el saldo de puntos es insuficiente.
     */
    private void verificarPuntos(Cliente cliente, int puntosNecesarios) {
        if (puntosNecesarios < 0) {
            throw new IllegalArgumentException("La cantidad de puntos no puede ser negativa.");
        }
        if (cliente.getCuentaPuntos().puntosDisponibles() < puntosNecesarios) {
            throw new IllegalStateException("El cliente no tiene puntos suficientes.");
        }
    }
}
