package consumibles;

import personajes.Personaje;
import java.util.Random;

public class CuchilloArrojadizo extends Consumible {
	private Random dado = new Random();

	public CuchilloArrojadizo(int cantidad) {
		super("Cuchillo Arrojadizo", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] No quedan " + nombre + "." + motor.MotorCombate.ANSI_RESET);
			return;
		}

		cantidad--;

		int tirada = dado.nextInt(20) + 1;
		int daño = dado.nextInt(4) + 1; 

		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "\n[OBJETO] " + usuario.getNombre() + " lanza " + nombre + " a " + objetivo.getNombre() + "!" + motor.MotorCombate.ANSI_RESET);

		if (tirada >= objetivo.getDefensaTotal()) {
			System.out.println(motor.MotorCombate.ANSI_ROJO + "[DAÑO CORTANTE] IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño." + motor.MotorCombate.ANSI_RESET);
			objetivo.recibirDaño(daño, false);
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[ALERTA] FALLO! El cuchillo no alcanza el objetivo." + motor.MotorCombate.ANSI_RESET);
		}
	}
}