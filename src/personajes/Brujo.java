package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un héroe de tipo brujo. Especialista en magia ofensiva
 * y estados alterados.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Brujo extends Personaje {

	/**
	 * Crea un personaje brujo y le asigna sus limitaciones de equipo.
	 * 
	 * @param nombre  Nombre del héroe.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Brujo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

		super(nombre, raza, TipoClase.BRUJO, fue, des, con, intel, defBase);

		// Puede usar armas a distancia
		this.armasPermitidas.add(CategoriaArma.distancia);
		// Solo permite equipar armadura ligera
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);

		this.mensajePreparacion = "está canalizando energía demoniaca...";
	}
}