package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.AdministradorFactory;
import co.uniquindio.edu.poo.Model.Asiento;
import co.uniquindio.edu.poo.Model.CinemaUQ;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.CompraFacade;
import co.uniquindio.edu.poo.Model.DescuentoDiaEspecial;
import co.uniquindio.edu.poo.Model.DescuentoPorcentaje;
import co.uniquindio.edu.poo.Model.DescuentoValorFijo;
import co.uniquindio.edu.poo.Model.DetalleConfiteria;
import co.uniquindio.edu.poo.Model.Entrada;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.IServicioAdmin;
import co.uniquindio.edu.poo.Model.ItemConfiteria;
import co.uniquindio.edu.poo.Model.NotificacionCentro;
import co.uniquindio.edu.poo.Model.Movimiento;
import co.uniquindio.edu.poo.Model.Persona;
import co.uniquindio.edu.poo.Model.PersonaFactory;
import co.uniquindio.edu.poo.Model.Precio;
import co.uniquindio.edu.poo.Model.PrecioBase;
import co.uniquindio.edu.poo.Model.Promocion;
import co.uniquindio.edu.poo.Model.ReporteFactory;
import co.uniquindio.edu.poo.Model.ServicioAdmin;
import co.uniquindio.edu.poo.Model.ServicioAdminProxy;
import co.uniquindio.edu.poo.Model.ServicioAutenticacion;
import co.uniquindio.edu.poo.Model.ServicioCliente;
import co.uniquindio.edu.poo.Model.TarjetaVirtual;
import co.uniquindio.edu.poo.Model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sesión compartida por todos los controladores (Singleton).
 * <p>
 * Guarda los servicios del modelo, la persona que inició sesión y el carrito de la compra
 * en curso (función, asientos, confitería y promociones). Así cada controlador solo
 * se comunica con el modelo y no tiene que pasarle datos a los demás controladores.
 */
public final class SesionApp {

    /** Puntos que se acumulan por cada entrada pagada y que cuesta redimir una entrada. */
    public static final int PUNTOS_POR_ENTRADA = 10;

    /** Rol de cliente en los formularios de registro. */
    public static final String ROL_CLIENTE = "CLIENTE";

    /** Rol de administrador en los formularios de registro. */
    public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";

    private static SesionApp instancia;

    private final CinemaUQ cinema;
    private final NotificacionCentro notificador;
    private final CompraFacade compraFacade;
    private final ServicioAutenticacion servicioAutenticacion;
    private final ServicioCliente servicioCliente;
    private final ServicioAdmin servicioAdmin;

    private Persona personaActual;
    private IServicioAdmin servicioAdminProxy;
    private ClienteDashboardController dashboardCliente;

    private Funcion funcionSeleccionada;
    private final List<Asiento> asientosSeleccionados;
    private final List<Entrada> entradasRedimidas;
    private final List<DetalleConfiteria> detallesConfiteria;
    private final List<Promocion> promocionesAplicadas;
    private String idCompraEnCurso;

    /**
     * Crea la sesión, los servicios del modelo y carga los datos iniciales del cine.
     */
    private SesionApp() {
        this.cinema = CinemaUQ.getInstance();
        this.notificador = new NotificacionCentro();
        this.compraFacade = new CompraFacade(cinema, PUNTOS_POR_ENTRADA);
        this.servicioAutenticacion = new ServicioAutenticacion(cinema);
        this.servicioCliente = new ServicioCliente(cinema);
        this.servicioAdmin = new ServicioAdmin(cinema, notificador, compraFacade, new ReporteFactory());
        this.asientosSeleccionados = new ArrayList<>();
        this.entradasRedimidas = new ArrayList<>();
        this.detallesConfiteria = new ArrayList<>();
        this.promocionesAplicadas = new ArrayList<>();
        DatosIniciales.cargar(cinema, compraFacade, notificador);
    }

    /**
     * Obtiene la única instancia de la sesión.
     *
     * @return sesión de la aplicación
     */
    public static SesionApp getInstance() {
        if (instancia == null) {
            instancia = new SesionApp();
        }
        return instancia;
    }

    public CinemaUQ getCinema() {
        return cinema;
    }

    public NotificacionCentro getNotificador() {
        return notificador;
    }

    public CompraFacade getCompraFacade() {
        return compraFacade;
    }

    public ServicioAutenticacion getServicioAutenticacion() {
        return servicioAutenticacion;
    }

    public ServicioCliente getServicioCliente() {
        return servicioCliente;
    }

    public Persona getPersonaActual() {
        return personaActual;
    }

    public ClienteDashboardController getDashboardCliente() {
        return dashboardCliente;
    }

    public void setDashboardCliente(ClienteDashboardController dashboardCliente) {
        this.dashboardCliente = dashboardCliente;
    }

    public Funcion getFuncionSeleccionada() {
        return funcionSeleccionada;
    }

    public List<Asiento> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public List<Entrada> getEntradasRedimidas() {
        return Collections.unmodifiableList(entradasRedimidas);
    }

    public List<DetalleConfiteria> getDetallesConfiteria() {
        return detallesConfiteria;
    }

