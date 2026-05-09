package estado;

import personajes.Personaje;

/**
 * Estado beneficioso que aumenta temporalmente el poder de ataque físico.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class EstadoFuria extends Estado {

	/**
	 * Constructor básico para este buff pasivo.
	 * 
	 * @param turnos   Turnos activo.
	 * @param potencia Cantidad de daño bruto que añade pasivamente.
	 */
	public EstadoFuria(int turnos, int potencia) {
		super("Furia", turnos, potencia);
	}

	/**
	 * Anuncia la condición alterada mediante un mensaje en rojo.
	 * 
	 * @param obj Personaje sobre el que actúa.
	 */
	@Override
	public void alPasarTurnoEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_ROJO + "[ESTADO] " + obj.getNombre() + " está enfurecido con +"
				+ potencia + " de daño!" + motor.MotorCombate.ANSI_RESET);
	}

	/**
	 * Devuelve el bonus numérico de ataque mientras está activa.
	 * 
	 * @return La cifra entera de potencia extra a sumar.
	 */
	public int getModificadorDaño() {
		return this.potencia; // Propagar el valor del daño extra sumando la potencia base de la furia
	}

	/**
	 * Mensaje indicativo al final del bucle.
	 * 
	 * @param obj Atacante afectado.
	 */
	@Override
	public void alTerminarEstado(Personaje obj) {
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ESTADO] La furia de " + obj.getNombre()
				+ " se ha calmado." + motor.MotorCombate.ANSI_RESET);
	}
}
