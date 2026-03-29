package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

/**
 * Clase que representa a un monstruo genérico. Puede equipar cualquier arma
 * pero no usa armaduras.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Monstruo extends Personaje {

	/**
	 * Crea un personaje monstruo.
	 * 
	 * @param nombre  Nombre del monstruo.
	 * @param raza    Raza a la que pertenece.
	 * @param fue     Fuerza base.
	 * @param des     Destreza base.
	 * @param con     Constitución base.
	 * @param intel   Inteligencia base.
	 * @param defBase Defensa inicial.
	 */
	public Monstruo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.MONSTRUO, fue, des, con, intel, defBase);

		// Puede usar cualquier tipo de arma
		this.armasPermitidas.add(CategoriaArma.melee);
		this.armasPermitidas.add(CategoriaArma.distancia);
		// Lucha sin armadura
		this.armadurasPermitidas.add(CategoriaArmadura.NADA);

		this.mensajePreparacion = "ruge salvajemente...";
	}
}