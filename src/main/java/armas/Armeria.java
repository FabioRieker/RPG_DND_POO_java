package armas;

import java.util.HashMap;
import java.util.Map;

/**
 * Catálogo general que instancia y guarda todas las armas disponibles en el
 * juego. Permite buscar la configuración de un arma por su nombre a lo largo de
 * la aventura.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Armeria {
	private Map<String, Arma> listaArmas = new HashMap<>();

	/**
	 * Inicia el repertorio con las instancias de cada arma estándar del sistema.
	 */
	public Armeria() {
		listaArmas.put("Espada", new ArmaCuerpoACuerpo("Espada Larga", 1, 8));
		listaArmas.put("Hacha", new ArmaCuerpoACuerpo("Hacha de Batalla", 1, 12));
		listaArmas.put("Daga", new ArmaCuerpoACuerpo("Daga de Asesino", 1, 4));
		listaArmas.put("Maza", new ArmaCuerpoACuerpo("Maza Pesada", 2, 8)); // Arma equilibrada en peso y daño
		listaArmas.put("Arco", new ArmaADistancia("Arco Corto", 1, 8));
		listaArmas.put("Ballesta", new ArmaADistancia("Ballesta Pesada", 1, 10));
		listaArmas.put("Jabalina", new ArmaADistancia("Jabalina de Caza", 1, 6));
		listaArmas.put("Cetro", new ArmaADistancia("Cetro de Almas", 2, 10));
		listaArmas.put("Garras Reales", new ArmaCuerpoACuerpo("Garras Reales", 2, 8)); // Arma exclusiva de los jefes
																						// enemigos
	}

	/**
	 * Recupera un arma estática o devuelve una instancia única de arma legendaria.
	 * 
	 * @param nombre Entrada exacta de texto con el nombre del modelo de arma.
	 * @return Objeto físico del arma demandada.
	 * @throws IllegalArgumentException si la búsqueda no encuentra coinicidencias.
	 */
	public Arma get(String nombre) {
		// Distinguir objetos legendarios para devolver su clase personalizada
		if (nombre.equals("Hoja Fénix"))
			return new HojaFenix();
		if (nombre.equals("Colmillo de Araña"))
			return new ColmilloArana();

		// Despachar una copia desde el catálogo de armas comunes
		Arma armaEncontrada = listaArmas.get(nombre);

		// Lanzar una excepción de advertencia si la cadena de texto no existe
		if (armaEncontrada == null) {
			throw new IllegalArgumentException("Arma no existe en la armeria: " + nombre);
		}
		return armaEncontrada;
	}
}