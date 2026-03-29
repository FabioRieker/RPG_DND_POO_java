package armas;

import personajes.Personaje;

/**
 * Define un tipo de arma que utiliza Fuerza bruta para calcular el daño al
 * impactar.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ArmaCuerpoACuerpo extends Arma {

	/**
	 * Constructor para un arma diseñada para el combate cerrado.
	 * 
	 * @param nombre        Nombre identificador.
	 * @param cantidadDados Sumatorio de dados base.
	 * @param carasDado     Matriz o tipo de dado utilizado.
	 */
	public ArmaCuerpoACuerpo(String nombre, int cantidadDados, int carasDado) {
		// La categoria de arma hace que se diferencie el tipo de arma
		super(nombre, cantidadDados, carasDado, CategoriaArma.melee);
	}

	/**
	 * Añade el modificador de Fuerza al daño de la tirada.
	 * 
	 * @param atacante Personaje que asesta el golpe físico.
	 * @param defensor Personaje receptor del ataque.
	 * @return Daño procesado final.
	 */
	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// Tirada de arma + Modificador de Fuerza
		int dañoBase = tirarDados();
		// Se divide para equilibrar las estadísticas generales
		int bonoFuerza = atacante.getFuerza() / 2;
		return dañoBase + bonoFuerza;
	}
}
