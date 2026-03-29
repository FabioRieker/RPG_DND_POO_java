package estado;

import personajes.Personaje;

/**
 * Estado perjudicial que aplica daño por envenenamiento en cada turno.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoVeneno extends Estado {

	/**
	 * Crea el debuff de estado de veneno.
	 * 
	 * @param turnos   Los turnos que durará el veneno.
	 * @param potencia La cantidad de puntos de vida que resta por turno.
	 */
	public EstadoVeneno(int turnos, int potencia) {
		super("Veneno", turnos, potencia);
	}

	/**
	 * Aplica el daño de veneno al personaje.
	 * 
	 * @param obj El personaje envenenado.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El veneno afecta a " + obj.getNombre()
				+ motor.MotorCombate.ANSI_RESET);
		obj.recibirDaño(potencia, true);
	}

	/**
	 * Mensaje que marca el fin de los turnos del veneno.
	 * 
	 * @param obj El personaje curado del veneno.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El veneno se ha disipado de " + obj.getNombre()
				+ motor.MotorCombate.ANSI_RESET);
	}
}