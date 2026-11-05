package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Combo;
import co.uniquindio.edu.poo.Model.DetalleConfiteria;
import co.uniquindio.edu.poo.Model.ItemConfiteria;
import co.uniquindio.edu.poo.Model.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controlador de la compra de confitería. Muestra el catálogo de productos y combos
 * (patrón Composite) y guarda en la sesión los que el cliente agrega a la compra.
 */
public class CompraConfiteriaController {

    @FXML
    private TableView<ItemConfiteria> tblCatalogoConfiteria;
    @FXML
    private TableColumn<ItemConfiteria, String> colItemCodigo;
    @FXML
    private TableColumn<ItemConfiteria, String> colItemNombre;
    @FXML
    private TableColumn<ItemConfiteria, String> colItemTipo;
    @FXML
    private TableColumn<ItemConfiteria, String> colItemPrecio;
    @FXML
    private TableColumn<ItemConfiteria, String> colItemPuntosRedencion;
    @FXML
    private Spinner<Integer> spnCantidadItem;
    @FXML
    private Button btnAgregarACompra;
    @FXML
    private Button btnRedimirConPuntos;
    @FXML
    private TableView<DetalleConfiteria> tblDetalleConfiteria;
    @FXML
    private TableColumn<DetalleConfiteria, String> colDetItem;
    @FXML
    private TableColumn<DetalleConfiteria, String> colDetCant;
    @FXML
    private TableColumn<DetalleConfiteria, String> colDetUnitario;
    @FXML
    private TableColumn<DetalleConfiteria, String> colDetSubtotal;
    @FXML
    private Label lblTotalConfiteria;
    @FXML
    private Button btnIrResumenPago;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Configura las tablas, el selector de cantidad y carga el catálogo.
     */
    @FXML
    private void initialize() {
        spnCantidadItem.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));

        VistaUtil.columna(colItemCodigo, CompraConfiteriaController::codigoDe);
        VistaUtil.columna(colItemNombre, ItemConfiteria::nombre);
        VistaUtil.columna(colItemTipo, item -> item instanceof Combo ? "Combo" : "Producto");
        VistaUtil.columna(colItemPrecio, item -> VistaUtil.moneda(item.precio()));
        VistaUtil.columna(colItemPuntosRedencion, item -> item.puntosRedencion() + " pts");

        VistaUtil.columna(colDetItem, d -> d.getItem().nombre());
        VistaUtil.columna(colDetCant, d -> String.valueOf(d.getCantidad()));
        VistaUtil.columna(colDetUnitario, d -> VistaUtil.moneda(d.getPrecioUnitario()));
        VistaUtil.columna(colDetSubtotal, d -> VistaUtil.moneda(d.subtotal()));

        tblCatalogoConfiteria.getItems().setAll(sesion.getCinema().getProductos());
        actualizarDetalle();
    }

    /**
     * Agrega el producto o combo seleccionado a la compra en curso.
     *
     * @param event evento del botón
     */
    @FXML
    private void onAgregarItemCompra(ActionEvent event) {
        ItemConfiteria item = tblCatalogoConfiteria.getSelectionModel().getSelectedItem();
        if (item == null) {
            VistaUtil.advertir("Seleccione un producto o combo del catálogo");
            return;
        }
        try {
            sesion.agregarConfiteria(item, spnCantidadItem.getValue());
            actualizarDetalle();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Redime con puntos el producto seleccionado usando {@code CompraFacade}.
     * El producto no se cobra; se entrega en la confitería.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRedimirItemConPuntos(ActionEvent event) {
        ItemConfiteria item = tblCatalogoConfiteria.getSelectionModel().getSelectedItem();
        if (item == null) {
            VistaUtil.advertir("Seleccione un producto o combo del catálogo");
            return;
        }
        int cantidad = spnCantidadItem.getValue();
        int puntos = item.puntosRedencion() * cantidad;
        if (!VistaUtil.confirmar("¿Redimir " + cantidad + " x " + item.nombre() + " por " + puntos + " puntos?")) {
            return;
        }
        try {
            DetalleConfiteria detalle = sesion.getCompraFacade()
                    .redimirItemConfiteria(sesion.getClienteActual(), item, cantidad);
            sesion.registrarRedencion("Redención de " + puntos + " puntos: "
                    + detalle.getItem().nombre() + " x" + detalle.getCantidad());
            sesion.getDashboardCliente().actualizarEncabezado();
            VistaUtil.informar("Redención exitosa", "Redimió " + cantidad + " x " + item.nombre()
                    + ". Reclámelo en la confitería mostrando este mensaje.");
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Quita de la compra el detalle seleccionado.
     *
     * @param event evento del botón
     */
    @FXML
    private void onEliminarDetalle(ActionEvent event) {
        DetalleConfiteria detalle = tblDetalleConfiteria.getSelectionModel().getSelectedItem();
        if (detalle == null) {
            VistaUtil.advertir("Seleccione un detalle de la compra para quitarlo");
            return;
        }
        sesion.getDetallesConfiteria().remove(detalle);
        actualizarDetalle();
    }

    /**
     * Continúa al resumen y pago de la compra.
     *
     * @param event evento del botón
     */
    @FXML
    private void onIrACheckout(ActionEvent event) {
        if (!sesion.carritoTieneConceptos()) {
            VistaUtil.advertir("Agregue al menos un producto o seleccione asientos antes de pagar");
            return;
        }
        sesion.getDashboardCliente().mostrar("CheckoutPagoView");
    }

    /**
     * Muestra los detalles de confitería de la sesión y su subtotal.
     */
    private void actualizarDetalle() {
        tblDetalleConfiteria.getItems().setAll(sesion.getDetallesConfiteria());
        double total = 0;
        for (DetalleConfiteria detalle : sesion.getDetallesConfiteria()) {
            total += detalle.subtotal();
        }
        lblTotalConfiteria.setText(VistaUtil.moneda(total));
    }

    /**
     * Obtiene el código de un producto o de un combo.
     *
     * @param item producto o combo
     * @return código del ítem
     */
    static String codigoDe(ItemConfiteria item) {
        if (item instanceof Producto producto) {
            return producto.getCodigo();
        }
        if (item instanceof Combo combo) {
            return combo.getCodigo();
        }
        return "";
    }
}
