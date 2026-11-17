package co.uniquindio.edu.poo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Carga las vistas FXML y cambia lo que se muestra en la ventana.
 * Todas las vistas están en la carpeta de recursos indicada en {@link #RUTA_VISTAS}.
 */
public final class Navegador {

    /** Carpeta de recursos donde están los archivos .fxml. */
    public static final String RUTA_VISTAS = "/View/";

    /**
     * Evita que se creen objetos de esta clase de utilidades.
     */
    private Navegador() {
    }

    /**
     * Carga una vista FXML.
     *
     * @param vista nombre del archivo sin la extensión, por ejemplo {@code "LoginRegistroView"}
     * @return cargador con la vista ya cargada; su controlador se obtiene con {@code getController()}
     * @throws IllegalStateException si la vista no existe o tiene errores
     */
    public static FXMLLoader cargar(String vista) {
        URL url = Navegador.class.getResource(RUTA_VISTAS + vista + ".fxml");
        if (url == null) {
            throw new IllegalStateException("No se encontró la vista " + RUTA_VISTAS + vista + ".fxml");
        }
        FXMLLoader loader = new FXMLLoader(url);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista " + vista + ": " + e.getMessage(), e);
        }
        return loader;
    }

    /**
     * Reemplaza toda la ventana por otra vista (por ejemplo, del login al panel del cliente).
     *
     * @param origen cualquier nodo de la ventana actual
     * @param vista  nombre de la vista a mostrar
     * @param titulo título de la ventana
     * @param <T>    tipo del controlador de la vista
     * @return controlador de la nueva vista
     */
    public static <T> T cambiarVentana(Node origen, String vista, String titulo) {
        FXMLLoader loader = cargar(vista);
        Stage stage = (Stage) origen.getScene().getWindow();
        stage.setScene(new Scene((Parent) loader.getRoot()));
        stage.setTitle(titulo);
        stage.centerOnScreen();
        return loader.getController();
    }

    /**
     * Muestra una vista en el centro de un panel principal, sin cambiar el menú ni el encabezado.
     *
     * @param contenedor panel principal (BorderPane) del cliente o del administrador
     * @param vista      nombre de la vista a mostrar
     * @param <T>        tipo del controlador de la vista
     * @return controlador de la vista mostrada
     */
    public static <T> T mostrarEnCentro(BorderPane contenedor, String vista) {
        FXMLLoader loader = cargar(vista);
        contenedor.setCenter(loader.getRoot());
        return loader.getController();
    }
}
