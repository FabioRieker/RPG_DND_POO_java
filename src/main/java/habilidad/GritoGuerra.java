package habilidad;

import personajes.Personaje;
import java.util.ArrayList;

/**
 * Grito híbrido que inspira a los aliados y aumenta su ataque.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class GritoGuerra extends HabilidadHibrida {

	/**
	 * Construye los costes y características del Grito de Guerra.
	 */
	public GritoGuerra() {
		super("Grito de Guerra", 20, 15, 0, 0, Estadistica.FUERZA, Efecto.NINGUNO);
	}

	@Override
	public void ejecutar(Personaje usuario, ArrayList<Personaje> aliados) {
		if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
			System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
			return;
		}

		usuario.consumirRecursos(costeEnergia, costeMana);

		System.out.println(usuario.getNombre() + " lanza un " + nombre + "!");
		System.out.println("Coste: " + costeEnergia + " PE + " + costeMana + " PM");
		System.out.println("--- ALIADOS RECIBEN +3 DE DAÑO ---");
		for (Personaje aliado : aliados) {
			System.out.println("-- " + aliado.getNombre() + " recibe +3 de daño!");
			aliado.aplicarEstado(new estado.EstadoFuria(3, 3));
		}
	}
}