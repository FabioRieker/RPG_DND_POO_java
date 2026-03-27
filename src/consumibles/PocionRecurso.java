package consumibles;

import personajes.Personaje;

public class PocionRecurso extends Consumible {

	public PocionRecurso(int cantidad) {
		super("Poción de Recurso", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println("No quedan " + nombre + ".");
			return;
		}

		cantidad--;
		usuario.recuperarRecursos(40);
		System.out.println(usuario.getNombre() + " usa " + nombre + ".");
	}
}