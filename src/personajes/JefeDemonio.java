package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.EstadoFuria;

/**
 * Jefe de tipo Demonio que puede ejecutar ataques especiales. Se centra en
 * ataques con fuego y quemaduras.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class JefeDemonio extends Jefe {

	/**
	 * Crea un Jefe Demonio con estadísticas base.
	 * 
	 * @param nombre  Nombre del demonio.
	 * @param fue     Fuerza.
	 * @param des     Destreza.
	 * @param con     Constitución.
	 * @param intel   Inteligencia.
	 * @param defBase Defensa inicial sin equipamiento.
	 */
	public JefeDemonio(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.TIEFLING, TipoJefe.DEMONIO, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);
	}

	/**
	 * Realiza un ataque ígneo que ignora parte de la defensa rival. Aplica el
	 * estado Quemadura al objetivo.
	 * 
	 * @param objetivo Personaje al que se ataca.
	 */
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