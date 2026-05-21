package habilidad;

/**
 * Clase generica para representar habilidades hibridas que solo requieren
 * la asignacion de sus estadisticas basicas sin logica adicional.
 */
public class HabilidadHibridaGenerica extends HabilidadHibrida {

	/**
	 * Construye una habilidad hibrida usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de habilidad hibrida a instanciar.
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
