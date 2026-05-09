package habilidad;

/**
 * Golpe letal de fuerza bruta que inflige bastante daño físico.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class TajoSismico extends HabilidadFisica {
	/**
	 * Construye los costes y características del Tajo Sísmico.
	 */
	public TajoSismico() {
		super("Tajo Sísmico", 30, 2, 8, Estadistica.FUERZA, "SANGRADO");
	}
}