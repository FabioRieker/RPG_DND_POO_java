package consumibles;

import personajes.Personaje;
import java.util.Random;

/**
 * Arma arrojadiza que inflige daño al usarla. Sirve para realizar ataques a
 * distancia.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class CuchilloArrojadizo extends Consumible {
	private Random dado = new Random();

	/**
	 * Crea los cuchillos arrojadizos.
	 * 
	 * @param cantidad Cantidad de cuchillos disponibles en el inventario.
	 */
	public CuchilloArrojadizo(int cantidad) {
		super("Cuchillo Arrojadizo", cantidad);
	}

	/**
	 * Lanza el cuchillo y calcula si acierta o falla el ataque.
	 * 
	 * @param usuario  Personaje que ataca.
	 * @param objetivo Personaje al que se lanza el cuchillo.
	 */
	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "."
					+ motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;

		int tirada = dado.nextInt(20) + 1;
		// Calcula el daño base con un dado liviano simulado
		int daño = dado.nextInt(4) + 1;

		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "\n[OBJETO] " + usuario.getNombre() + " lanza " + nombre
				+ " a " + objetivo.getNombre() + "!" + motor.MotorCombate.ANSI_RESET);

		if (tirada >= objetivo.getDefensaTotal()) {
			System.out.println(motor.MotorCombate.ANSI_ROJO + "[DAÑO CORTANTE] IMPACTO! " + objetivo.getNombre()
					+ " recibe " + daño + " de daño." + motor.MotorCombate.ANSI_RESET);
			objetivo.recibirDaño(daño, false);
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ALERTA] FALLO! El cuchillo no alcanza el objetivo."
					+ motor.MotorCombate.ANSI_RESET);
		}
	}
}