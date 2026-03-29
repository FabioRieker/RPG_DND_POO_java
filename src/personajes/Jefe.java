package personajes;

/**
 * Clase abstracta para los Jefes del juego. Tienen habilidades especiales y
 * suelen ser más fuertes.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class Jefe extends Personaje {

	protected TipoJefe tipo;

	/**
	 * Crea un enemigo de tipo Jefe.
	 * 
	 * @param nombre  Nombre del jefe.
	 * @param raza    Raza a la que pertenece.
	 * @param tipo    Tipo específico de jefe.
	 * @param fue     Fuerza inicial.
	 * @param des     Destreza inicial.
	 * @param con     Constitución inicial.
	 * @param intel   Inteligencia inicial.
	 * @param defBase Defensa base inicial sin armaduras.
	 */
	public Jefe(String nombre, Raza raza, TipoJefe tipo, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.JEFE, fue, des, con, intel, defBase);
		this.tipo = tipo;
	}

	/**
	 * Devuelve el tipo exacto del jefe actual.
	 * 
	 * @return El tipo de jefe.
	 */
	public TipoJefe getTipo() {
		return tipo;
	}

	/**
	 * Ejecuta el ataque letal nativo o característico del jefe.
	 * 
	 * @param objetivo Enemigo que recibe la habilidad especial.
	 */
	public abstract void habilidadEspecial(Personaje objetivo);
}
