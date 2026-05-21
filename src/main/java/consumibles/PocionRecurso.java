package consumibles;

import personajes.Personaje;

/**
 * Pocion que sirve para recuperar Mana (MP) y Energia (SP). Resta una unidad al
 * usarla.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class PocionRecurso extends Consumible {

	/**
	 * Crea un set de pociones de recurso.
	 * 
	 * @param cantidad Numero de pociones disponibles.
	 */
	public PocionRecurso(int cantidad) {
		super("Poción de Recurso", cantidad);
	}

	/**
	 * Recupera recursos llamando al metodo del personaje.
	 * 
	 * @param usuario  Personaje que usa la pocion.
	 * @param objetivo Personaje que recupera la energia.
	 */
	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "."
					+ motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;
		System.out.println(motor.MotorCombate.ANSI_AZUL + "\n[OBJETO] " + usuario.getNombre() + " usa " + nombre + "."
				+ motor.MotorCombate.ANSI_RESET);
		// Recupera 40 puntos a las dos estadisticas secundarias
		usuario.recuperarRecursos(40);
	}
}