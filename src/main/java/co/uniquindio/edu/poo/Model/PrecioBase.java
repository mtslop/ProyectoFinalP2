package co.uniquindio.edu.poo.Model;

/**
 * Representa el precio original antes de aplicar cualquier promoción.
 * Es el componente concreto que puede envolverse con objetos decoradores.
 */
public class PrecioBase implements Precio {

    private double valor;

    /**
     * Crea un precio base.
     *
     * @param valor valor inicial; debe ser mayor o igual que cero.
     * @throws IllegalArgumentException si el valor es negativo o no es finito.
     */
    public PrecioBase(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("El precio base debe ser un valor válido y no negativo.");
        }
        this.valor = valor;
    }

    /**
     * Obtiene el valor original del precio.
     *
     * @return valor base.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Actualiza el valor original del precio.
     * Este método permite que Compra actualice el subtotal sin perder los
     * decoradores que ya estén aplicados sobre este objeto.
     *
     * @param valor nuevo valor base; debe ser mayor o igual que cero.
     * @throws IllegalArgumentException si el valor es negativo o no es finito.
     */
    public void setValor(double valor) {
        if (!Double.isFinite(valor) || valor < 0) {
            throw new IllegalArgumentException("El precio base debe ser un valor válido y no negativo.");
        }
        this.valor = valor;
    }

    /**
     * Retorna el valor del precio sin descuentos.
     *
     * @return valor base.
     */
    @Override
    public double calcular() {
        return valor;
    }

    /**
     * Describe el precio original.
     *
     * @return texto que identifica el precio como precio base.
     */
    @Override
    public String descripcion() {
        return "Precio base";
    }
}
