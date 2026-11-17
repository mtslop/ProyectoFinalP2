package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Asiento;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.TipoAsiento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controlador del mapa de asientos de una función. Dibuja un botón por asiento de la sala,
 * marca los ocupados y guarda en la sesión los que el cliente selecciona.
 */
public class SeleccionAsientosController {

    private static final String ESTILO_BASE = "-fx-min-width: 44; -fx-min-height: 34; -fx-font-size: 11px; "
            + "-fx-background-radius: 4; -fx-border-radius: 4; -fx-cursor: hand; ";
    private static final String ESTILO_DISPONIBLE = ESTILO_BASE
            + "-fx-background-color: #0D0D0D; -fx-text-fill: #EFE5D2; -fx-border-color: #EFE5D2;";
    private static final String ESTILO_ACCESIBLE = ESTILO_BASE
            + "-fx-background-color: #362822; -fx-text-fill: #EFE5D2; -fx-border-color: #C28D39; -fx-border-width: 2;";
    private static final String ESTILO_SELECCIONADO = ESTILO_BASE
            + "-fx-background-color: #C28D39; -fx-text-fill: #0D0D0D; -fx-font-weight: bold;";
    private static final String ESTILO_OCUPADO = ESTILO_BASE
            + "-fx-background-color: #710912; -fx-text-fill: #EFE5D2; -fx-opacity: 0.8;";
    private static final String ESTILO_ETIQUETA = ESTILO_BASE
            + "-fx-background-color: #C28D39; -fx-text-fill: #0D0D0D; -fx-font-weight: bold; -fx-padding: 4 10;";

    @FXML
    private Label lblHeaderPelicula;
    @FXML
    private Label lblHeaderFecha;
    @FXML
    private Label lblHeaderHora;
    @FXML
    private Label lblHeaderSala;
    @FXML
    private GridPane gridMapaAsientos;
    @FXML
    private Label lblSideTituloPelicula;
    @FXML
    private Label lblSideDetallesFuncion;
    @FXML
    private FlowPane flowAsientosSeleccionados;
    @FXML
    private Label lblCantidadBoletas;
    @FXML
    private Label lblPrecioUnitario;
    @FXML
    private Label lblSubtotalEntradas;
    @FXML
    private Button btnIrCompraConfiteria;
    @FXML
    private Button btnContinuarPago;
    @FXML
    private Button btnVolverCartelera;
    @FXML
    private Label lblPuntosEstimados;

    private final SesionApp sesion = SesionApp.getInstance();
    private Funcion funcion;

    /**
     * Muestra los datos de la función seleccionada y dibuja su mapa de asientos.
     */
    @FXML
    private void initialize() {
        funcion = sesion.getFuncionSeleccionada();
        if (funcion == null) {
            lblHeaderPelicula.setText("Pelicula: seleccione una función en la cartelera");
            btnContinuarPago.setDisable(true);
            btnIrCompraConfiteria.setDisable(true);
            return;
        }
        String titulo = funcion.getPelicula().getTitulo();
        String hora = funcion.getFechaHora().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
        lblHeaderPelicula.setText("Pelicula: " + titulo);
        lblHeaderFecha.setText("Fecha: " + VistaUtil.fecha(funcion.getFechaHora().toLocalDate()));
        lblHeaderHora.setText("Hora: " + hora);
        lblHeaderSala.setText("Sala: " + funcion.getSala().getNumero());
        lblSideTituloPelicula.setText(titulo);
        lblSideDetallesFuncion.setText("Sala " + funcion.getSala().getNumero() + " - " + hora);
        lblPrecioUnitario.setText(VistaUtil.moneda(funcion.getPrecioBase()));
        dibujarMapa();
        actualizarResumen();
    }

    /**
     * Abre la confitería sin perder los asientos seleccionados.
     *
     * @param event evento del botón
     */
    @FXML
    private void onIrACompraConfiteria(ActionEvent event) {
        sesion.getDashboardCliente().mostrar("CompraConfiteriaView");
    }

