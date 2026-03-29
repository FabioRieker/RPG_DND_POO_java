package motor;

import personajes.Personaje;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una habitación o nivel del mapa. Guarda la lista de
 * monstruos que hay dentro.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Sala {
	private int numero;
	private List<Personaje> enemigos;

	public Sala(int numero) {
		this.numero = numero;
		this.enemigos = new ArrayList<>();
	}

	public void agregarEnemigo(Personaje enemigo) {
		this.enemigos.add(enemigo);
	}

	public List<Personaje> getEnemigos() {
		return enemigos;
	}

	public int getNumero() {
		return numero;
	}
}