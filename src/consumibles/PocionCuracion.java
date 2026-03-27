package consumibles;

import personajes.Personaje;

public class PocionCuracion extends Consumible {

	public PocionCuracion(int cantidad) {
		super("Poción de Curación", cantidad);
	}

	@Override
	public void usar(Personaje usuario, Personaje objetivo) {
		if (cantidad <= 0) {
			System.out.println("No quedan " + nombre + ".");
			return;
		}

		cantidad--;
		usuario.curar(50); 
		System.out.println(usuario.getNombre() + " usa " + nombre + ".");
	}
}