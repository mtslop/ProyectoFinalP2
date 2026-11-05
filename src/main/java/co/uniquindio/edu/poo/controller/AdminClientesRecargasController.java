package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.EstadoSolicitud;
import co.uniquindio.edu.poo.Model.EstrategiaPuntosEstandar;
import co.uniquindio.edu.poo.Model.EstrategiaPuntosVip;
import co.uniquindio.edu.poo.Model.IServicioAdmin;
import co.uniquindio.edu.poo.Model.SolicitudRecarga;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Controlador de recargas de tarjetas, consulta de clientes, asignación de la
 * estrategia de puntos (patrón Strategy) y consulta de todas las compras.
 */
public class AdminClientesRecargasController {

    private static final String ESTRATEGIA_ESTANDAR = "Estándar (1 punto por $1.000)";
    private static final String ESTRATEGIA_VIP = "VIP (x1,5 puntos)";

    @FXML
    private TextField txtAdminCorreoClienteRecarga;
    @FXML
    private TextField txtAdminMontoRecargaDirecta;
    @FXML
    private TableView<SolicitudRecarga> tblSolicitudesRecargaAdmin;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolId;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolCliente;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolTarjeta;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolMonto;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolFecha;
    @FXML
    private TableColumn<SolicitudRecarga, String> colAdmSolEstado;

    @FXML
    private TableView<Cliente> tblAdminClientes;
    @FXML
    private TableColumn<Cliente, String> colAdmCliId;
    @FXML
    private TableColumn<Cliente, String> colAdmCliNombre;
    @FXML
    private TableColumn<Cliente, String> colAdmCliCorreo;
    @FXML
    private TableColumn<Cliente, String> colAdmCliSaldo;
    @FXML
    private TableColumn<Cliente, String> colAdmCliPuntos;
    @FXML
    private ComboBox<String> cmbEstrategiaCliente;

    @FXML
    private TableView<Compra> tblAdminComprasGlobal;
    @FXML
    private TableColumn<Compra, String> colAdmCmpId;
    @FXML
    private TableColumn<Compra, String> colAdmCmpCliente;
    @FXML
    private TableColumn<Compra, String> colAdmCmpFuncion;
    @FXML
    private TableColumn<Compra, String> colAdmCmpTotal;
    @FXML
    private TableColumn<Compra, String> colAdmCmpEstado;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Configura las tablas y carga solicitudes, clientes y compras.
     */
    @FXML
    private void initialize() {
        cmbEstrategiaCliente.getItems().setAll(ESTRATEGIA_ESTANDAR, ESTRATEGIA_VIP);

        VistaUtil.columna(colAdmSolId, SolicitudRecarga::getId);
        VistaUtil.columna(colAdmSolCliente, s -> s.getCliente().getNombre() + " (" + s.getCliente().getCorreo() + ")");
        VistaUtil.columna(colAdmSolTarjeta, s -> s.getCliente().getTarjeta().getNumero());
        VistaUtil.columna(colAdmSolMonto, s -> VistaUtil.moneda(s.getMonto()));
        VistaUtil.columna(colAdmSolFecha, s -> VistaUtil.fechaHora(s.getFecha()));
        VistaUtil.columna(colAdmSolEstado, s -> s.getEstado().name());

        VistaUtil.columna(colAdmCliId, Cliente::getId);
        VistaUtil.columna(colAdmCliNombre, Cliente::getNombre);
        VistaUtil.columna(colAdmCliCorreo, Cliente::getCorreo);
        VistaUtil.columna(colAdmCliSaldo, c -> VistaUtil.moneda(c.getTarjeta().getSaldo()));
        VistaUtil.columna(colAdmCliPuntos, c -> String.valueOf(c.getCuentaPuntos().puntosDisponibles()));

        VistaUtil.columna(colAdmCmpId, Compra::getId);
        VistaUtil.columna(colAdmCmpCliente, c -> c.getCliente().getNombre());
        VistaUtil.columna(colAdmCmpFuncion, c -> c.getFuncion() == null ? "Confitería"
                : c.getFuncion().getId() + " - " + c.getFuncion().getPelicula().getTitulo());
        VistaUtil.columna(colAdmCmpTotal, c -> VistaUtil.moneda(c.getTotal()));
        VistaUtil.columna(colAdmCmpEstado, c -> c.getEstado().nombre());

        cargarDatos();
    }

