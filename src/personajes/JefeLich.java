package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoVeneno;

/**
 * Jefe de tipo Lich. Es un no-muerto que canaliza energía oscura y envenena.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class JefeLich extends Jefe {

	/**
	 * Crea un Jefe Lich con estadísticas base.
	 * 
	 * @param nombre  Nombre del lich.
	 * @param fue     Fuerza.
	 * @param des     Destreza.
	 * @param con     Constitución.
	 * @param intel   Inteligencia.
	 * @param defBase Defensa inicial sin equipamiento.
	 */
	public JefeLich(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.NO_MUERTO, TipoJefe.LICH, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.distancia);
		this.armadurasPermitidas.add(CategoriaArmadura.LIGERA);
	}

	/**
	 * Un hechizo nigromántico que hace mucho daño y aplica Veneno.
	 * 
	 * @param objetivo Enemigo que recibe la habilidad.
	 */
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