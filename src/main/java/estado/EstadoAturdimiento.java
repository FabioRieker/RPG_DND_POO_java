package estado;

import personajes.Personaje;

/**
 * Estado que impide al personaje realizar acciones durante su turno. Representa
 * un fuerte golpe prolongado.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoAturdimiento extends Estado {

	/**
	 * Crea un estado de aturdimiento genérico.
	 * 
	 * @param turnos Cantidad de turnos que el personaje no podrá moverse.
	 */
	public EstadoAturdimiento(int turnos) {
		super("Aturdimiento", turnos, 0);
	}

	/**
	 * Imprime por pantalla un aviso de la parálisis.
	 * 
	 * @param obj Personaje bajo los efectos del aturdimiento.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre()
				+ " esta aturdido y no puede actuar!" + motor.MotorCombate.ANSI_RESET);
	}

	/**
	 * Imprime por pantalla el final de la parálisis.
	 * 
	 * @param obj Personaje que se recupera de la condición.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " ya no esta aturdido."
				+ motor.MotorCombate.ANSI_RESET);
	}
}
