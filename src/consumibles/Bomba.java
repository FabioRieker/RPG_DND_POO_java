package consumibles;

import personajes.Personaje;
import java.util.Random;

public class Bomba extends Consumible {
	private Random dado = new Random();

	public Bomba(int cantidad) {
		super("Bomba de Mano", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println("No quedan " + nombre + ".");
			return;
		}

		cantidad--;
		int tirada = dado.nextInt(20) + 1;
		int daño = dado.nextInt(6) + 1 + dado.nextInt(6) + 1; // Daño basado en 2d6

		System.out.println(usuario.getNombre() + " lanza " + nombre + " a " + objetivo.getNombre() + "!");

		if (tirada >= objetivo.getDefensaTotal()) {
			System.out.println("IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de dano.");
			objetivo.recibirDaño(daño, false);
		} else {
			System.out.println("FALLO! La bomba no alcanza el objetivo.");
		}
	}
}