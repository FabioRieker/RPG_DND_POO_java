package habilidad;

import personajes.Personaje;
import estado.EstadoCongelado;
import java.util.ArrayList;

/**
 * Magia de hielo de área que daña y congela a los enemigos.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class Ventisca extends HechizoMagico {

	/**
	 * Construye los costes y características de la Ventisca.
	 */
	public Ventisca() {
		super("Ventisca", 40, 2, 6, null);
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
		System.out.println("--- VENTISCA ---");

		for (Personaje obj : objetivos) {
			int tirada = dado.nextInt(20) + 1;
			int totalImpacto = tirada + bono;

			System.out.println("-> " + obj.getNombre() + " - Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

			if (totalImpacto >= obj.getDefensaTotal()) {
				aplicarEfectoImpacto(usuario, obj, bono);
			} else {
				System.out.println("  ¡FALLO! El hechizo no atraviesa la defensa.");
			}
		}
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("  ¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
		objetivo.recibirDaño(daño, false);

		int probabilidadCongelar = dado.nextInt(100) + 1;
		if (probabilidadCongelar <= 50 && !objetivo.tieneEstado("Congelado")) {
			objetivo.aplicarEstado(new EstadoCongelado(2, 3));
			System.out.println("  -- ¡" + objetivo.getNombre() + " está congelado!");
		}
	}
}