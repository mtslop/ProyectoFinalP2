package co.uniquindio.edu.poo;

import co.uniquindio.edu.poo.controller.Navegador;
import co.uniquindio.edu.poo.controller.SesionApp;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación CinemaUQ. Carga los datos iniciales y abre el inicio de sesión.
 */
public class CinemaApp extends Application {

    /**
     * Abre la ventana principal con la vista de inicio de sesión.
     *
     * @param stage ventana principal de JavaFX
     */
    @Override
    public void start(Stage stage) {
        SesionApp.getInstance();
        Parent raiz = Navegador.cargar("LoginRegistroView").getRoot();
        stage.setTitle("CinemaUQ");
        stage.setScene(new Scene(raiz));
        stage.show();
    }

    /**
     * Inicia la aplicación JavaFX.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
