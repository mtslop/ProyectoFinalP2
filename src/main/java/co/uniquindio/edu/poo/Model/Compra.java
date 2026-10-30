package co.uniquindio.edu.poo.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa una compra realizada por un cliente en CinemaUQ.
 * Contiene las entradas, los productos de confitería, el cálculo del precio,
 * la tarjeta utilizada y el estado del proceso de compra.
 *
 * <p>Los métodos encadenables {@code paraCliente}, {@code paraFuncion},
 * {@code agregarAsiento}, {@code agregarItem} y {@code aplicarPromocion}
 * permiten configurar la compra antes de ejecutar {@code build()}.</p>
 */
public class Compra {

    private final String id;
    private Cliente cliente;
    private Funcion funcion;
    private final LocalDateTime fecha;
    private final List<Entrada> entradas;
    private final List<DetalleConfiteria> detallesConfiteria;
    private Precio precio;
    private TarjetaVirtual tarjeta;
    private double subtotal;
    private double total;
    private EstadoCompra estado;
    private int puntosGanados;
    private int puntosRedimidos;
    private double montoReembolsado;

    /**
     * Crea una compra nueva en estado pendiente.
     *
     * @param id identificador de la compra.
     * @throws NullPointerException si el identificador es null.
     * @throws IllegalArgumentException si el identificador está vacío.
     */
    public Compra(String id) {
        this.id = Objects.requireNonNull(id, "El identificador no puede ser null.");
        if (id.isBlank()) {
            throw new IllegalArgumentException("El identificador de la compra no puede estar vacío.");
        }
        this.fecha = LocalDateTime.now();
        this.entradas = new ArrayList<>();
        this.detallesConfiteria = new ArrayList<>();
        this.precio = new PrecioBase(0);
        this.estado = new EstadoPendiente();
    }

    /**
     * Crea una compra con sus datos principales.
     *
     * @param id identificador de la compra.
     * @param cliente cliente que realiza la compra.
     * @param funcion función cinematográfica asociada.
     * @param tarjeta tarjeta virtual que se utilizará para pagar.
     */
    public Compra(String id, Cliente cliente, Funcion funcion, TarjetaVirtual tarjeta) {
        this(id);
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser null.");
        this.funcion = Objects.requireNonNull(funcion, "La función no puede ser null.");
        this.tarjeta = Objects.requireNonNull(tarjeta, "La tarjeta no puede ser null.");
    }

    /**
     * Calcula el subtotal sumando las entradas y los detalles de confitería,
     * actualiza el precio base conservando las promociones decoradoras y
     * calcula el total final.
     *
     * @return total de la compra después de aplicar las promociones.
     */
    public double calcularTotal() {
        subtotal = 0;
        for (Entrada entrada : entradas) {
            subtotal += entrada.getPrecio();
        }
        for (DetalleConfiteria detalle : detallesConfiteria) {
            subtotal += detalle.subtotal();
        }

        actualizarPrecioBase(subtotal);
        total = precio.calcular();
        if (!Double.isFinite(total) || total < 0) {
            throw new IllegalStateException("El cálculo produjo un total no válido.");
        }
        return total;
    }

    /**
     * Actualiza el precio base que se encuentra al fondo de la cadena de decoradores.
     *
     * @param nuevoSubtotal subtotal actualizado de la compra.
     */
    private void actualizarPrecioBase(double nuevoSubtotal) {
        Precio componente = precio;
        while (componente instanceof PromocionDecorador) {
            componente = ((PromocionDecorador) componente).getPrecio();
        }

        if (componente instanceof PrecioBase) {
            ((PrecioBase) componente).setValor(nuevoSubtotal);
        } else {
            precio = new PrecioBase(nuevoSubtotal);
        }
    }

    /**
     * Asocia un cliente a la compra.
     *
     * @param cliente cliente que realiza la compra.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws NullPointerException si el cliente es null.
     */
    public Compra paraCliente(Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente no puede ser null.");
        return this;
    }

    /**
     * Agrega una entrada para el asiento indicado y la función configurada.
     *
     * @param asiento asiento que se desea comprar.
     * @param precioEntrada precio de la entrada.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws IllegalStateException si todavía no se ha asociado una función.
     * @throws NullPointerException si el asiento es null.
     */
    public Compra agregarAsiento(Asiento asiento, double precioEntrada) {
        if (funcion == null) {
            throw new IllegalStateException("Debe asociar una función antes de agregar asientos.");
        }
        Entrada entrada = new Entrada(UUID.randomUUID().toString(), precioEntrada, funcion, asiento);
        entradas.add(entrada);
        return this;
    }

