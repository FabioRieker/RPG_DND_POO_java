package estado;

import personajes.Personaje;

/**
 * Estado beneficioso pasivo que restaura pequeñas cantidades de vida
 * automaticamente.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoRenovar extends Estado {

	/**
	 * Inicia la sanacion en el tiempo.
	 * 
	 * @param turnos   Cuantas veces curara.
	 * @param potencia La cantidad que se inyecta en vida de forma directa.
	 */
	public EstadoRenovar(int turnos, int potencia) {
		super("Renovar", turnos, potencia);
	}

	/**
	 * Aplica curar() repetidamente al resolverse un turno entero del bucle
	 * principal.
	 * 
	 * @param obj Objetivo amigo seleccionado previamente.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_MORADO + "[ESTADO] La luz sana a " + obj.getNombre()
				+ motor.MotorCombate.ANSI_RESET);
		obj.curar(potencia);
	}

	/**
	 * Imprime la conclusion del tiempo activo de este buff.
	 * 
	 * @param obj Objetivo amigo seleccionado previamente.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_MORADO + "[ESTADO] El efecto de renovación termina en "
				+ obj.getNombre() + motor.MotorCombate.ANSI_RESET);
	}
}
