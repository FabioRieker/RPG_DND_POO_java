package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un heroe de tipo monje. Combate principalmente sin
 * armas y tiene mucha agilidad.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Monje extends Personaje {

	/**
	 * Crea un personaje monje y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del heroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitucion base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Monje(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

		super(nombre, raza, TipoClase.MONJE, fue, des, con, intel, defBase);

		// Puede usar armas cuerpo a cuerpo
		this.armasPermitidas.add(CategoriaArma.melee);
		// No utiliza ningun tipo de armadura
		this.armadurasPermitidas.add(CategoriaArmadura.NADA);

		this.mensajePreparacion = "esta haciendo taichi a la fresca";
	}
}