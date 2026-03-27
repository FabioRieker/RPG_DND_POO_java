package consumibles;

import personajes.Personaje;

public abstract class Consumible {
	protected String nombre;
	protected int cantidad;

	public Consumible(String nombre, int cantidad) {
		this.nombre = nombre;
		this.cantidad = cantidad;
	}

	// sirve tanto para atacar como para curarse
	public abstract void usar(Personaje usuario, Personaje objetivo);

	public int getCantidad() {
		return cantidad;
	}

	public String getNombre() {
		return nombre;
	}
}