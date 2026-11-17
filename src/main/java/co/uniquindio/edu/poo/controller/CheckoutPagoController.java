package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Asiento;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Compra;
import co.uniquindio.edu.poo.Model.DetalleConfiteria;
import co.uniquindio.edu.poo.Model.Entrada;
import co.uniquindio.edu.poo.Model.Funcion;
import co.uniquindio.edu.poo.Model.Promocion;
import co.uniquindio.edu.poo.Model.TarjetaVirtual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del resumen y pago de la compra. Arma la compra con el Builder,
 * aplica promociones (patrón Decorator), redime entradas con puntos y paga con la tarjeta
 * virtual a través de {@code CompraFacade}.
 */
public class CheckoutPagoController {

    @FXML
    private Label lblIdCompra;
    @FXML
    private Label lblEstadoCompra;
    @FXML
    private Label lblResumenFuncion;
    @FXML
    private Label lblResumenAsientos;
    @FXML
    private ComboBox<Promocion> cmbPromocionesVigentes;
    @FXML
    private Button btnAplicarPromocion;
    @FXML
    private TextArea txtDescripcionDecorador;
    @FXML
    private Button btnRedimirEntradaPuntos;
    @FXML
    private Label lblEstadoTarjeta;
    @FXML
    private Label lblNumeroTarjeta;
    @FXML
    private Label lblSaldoDisponiblePago;
    @FXML
    private Label lblSubtotalOrden;
    @FXML
    private Label lblPuntosAGanar;
    @FXML
    private Label lblTotalAPagar;
    @FXML
    private Button btnPagarCompra;
    @FXML
    private Button btnSolicitarRecargaEmergencia;
    @FXML
    private Button btnCancelarOrdenPendiente;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Carga las promociones vigentes y muestra el resumen de la compra en curso.
     */
    @FXML
    private void initialize() {
        cmbPromocionesVigentes.setConverter(new StringConverter<>() {
            @Override
            public String toString(Promocion promocion) {
                return promocion == null ? "" : promocion.getNombre() + " (" + promocion.getTipo() + ")";
            }

            @Override
            public Promocion fromString(String texto) {
                return null;
            }
        });
        List<Promocion> vigentes = new ArrayList<>();
        for (Promocion promocion : sesion.getCinema().getPromociones()) {
            if (promocion.estaVigente()) {
                vigentes.add(promocion);
            }
        }
        cmbPromocionesVigentes.getItems().setAll(vigentes);
        refrescar();
    }

