package armas;

import personajes.Personaje;

/**
 * Define un tipo de arma que utiliza Destreza para calcular el daño al
 * impactar.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ArmaADistancia extends Arma {

	/**
	 * Constructor para un arma de proyectiles o arrojadiza.
	 * 
	 * @param nombre        Nombre identificador.
	 * @param cantidadDados Sumatorio de dados que requiere.
	 * @param carasDado     Tipo de dado utilizado (ej. dado de 6 caras).
	 */
	public ArmaADistancia(String nombre, int cantidadDados, int carasDado) {
		super(nombre, cantidadDados, carasDado, CategoriaArma.distancia);
	}

	/**
	 * Añade el modificador de Destreza al resultado del dado base.
	 * 
	 * @param atacante Personaje que ejecuta el letal.
	 * @param defensor Personaje objetivo del disparo.
	 * @return Daño procesado final.
	 */
	@Override
	public int calcularDaño(Personaje atacante, Personaje defensor) {
		// Tirada de arma + Modificador de Destreza
		int dañoBase = tirarDados();
		int bonoDestreza = atacante.getDestreza() / 2;

		return dañoBase + bonoDestreza;
	}
}
