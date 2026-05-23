package habilidad;

/**
 * Clase generica para representar hechizos magicos que solo requieren
 * la asignacion de sus estadisticas basicas sin logica adicional.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class HechizoMagicoGenerico extends HechizoMagico {

	/**
	 * Construye un hechizo magico usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de hechizo magico a instanciar.
	 */
	public HechizoMagicoGenerico(TipoHechizoMagicoSimple tipo) {
		super(
			tipo.getNombre(),
			tipo.getCosteMana(),
			tipo.getCantidadDados(),
			tipo.getCarasDado(),
			tipo.getEstadoAsociado()
		);
	}
}
