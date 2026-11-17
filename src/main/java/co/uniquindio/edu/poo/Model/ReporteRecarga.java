package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReporteRecarga extends  ReporteOperativo{


    /**
     * Crea el reporte de recarga vacío. Se llena al invocar generar(...).
     */
    public ReporteRecarga() {
        super();
    }

    @Override
    public String toString() {
        return "ReporteRecargas{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de recargas";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Fecha", "Cliente", "Tarjeta", "Valor"};
    }



    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        List<String[]> filas = new ArrayList<>();
        for (Persona p : cinema.getUsuarios()) {
            if (p instanceof Cliente cliente) {
                TarjetaVirtual tarjeta = cliente.getTarjeta();
                for (Movimiento m : tarjeta.getMovimientos()) {
                    if (m.getTipo() == TipoMovimiento.RECARGA
                            && estaEnPeriodo(m.getFecha().toLocalDate(), inicio, fin)) {
                        filas.add(new String[]{
                                m.getFecha().toLocalDate().toString(),
                                cliente.getNombre(),
                                tarjeta.getNumero(),
                                String.format("%.0f", m.getValor())
                        });
                    }
                }
            }
        }
        return filas;
    }

    /**
     * Agrega el número de recargas y el valor total recargado.
     */
    @Override
    protected void calcularTotales() {
        double total = 0;
        for (String[] fila : getRegistros()) {
            total += Double.parseDouble(fila[3]);
        }
        getTotales().put("Número de recargas", (double) getRegistros().size());
        getTotales().put("Total recargado", total);
    }
}