    /**
     * Agrega una entrada que ya fue construida.
     *
     * @param entrada entrada que se agregará a la compra.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws NullPointerException si la entrada es null.
     * @throws IllegalArgumentException si la entrada pertenece a otra función.
     */
    public Compra agregarAsiento(Entrada entrada) {
        Objects.requireNonNull(entrada, "La entrada no puede ser null.");
        if (funcion != null && !funcion.equals(entrada.getFuncion())) {
            throw new IllegalArgumentException("La entrada pertenece a una función diferente.");
        }
        if (funcion == null) {
            funcion = entrada.getFuncion();
        }
        entradas.add(entrada);
        return this;
    }

    /**
     * Agrega un producto de confitería con la cantidad solicitada.
     * El precio unitario queda guardado en el detalle en el momento de agregarlo.
     *
     * @param item producto que se desea comprar.
     * @param cantidad número de unidades.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws NullPointerException si el producto es null.
     * @throws IllegalArgumentException si la cantidad no es mayor que cero.
     */
    public Compra agregarItem(ItemConfiteria item, int cantidad) {
        detallesConfiteria.add(new DetalleConfiteria(cantidad, item));
        return this;
    }

    /**
     * Configura el objeto Precio que contiene el precio base y las promociones.
     * Normalmente se recibe un decorador que envuelve otro objeto Precio.
     *
     * @param precio precio o cadena de promociones que se aplicará a la compra.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws NullPointerException si el precio es null.
     */
    public Compra aplicarPromocion(Precio precio) {
        this.precio = Objects.requireNonNull(precio, "El precio no puede ser null.");
        return this;
    }

    /**
     * Valida que la compra tenga sus datos principales y al menos un producto,
     * calcula el total y retorna la compra construida.
     *
     * @return esta compra lista para ser procesada por CompraFacade.
     * @throws IllegalStateException si faltan datos obligatorios o no hay conceptos.
     */
    public Compra build() {
        if (cliente == null) {
            throw new IllegalStateException("La compra debe tener un cliente.");
        }
        if (!entradas.isEmpty() && funcion == null) {
            throw new IllegalStateException("La compra debe tener una función cuando contiene entradas.");
        }
        if (!entradas.isEmpty() && funcion.estaCancelada()) {
            throw new IllegalStateException("No se pueden comprar entradas para una función cancelada.");
        }
        if (tarjeta == null) {
            throw new IllegalStateException("La compra debe tener una tarjeta virtual.");
        }
        if (entradas.isEmpty() && detallesConfiteria.isEmpty()) {
            throw new IllegalStateException("La compra debe contener al menos una entrada o un producto.");
        }
        calcularTotal();
        return this;
    }

    /**
     * Cambia el estado de la compra a pagada y ocupa los asientos de sus entradas.
     * La validación y el descuento del saldo de la tarjeta son responsabilidad de CompraFacade.
     *
     * @throws IllegalStateException si el estado actual no permite pagar la compra.
     */
    public void pagar() {
        EstadoCompra nuevoEstado = estado.pagar();
        List<Entrada> asientosOcupados = new ArrayList<>();
        try {
            for (Entrada entrada : entradas) {
                entrada.getFuncion().ocuparAsiento(entrada.getAsiento());
                asientosOcupados.add(entrada);
            }
            estado = nuevoEstado;
        } catch (RuntimeException excepcion) {
            for (Entrada entrada : asientosOcupados) {
                entrada.getFuncion().liberarAsiento(entrada.getAsiento());
            }
            throw excepcion;
        }
    }

    /**
     * Cambia la compra al estado cancelado y libera los asientos asociados.
     * El cálculo y el pago del reembolso se realizan desde CompraFacade.
     *
     * @throws IllegalStateException si el estado actual no permite cancelar la compra.
     */
    public void cancelar() {
        boolean estabaPagada = "PAGADA".equals(estado.nombre());
        EstadoCompra nuevoEstado = estado.cancelar();
        if (estabaPagada) {
            liberarAsientos();
        }
        estado = nuevoEstado;
    }

    /**
     * Libera en la función todos los asientos que estaban asociados a las entradas.
     */
    public void liberarAsientos() {
        for (Entrada entrada : entradas) {
            entrada.getFuncion().liberarAsiento(entrada.getAsiento());
        }
    }

