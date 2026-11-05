package co.uniquindio.edu.poo.controller;

import co.uniquindio.edu.poo.Model.Administrador;
import co.uniquindio.edu.poo.Model.Cliente;
import co.uniquindio.edu.poo.Model.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controlador del registro de personas desde el panel del administrador.
 * Usa {@code ClienteFactory} o {@code AdministradorFactory} según el rol (patrón Factory Method).
 */
public class AdminRegistroUsuariosController {

    @FXML
    private ComboBox<String> cmbAdminTipoUsuario;
    @FXML
    private TextField txtUsuId;
    @FXML
    private TextField txtUsuNombre;
    @FXML
    private TextField txtUsuCorreo;
    @FXML
    private PasswordField txtUsuContrasena;
    @FXML
    private TextField txtUsuTelefonoCliente;
    @FXML
    private TextField txtUsuCargoAdmin;
    @FXML
    private Button btnGuardarNuevoUsuario;
    @FXML
    private TableView<Persona> tblDirectorioPersonas;
    @FXML
    private TableColumn<Persona, String> colPerId;
    @FXML
    private TableColumn<Persona, String> colPerNombre;
    @FXML
    private TableColumn<Persona, String> colPerCorreo;
    @FXML
    private TableColumn<Persona, String> colPerRol;
    @FXML
    private TableColumn<Persona, String> colPerDetalle;

    private final SesionApp sesion = SesionApp.getInstance();

    /**
     * Llena la lista de roles, configura la tabla y carga el directorio de personas.
     */
    @FXML
    private void initialize() {
        cmbAdminTipoUsuario.getItems().setAll(SesionApp.ROL_CLIENTE, SesionApp.ROL_ADMINISTRADOR);
        cmbAdminTipoUsuario.setValue(SesionApp.ROL_CLIENTE);
        onSeleccionarTipoUsuario(null);

        VistaUtil.columna(colPerId, Persona::getId);
        VistaUtil.columna(colPerNombre, Persona::getNombre);
        VistaUtil.columna(colPerCorreo, Persona::getCorreo);
        VistaUtil.columna(colPerRol, p -> p.esAdministrador() ? "ADMINISTRADOR" : "CLIENTE");
        VistaUtil.columna(colPerDetalle, AdminRegistroUsuariosController::detalle);
        cargarDirectorio();
    }

    /**
     * Habilita el campo de teléfono para clientes o el de cargo para administradores.
     *
     * @param event evento de la lista desplegable
     */
    @FXML
    private void onSeleccionarTipoUsuario(ActionEvent event) {
        boolean esCliente = SesionApp.ROL_CLIENTE.equals(cmbAdminTipoUsuario.getValue());
        txtUsuTelefonoCliente.setDisable(!esCliente);
        txtUsuCargoAdmin.setDisable(esCliente);
    }

    /**
     * Registra la persona con la fábrica que corresponde a su rol.
     *
     * @param event evento del botón
     */
    @FXML
    private void onRegistrarUsuarioDesdeAdmin(ActionEvent event) {
        try {
            String rol = cmbAdminTipoUsuario.getValue();
            String datoExtra = SesionApp.ROL_CLIENTE.equals(rol)
                    ? VistaUtil.requerido(txtUsuTelefonoCliente.getText(), "teléfono")
                    : VistaUtil.requerido(txtUsuCargoAdmin.getText(), "cargo");
            Persona persona = sesion.registrarPersona(rol,
                    VistaUtil.requerido(txtUsuId.getText(), "identificación"),
                    VistaUtil.requerido(txtUsuNombre.getText(), "nombre"),
                    VistaUtil.requerido(txtUsuCorreo.getText(), "correo"),
                    VistaUtil.requerido(txtUsuContrasena.getText(), "contraseña"),
                    datoExtra);
            VistaUtil.informar("Persona registrada", persona.getNombre() + " quedó registrada como " + rol + ".");
            txtUsuId.clear();
            txtUsuNombre.clear();
            txtUsuCorreo.clear();
            txtUsuContrasena.clear();
            txtUsuTelefonoCliente.clear();
            txtUsuCargoAdmin.clear();
            cargarDirectorio();
        } catch (Exception e) {
            VistaUtil.error(e);
        }
    }

    /**
     * Carga todas las personas registradas en el cine.
     */
    private void cargarDirectorio() {
        tblDirectorioPersonas.getItems().setAll(sesion.getCinema().getUsuarios());
    }

    /**
     * Obtiene el teléfono de un cliente o el cargo de un administrador.
     *
     * @param persona persona del directorio
     * @return dato propio de su rol
     */
    private static String detalle(Persona persona) {
        if (persona instanceof Cliente cliente) {
            return cliente.getTelefono();
        }
        if (persona instanceof Administrador administrador) {
            return administrador.getCargo();
        }
        return "";
    }
}
