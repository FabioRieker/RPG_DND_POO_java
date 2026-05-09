package habilidad;

/**
 * Estallido de magia pura que inflige daño puro sin importar la defensa.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ExplosionArcana extends HabilidadHibrida {

	/**
	 * Construye los costes y características de la Explosión Arcana.
	 */
	public ExplosionArcana() {
		super("Explosión Arcana", 15, 15, 3, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
	}
}