package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoFuria;

public class JefeDemonio extends Jefe {

	public JefeDemonio(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.TIEFLING, TipoJefe.DEMONIO, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);
	}

	@Override
	public void habilidadEspecial(Personaje objetivo) {
		System.out.println("\n" + nombre + " desata ira infernal!");
		System.out.println(nombre + " entra en modo berserker!");

		int daño = 10 + (this.fuerza / 2);
		objetivo.recibirDaño(daño, false);

		if (!this.tieneEstado("Furia")) {
			this.aplicarEstado(new EstadoFuria(3, 8));
		}
	}
}