    /**
     * Aplica la promoción seleccionada; se pueden acumular varias promociones distintas.
     *
     * @param event evento del botón
     */
    @FXML
    private void onAplicarPromocion(ActionEvent event) {
        Promocion promocion = cmbPromocionesVigentes.getValue();
        if (promocion == null) {
            VistaUtil.advertir("Seleccione una promoción vigente");
            return;
        }
        try {
            sesion.aplicarPromocion(promocion);
            refrescar();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Redime con puntos la entrada del último asiento seleccionado.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRedimirEntrada(ActionEvent event) {
        if (!VistaUtil.confirmar("¿Redimir una entrada por " + SesionApp.PUNTOS_POR_ENTRADA
                + " puntos? Los puntos se descuentan de inmediato.")) {
            return;
        }
        try {
            Entrada entrada = sesion.redimirEntrada();
            sesion.getDashboardCliente().actualizarEncabezado();
            VistaUtil.informar("Entrada redimida", "La entrada del asiento " + entrada.getAsiento().codigo()
                    + " quedó pagada con puntos.");
            refrescar();
        } catch (RuntimeException e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Paga la compra con la tarjeta virtual y muestra el historial de compras.
     *
     * @param event evento del botón
     */
    @FXML
    private void onPagarConTarjeta(ActionEvent event) {
        try {
            Compra compra = sesion.construirCompra();
            if (!VistaUtil.confirmar("¿Confirmar el pago de " + VistaUtil.moneda(compra.getTotal())
                    + " con su tarjeta virtual?")) {
                return;
            }
            Compra pagada = sesion.pagarCompra();
            VistaUtil.informar("Compra exitosa", "Compra " + pagada.getId() + " pagada por "
                    + VistaUtil.moneda(pagada.getTotal()) + ". Ganó " + pagada.getPuntosGanados() + " puntos.");
            sesion.getDashboardCliente().mostrar("HistorialComprasClienteView");
        } catch (RuntimeException e) {
            VistaUtil.error(e);
            refrescar();
        }
    }

    /**
     * Abre el monedero para que el cliente solicite una recarga. La compra en curso se conserva.
     *
     * @param event evento del botón
     */
    @FXML
    private void onIrASolicitarRecarga(ActionEvent event) {
        sesion.getDashboardCliente().mostrar("MonederoPuntosClienteView");
    }

    /**
     * Cancela la orden pendiente: vacía el carrito y vuelve a la cartelera.
     *
     * @param event evento del botón
     */
    @FXML
    private void onCancelarCompraPendiente(ActionEvent event) {
        if (!sesion.getEntradasRedimidas().isEmpty()) {
            VistaUtil.advertir("Ya redimió entradas con puntos en esta orden. Complete el pago para conservarlas.");
            return;
        }
        if (VistaUtil.confirmar("¿Cancelar la orden? Se quitarán los asientos y productos seleccionados.")) {
            sesion.limpiarCarrito();
            sesion.getDashboardCliente().mostrarCartelera();
        }
    }

    /**
     * Vuelve a armar la compra con los datos de la sesión y muestra sus valores.
     */
    private void refrescar() {
        Cliente cliente = sesion.getClienteActual();
        TarjetaVirtual tarjeta = cliente.getTarjeta();
        lblNumeroTarjeta.setText(tarjeta.getNumero());
        lblEstadoTarjeta.setText(tarjeta.isActiva() ? "ACTIVA" : "INACTIVA");
        lblSaldoDisponiblePago.setText(VistaUtil.moneda(tarjeta.getSaldo()));
        lblIdCompra.setText(sesion.getIdCompraEnCurso());

        Funcion funcion = sesion.getFuncionSeleccionada();
        lblResumenFuncion.setText(funcion == null ? "Solo confitería"
                : funcion.getPelicula().getTitulo() + " - Sala " + funcion.getSala().getNumero()
                + " - " + VistaUtil.fechaHora(funcion.getFechaHora()));
        lblResumenAsientos.setText(textoAsientos());
        btnRedimirEntradaPuntos.setDisable(sesion.getAsientosSeleccionados().isEmpty());

        if (!sesion.carritoTieneConceptos()) {
            lblEstadoCompra.setText("SIN PRODUCTOS");
            lblSubtotalOrden.setText(VistaUtil.moneda(0));
            lblTotalAPagar.setText(VistaUtil.moneda(0));
            lblPuntosAGanar.setText("0 pts");
            txtDescripcionDecorador.setText("Agregue asientos o productos para armar la compra.");
            btnPagarCompra.setDisable(true);
            return;
        }
        try {
            Compra compra = sesion.construirCompra();
            lblEstadoCompra.setText(compra.getEstado().nombre());
            lblSubtotalOrden.setText(VistaUtil.moneda(compra.getSubtotal()));
            lblTotalAPagar.setText(VistaUtil.moneda(compra.getTotal()));
            lblPuntosAGanar.setText((compra.getEntradas().size() * SesionApp.PUNTOS_POR_ENTRADA) + " pts");
            txtDescripcionDecorador.setText(describir(compra));
            btnPagarCompra.setDisable(false);
        } catch (RuntimeException e) {
            txtDescripcionDecorador.setText(e.getMessage());
            btnPagarCompra.setDisable(true);
        }
    }

    /**
     * Arma el texto con los asientos a pagar y los redimidos con puntos.
     *
     * @return texto de los asientos
     */
    private String textoAsientos() {
        List<String> codigos = new ArrayList<>();
        for (Asiento asiento : sesion.getAsientosSeleccionados()) {
            codigos.add(asiento.codigo());
        }
        for (Entrada entrada : sesion.getEntradasRedimidas()) {
            codigos.add(entrada.getAsiento().codigo() + " (puntos)");
        }
        return codigos.isEmpty() ? "Ninguno" : String.join(", ", codigos);
    }

    /**
     * Describe los conceptos de la compra y la cadena de decoradores del precio.
     *
     * @param compra compra armada
     * @return texto del detalle
     */
    private String describir(Compra compra) {
        StringBuilder sb = new StringBuilder();
        for (Entrada entrada : compra.getEntradas()) {
            sb.append("Entrada ").append(entrada.getAsiento().codigo()).append(": ")
                    .append(VistaUtil.moneda(entrada.getPrecio())).append('\n');
        }
        for (DetalleConfiteria detalle : compra.getDetallesConfiteria()) {
            sb.append(detalle.getItem().nombre()).append(" x").append(detalle.getCantidad()).append(": ")
                    .append(VistaUtil.moneda(detalle.subtotal())).append('\n');
        }
        sb.append('\n').append("Precio: ").append(compra.getPrecio().descripcion());
        if (sesion.getPromocionesAplicadas().isEmpty()) {
            sb.append("\nSin promociones aplicadas.");
        }
        return sb.toString();
    }
}
