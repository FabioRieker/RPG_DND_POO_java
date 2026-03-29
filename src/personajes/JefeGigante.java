package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoAturdimiento;

/**
 * Jefe de tipo Gigante. Tiene mucha salud y puede aturdir con sus ataques
 * especiales.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class JefeGigante extends Jefe {

	/**
	 * Crea un Jefe Gigante con estadísticas base.
	 * 
	 * @param nombre  Nombre del gigante.
	 * @param fue     Fuerza.
	 * @param des     Destreza.
	 * @param con     Constitución.
	 * @param intel   Inteligencia.
	 * @param defBase Defensa inicial sin equipamiento.
	 */
	public JefeGigante(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.BESTIA, TipoJefe.GIGANTE, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
	}

	/**
	 * Pisa fuerte el suelo, dañando e intentando aturdir al rival.
	 * 
	 * @param objetivo Personaje al que ataca.
	 */
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