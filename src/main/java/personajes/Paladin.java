package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un heroe de tipo paladin. Es un personaje equilibrado
 * entre ataque fisico y magia defensiva.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Paladin extends Personaje {

	/**
	 * Crea un personaje paladin y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del heroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitucion base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Paladin(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.PALADIN, fue, des, con, intel, defBase);
		// Puede usar armas cuerpo a cuerpo
		this.armasPermitidas.add(CategoriaArma.melee);
		// Permite usar armaduras pesadas
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);

		this.mensajePreparacion = "está cargado su fuerza...";
	}
}