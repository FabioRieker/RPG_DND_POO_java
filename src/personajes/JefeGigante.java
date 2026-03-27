package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoAturdimiento;

public class JefeGigante extends Jefe {

	public JefeGigante(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.BESTIA, TipoJefe.GIGANTE, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
	}

	@Override
	public void habilidadEspecial(Personaje objetivo) {
		System.out.println("\n" + nombre + " lanza rocas!");
		System.out.println(nombre + " aplasta todo a su paso!");

		int daño = 20 + (this.fuerza / 3);
		objetivo.recibirDaño(daño, false);

		if (!objetivo.tieneEstado("Aturdimiento")) {
			objetivo.aplicarEstado(new EstadoAturdimiento(1));
		}
	}
}