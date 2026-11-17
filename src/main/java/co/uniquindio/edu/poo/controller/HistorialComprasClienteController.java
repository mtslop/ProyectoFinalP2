package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.Funcion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controlador del historial de compras del cliente: cancelaciones con la política
 * de reembolso y bandeja de notificaciones (patrón Observer).
 */
public class HistorialComprasClienteController {

    @FXML
    private TableView<Compra> tblHistorialCompras;
    @FXML
    private TableColumn<Compra, String> colHistId;
    @FXML
    private TableColumn<Compra, String> colHistFecha;
    @FXML
    private TableColumn<Compra, String> colHistFuncion;
    @FXML
    private TableColumn<Compra, String> colHistTotal;
    @FXML
    private TableColumn<Compra, String> colHistEstado;
    @FXML
    private TableColumn<Compra, String> colHistReembolso;
    @FXML
    private Button btnCancelarCompraCliente;
    @FXML
    private ListView<String> lstNotificacionesCliente;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Configura la tabla y carga las compras y notificaciones del cliente.
     */
    @FXML
    private void initialize() {
        VistaUtil.columna(colHistId, Compra::getId);
        VistaUtil.columna(colHistFecha, c -> VistaUtil.fechaHora(c.getFecha()));
        VistaUtil.columna(colHistFuncion, HistorialComprasClienteController::describirFuncion);
        VistaUtil.columna(colHistTotal, c -> VistaUtil.moneda(c.getTotal()));
        VistaUtil.columna(colHistEstado, c -> c.getEstado().nombre());
        VistaUtil.columna(colHistReembolso, c -> VistaUtil.moneda(c.getMontoReembolsado()));
        cargar();
    }

    /**
     * Cancela la compra seleccionada aplicando la política de reembolso:
     * 100 % con más de 24 horas, 80 % entre 2 y 24 horas y no se permite con menos de 2 horas.
     *
     * @param event evento del botón
     */
    @FXML
    private void onSolicitarCancelacionCompra(ActionEvent event) {
        Compra compra = tblHistorialCompras.getSelectionModel().getSelectedItem();
        if (compra == null) {
            VistaUtil.advertir("Seleccione una compra de la tabla");
            return;
        }
        if (!VistaUtil.confirmar("¿Cancelar la compra " + compra.getId() + "? El reembolso depende del tiempo "
                + "que falta para la función.")) {
            return;
        }
        try {
            double reembolso = sesion.getCompraFacade().cancelarCompra(compra);
            desuscribirSiNoTieneMasEntradas(compra);
            VistaUtil.informar("Compra cancelada", "Se reembolsaron " + VistaUtil.moneda(reembolso)
                    + " a su tarjeta virtual.");
            sesion.getDashboardCliente().actualizarEncabezado();
            cargar();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Quita al cliente de los avisos de la función si ya no tiene compras pagadas para ella.
     *
     * @param cancelada compra que se acaba de cancelar
     */
    private void desuscribirSiNoTieneMasEntradas(Compra cancelada) {
        Funcion funcion = cancelada.getFuncion();
        if (funcion == null) {
            return;
        }
        Cliente cliente = sesion.getClienteActual();
        for (Compra c : cliente.getHistorialCompras()) {
            if (c.getFuncion() == funcion && "PAGADA".equals(c.getEstado().nombre())) {
                return;
            }
        }
        sesion.getNotificador().desuscribir(funcion, cliente);
    }

    /**
     * Carga en la vista las compras y notificaciones del cliente.
     */
    private void cargar() {
        Cliente cliente = sesion.getClienteActual();
        tblHistorialCompras.getItems().setAll(sesion.getServicioCliente().consultarHistorial(cliente));
        lstNotificacionesCliente.getItems().setAll(cliente.getNotificaciones());
        if (cliente.getNotificaciones().isEmpty()) {
            lstNotificacionesCliente.getItems().add("No tiene notificaciones.");
        }
    }

    /**
     * Describe la película y la fecha de la función de una compra.
     *
     * @param compra compra a describir
     * @return texto de la función o "Confitería" si la compra no tiene función
     */
    private static String describirFuncion(Compra compra) {
        Funcion funcion = compra.getFuncion();
        if (funcion == null) {
            return "Confitería";
        }
        return funcion.getPelicula().getTitulo() + " - " + VistaUtil.fechaHora(funcion.getFechaHora());
    }
}
