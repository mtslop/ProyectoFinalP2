package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CuentaPuntos {

    private List<RegistroPuntos> registros;
    private IEstrategiaPuntos estrategia;

    /**
     * Crea una cuenta sin puntos con la estrategia estándar
     * (un punto por cada $1.000 pagados).
     */
    public CuentaPuntos() {
        this.registros = new ArrayList<>();
        this.estrategia = new EstrategiaPuntosEstandar(1000);
    }

    public List<RegistroPuntos> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroPuntos> registros) {
        this.registros = registros;
    }

    public IEstrategiaPuntos getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(IEstrategiaPuntos estrategia) {
        this.estrategia = estrategia;
    }

    @Override
    public String toString() {
        return "CuentaPuntos{" +
                "puntosDisponibles=" + puntosDisponibles() +
                ", registros=" + registros.size() +
                ", estrategia=" + estrategia +
                '}';
    }

    /**
     * Calcula los puntos disponibles en los lotes vigentes.
     *
     * @return total de puntos que pueden redimirse a la fecha actual
     */
    public int puntosDisponibles() {
        LocalDate hoy = LocalDate.now();
        int total = 0;
        for (RegistroPuntos r : registros) {
            if (!r.estaVencido(hoy)) {
                total += r.getCantidadDisponible();
            }
        }
        return total;
    }

    /**
     * Calcula los puntos de una compra con la estrategia actual y los registra
     * en un nuevo lote con vencimiento a un año.
     *
     * @param totalPagado valor total pagado en la compra
     * @return puntos obtenidos en la compra
     */
    public int acumular(double totalPagado) {
        int puntos = estrategia.calcularPuntos(totalPagado);
        if (puntos > 0) {
            registros.add(new RegistroPuntos(puntos, LocalDate.now()));
        }
        return puntos;
    }

    /**
     * Redime puntos consumiendo primero los lotes más próximos a vencer.
     * Si los puntos no alcanzan, no se descuenta ninguno.
     *
     * @param cantidad puntos que se desean redimir
     * @throws IllegalArgumentException si la cantidad no es mayor que cero
     * @throws IllegalStateException    si los puntos vigentes no alcanzan
     */
    public void redimir(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a redimir debe ser mayor que cero");
        }
        eliminarVencidos();
        int disponibles = puntosDisponibles();
        if (disponibles < cantidad) {
            throw new IllegalStateException(
                    "Puntos insuficientes: tiene " + disponibles + " y necesita " + cantidad);
        }
        List<RegistroPuntos> ordenados = new ArrayList<>(registros);
        ordenados.sort(Comparator.comparing(RegistroPuntos::getFechaVencimiento));
        int faltante = cantidad;
        for (RegistroPuntos r : ordenados) {
            faltante = r.descontar(faltante);
            if (faltante == 0) {
                break;
            }
        }
    }

    /**
     * Anula los puntos disponibles de los lotes vencidos. Los lotes se conservan
     * para mantener el historial de puntos acumulados.
     */
    public void eliminarVencidos() {
        LocalDate hoy = LocalDate.now();
        for (RegistroPuntos r : registros) {
            if (r.estaVencido(hoy)) {
                r.descontar(r.getCantidadDisponible());
            }
        }
    }
    /**
     * Registra un lote con una cantidad de puntos ya calculada, con vencimiento a un año.
     *
     * @param cantidad puntos a acumular
     */
    public void acumularCantidad(int cantidad) {
        if (cantidad > 0) {
            registros.add(new RegistroPuntos(cantidad, LocalDate.now()));
        }
    }
}
