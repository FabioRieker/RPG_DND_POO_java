package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Monstruo extends Personaje {

	public Monstruo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.MONSTRUO, fue, des, con, intel, defBase);

		// Cualquier tipo de arma
		this.armasPermitidas.add(CategoriaArma.melee);
		this.armasPermitidas.add(CategoriaArma.distancia);
		// Sin armadura
		this.armadurasPermitidas.add(CategoriaArmadura.NADA);

		this.mensajePreparacion = "ruge salvajemente...";
	}
}