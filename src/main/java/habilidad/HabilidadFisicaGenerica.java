package habilidad;

/**
 * Clase generica para representar habilidades fisicas que solo requieren
 * la asignacion de sus estadisticas basicas sin logica adicional.
 */
public class HabilidadFisicaGenerica extends HabilidadFisica {

	/**
	 * Construye una habilidad fisica usando los datos predefinidos en el enum.
	 * 
	 * @param tipo El tipo de habilidad fisica a instanciar.
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