    /**
     * Asocia la compra a una función.
     *
     * @param funcion función seleccionada por el cliente.
     * @return la misma compra para permitir el encadenamiento de operaciones.
     * @throws NullPointerException si la función es null.
     */
    public Compra paraFuncion(Funcion funcion) {
        this.funcion = Objects.requireNonNull(funcion, "La función no puede ser null.");
        return this;
    }

    /**
     * Registra el valor devuelto y completa la transición al estado reembolsado.
     * Este método debe ejecutarse después de que la tarjeta virtual haya recibido el dinero.
     *
     * @param monto valor que fue devuelto a la tarjeta.
     * @throws IllegalArgumentException si el monto es negativo, no es finito o supera el total.
     * @throws IllegalStateException si ya se registró un reembolso o el estado no lo permite.
     */
    public void registrarReembolso(double monto) {
        if (!Double.isFinite(monto) || monto < 0 || monto > total) {
            throw new IllegalArgumentException("El monto del reembolso no es válido.");
        }
        if (montoReembolsado > 0) {
            throw new IllegalStateException("Esta compra ya tiene un reembolso registrado.");
        }
        EstadoCompra nuevoEstado = estado.reembolsar();
        montoReembolsado = monto;
        estado = nuevoEstado;
    }

    /**
     * Obtiene el identificador de la compra.
     *
     * @return identificador.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el cliente asociado.
     *
     * @return cliente de la compra.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtiene la función asociada.
     *
     * @return función de la compra.
     */
    public Funcion getFuncion() {
        return funcion;
    }

    /**
     * Obtiene la fecha y hora de creación de la compra.
     *
     * @return fecha de creación.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene las entradas de la compra en una vista no modificable.
     *
     * @return lista de entradas.
     */
    public List<Entrada> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    /**
     * Obtiene los detalles de confitería en una vista no modificable.
     *
     * @return lista de detalles.
     */
    public List<DetalleConfiteria> getDetallesConfiteria() {
        return Collections.unmodifiableList(detallesConfiteria);
    }

    /**
     * Obtiene el objeto que calcula el precio de la compra.
     *
     * @return precio base o decorador configurado.
     */
    public Precio getPrecio() {
        return precio;
    }

    /**
     * Obtiene la tarjeta virtual asociada a la compra.
     *
     * @return tarjeta utilizada para el pago.
     */
    public TarjetaVirtual getTarjeta() {
        return tarjeta;
    }

    /**
     * Asocia una tarjeta virtual a la compra.
     *
     * @param tarjeta tarjeta que se utilizará para pagar.
     * @throws NullPointerException si la tarjeta es null.
     */
    public void setTarjeta(TarjetaVirtual tarjeta) {
        this.tarjeta = Objects.requireNonNull(tarjeta, "La tarjeta no puede ser null.");
    }

    /**
     * Obtiene el subtotal antes de promociones.
     *
     * @return subtotal de entradas y confitería.
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Obtiene el total después de las promociones.
     *
     * @return total que se debe pagar.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Obtiene el estado actual de la compra.
     *
     * @return estado actual.
     */
    public EstadoCompra getEstado() {
        return estado;
    }

    /**
     * Obtiene los puntos ganados por esta compra.
     *
     * @return cantidad de puntos acumulados.
     */
    public int getPuntosGanados() {
        return puntosGanados;
    }

    /**
     * Actualiza la cantidad de puntos ganados por la compra.
     *
     * @param puntosGanados cantidad de puntos; debe ser mayor o igual que cero.
     * @throws IllegalArgumentException si la cantidad es negativa.
     */
    public void setPuntosGanados(int puntosGanados) {
        if (puntosGanados < 0) {
            throw new IllegalArgumentException("Los puntos ganados no pueden ser negativos.");
        }
        this.puntosGanados = puntosGanados;
    }

    /**
     * Obtiene los puntos utilizados en la compra.
     *
     * @return cantidad de puntos redimidos.
     */
    public int getPuntosRedimidos() {
        return puntosRedimidos;
    }

    /**
     * Actualiza la cantidad de puntos utilizados en la compra.
     *
     * @param puntosRedimidos cantidad de puntos; debe ser mayor o igual que cero.
     * @throws IllegalArgumentException si la cantidad es negativa.
     */
    public void setPuntosRedimidos(int puntosRedimidos) {
        if (puntosRedimidos < 0) {
            throw new IllegalArgumentException("Los puntos redimidos no pueden ser negativos.");
        }
        this.puntosRedimidos = puntosRedimidos;
    }

    /**
     * Obtiene el dinero que ya fue devuelto al cliente.
     *
     * @return monto reembolsado.
     */
    public double getMontoReembolsado() {
        return montoReembolsado;
    }
}
