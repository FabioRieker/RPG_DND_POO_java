package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un héroe de tipo bardo. Es un personaje de apoyo que
 * usa magia para ayudar al equipo.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Bardo extends Personaje {

	/**
	 * Crea un personaje bardo y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del héroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Bardo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.BARDO, fue, des, con, intel, defBase);

		// Puede usar armas de cuerpo a cuerpo
		this.armasPermitidas.add(CategoriaArma.melee);
		// Permite armaduras ligeras y medias
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
		this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);

		this.mensajePreparacion = "está cargado su fuerza...";
	}
}