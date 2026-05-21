package consumibles;

import personajes.Personaje;

/**
 * Pocion basica que recupera vida al usarla. Sana una cantidad fija de salud y
 * resta una unidad al consumirse.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class PocionCuracion extends Consumible {

	/**
	 * Crea un set de pociones de curacion.
	 * 
	 * @param cantidad Numero de pociones disponibles.
	 */
	public PocionCuracion(int cantidad) {
		super("Poción de Curación", cantidad);
	}

	/**
	 * Llama al metodo curar del personaje objetivo.
	 * 
	 * @param usuario  Personaje que usa la pocion.
	 * @param objetivo Personaje que recibe la curacion.
	 */
	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		// Comprueba si quedan pociones en el inventario
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "."
					+ motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;
		System.out.println(motor.MotorCombate.ANSI_MORADO + "\n[OBJETO] " + usuario.getNombre() + " usa " + nombre + "."
				+ motor.MotorCombate.ANSI_RESET);
		// Aplica 50 puntos de curacion fija
		usuario.curar(50);
	}
}