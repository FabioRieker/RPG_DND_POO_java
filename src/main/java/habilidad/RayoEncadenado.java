package habilidad;

import personajes.Personaje;
import java.util.ArrayList;

/**
 * Magia eléctrica que causa daño pero su verdadero poder es afectar a múltiples
 * objetivos a la vez.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class RayoEncadenado extends HechizoMagico {

	/**
	 * Construye los costes y características del Rayo Encadenado.
	 */
	public RayoEncadenado() {
		super("Rayo Encadenado", 30, 2, 8, "MULTI");
	}

	@Override
	public void ejecutar(Personaje usuario, Personaje objetivo) {
		System.out.println(usuario.getNombre() + " quiere usar " + nombre + "...");
		System.out.println("Este hechizo afecta a múltiples objetivos. Usa la versión con lista de enemigos.");
	}

	@Override
	public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
		if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
			System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
			return;
		}

		usuario.consumirRecursos(costeEnergia, costeMana);
		int bono = usuario.getInteligencia() / 2;

		System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
		System.out.println("--- RAYO ENCADENADO ---");

		for (Personaje obj : objetivos) {
			int tirada = dado.nextInt(20) + 1;
			int totalImpacto = tirada + bono;

			System.out.println("-> " + obj.getNombre() + " - Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

			if (totalImpacto >= obj.getDefensaTotal()) {
				aplicarEfectoImpacto(usuario, obj, bono);
			} else {
				System.out.println("  ¡FALLO! El rayo no atraviesa la defensa.");
			}
		}
	}
}