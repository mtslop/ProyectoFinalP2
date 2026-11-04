package co.uniquindio.edu.poo.Model;

/**
 * Contrato común para los artículos que se venden en confitería.
 * Permite tratar de la misma forma los productos individuales y los combos.
 */
public interface ItemConfiteria {

    /**
     * Obtiene el nombre comercial del artículo.
     *
     * @return nombre del artículo.
     */
    String nombre();

    /**
     * Obtiene el precio de venta del artículo.
     * En un combo debe incluir el descuento aplicado.
     *
     * @return precio del artículo.
     */
    double precio();

    /**
     * Obtiene los puntos que otorga o que cuesta redimir el artículo.
     *
     * @return puntos de redención.
     */
    int puntosRedencion();
}
