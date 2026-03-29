package estado;

import personajes.Personaje;

/**
 * Estado perjudicial que impide al personaje realizar acciones durante su
 * turno. Representa daños graves que le impiden luchar momentáneamente.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoLisiado extends Estado {

	/**
	 * Crea el debuff de estado lisiado.
	 * 
	 * @param turnos   Duración en turnos del estado alterado.
	 * @param potencia No se usa para daño en este estado (suele ser 0).
	 */
	public EstadoLisiado(int turnos, int potencia) {
		super("Lisiado", turnos, potencia);
	}

	/**
	 * Muestra un aviso de que el personaje sigue bajo efecto de lesión.
	 * 
	 * @param obj El personaje afectado.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre()
				+ " esta lisiado y se mueve con dificultad." + motor.MotorCombate.ANSI_RESET);
	}

	/**
	 * Muestra un aviso indicando que el personaje se curó del estado.
	 * 
	 * @param obj El personaje afectado anteriormente.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] " + obj.getNombre() + " ya no esta lisiado."
				+ motor.MotorCombate.ANSI_RESET);
	}
}
