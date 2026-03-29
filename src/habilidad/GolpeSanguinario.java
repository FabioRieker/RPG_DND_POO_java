package habilidad;

/**
 * Habilidad híbrida que daña al enemigo y recupera vida al atacante
 * simultáneamente.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GolpeSanguinario extends HabilidadHibrida {

	/**
	 * Construye los costes y características del Golpe Sanguinario.
	 */
	public GolpeSanguinario() {
		super("Golpe Sanguinario", 15, 10, 1, 8, Estadistica.FUERZA, Efecto.CURAR_VIDA);
	}
}