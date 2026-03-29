package personajes;

import armas.CategoriaArma;
import estado.EstadoQuemadura;
import armaduras.CategoriaArmadura;

/**
 * Jefe de tipo Dragón. Tiene mucha fuerza bruta y un aliento poderoso.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class JefeDragon extends Jefe {

	/**
	 * Crea un Jefe Dragón con estadísticas base.
	 * 
	 * @param nombre  Nombre del dragón.
	 * @param fue     Fuerza.
	 * @param des     Destreza.
	 * @param con     Constitución.
	 * @param intel   Inteligencia.
	 * @param defBase Defensa inicial sin equipamiento.
	 */
	public JefeDragon(String nombre, int fue, int des, int con, int intel, int defBase) {
		super(nombre, Raza.DRACONIDO, TipoJefe.DRAGON, fue, des, con, intel, defBase);

		this.armasPermitidas.add(CategoriaArma.melee);
		this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
	}

	/**
	 * Usa su aliento para infligir gran cantidad de daño directo.
	 * 
	 * @param objetivo Personaje al que lanza el aliento.
	 */
	@Override
	public void habilidadEspecial(Personaje objetivo) {
		// Se unen los mensajes para darle más impacto narrativo
		System.out.println("\n¡" + nombre + " usa su Aliento de Dragón!");
		System.out.println("¡Exhala una ráfaga de fuego destructor sobre " + objetivo.getNombre() + "!");

		int dañoFuego = 15 + (this.inteligencia / 2);

		objetivo.recibirDaño(dañoFuego, false);

		if (!objetivo.tieneEstado("Quemadura")) {
			objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
		}
	}
}
