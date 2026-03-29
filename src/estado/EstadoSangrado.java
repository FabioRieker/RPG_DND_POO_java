package estado;

import personajes.Personaje;

/**
 * Estado perjudicial que causa pérdida de vida constante sin mitigación de
 * armadura.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoSangrado extends Estado {

	/**
	 * Base del sangrado pasivo.
	 * 
	 * @param turnos   Rondes afectado.
	 * @param potencia Reducción contínua perjudicial.
	 */
	public EstadoSangrado(int turnos, int potencia) {
		super("Sangrado", turnos, potencia);
	}

	/**
	 * Aplica daño puro con flag booleana `ignoraArmadura = true`.
	 * 
	 * @param obj Combatiente en cuestión.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " pierde sangre."
				+ motor.MotorCombate.ANSI_RESET);
		obj.recibirDaño(potencia, true);
	}

	/**
	 * Avisa mediante Sysout del cese del sangrado.
	 * 
	 * @param obj Combatiente en cuestión.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El sangrado de " + obj.getNombre()
				+ " se ha detenido." + motor.MotorCombate.ANSI_RESET);
	}
}
