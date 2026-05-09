package armas;

import personajes.Personaje;
import estado.EstadoQuemadura;

/**
 * Arma legendaria que se obtiene como botín al derrotar a ciertos jefes. Aplica
 * daño de fuego residual y el estado alterado de quemadura al impactar.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class HojaFenix extends ArmaCuerpoACuerpo {
	/**
	 * Instancia la reliquia mítica con base estadística fija.
	 */
	public HojaFenix() {
		super("Hoja Fénix", 1, 8);
	}

	/**
	 * Añade periódicamente el perjuicio de quemaduras si logra penetrar.
	 * 
	 * @param defensor Personaje agredido.
	 */
	@Override
	public void aplicarEfectosEspeciales(Personaje defensor) {
		if (!defensor.tieneEstado("Quemadura")) {
			// Aplicar 4 puntos de daño por quemadura durante 3 turnos
			defensor.aplicarEstado(new EstadoQuemadura(3, 4));
			System.out.println("La Hoja Fénix prende fuego a " + defensor.getNombre());
		}
	}
}