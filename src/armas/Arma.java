package armas;

import personajes.Personaje;
import java.util.Random;

/**
 * Clase que representa un arma genérica del juego. Se encarga de almacenar los
 * datos de daño, caras del dado y tipo de arma. Es la base para construir armas
 * cuerpo a cuerpo o a distancia.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class Arma {
	protected String nombre;
	protected int cantidadDados;
	protected int carasDado;
	protected Random dado;
	protected CategoriaArma categoria;

	/**
	 * Define los atributos base compartidos por cualquier arma (daño y
	 * clasificación).
	 * 
	 * @param nombre        Nombre del arma (ej. "Espada de Madera").
	 * @param cantidadDados Número de dados para la tirada de daño básico.
	 * @param carasDado     Tipo de dado (de 6 caras, de 4 caras, etc).
	 * @param categoria     Si el arma es a distancia o cuerpo a cuerpo.
	 */
	public Arma(String nombre, int cantidadDados, int carasDado, CategoriaArma categoria) {
		this.nombre = nombre;
		this.cantidadDados = cantidadDados;
		this.carasDado = carasDado;
		this.dado = new Random();
		this.categoria = categoria; // Asignar categoría para chequear posteriormente si la clase es apata para
									// equiparla
	}

	/**
	 * Obtiene el nombre del arma.
	 * 
	 * @return Etiqueta del arma en formato texto.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Consulta la categoría a la que pertenece el arma.
	 * 
	 * @return El tipo de distancia operativa del arma.
	 */
	public CategoriaArma getCategoria() {
		return categoria;
	}

	/**
	 * Ejecuta la tirada de los dados del arma para calcular su daño propio inicial.
	 * 
	 * @return Sumatorio de los dados lanzados internamente.
	 */
	protected int tirarDados() {
		int total = 0;
		for (int i = 0; i < cantidadDados; i++) {
			total += dado.nextInt(carasDado) + 1;
		}
		return total;
	}

	/**
	 * Suma la fuerza o la destreza a la tirada base para concretar el daño.
	 * 
	 * @param atacante Personaje que ataca y usa el arma actual.
	 * @param defensor Personaje objetivo del daño.
	 * @return Daño numérico calculado.
	 */
	public abstract int calcularDaño(Personaje atacante, Personaje defensor);

	/**
	 * Aplica efectos secundarios especiales al impactar.
	 * 
	 * @param defensor Personaje que recibe el golpe y la posible alteración de
	 *                 estado.
	 */
	public void aplicarEfectosEspeciales(Personaje defensor) {
		// Método opcional para aplicar estados alterados (no hacerlo abstracto previene
		// sobrecargar armas genéricas)
	}
}