    public List<Promocion> getPromocionesAplicadas() {
        return Collections.unmodifiableList(promocionesAplicadas);
    }

    @Override
    public String toString() {
        return "SesionApp{" +
                "personaActual=" + (personaActual != null ? personaActual.getNombre() : "ninguna") +
                ", asientos=" + asientosSeleccionados.size() +
                ", confiteria=" + detallesConfiteria.size() +
                '}';
    }

    /**
     * Guarda la persona que inició sesión y crea el proxy de administración para ella.
     *
     * @param persona persona autenticada
     */
    public void iniciarSesion(Persona persona) {
        this.personaActual = persona;
        this.servicioAdminProxy = new ServicioAdminProxy(servicioAdmin, persona);
        limpiarCarrito();
    }

    /**
     * Cierra la sesión actual y vacía el carrito.
     */
    public void cerrarSesion() {
        this.personaActual = null;
        this.servicioAdminProxy = null;
        this.dashboardCliente = null;
        limpiarCarrito();
    }

    /**
     * Obtiene el servicio de administración protegido por el proxy.
     *
     * @return proxy del servicio de administración
     * @throws IllegalStateException si no hay una sesión iniciada
     */
    public IServicioAdmin getServicioAdmin() {
        if (servicioAdminProxy == null) {
            throw new IllegalStateException("Debe iniciar sesión para usar el panel de administración");
        }
        return servicioAdminProxy;
    }

    /**
     * Obtiene el cliente que inició sesión.
     *
     * @return cliente actual
     * @throws IllegalStateException si no hay sesión o la persona no es cliente
     */
    public Cliente getClienteActual() {
        if (personaActual instanceof Cliente cliente) {
            return cliente;
        }
        throw new IllegalStateException("No hay un cliente con la sesión iniciada");
    }

