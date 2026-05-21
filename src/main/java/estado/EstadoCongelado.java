package estado;

import personajes.Personaje;

/**
 * Estado que aplica daño continuo por frio al terminar la ronda. Ademas bloquea
 * la movilidad del personaje mientras dure.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoCongelado extends Estado {

	/**
	 * Crea un debuff de estado helado.
	 * 
	 * @param turnos   Los turnos que va a durar el efecto.
	 * @param potencia El daño que causara en cada turno.
	 */
	public EstadoCongelado(int turnos, int potencia) {
		super("Congelado", turnos, potencia);
	}

	/**
	 * Disminuye la salud del perjudicado segun la potencia de congelacion.
	 * 
	 * @param obj El objetivo afectado.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AZUL + "[ESTADO] El frío hiela a " + obj.getNombre()
				+ motor.MotorCombate.ANSI_RESET);
		obj.recibirDaño(potencia, true);
	}

	/**
	 * Informa al jugador del final del periodo.
	 * 
	 * @param obj El objetivo afectado.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AZUL + "[ESTADO] " + obj.getNombre() + " se ha descongelado."
				+ motor.MotorCombate.ANSI_RESET);
	}
}
