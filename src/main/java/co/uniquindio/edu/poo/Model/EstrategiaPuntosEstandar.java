package co.uniquindio.edu.poo.Model;

public class EstrategiaPuntosEstandar implements IEstrategiaPuntos{

    private double valorPorPunto;

    /**
     * Crea la estrategia de puntos para clientes estándar.
     *
     * @param valorPorPunto valor en pesos que se debe pagar para obtener un punto
     */
    public EstrategiaPuntosEstandar(double valorPorPunto) {
        this.valorPorPunto = valorPorPunto;
    }

    public double getValorPorPunto() {
        return valorPorPunto;
    }

    public void setValorPorPunto(double valorPorPunto) {
        this.valorPorPunto = valorPorPunto;
    }

    @Override
    public String toString() {
        return "EstrategiaPuntosEstandar{" +
                "valorPorPunto=" + valorPorPunto +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calcularPuntos(double totalPagado) {
        return (int) (totalPagado / valorPorPunto);
    }
}
