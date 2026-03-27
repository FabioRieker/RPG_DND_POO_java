package habilidad;

import personajes.Personaje;
import estado.EstadoRenovar;

// Hechizo de luz sagrada que también cura
// Daño: 2d8, Coste: 15 PE + 20 PM
// Sana al objetivo y aplica Renovar
public class LuzSagrada extends HabilidadHibrida {

	public LuzSagrada() {
		super("Luz Sagrada", 15, 20, 2, 8, Estadistica.INTELIGENCIA, Efecto.NINGUNO);
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);

		// Solo cura y aplica renovar si impactó
		objetivo.curar(20);
		objetivo.aplicarEstado(new EstadoRenovar(3, 10));
		System.out.println("-- " + objetivo.getNombre() + " sanado + RENOVAR!");
	}
}