    /**
     * Continúa al resumen y pago si hay al menos un asiento seleccionado.
     *
     * @param event evento del botón
     */
    @FXML
    private void onContinuarAlPago(ActionEvent event) {
        if (sesion.getAsientosSeleccionados().isEmpty() && sesion.getEntradasRedimidas().isEmpty()) {
            VistaUtil.advertir("Seleccione al menos un asiento para continuar");
            return;
        }
        sesion.getDashboardCliente().mostrar("CheckoutPagoView");
    }

    /**
     * Vuelve a la cartelera. Los asientos seleccionados se conservan mientras no se cambie de función.
     *
     * @param event evento del botón
     */
    @FXML
    private void onVolverACartelera(ActionEvent event) {
        sesion.getDashboardCliente().mostrarCartelera();
    }

    /**
     * Crea un botón por asiento, organizado por filas (A, B, C...) y número de asiento.
     */
    private void dibujarMapa() {
        gridMapaAsientos.getChildren().clear();
        List<String> filas = new ArrayList<>();
        for (Asiento asiento : funcion.getSala().getAsientos()) {
            if (!filas.contains(asiento.getFila())) {
                filas.add(asiento.getFila());
            }
            int fila = filas.indexOf(asiento.getFila());
            gridMapaAsientos.add(crearBoton(asiento), asiento.getNumero(), fila);
        }
        for (int i = 0; i < filas.size(); i++) {
            Label etiqueta = new Label(filas.get(i));
            etiqueta.setStyle("-fx-text-fill: #C28D39; -fx-font-weight: bold; -fx-font-size: 14px;");
            gridMapaAsientos.add(etiqueta, 0, i);
        }
    }

    /**
     * Crea el botón de un asiento con el color de su estado.
     *
     * @param asiento asiento de la sala
     * @return botón del asiento
     */
    private Button crearBoton(Asiento asiento) {
        Button boton = new Button(asiento.codigo());
        boolean ocupado = funcion.getAsientosOcupados().contains(asiento);
        if (ocupado) {
            boton.setStyle(ESTILO_OCUPADO);
            boton.setDisable(true);
        } else {
            boton.setStyle(estiloLibre(asiento));
            boton.setOnAction(e -> alternarSeleccion(asiento, boton));
        }
        return boton;
    }

    /**
     * Selecciona o quita la selección de un asiento libre.
     *
     * @param asiento asiento pulsado
     * @param boton   botón del asiento
     */
    private void alternarSeleccion(Asiento asiento, Button boton) {
        List<Asiento> seleccionados = sesion.getAsientosSeleccionados();
        if (seleccionados.contains(asiento)) {
            seleccionados.remove(asiento);
        } else {
            seleccionados.add(asiento);
        }
        boton.setStyle(estiloLibre(asiento));
        actualizarResumen();
    }

    /**
     * Obtiene el estilo de un asiento libre según si está seleccionado o es accesible.
     *
     * @param asiento asiento libre
     * @return estilo CSS del botón
     */
    private String estiloLibre(Asiento asiento) {
        if (sesion.getAsientosSeleccionados().contains(asiento)) {
            return ESTILO_SELECCIONADO;
        }
        return asiento.getTipo() == TipoAsiento.ACCESIBLE ? ESTILO_ACCESIBLE : ESTILO_DISPONIBLE;
    }

    /**
     * Actualiza la lista de asientos seleccionados, la cantidad de boletas, el subtotal y los puntos.
     */
    private void actualizarResumen() {
        List<Asiento> seleccionados = sesion.getAsientosSeleccionados();
        flowAsientosSeleccionados.getChildren().clear();
        for (Asiento asiento : seleccionados) {
            Label etiqueta = new Label(asiento.codigo());
            etiqueta.setStyle(ESTILO_ETIQUETA);
            flowAsientosSeleccionados.getChildren().add(etiqueta);
        }
        int cantidad = seleccionados.size();
        lblCantidadBoletas.setText(String.valueOf(cantidad));
        lblSubtotalEntradas.setText(VistaUtil.moneda(cantidad * funcion.getPrecioBase()));
        lblPuntosEstimados.setText((cantidad * SesionApp.PUNTOS_POR_ENTRADA) + " pts");
    }
}
