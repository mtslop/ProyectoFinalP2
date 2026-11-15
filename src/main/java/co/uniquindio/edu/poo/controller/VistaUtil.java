package co.uniquindio.edu.poo.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

public final class VistaUtil {

    private static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private VistaUtil() {
    }

    public static <T> void columna(TableColumn<T, String> columna, Function<T, String> valor) {
        columna.setCellValueFactory(celda -> new SimpleStringProperty(valor.apply(celda.getValue())));
    }

    public static void informar(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    public static void advertir(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "Atención", mensaje);
    }

    public static void error(Exception e) {
        String mensaje = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        mostrar(Alert.AlertType.ERROR, "No se pudo completar la operación", mensaje);
    }

    public static boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK, ButtonType.CANCEL);
        alerta.setTitle("Confirmar");
        alerta.setHeaderText(null);
        Navegador.aplicarTema(alerta.getDialogPane());
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    public static String moneda(double valor) {
        return String.format(COLOMBIA, "$ %,.0f", valor);
    }

    public static String fecha(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FECHA);
    }

    public static String fechaHora(LocalDateTime fechaHora) {
        return fechaHora == null ? "" : fechaHora.format(FECHA_HORA);
    }

    public static double leerDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(requerido(texto, campo).replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número");
        }
    }

    public static int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(requerido(texto, campo));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero");
        }
    }

    public static String requerido(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
        return texto.trim();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        Navegador.aplicarTema(alerta.getDialogPane());
        alerta.showAndWait();
    }
}