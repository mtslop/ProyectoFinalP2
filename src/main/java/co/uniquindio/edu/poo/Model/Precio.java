package co.uniquindio.edu.poo.Model;

/**
 * Contrato para calcular el precio de una compra o de uno de sus conceptos.
 * Se utiliza como componente del patrón Decorator para permitir combinar
 * diferentes promociones sin modificar la implementación del precio base.
 */
public interface Precio {

    /**
     * Calcula el valor final representado por este precio.
     *
     * @return valor calculado, expresado en la misma unidad monetaria del sistema.
     */
    double calcular();

    /**
     * Devuelve una descripción del precio y de las promociones aplicadas.
     *
     * @return descripción legible del cálculo.
     */
    String descripcion();
}
