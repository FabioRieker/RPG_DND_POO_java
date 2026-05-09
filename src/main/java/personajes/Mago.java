package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un héroe de tipo mago. Se centra en el uso de magia y
 * ataques a distancia.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Mago extends Personaje {

	/**
	 * Crea un personaje mago y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del héroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Mago(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		// Siempre se crea con el tipo de clase Mago
		super(nombre, raza, TipoClase.MAGO, fue, des, con, intel, defBase);

		// Puede usar armas a distancia
		this.armasPermitidas.add(CategoriaArma.distancia);
		// Permite equipar armaduras ligeras
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);

		this.mensajePreparacion = "está canalizando energía arcana...";
	}
}