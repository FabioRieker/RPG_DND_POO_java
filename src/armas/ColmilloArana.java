package armas;

import personajes.Personaje;
import estado.EstadoVeneno;

/**
 * Arma legendaria que se obtiene como botín al derrotar a ciertos jefes. Aplica
 * daño extra y el estado alterado de veneno al golpear.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class ColmilloArana extends ArmaCuerpoACuerpo {
	/**
	 * Instancia el arma legendaria precargada con sus estadísticas inamovibles.
	 */
	public ColmilloArana() {
		super("Colmillo de Araña", 1, 4);
	}

	/**
	 * Realiza la comprobación e inoculación del estado alterado Veneno sobre el
	 * receptor.
	 * 
	 * @param defensor Combatiente impactado por la cuchilla.
	 */
	@Override
	public void aplicarEfectosEspeciales(Personaje defensor) {
		if (!defensor.tieneEstado("Veneno")) {
			// Aplicar 5 puntos de daño por veneno continuo durante 3 turnos
			defensor.aplicarEstado(new EstadoVeneno(3, 5));
			System.out.println("! El Colmillo de Araña inyecta veneno en " + defensor.getNombre());
		}
	}
}