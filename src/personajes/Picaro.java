package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un héroe de tipo pícaro. Destaca por su agilidad y uso
 * de ataques rápidos o a distancia.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Picaro extends Personaje {

	/**
	 * Crea un personaje pícaro y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del héroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Picaro(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

		super(nombre, raza, TipoClase.PICARO, fue, des, con, intel, defBase);

		// Puede usar armas de cuerpo a cuerpo y a distancia
		this.armasPermitidas.add(CategoriaArma.melee);
		this.armasPermitidas.add(CategoriaArma.distancia);
		// Solo permite equipar armaduras ligeras
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);

		this.mensajePreparacion = "está canalizando energía arcana...";
	}
}