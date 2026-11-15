package co.uniquindio.edu.poo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class Navegador {

    public static final String RUTA_VISTAS = "/View/";
    public static final String RUTA_CSS = "/styles/cinemauq.css";

    private Navegador() {
    }

    public static FXMLLoader cargar(String vista) {
        URL url = Navegador.class.getResource(RUTA_VISTAS + vista + ".fxml");
        if (url == null) {
            throw new IllegalStateException("No se encontró la vista " + RUTA_VISTAS + vista + ".fxml");
        }
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Parent root = loader.load();
            aplicarTema(root);
            return loader;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista " + vista + ": " + e.getMessage(), e);
        }
    }

    public static void aplicarTema(Parent root) {
        URL css = Navegador.class.getResource(RUTA_CSS);
        if (css != null) {
            String uri = css.toExternalForm();
            if (!root.getStylesheets().contains(uri)) {
                root.getStylesheets().add(uri);
            }
        }
    }

    public static <T> T cambiarVentana(Node origen, String vista, String titulo) {
        FXMLLoader loader = cargar(vista);
        Stage stage = (Stage) origen.getScene().getWindow();
        stage.setScene(new Scene((Parent) loader.getRoot()));
        stage.setTitle(titulo);
        stage.centerOnScreen();
        return loader.getController();
    }

    public static <T> T mostrarEnCentro(BorderPane contenedor, String vista) {
        FXMLLoader loader = cargar(vista);
        contenedor.setCenter(loader.getRoot());
        return loader.getController();
    }
}