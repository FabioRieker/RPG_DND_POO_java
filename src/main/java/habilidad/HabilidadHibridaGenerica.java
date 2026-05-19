package habilidad;

/**
 * Clase genérica para representar habilidades híbridas que solo requieren
 * la asignación de sus estadísticas básicas sin lógica adicional.
 */
public class HabilidadHibridaGenerica extends HabilidadHibrida {

	/**
	 * Construye una habilidad híbrida usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de habilidad híbrida a instanciar.
	 */
	public HabilidadHibridaGenerica(TipoHabilidadHibridaSimple tipo) {
		super(
			tipo.getNombre(),
			tipo.getCosteEnergia(),
			tipo.getCosteMana(),
			tipo.getCantidadDados(),
			tipo.getCarasDado(),
			tipo.getEstadistica(),
			tipo.getEfectoExtra()
		);
	}
}