    /**
     * Registra una persona nueva con la fábrica que corresponde a su rol (patrón Factory Method).
     *
     * @param rol        {@code "CLIENTE"} o {@code "ADMINISTRADOR"}
     * @param id         identificación
     * @param nombre     nombre completo
     * @param correo     correo electrónico, que debe ser único
     * @param contrasena contraseña de acceso
     * @param datoExtra  teléfono si es cliente, o cargo si es administrador
     * @return persona registrada
     * @throws Exception si falta un dato o el correo ya está registrado
     */
    public Persona registrarPersona(String rol, String id, String nombre, String correo,
                                    String contrasena, String datoExtra) throws Exception {
        if (rol == null) {
            throw new IllegalArgumentException("Seleccione si la persona es CLIENTE o ADMINISTRADOR");
        }
        if (!correo.contains("@")) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }
        for (Persona p : cinema.getUsuarios()) {
            if (p.getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe una persona con la identificación " + id);
            }
        }
        if (ROL_CLIENTE.equals(rol)) {
            return servicioAutenticacion.registrarCliente(id, nombre, correo, contrasena, datoExtra);
        }
        if (cinema.buscarPersonaPorCorreo(correo) != null) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + correo);
        }
        PersonaFactory fabrica = new AdministradorFactory();
        Persona administrador = fabrica.crearPersona(id, nombre, correo, contrasena, datoExtra);
        cinema.agregarPersona(administrador);
        return administrador;
    }

    /**
     * Deja registrado en la tarjeta del cliente un movimiento de redención de puntos.
     *
     * @param descripcion detalle de lo que se redimió
     */
    public void registrarRedencion(String descripcion) {
        TarjetaVirtual tarjeta = getClienteActual().getTarjeta();
        String id = tarjeta.getNumero() + "-" + (tarjeta.getMovimientos().size() + 1);
        tarjeta.registrarMovimiento(new Movimiento(id, 0, LocalDateTime.now(), descripcion, TipoMovimiento.REDENCION));
    }

    /**
     * Selecciona la función para la que se van a comprar entradas.
     * Si es una función distinta a la anterior, se descartan los asientos elegidos.
     *
     * @param funcion función seleccionada
     */
    public void seleccionarFuncion(Funcion funcion) {
        if (funcionSeleccionada != funcion) {
            asientosSeleccionados.clear();
        }
        this.funcionSeleccionada = funcion;
    }

    /**
     * Agrega un producto o combo al carrito. Si ya estaba, suma la cantidad.
     *
     * @param item     producto o combo
     * @param cantidad unidades a agregar
     */
    public void agregarConfiteria(ItemConfiteria item, int cantidad) {
        for (int i = 0; i < detallesConfiteria.size(); i++) {
            DetalleConfiteria d = detallesConfiteria.get(i);
            if (d.getItem() == item) {
                detallesConfiteria.set(i, new DetalleConfiteria(d.getCantidad() + cantidad, item));
                return;
            }
        }
        detallesConfiteria.add(new DetalleConfiteria(cantidad, item));
    }

    /**
     * Agrega una promoción a la compra en curso, sin repetirla.
     *
     * @param promocion promoción a aplicar
     * @throws IllegalStateException si la promoción ya fue aplicada
     */
    public void aplicarPromocion(Promocion promocion) {
        if (promocionesAplicadas.contains(promocion)) {
            throw new IllegalStateException("La promoción " + promocion.getNombre() + " ya está aplicada");
        }
        promocionesAplicadas.add(promocion);
    }

    /**
     * Indica si el carrito tiene entradas, entradas redimidas o productos.
     *
     * @return {@code true} si hay algo para comprar
     */
    public boolean carritoTieneConceptos() {
        return !asientosSeleccionados.isEmpty() || !entradasRedimidas.isEmpty() || !detallesConfiteria.isEmpty();
    }

    /**
     * Redime con puntos la entrada del último asiento seleccionado (patrón Facade).
     * El asiento queda ocupado y pasa de la lista de asientos a pagar a la de entradas redimidas.
     *
     * @return entrada emitida sin costo
     * @throws IllegalStateException si no hay asientos seleccionados o puntos suficientes
     */
    public Entrada redimirEntrada() {
        if (funcionSeleccionada == null || asientosSeleccionados.isEmpty()) {
            throw new IllegalStateException("Seleccione al menos un asiento para redimir una entrada");
        }
        Asiento asiento = asientosSeleccionados.get(asientosSeleccionados.size() - 1);
        Entrada entrada = compraFacade.redimirEntrada(getClienteActual(), funcionSeleccionada, asiento);
        asientosSeleccionados.remove(asiento);
        entradasRedimidas.add(entrada);
        registrarRedencion("Redención de " + PUNTOS_POR_ENTRADA + " puntos: entrada " + asiento.codigo()
                + " - " + funcionSeleccionada.getPelicula().getTitulo());
        return entrada;
    }

    /**
     * Arma la compra en curso con el Builder de {@link Compra} y calcula su total.
     * La compra queda en estado pendiente; todavía no se cobra.
     *
     * @return compra construida con los datos del carrito
     * @throws IllegalStateException si falta información para armar la compra
     */
    public Compra construirCompra() {
        Cliente cliente = getClienteActual();
        Compra compra = new Compra(getIdCompraEnCurso())
                .paraCliente(cliente);
        compra.setTarjeta(cliente.getTarjeta());
        if (funcionSeleccionada != null && (!asientosSeleccionados.isEmpty() || !entradasRedimidas.isEmpty())) {
            compra.paraFuncion(funcionSeleccionada);
        }
        for (Asiento asiento : asientosSeleccionados) {
            compra.agregarAsiento(asiento, funcionSeleccionada.getPrecioBase());
        }
        for (Entrada entrada : entradasRedimidas) {
            compra.agregarAsiento(entrada);
        }
        for (DetalleConfiteria detalle : detallesConfiteria) {
            compra.agregarItem(detalle.getItem(), detalle.getCantidad());
        }
        Precio precio = new PrecioBase(0);
        for (Promocion promocion : promocionesAplicadas) {
            precio = decorar(precio, promocion);
        }
        compra.aplicarPromocion(precio);
        compra.calcularTotal();
        return compra;
    }

    /**
     * Paga la compra en curso con la tarjeta del cliente usando {@link CompraFacade},
     * suscribe al cliente a la función (patrón Observer) y vacía el carrito.
     *
     * @return compra pagada
     * @throws IllegalStateException si el carrito está vacío o la tarjeta no tiene saldo
     */
    public Compra pagarCompra() {
        if (!carritoTieneConceptos()) {
            throw new IllegalStateException("No hay entradas ni productos en la compra");
        }
        Compra compra = compraFacade.realizarCompra(construirCompra());
        if (compra.getFuncion() != null && !compra.getEntradas().isEmpty()) {
            notificador.suscribir(compra.getFuncion(), compra.getCliente());
        }
        limpiarCarrito();
        return compra;
    }

    /**
     * Vacía el carrito de la compra en curso.
     */
    public void limpiarCarrito() {
        funcionSeleccionada = null;
        asientosSeleccionados.clear();
        entradasRedimidas.clear();
        detallesConfiteria.clear();
        promocionesAplicadas.clear();
        idCompraEnCurso = null;
    }

    /**
     * Obtiene el identificador de la compra en curso; se genera la primera vez que se pide.
     *
     * @return identificador con formato CMP-0001
     */
    public String getIdCompraEnCurso() {
        if (idCompraEnCurso == null) {
            idCompraEnCurso = String.format("CMP-%04d", cinema.getCompras().size() + 1);
        }
        return idCompraEnCurso;
    }

    /**
     * Envuelve un precio con el decorador que corresponde al tipo de la promoción (patrón Decorator).
     *
     * @param precio    precio a decorar
     * @param promocion promoción a aplicar
     * @return precio decorado
     */
    private Precio decorar(Precio precio, Promocion promocion) {
        return switch (promocion.getTipo()) {
            case PORCENTAJE -> new DescuentoPorcentaje(precio, promocion.getValor());
            case VALORFIJO -> new DescuentoValorFijo(precio, promocion.getValor());
            case DIA_ESPECIAL -> new DescuentoDiaEspecial(precio, promocion.getDia(), promocion.getValor());
        };
    }
}
