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

/**
 * Utilidades comunes de los controladores: mensajes, formatos y columnas de tablas.
 */
public final class VistaUtil {

    private static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Evita que se creen objetos de esta clase de utilidades.
     */
    private VistaUtil() {
    }

    /**
     * Configura una columna de tabla para que muestre el texto que devuelve la función.
     *
     * @param columna columna a configurar
     * @param valor   función que obtiene el texto a partir del objeto de la fila
     * @param <T>     tipo de los objetos de la tabla
     */
    public static <T> void columna(TableColumn<T, String> columna, Function<T, String> valor) {
        columna.setCellValueFactory(celda -> new SimpleStringProperty(valor.apply(celda.getValue())));
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param titulo  título de la ventana
     * @param mensaje texto del mensaje
     */
    public static void informar(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    /**
     * Muestra un mensaje de advertencia, por ejemplo cuando falta un dato.
     *
     * @param mensaje texto del mensaje
     */
    public static void advertir(String mensaje) {
        mostrar(Alert.AlertType.WARNING, "Atención", mensaje);
    }

    /**
     * Muestra el mensaje de una excepción lanzada por el modelo.
     *
     * @param e excepción atrapada en el controlador
     */
    public static void error(Exception e) {
        String mensaje = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        mostrar(Alert.AlertType.ERROR, "No se pudo completar la operación", mensaje);
    }

    /**
     * Pide confirmación al usuario.
     *
     * @param mensaje pregunta a mostrar
     * @return {@code true} si el usuario aceptó
     */
    public static boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.OK, ButtonType.CANCEL);
        alerta.setTitle("Confirmar");
        alerta.setHeaderText(null);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    /**
     * Da formato de moneda colombiana, por ejemplo {@code $ 12.500}.
     *
     * @param valor valor a formatear
     * @return texto con el valor
     */
    public static String moneda(double valor) {
        return String.format(COLOMBIA, "$ %,.0f", valor);
    }

    /**
     * Da formato a una fecha, por ejemplo {@code 05/11/2026}.
     *
     * @param fecha fecha a formatear
     * @return texto con la fecha, o vacío si es {@code null}
     */
    public static String fecha(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FECHA);
    }

    /**
     * Da formato a una fecha con hora, por ejemplo {@code 05/11/2026 19:30}.
     *
     * @param fechaHora fecha y hora a formatear
     * @return texto con la fecha y hora, o vacío si es {@code null}
     */
    public static String fechaHora(LocalDateTime fechaHora) {
        return fechaHora == null ? "" : fechaHora.format(FECHA_HORA);
    }

    /**
     * Convierte el texto de un campo en número decimal.
     *
     * @param texto texto escrito por el usuario
     * @param campo nombre del campo, para el mensaje de error
     * @return número leído
     * @throws IllegalArgumentException si el texto está vacío o no es un número
     */
    public static double leerDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(requerido(texto, campo).replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número");
        }
    }

    /**
     * Convierte el texto de un campo en número entero.
     *
     * @param texto texto escrito por el usuario
     * @param campo nombre del campo, para el mensaje de error
     * @return número leído
     * @throws IllegalArgumentException si el texto está vacío o no es un número entero
     */
    public static int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(requerido(texto, campo));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero");
        }
    }

    /**
     * Verifica que un campo de texto no esté vacío.
     *
     * @param texto texto escrito por el usuario
     * @param campo nombre del campo, para el mensaje de error
     * @return texto sin espacios al inicio ni al final
     * @throws IllegalArgumentException si el texto está vacío
     */
    public static String requerido(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
        return texto.trim();
    }

    /**
     * Muestra una alerta del tipo indicado.
     *
     * @param tipo    tipo de alerta
     * @param titulo  título de la ventana
     * @param mensaje texto del mensaje
     */
    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
