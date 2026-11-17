package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.CinemaUQ;
import co.uniquindio.edu.poo.Model.Combo;
import co.uniquindio.edu.poo.Model.ItemConfiteria;
import co.uniquindio.edu.poo.Model.Producto;
import co.uniquindio.edu.poo.Model.Promocion;
import co.uniquindio.edu.poo.Model.TipoPromocion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la administración de confitería (productos y combos, patrón Composite)
 * y de las promociones que después se aplican como decoradores del precio.
 */
public class AdminConfiteriaPromocionesController {

    @FXML
    private TextField txtProdCodigo;
    @FXML
    private TextField txtProdNombre;
    @FXML
    private TextField txtProdPrecio;
    @FXML
    private TextField txtProdPuntos;

    @FXML
    private TextField txtComboCodigo;
    @FXML
    private TextField txtComboNombre;
    @FXML
    private TextField txtComboDescuento;
    @FXML
    private TextField txtComboPuntos;
    @FXML
    private ComboBox<ItemConfiteria> cmbItemParaCombo;
    @FXML
    private ListView<String> lstItemsDelCombo;

    @FXML
    private TableView<ItemConfiteria> tblAdminConfiteria;
    @FXML
    private TableColumn<ItemConfiteria, String> colAdmItemCod;
    @FXML
    private TableColumn<ItemConfiteria, String> colAdmItemNom;
    @FXML
    private TableColumn<ItemConfiteria, String> colAdmItemPre;
    @FXML
    private TableColumn<ItemConfiteria, String> colAdmItemPts;

    @FXML
    private TextField txtPromoNombre;
    @FXML
    private ComboBox<TipoPromocion> cmbTipoPromocion;
    @FXML
    private TextField txtPromoValor;
    @FXML
    private ComboBox<DayOfWeek> cmbPromoDiaSemana;
    @FXML
    private DatePicker dpPromoInicio;
    @FXML
    private DatePicker dpPromoFin;
    @FXML
    private TableView<Promocion> tblAdminPromociones;
    @FXML
    private TableColumn<Promocion, String> colPromoNombre;
    @FXML
    private TableColumn<Promocion, String> colPromoTipo;
    @FXML
    private TableColumn<Promocion, String> colPromoValor;
    @FXML
    private TableColumn<Promocion, String> colPromoDia;
    @FXML
    private TableColumn<Promocion, String> colPromoVigencia;

    private final SesionApp sesion = SesionApp.getInstance();
    private final CinemaUQ cinema = sesion.getCinema();
    private final List<ItemConfiteria> itemsDelCombo = new ArrayList<>();

    /**
     * Configura las listas, las tablas y carga el catálogo y las promociones.
     */
    @FXML
    private void initialize() {
        cmbItemParaCombo.setConverter(AdminCarteleraSalasController.convertidor(
                item -> item.nombre() + " - " + VistaUtil.moneda(item.precio())));
        cmbTipoPromocion.getItems().setAll(TipoPromocion.values());
        cmbPromoDiaSemana.getItems().setAll(DayOfWeek.values());

        VistaUtil.columna(colAdmItemCod, CompraConfiteriaController::codigoDe);
        VistaUtil.columna(colAdmItemNom, ItemConfiteria::nombre);
        VistaUtil.columna(colAdmItemPre, item -> VistaUtil.moneda(item.precio()));
        VistaUtil.columna(colAdmItemPts, item -> String.valueOf(item.puntosRedencion()));

        VistaUtil.columna(colPromoNombre, Promocion::getNombre);
        VistaUtil.columna(colPromoTipo, p -> p.getTipo().name());
        VistaUtil.columna(colPromoValor, p -> p.getTipo() == TipoPromocion.VALORFIJO
                ? VistaUtil.moneda(p.getValor()) : p.getValor() + " %");
        VistaUtil.columna(colPromoDia, p -> p.getDia() == null ? "Todos" : p.getDia().name());
        VistaUtil.columna(colPromoVigencia, p -> VistaUtil.fecha(p.getFechaInicio()) + " a "
                + VistaUtil.fecha(p.getFechaFin()));

        cargarDatos();
    }

