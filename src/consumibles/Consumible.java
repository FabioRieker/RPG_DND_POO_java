package consumibles;

import personajes.Personaje;

/**
 * Interfaz base para todos los objetos consumibles del juego. Determina el
 * contrato para que cualquier objeto pueda ser utilizado en combate.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class Consumible {
	protected String nombre;
	protected int cantidad;

	/**
	 * Crea el consumible con su nombre y cantidad correspondiente.
	 * 
	 * @param nombre   Nombre del objeto.
	 * @param cantidad Unidades de este objeto.
	 */
	public Consumible(String nombre, int cantidad) {
		this.nombre = nombre;
		// Asigna 1 por defecto si la cantidad indicada es inválida
		this.cantidad = cantidad > 0 ? cantidad : 1;
	}

	/**
	 * Aplica las funciones del consumible (ya sean curaciones o ataques).
	 * 
	 * @param usuario  Personaje que utiliza el objeto.
	 * @param objetivo Personaje al que se le aplica (puede ser aliado o enemigo,
	 *                 según el caso).
	 */
	public abstract void usar(Personaje usuario, Personaje objetivo);

	/**
	 * Consulta la cantidad que queda.
	 * 
	 * @return Número de usos posibles.
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * Devuelve el nombre identificador del objeto.
	 * 
	 * @return Etiqueta del consumible en cadena de texto.
	 */
	public String getNombre() {
		return nombre;
	}
}