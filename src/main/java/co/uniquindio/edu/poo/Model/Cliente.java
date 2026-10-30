package co.uniquindio.edu.poo.Model;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona implements IClienteObserver {

    private String telefono;
    private TarjetaVirtual tarjeta;
    private CuentaPuntos cuentaPuntos;
    private List<Compra> historialCompras;
    private List<String> notificaciones;

    public Cliente(String id, String nombre, String correo, String contrasena, String telefono) {
        super(id, nombre, correo, contrasena);
        this.telefono = telefono;
        this.historialCompras = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        // Se inicializan sus componentes propios (Composicion en el diagrama)
        this.tarjeta = new TarjetaVirtual("TV-" + id);
        this.cuentaPuntos = new CuentaPuntos();
    }

    /**
     * Agrega una nueva compra al historial del cliente.
     *
     * @param compra Objeto de tipo Compra a registrar.
     */
    public void agregarCompra(Compra compra) {
        if (compra != null) {
            this.historialCompras.add(compra);
        }
    }

    /**
     * Indica si este usuario es administrador.
     *
     * @return false, ya que es un cliente.
     */
    @Override
    public boolean esAdministrador() {
        return false;
    }

    /**
     * Metodo del patron Observer que recibe un mensaje de notificacion y lo guarda.
     *
     * @param mensaje Texto de la notificacion enviada por el cine.
     */
    @Override
    public void actualizar(String mensaje) {
        if (mensaje != null && !mensaje.isEmpty()) {
            this.notificaciones.add(mensaje);
        }
    }


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TarjetaVirtual getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaVirtual tarjeta) {
        this.tarjeta = tarjeta;
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public void setCuentaPuntos(CuentaPuntos cuentaPuntos) {
        this.cuentaPuntos = cuentaPuntos;
    }

    public List<Compra> getHistorialCompras() {
        return historialCompras;
    }

    public void setHistorialCompras(List<Compra> historialCompras) {
        this.historialCompras = historialCompras;
    }

    public List<String> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<String> notificaciones) {
        this.notificaciones = notificaciones;
    }
}