package co.uniquindio.edu.poo.Model;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReporteProductos extends ReporteOperativo{

    /**
     * Crea el reporte de productos vacío. Se llena al invocar generar(...).
     */
    public ReporteProductos() {
        super();
    }

    @Override
    public String toString() {
        return "ReporteProductos{" + super.toString() + '}';
    }

    @Override
    protected String definirTitulo() {
        return "Reporte de productos de confitería vendidos";
    }

    @Override
    protected String[] definirEncabezados() {
        return new String[]{"Producto", "Unidades", "Valor vendido"};
    }



    @Override
    protected List<String[]> obtenerRegistros(CinemaUQ cinema, LocalDate inicio, LocalDate fin) {
        Map<String, Integer> unidades = new LinkedHashMap<>();
        Map<String, Double> valores = new LinkedHashMap<>();
        for (Compra c : cinema.getCompras()) {
            if ("PAGADA".equals(c.getEstado().nombre())
                    && estaEnPeriodo(c.getFecha().toLocalDate(), inicio, fin)) {
                for (DetalleConfiteria d : c.getDetallesConfiteria()) {
                    String nombre = d.getItem().nombre();
                    unidades.merge(nombre, d.getCantidad(), Integer::sum);
                    valores.merge(nombre, d.subtotal(), Double::sum);
                }
            }
        }
        List<String[]> filas = new ArrayList<>();
        for (String nombre : unidades.keySet()) {
            filas.add(new String[]{
                    nombre,
                    String.valueOf(unidades.get(nombre)),
                    String.format("%.0f", valores.get(nombre))
            });
        }
        return filas;
    }

    /**
     * Agrega el total de unidades y el valor total vendido.
     */
    @Override
    protected void calcularTotales() {
        double unidades = 0;
        double valor = 0;
        for (String[] fila : getRegistros()) {
            unidades += Double.parseDouble(fila[1]);
            valor += Double.parseDouble(fila[2]);
        }
        getTotales().put("Unidades vendidas", unidades);
        getTotales().put("Valor total confitería", valor);
    }
}

