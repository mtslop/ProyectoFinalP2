package co.uniquindio.edu.poo.Model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CinemaUQ {

    private static CinemaUQ instancia;

    private List<Persona> usuarios;
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<ItemConfiteria> productos;
    private List<Compra> compras;
    private List<Promocion> promociones;
    private List<SolicitudRecarga> solicitudes;

    private CinemaUQ() {
        this.usuarios = new ArrayList<>();
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.promociones = new ArrayList<>();
        this.solicitudes = new ArrayList<>();
    }

    /**
     * Obtiene la unica instancia de la clase CinemaUQ.
     * Si no ha sido creada, la instancia por primera vez.
     *
     * @return Instancia unica de CinemaUQ.
     */
    public static CinemaUQ getInstance() {
        if (instancia == null) {
            instancia = new CinemaUQ();
        }
        return instancia;
    }

    /**
     * Carga datos iniciales de prueba (como un administrador por defecto y un cliente).
     */
    public void cargarDatosIniciales() {
        PersonaFactory adminFactory = new AdministradorFactory();
        PersonaFactory clienteFactory = new ClienteFactory();

        Persona admin = adminFactory.crearPersona("1001", "Admin General", "admin@cinemauq.com", "admin123", "Gerente");
        Persona cliente = clienteFactory.crearPersona("1002", "Juan Perez", "juan@correo.com", "1234", "3100000000");

        agregarPersona(admin);
        agregarPersona(cliente);
    }

    /**
     * Busca una persona registrada en la lista de usuarios mediante su correo.
     *
     * @param correo Correo electronico a buscar.
     * @return El objeto Persona encontrado, o null si no existe.
     */
    public Persona buscarPersonaPorCorreo(String correo) {
        for (Persona p : usuarios) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Busca funciones programadas para una pelicula y fecha especificas.
     *
     * @param codigoPelicula Codigo de la pelicula a consultar.
     * @param fecha          Fecha en la que se desea ver la funcion.
     * @return Lista de funciones que coinciden con la busqueda.
     */
    public List<Funcion> buscarFunciones(String codigoPelicula, LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion f : funciones) {
            if (f.getPelicula() != null && f.getPelicula().getCodigo().equals(codigoPelicula)) {
                if (f.getFechaHora() != null && f.getFechaHora().toLocalDate().equals(fecha)) {
                    resultado.add(f);
                }
            }
        }
        return resultado;
    }

    /**
     * Agrega una nueva persona (Cliente o Administrador) a la lista de usuarios si no esta repetida.
     *
     * @param persona Persona a registrar.
     */
    public void agregarPersona(Persona persona) {
        if (persona != null && buscarPersonaPorCorreo(persona.getCorreo()) == null) {
            this.usuarios.add(persona);
        }
    }

    /**
     * Registra una nueva compra en la lista general de compras del cine.
     *
     * @param compra Compra realizada.
     */
    public void agregarCompra(Compra compra) {
        if (compra != null) {
            this.compras.add(compra);
        }
    }


    public List<Persona> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Persona> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = funciones;
    }

    public List<ItemConfiteria> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemConfiteria> productos) {
        this.productos = productos;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(List<Promocion> promociones) {
        this.promociones = promociones;
    }

    public List<SolicitudRecarga> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<SolicitudRecarga> solicitudes) {
        this.solicitudes = solicitudes;
    }
}