package motor;

import personajes.Personaje;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una habitacion o nivel del mapa. Guarda la lista de
 * monstruos que hay dentro.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Sala {
	private int numero;
	private List<Personaje> enemigos;

	/**
	 * Crea una sala vacía identificada con un número.
	 *
	 * @param numero Número identificador de la sala (1-20).
	 */
	public Sala(int numero) {
		this.numero = numero;
		this.enemigos = new ArrayList<>();
	}

	/**
	 * Añade un personaje enemigo a la lista de la sala.
	 *
	 * @param enemigo Personaje a incluir como oponente.
	 */
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