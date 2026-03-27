package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoVeneno;

public class JefeLich extends Jefe {

	public JefeLich(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.NO_MUERTO, TipoJefe.LICH, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.distancia);
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
	}

	@Override
	public void habilidadEspecial(Personaje objetivo) {
		System.out.println("\n" + nombre + " invoca a los muertos!");
		System.out.println(nombre + " canaliza energía necromántica!");

		int daño = 12 + (this.inteligencia / 2);
		objetivo.recibirDaño(daño, true);

		if (!objetivo.tieneEstado("Veneno")) {
			objetivo.aplicarEstado(new EstadoVeneno(4, 6));
		}
	}
}