package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Mago extends Personaje {

	public Mago(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		// El Mago siempre nace con TipoClase.MAGO
		super(nombre, raza, TipoClase.MAGO, fue, des, con, intel, defBase);

		// Armas a distancia
		this.armasPermitidas.add(CategoriaArma.distancia);
		// armaduras ligeras
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);

		this.mensajePreparacion = "está canalizando energía arcana...";
	}
}