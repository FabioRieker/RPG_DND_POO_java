package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un héroe de tipo guerrero. Está enfocado en el combate
 * cuerpo a cuerpo y resistir ataques.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Guerrero extends Personaje {

	/**
	 * Crea un personaje guerrero y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del héroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Guerrero(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.GUERRERO, fue, des, con, intel, defBase);

		// Puede usar armas cuerpo a cuerpo
		this.armasPermitidas.add(CategoriaArma.melee);
		// Permite equipar armaduras medias y pesadas
		this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);

		this.mensajePreparacion = "está cargado su fuerza...";
	}
}