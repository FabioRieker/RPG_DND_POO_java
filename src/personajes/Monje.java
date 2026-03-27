package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Monje extends Personaje {

	public Monje(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

		super(nombre, raza, TipoClase.MONJE, fue, des, con, intel, defBase);

		// Armas a melee
		this.armasPermitidas.add(CategoriaArma.melee);
		// NADA DE ARMADURA
		this.armadurasPermitidas.add(CategoriaArmadura.NADA);

		this.mensajePreparacion = "esta haciendo taichi a la fresca";
	}
}