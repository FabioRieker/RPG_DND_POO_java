package personajes;

import armas.CategoriaArma;
import estado.EstadoQuemadura;
import armaduras.CategoriaArmadura;

public class JefeDragon extends Jefe {

	public JefeDragon(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.DRACONIDO, TipoJefe.DRAGON, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
	}

	@Override
	public void habilidadEspecial(Personaje objetivo) {
		// Combinamos los dos mensajes para que quede más épico
		System.out.println("\n¡" + nombre + " usa su Aliento de Dragón!");
		System.out.println("¡Exhala una ráfaga de fuego destructor sobre " + objetivo.getNombre() + "!");

		int dañoFuego = 15 + (this.inteligencia / 2);

		objetivo.recibirDaño(dañoFuego, false);

		if (!objetivo.tieneEstado("Quemadura")) {
			objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
		}
	}
}
