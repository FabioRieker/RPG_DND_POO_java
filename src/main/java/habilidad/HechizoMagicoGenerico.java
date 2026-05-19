package habilidad;

/**
 * Clase genérica para representar hechizos mágicos que solo requieren
 * la asignación de sus estadísticas básicas sin lógica adicional.
 */
public class HechizoMagicoGenerico extends HechizoMagico {

	/**
	 * Construye un hechizo mágico usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de hechizo mágico a instanciar.
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
