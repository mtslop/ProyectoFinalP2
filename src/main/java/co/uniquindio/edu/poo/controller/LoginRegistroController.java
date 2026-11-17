package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador de la vista de inicio de sesión y registro de personas.
 */
public class LoginRegistroController {

    @FXML
    private TextField txtLoginCorreo;
    @FXML
    private PasswordField txtLoginContrasena;
    @FXML
    private Label lblMensajeLogin;
    @FXML
    private Button btnIniciarSesion;

    @FXML
    private ComboBox<String> cmbTipoPersonaRegistro;
    @FXML
    private TextField txtRegistroId;
    @FXML
    private TextField txtRegistroNombre;
    @FXML
    private TextField txtRegistroCorreo;
    @FXML
    private TextField txtRegistroTelefono;
    @FXML
    private TextField txtRegistroCargoAdmin;
    @FXML
    private PasswordField txtRegistroContrasena;
    @FXML
    private Label lblMensajeRegistro;
    @FXML
    private Button btnRegistrarPersona;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Llena la lista de tipos de persona y deja el formulario listo para un cliente.
     */
    @FXML
    private void initialize() {
        cmbTipoPersonaRegistro.getItems().setAll(SesionApp.ROL_CLIENTE, SesionApp.ROL_ADMINISTRADOR);
        cmbTipoPersonaRegistro.setValue(SesionApp.ROL_CLIENTE);
        onCambiarTipoPersona(null);
    }

    /**
     * Valida el correo y la contraseña y abre el panel del cliente o del administrador.
     *
     * @param event evento del botón
     */
    @FXML
    private void onIniciarSesion(ActionEvent event) {
        try {
            String correo = VistaUtil.requerido(txtLoginCorreo.getText(), "correo");
            String contrasena = VistaUtil.requerido(txtLoginContrasena.getText(), "contraseña");
            Persona persona = sesion.getServicioAutenticacion().iniciarSesion(correo, contrasena);
            sesion.iniciarSesion(persona);
            if (persona.esAdministrador()) {
                Navegador.cambiarVentana(btnIniciarSesion, "AdminDashboardReportesView", "CinemaUQ - Administración");
            } else {
                Navegador.cambiarVentana(btnIniciarSesion, "ClienteDashBoardView", "CinemaUQ - " + persona.getNombre());
            }
        } catch (Exception e) {
            lblMensajeLogin.setText(e.getMessage());
        }
    }

    /**
     * Habilita el campo de teléfono para clientes o el de cargo para administradores.
     *
     * @param event evento de la lista desplegable
     */
    @FXML
    private void onCambiarTipoPersona(ActionEvent event) {
        boolean esCliente = SesionApp.ROL_CLIENTE.equals(cmbTipoPersonaRegistro.getValue());
        txtRegistroTelefono.setDisable(!esCliente);
        txtRegistroCargoAdmin.setDisable(esCliente);
    }

    /**
     * Registra un cliente o un administrador con su fábrica correspondiente.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarPersona(ActionEvent event) {
        try {
            String rol = cmbTipoPersonaRegistro.getValue();
            boolean esCliente = SesionApp.ROL_CLIENTE.equals(rol);
            String datoExtra = esCliente
                    ? VistaUtil.requerido(txtRegistroTelefono.getText(), "teléfono")
                    : VistaUtil.requerido(txtRegistroCargoAdmin.getText(), "cargo");
            Persona persona = sesion.registrarPersona(rol,
                    VistaUtil.requerido(txtRegistroId.getText(), "identificación"),
                    VistaUtil.requerido(txtRegistroNombre.getText(), "nombre"),
                    VistaUtil.requerido(txtRegistroCorreo.getText(), "correo"),
                    VistaUtil.requerido(txtRegistroContrasena.getText(), "contraseña"),
                    datoExtra);
            lblMensajeRegistro.setText("Registro exitoso de " + persona.getNombre() + ". Ya puede iniciar sesión.");
            limpiarRegistro();
        } catch (Exception e) {
            lblMensajeRegistro.setText(e.getMessage());
        }
    }

    /**
     * Borra los campos del formulario de registro.
     */
    private void limpiarRegistro() {
        txtRegistroId.clear();
        txtRegistroNombre.clear();
        txtRegistroCorreo.clear();
        txtRegistroTelefono.clear();
        txtRegistroCargoAdmin.clear();
        txtRegistroContrasena.clear();
    }
}
