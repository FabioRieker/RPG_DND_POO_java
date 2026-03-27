package armas;

import personajes.Personaje;
import estado.EstadoVeneno;

public class ColmilloArana extends ArmaCuerpoACuerpo {
	public ColmilloArana() {
		super("Colmillo de Araña", 1, 4);
	}

	@Override
	public void aplicarEfectosEspeciales(Personaje defensor) {
		if (!defensor.tieneEstado("Veneno")) {
			// aplico estado veneno: 3 turnos, 5 de daño por turno
			defensor.aplicarEstado(new EstadoVeneno(3, 5));
			System.out.println("! El Colmillo de Araña inyecta veneno en " + defensor.getNombre());
		}
	}
}