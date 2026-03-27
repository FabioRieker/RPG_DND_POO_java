package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Bardo extends Personaje {

	public Bardo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
		super(nombre, raza, TipoClase.BARDO, fue, des, con, intel, defBase);

		// Armas a melee
		this.armasPermitidas.add(CategoriaArma.melee);
		// armaduras ligera y media
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
		this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);

		this.mensajePreparacion = "está cargado su fuerza...";
	}
}