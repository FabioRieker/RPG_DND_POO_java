package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;
import java.util.ArrayList;

/**
 * Magia nociva de área que hace un poco de daño inicial pero aplica Veneno.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public class NubeToxica extends HechizoMagico {

	/**
	 * Construye los costes y características de la Nube Tóxica.
	 */
	public NubeToxica() {
		super("Nube Tóxica", 35, 1, 6, null);
	}

	@Override
	public void ejecutar(Personaje usuario, Personaje objetivo) {
		System.out.println(usuario.getNombre() + " intenta usar " + nombre
				+ " en un solo objetivo, pero cancela al ver el derroche de maná.");
		System.out.println("-> Este hechizo solo debe usarse contra grupos de enemigos.");
	}

	@Override
	public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
		if (!usuario.tieneRecursos(0, costeMana)) {
			System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
			return;
		}

		usuario.consumirRecursos(0, costeMana);

		int bono = usuario.getInteligencia() / 2;

		System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
		System.out.println("--- NUBE TÓXICA ---");

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

		// Solo aplica veneno si impactó
		if (!objetivo.tieneEstado("Veneno")) {
			objetivo.aplicarEstado(new EstadoVeneno(3, 5));
			System.out.println("  -- ¡" + objetivo.getNombre() + " está envenenado!");
		}
	}
}