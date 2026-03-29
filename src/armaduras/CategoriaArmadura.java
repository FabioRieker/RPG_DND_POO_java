package armaduras;

/**
 * Enumeración que define los tipos de defensa que se pueden equipar. Se utiliza
 * para calcular la protección base de cada personaje.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public enum CategoriaArmadura {
	NADA("Sin armadura", 0, 2), LIGERA("Armadura Ligera", 3, 1), MEDIA("Armadura Media", 6, 0),
	PESADA("Armadura Pesada", 10, -5);

	public final String nombre;
	public final int bonoDefensa;
	public final int modificadorDestreza;

	/**
	 * Constructor interno del Enum que asigna los puntos de defensa y velocidad de
	 * la armadura.
	 * 
	 * @param nombre   El nombre base de la armadura.
	 * @param defensa  Puntos de defensa que añade.
	 * @param destreza Penalización o bonificación de velocidad asociada al tipo de
	 *                 armadura.
	 */
	CategoriaArmadura(String nombre, int defensa, int destreza) {
		this.nombre = nombre;
		this.bonoDefensa = defensa;
		this.modificadorDestreza = destreza;
	}
}