    /**
     * Recarga directamente la tarjeta de un cliente buscado por correo o identificación.
     * Se crea una solicitud y el administrador la aprueba en el mismo paso.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRecargarTarjetaDirecto(ActionEvent event) {
        try {
            Cliente cliente = buscarCliente(VistaUtil.requerido(txtAdminCorreoClienteRecarga.getText(), "correo o ID"));
            double monto = VistaUtil.leerDecimal(txtAdminMontoRecargaDirecta.getText(), "monto");
            SolicitudRecarga solicitud = sesion.getServicioCliente().solicitarRecarga(cliente, monto);
            admin().recargarTarjeta(solicitud);
            VistaUtil.informar("Recarga realizada", "Se recargaron " + VistaUtil.moneda(monto)
                    + " a la tarjeta de " + cliente.getNombre() + ".");
            txtAdminCorreoClienteRecarga.clear();
            txtAdminMontoRecargaDirecta.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Rechaza la solicitud de recarga seleccionada.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRechazarSolicitudRecarga(ActionEvent event) {
        SolicitudRecarga solicitud = solicitudPendienteSeleccionada();
        if (solicitud == null) {
            return;
        }
        try {
            admin().rechazarRecarga(solicitud);
            VistaUtil.informar("Solicitud rechazada", "La solicitud " + solicitud.getId() + " fue rechazada.");
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Aprueba la solicitud de recarga seleccionada y recarga la tarjeta del cliente.
     *
     * @param event evento del botón
     */
    @FXML
    private void onAprobarSolicitudRecarga(ActionEvent event) {
        SolicitudRecarga solicitud = solicitudPendienteSeleccionada();
        if (solicitud == null) {
            return;
        }
        try {
            admin().recargarTarjeta(solicitud);
            VistaUtil.informar("Recarga aprobada", "Se recargaron " + VistaUtil.moneda(solicitud.getMonto())
                    + " a " + solicitud.getCliente().getNombre() + ".");
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Cambia la estrategia de puntos del cliente seleccionado (patrón Strategy).
     *
     * @param event evento del botón
     */
    @FXML
    private void onActualizarEstrategiaCliente(ActionEvent event) {
        Cliente cliente = tblAdminClientes.getSelectionModel().getSelectedItem();
        String estrategia = cmbEstrategiaCliente.getValue();
        if (cliente == null || estrategia == null) {
            VistaUtil.advertir("Seleccione un cliente y una estrategia");
            return;
        }
        if (ESTRATEGIA_VIP.equals(estrategia)) {
            cliente.getCuentaPuntos().setEstrategia(new EstrategiaPuntosVip(1000, 1.5));
        } else {
            cliente.getCuentaPuntos().setEstrategia(new EstrategiaPuntosEstandar(1000));
        }
        VistaUtil.informar("Estrategia asignada", cliente.getNombre() + " ahora tiene la estrategia " + estrategia + ".");
        cargarDatos();
    }

    /**
     * Obtiene la solicitud seleccionada, verificando que siga pendiente.
     *
     * @return solicitud seleccionada, o {@code null} si no hay una válida
     */
    private SolicitudRecarga solicitudPendienteSeleccionada() {
        SolicitudRecarga solicitud = tblSolicitudesRecargaAdmin.getSelectionModel().getSelectedItem();
        if (solicitud == null) {
            VistaUtil.advertir("Seleccione una solicitud de la tabla");
            return null;
        }
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            VistaUtil.advertir("La solicitud " + solicitud.getId() + " ya fue atendida (" + solicitud.getEstado() + ")");
            return null;
        }
        return solicitud;
    }

    /**
     * Busca un cliente por su correo o su identificación.
     *
     * @param dato correo o identificación
     * @return cliente encontrado
     * @throws IllegalArgumentException si no existe un cliente con ese dato
     */
    private Cliente buscarCliente(String dato) {
        for (Cliente cliente : admin().consultarClientes()) {
            if (cliente.getCorreo().equalsIgnoreCase(dato) || cliente.getId().equals(dato)) {
                return cliente;
            }
        }
        throw new IllegalArgumentException("No existe un cliente con el correo o ID " + dato);
    }

    /**
     * Recarga las tablas de solicitudes, clientes y compras.
     */
    private void cargarDatos() {
        tblSolicitudesRecargaAdmin.getItems().setAll(sesion.getCinema().getSolicitudes());
        tblAdminClientes.getItems().setAll(admin().consultarClientes());
        tblAdminComprasGlobal.getItems().setAll(admin().consultarCompras(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31)));
    }

    /**
     * Obtiene el servicio de administración protegido por el proxy.
     *
     * @return proxy de administración
     */
    private IServicioAdmin admin() {
        return sesion.getServicioAdmin();
    }
}
