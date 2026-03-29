package estado;

import personajes.Personaje;

/**
 * Estado perjudicial que aplica daño por fuego al final de cada turno e ignora
 * armadura.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoQuemadura extends Estado {

	/**
	 * Creación genérica del debuff quemadura.
	 * 
	 * @param turnos   Longitud que tendrá el timer interno.
	 * @param potencia Puntos netos perdidos.
	 */
	public EstadoQuemadura(int turnos, int potencia) {
		super("Quemadura", turnos, potencia);
	}

	/**
	 * Produce la quita de salud pasiva.
	 * 
	 * @param obj Sujeto de pruebas que se está quemando.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] El fuego arde en " + obj.getNombre()
				+ motor.MotorCombate.ANSI_RESET);
		obj.recibirDaño(potencia, true);
	}

	/**
	 * Cierra el ciclo de fuego.
	 * 
	 * @param obj Sujeto que se estaba quemando.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] Las llamas se han extinguido en "
				+ obj.getNombre() + motor.MotorCombate.ANSI_RESET);
	}
}
