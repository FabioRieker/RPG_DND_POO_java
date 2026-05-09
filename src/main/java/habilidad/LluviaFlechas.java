package habilidad;

/**
 * Técnica a distancia que dispara un abanico de flechas contra un objetivo o
 * varios.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class LluviaFlechas extends HabilidadFisica {
	/**
	 * Construye los costes y características de la Lluvia de Flechas.
	 */
	public LluviaFlechas() {
		super("Lluvia de Flechas", 40, 1, 8, Estadistica.DESTREZA, null);
	}
}