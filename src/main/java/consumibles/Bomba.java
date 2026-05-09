package consumibles;

import personajes.Personaje;
import java.util.Random;

/**
 * Objeto ofensivo que se lanza contra enemigos para infligir daño. Requiere
 * unidades en el inventario para poder usarse.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Bomba extends Consumible {
	private Random dado = new Random();

	/**
	 * Crea unas bombas para usar en el juego.
	 * 
	 * @param cantidad Cantidad de bombas.
	 */
	public Bomba(int cantidad) {
		super("Bomba de Mano", cantidad);
	}

	/**
	 * Calcula el daño aleatorio y lo aplica sobre el objetivo.
	 * 
	 * @param usuario  Personaje que lanza la bomba.
	 * @param objetivo Personaje enemigo que recibe el ataque.
	 */
	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		// Verifica si todavía disponemos de bombas
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "."
					+ motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;
		int tirada = dado.nextInt(20) + 1;
		// Calcula el daño total simulando dos dados convencionales
		int daño = dado.nextInt(6) + 1 + dado.nextInt(6) + 1;

		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "\n[OBJETO] " + usuario.getNombre() + " lanza " + nombre
				+ " a " + objetivo.getNombre() + "!" + motor.MotorCombate.ANSI_RESET);

		if (tirada >= objetivo.getDefensaTotal()) {
			System.out.println(motor.MotorCombate.ANSI_ROJO + "[DAÑO FUEGO] IMPACTO! " + objetivo.getNombre()
					+ " recibe " + daño + " de daño." + motor.MotorCombate.ANSI_RESET);
			objetivo.recibirDaño(daño, false);
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ALERTA] FALLO! La bomba no alcanza el objetivo."
					+ motor.MotorCombate.ANSI_RESET);
		}
	}
}