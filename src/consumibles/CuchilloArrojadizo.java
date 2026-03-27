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
			System.out.println("No quedan " + nombre + ".");
			return;
		}

		cantidad--;
		int tirada = dado.nextInt(20) + 1;
		int daño = dado.nextInt(4) + 1; 

		System.out.println(usuario.getNombre() + " lanza " + nombre + " a " + objetivo.getNombre() + "!");

		if (tirada >= objetivo.getDefensaTotal()) {
			System.out.println("IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de dano.");
			objetivo.recibirDaño(daño, false);
		} else {
			System.out.println("FALLO! El cuchillo no alcanza el objetivo.");
		}
	}
}