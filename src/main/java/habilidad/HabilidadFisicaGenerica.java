package habilidad;

/**
 * Clase genérica para representar habilidades físicas que solo requieren
 * la asignación de sus estadísticas básicas sin lógica adicional.
 */
public class HabilidadFisicaGenerica extends HabilidadFisica {

	/**
	 * Construye una habilidad física usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de habilidad física a instanciar.
	 */
	public HabilidadFisicaGenerica(TipoHabilidadFisicaSimple tipo) {
		super(
			tipo.getNombre(),
			tipo.getCosteEnergia(),
			tipo.getCantidadDados(),
			tipo.getCarasDado(),
			tipo.getEstadistica(),
			tipo.getEstadoAsociado()
		);
	}
}