    /**
     * Registra un producto simple de confitería.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarProductoSimple(ActionEvent event) {
        try {
            String codigo = validarCodigoLibre(VistaUtil.requerido(txtProdCodigo.getText(), "código"));
            Producto producto = new Producto(codigo,
                    VistaUtil.requerido(txtProdNombre.getText(), "nombre"),
                    VistaUtil.leerDecimal(txtProdPrecio.getText(), "precio"),
                    VistaUtil.leerEntero(txtProdPuntos.getText(), "puntos de redención"));
            sesion.getServicioAdmin().registrarItemConfiteria(producto);
            VistaUtil.informar("Producto registrado", producto.nombre() + " quedó en el catálogo.");
            txtProdCodigo.clear();
            txtProdNombre.clear();
            txtProdPrecio.clear();
            txtProdPuntos.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Agrega el ítem seleccionado a la lista del combo que se está armando.
     *
     * @param event evento del botón
     */
    @FXML
    private void onAgregarItemACombo(ActionEvent event) {
        ItemConfiteria item = cmbItemParaCombo.getValue();
        if (item == null) {
            VistaUtil.advertir("Seleccione un producto o combo para agregar");
            return;
        }
        itemsDelCombo.add(item);
        lstItemsDelCombo.getItems().add(item.nombre() + " - " + VistaUtil.moneda(item.precio()));
    }

    /**
     * Crea el combo con los ítems agregados y lo registra en el catálogo.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarCombo(ActionEvent event) {
        try {
            if (itemsDelCombo.isEmpty()) {
                throw new IllegalArgumentException("Agregue al menos un ítem al combo");
            }
            String codigo = validarCodigoLibre(VistaUtil.requerido(txtComboCodigo.getText(), "código del combo"));
            Combo combo = new Combo(codigo,
                    VistaUtil.requerido(txtComboNombre.getText(), "nombre del combo"),
                    VistaUtil.leerDecimal(txtComboDescuento.getText(), "descuento"),
                    VistaUtil.leerEntero(txtComboPuntos.getText(), "puntos de redención"));
            for (ItemConfiteria item : itemsDelCombo) {
                combo.agregar(item);
            }
            sesion.getServicioAdmin().registrarItemConfiteria(combo);
            VistaUtil.informar("Combo registrado", combo.nombre() + " cuesta " + VistaUtil.moneda(combo.precio()) + ".");
            txtComboCodigo.clear();
            txtComboNombre.clear();
            txtComboDescuento.clear();
            txtComboPuntos.clear();
            itemsDelCombo.clear();
            lstItemsDelCombo.getItems().clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Registra una promoción con su tipo, valor, día y periodo de vigencia.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarPromocion(ActionEvent event) {
        try {
            if (cmbTipoPromocion.getValue() == null) {
                throw new IllegalArgumentException("Seleccione el tipo de promoción");
            }
            Promocion promocion = new Promocion(
                    VistaUtil.requerido(txtPromoNombre.getText(), "nombre de la promoción"),
                    cmbTipoPromocion.getValue(),
                    VistaUtil.leerDecimal(txtPromoValor.getText(), "valor"),
                    cmbPromoDiaSemana.getValue(),
                    dpPromoInicio.getValue(),
                    dpPromoFin.getValue());
            sesion.getServicioAdmin().registrarPromocion(promocion);
            VistaUtil.informar("Promoción registrada", promocion.getNombre()
                    + (promocion.estaVigente() ? " está vigente hoy." : " no está vigente hoy."));
            txtPromoNombre.clear();
            txtPromoValor.clear();
            cargarDatos();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Verifica que ningún producto o combo use ya el código.
     *
     * @param codigo código a validar
     * @return el mismo código
     * @throws IllegalArgumentException si el código ya existe
     */
    private String validarCodigoLibre(String codigo) {
        for (ItemConfiteria item : cinema.getProductos()) {
            if (CompraConfiteriaController.codigoDe(item).equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException("Ya existe un ítem con el código " + codigo);
            }
        }
        return codigo;
    }

    /**
     * Recarga el catálogo, la lista de ítems para combos y las promociones.
     */
    private void cargarDatos() {
        tblAdminConfiteria.getItems().setAll(cinema.getProductos());
        cmbItemParaCombo.getItems().setAll(cinema.getProductos());
        tblAdminPromociones.getItems().setAll(cinema.getPromociones());
    }
}
