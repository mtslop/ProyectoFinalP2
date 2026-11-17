package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Movimiento;
import co.uniquindio.edu.poo.Model.RegistroPuntos;
import co.uniquindio.edu.poo.Model.SolicitudRecarga;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del monedero del cliente: solicitudes de recarga, movimientos de la
 * tarjeta virtual y lotes de puntos con su vencimiento.
 */
public class MonederoPuntosClienteController {

    @FXML
    private TextField txtMontoSolicitudRecarga;
    @FXML
    private Button btnEnviarSolicitudRecarga;
    @FXML
    private TableView<SolicitudRecarga> tblMisSolicitudesRecarga;
    @FXML
    private TableColumn<SolicitudRecarga, String> colSolId;
    @FXML
    private TableColumn<SolicitudRecarga, String> colSolMonto;
    @FXML
    private TableColumn<SolicitudRecarga, String> colSolEstado;
    @FXML
    private Label lblResumenTarjetaNumero;
    @FXML
    private TableView<Movimiento> tblMovimientosTarjeta;
    @FXML
    private TableColumn<Movimiento, String> colMovId;
    @FXML
    private TableColumn<Movimiento, String> colMovFecha;
    @FXML
    private TableColumn<Movimiento, String> colMovTipo;
    @FXML
    private TableColumn<Movimiento, String> colMovValor;
    @FXML
    private TableColumn<Movimiento, String> colMovDescripcion;
    @FXML
    private Label lblTotalPuntosDisponibles;
    @FXML
    private Label lblEstrategiaActiva;
    @FXML
    private Button btnDepurarPuntosVencidos;
    @FXML
    private TableView<RegistroPuntos> tblRegistrosPuntos;
    @FXML
    private TableColumn<RegistroPuntos, String> colRegCantidadInicial;
    @FXML
    private TableColumn<RegistroPuntos, String> colRegCantidadDisponible;
    @FXML
    private TableColumn<RegistroPuntos, String> colRegFechaObtencion;
    @FXML
    private TableColumn<RegistroPuntos, String> colRegFechaVencimiento;
    @FXML
    private TableColumn<RegistroPuntos, String> colRegEstadoVencido;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Configura las tablas y carga los datos de la tarjeta y de los puntos del cliente.
     */
    @FXML
    private void initialize() {
        VistaUtil.columna(colSolId, SolicitudRecarga::getId);
        VistaUtil.columna(colSolMonto, s -> VistaUtil.moneda(s.getMonto()));
        VistaUtil.columna(colSolEstado, s -> s.getEstado().name());

        VistaUtil.columna(colMovId, Movimiento::getId);
        VistaUtil.columna(colMovFecha, m -> VistaUtil.fechaHora(m.getFecha()));
        VistaUtil.columna(colMovTipo, m -> m.getTipo().name());
        VistaUtil.columna(colMovValor, m -> VistaUtil.moneda(m.getValor()));
        VistaUtil.columna(colMovDescripcion, Movimiento::getDescripcion);

        VistaUtil.columna(colRegCantidadInicial, r -> String.valueOf(r.getCantidad()));
        VistaUtil.columna(colRegCantidadDisponible, r -> String.valueOf(r.getCantidadDisponible()));
        VistaUtil.columna(colRegFechaObtencion, r -> VistaUtil.fecha(r.getFechaObtencion()));
        VistaUtil.columna(colRegFechaVencimiento, r -> VistaUtil.fecha(r.getFechaVencimiento()));
        VistaUtil.columna(colRegEstadoVencido, r -> r.estaVencido(LocalDate.now()) ? "VENCIDO" : "VIGENTE");

        cargar();
    }

    /**
     * Crea una solicitud de recarga pendiente para que el administrador la apruebe.
     *
     * @param event evento del botón
     */
    @FXML
    private void onSolicitarRecarga(ActionEvent event) {
        try {
            double monto = VistaUtil.leerDecimal(txtMontoSolicitudRecarga.getText(), "monto");
            SolicitudRecarga solicitud = sesion.getServicioCliente().solicitarRecarga(sesion.getClienteActual(), monto);
            txtMontoSolicitudRecarga.clear();
            VistaUtil.informar("Solicitud enviada", "La solicitud " + solicitud.getId() + " por "
                    + VistaUtil.moneda(monto) + " quedó pendiente de aprobación del administrador.");
            cargar();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Anula los puntos de los lotes vencidos (los puntos duran un año).
     *
     * @param event evento del botón
     */
    @FXML
    private void onEliminarPuntosVencidos(ActionEvent event) {
        sesion.getClienteActual().getCuentaPuntos().eliminarVencidos();
        VistaUtil.informar("Puntos depurados", "Se anularon los puntos de los lotes vencidos.");
        cargar();
        sesion.getDashboardCliente().actualizarEncabezado();
    }

    /**
     * Carga las solicitudes, los movimientos y los puntos del cliente.
     */
    private void cargar() {
        Cliente cliente = sesion.getClienteActual();
        List<SolicitudRecarga> propias = new ArrayList<>();
        for (SolicitudRecarga solicitud : sesion.getCinema().getSolicitudes()) {
            if (solicitud.getCliente() == cliente) {
                propias.add(solicitud);
            }
        }
        tblMisSolicitudesRecarga.getItems().setAll(propias);

        lblResumenTarjetaNumero.setText("Numero Tarjeta: " + cliente.getTarjeta().getNumero()
                + "   |   Saldo: " + VistaUtil.moneda(cliente.getTarjeta().getSaldo()));
        tblMovimientosTarjeta.getItems().setAll(sesion.getServicioCliente().consultarMovimientos(cliente));

        lblTotalPuntosDisponibles.setText(sesion.getServicioCliente().consultarPuntos(cliente) + " pts");
        lblEstrategiaActiva.setText(cliente.getCuentaPuntos().getEstrategia().getClass().getSimpleName());
        tblRegistrosPuntos.getItems().setAll(cliente.getCuentaPuntos().getRegistros());
    }
}
