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
	 * Constructor para un arma disenada para el combate cerrado.
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
	 * añade el modificador de Fuerza al daño de la tirada.
	 * 
	 * @param atacante Personaje que asesta el golpe fisico.
	 * @param defensor Personaje receptor del ataque.
	 * @return daño procesado final.
	 */
	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// Tirada de arma + Modificador de Fuerza
		int dañoBase = tirarDados();
		// Se divide para equilibrar las estadisticas generales
		int bonoFuerza = atacante.getFuerza() / 2;
		return dañoBase + bonoFuerza;
	}
}
