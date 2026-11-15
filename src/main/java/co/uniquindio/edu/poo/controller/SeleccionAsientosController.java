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

public class SeleccionAsientosController {

    @FXML private Label lblHeaderPelicula;
    @FXML private Label lblHeaderFecha;
    @FXML private Label lblHeaderHora;
    @FXML private Label lblHeaderSala;
    @FXML private GridPane gridMapaAsientos;
    @FXML private Label lblSideTituloPelicula;
    @FXML private Label lblSideDetallesFuncion;
    @FXML private FlowPane flowAsientosSeleccionados;
    @FXML private Label lblCantidadBoletas;
    @FXML private Label lblPrecioUnitario;
    @FXML private Label lblSubtotalEntradas;
    @FXML private Button btnIrCompraConfiteria;
    @FXML private Button btnContinuarPago;
    @FXML private Button btnVolverCartelera;
    @FXML private Label lblPuntosEstimados;

    private final SesionApp sesion = SesionApp.getInstance();
    private Funcion funcion;

    @FXML
    private void initialize() {
        funcion = sesion.getFuncionSeleccionada();
        if (funcion == null) {
            lblHeaderPelicula.setText("Seleccione una función en la cartelera");
            btnContinuarPago.setDisable(true);
            btnIrCompraConfiteria.setDisable(true);
            return;
        }
        String titulo = funcion.getPelicula().getTitulo();
        String hora = funcion.getFechaHora().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
        lblHeaderPelicula.setText(titulo);
        lblHeaderFecha.setText(VistaUtil.fecha(funcion.getFechaHora().toLocalDate()));
        lblHeaderHora.setText(hora);
        lblHeaderSala.setText("Sala " + funcion.getSala().getNumero());
        lblSideTituloPelicula.setText(titulo);
        lblSideDetallesFuncion.setText("Sala " + funcion.getSala().getNumero() + " · " + hora);
        lblPrecioUnitario.setText(VistaUtil.moneda(funcion.getPrecioBase()));
        dibujarMapa();
        actualizarResumen();
    }

    @FXML
    private void onIrACompraConfiteria(ActionEvent event) {
        sesion.getDashboardCliente().mostrar("CompraConfiteriaView");
    }

    @FXML
    private void onContinuarAlPago(ActionEvent event) {
        if (sesion.getAsientosSeleccionados().isEmpty() && sesion.getEntradasRedimidas().isEmpty()) {
            VistaUtil.advertir("Seleccione al menos un asiento para continuar");
            return;
        }
        sesion.getDashboardCliente().mostrar("CheckoutPagoView");
    }

    @FXML
    private void onVolverACartelera(ActionEvent event) {
        sesion.getDashboardCliente().mostrarCartelera();
    }

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
            etiqueta.getStyleClass().add("row-indicator");
            gridMapaAsientos.add(etiqueta, 0, i);
        }
    }

    private Button crearBoton(Asiento asiento) {
        Button boton = new Button(asiento.codigo());
        boton.getStyleClass().add("seat-btn");
        boolean ocupado = funcion.getAsientosOcupados().contains(asiento);
        if (ocupado) {
            boton.getStyleClass().add("seat-occupied");
            boton.setDisable(true);
        } else {
            aplicarClaseLibre(asiento, boton);
            boton.setOnAction(e -> alternarSeleccion(asiento, boton));
        }
        return boton;
    }

    private void alternarSeleccion(Asiento asiento, Button boton) {
        List<Asiento> seleccionados = sesion.getAsientosSeleccionados();
        if (seleccionados.contains(asiento)) {
            seleccionados.remove(asiento);
        } else {
            seleccionados.add(asiento);
        }
        aplicarClaseLibre(asiento, boton);
        actualizarResumen();
    }

    private void aplicarClaseLibre(Asiento asiento, Button boton) {
        boton.getStyleClass().removeAll("seat-available", "seat-preferencial", "seat-accessible", "seat-selected");
        if (sesion.getAsientosSeleccionados().contains(asiento)) {
            boton.getStyleClass().add("seat-selected");
        } else if (asiento.getTipo() == TipoAsiento.ACCESIBLE) {
            boton.getStyleClass().add("seat-accessible");
        } else if (asiento.getTipo() == TipoAsiento.PREFERENCIAL) {
            boton.getStyleClass().add("seat-preferencial");
        } else {
            boton.getStyleClass().add("seat-available");
        }
    }

    private void actualizarResumen() {
        List<Asiento> seleccionados = sesion.getAsientosSeleccionados();
        flowAsientosSeleccionados.getChildren().clear();
        for (Asiento asiento : seleccionados) {
            Label etiqueta = new Label(asiento.codigo());
            etiqueta.getStyleClass().add("seat-chip");
            flowAsientosSeleccionados.getChildren().add(etiqueta);
        }
        int cantidad = seleccionados.size();
        lblCantidadBoletas.setText(String.valueOf(cantidad));
        lblSubtotalEntradas.setText(VistaUtil.moneda(cantidad * funcion.getPrecioBase()));
        lblPuntosEstimados.setText((cantidad * SesionApp.PUNTOS_POR_ENTRADA) + " pts");
    }
}