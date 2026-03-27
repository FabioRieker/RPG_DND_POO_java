package habilidad;

import personajes.Personaje;
import estado.EstadoFuria;
import java.util.ArrayList;

// Habilidades que usan tanto energia como mana
// Útiles para clases híbridas como el Paladín
public class HabilidadHibrida extends AccionCombate {

	private Efecto efecto;

	public HabilidadHibrida(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
			Estadistica estadistica, Efecto efecto) {
		super(nombre, costeEnergia, costeMana, dadosCantidad, dadosCaras, estadistica);
		this.efecto = efecto;
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");

		// Explosion Arcana hace daño puro
		boolean esPuro = this.nombre.equals("Explosión Arcana");
		objetivo.recibirDaño(daño, esPuro);

		aplicarEfectoEspecial(usuario, objetivo, daño);
	}

	private void aplicarEfectoEspecial(Personaje usuario, Personaje objetivo, int daño) {
		switch (efecto) {
		case CURAR_VIDA:
			usuario.curar(daño);
			System.out.println("-- " + usuario.getNombre() + " se cura " + daño + " HP!");
			break;
		case ROBO_VIDA:
			usuario.curar(daño);
			System.out.println("-- " + usuario.getNombre() + " roba " + daño + " HP!");
			break;
		case BUFF_ALIADOS:
			System.out.println("-- " + objetivo.getNombre() + " recibe +" + daño + " de daño temporal!");
			objetivo.aplicarEstado(new EstadoFuria(3, daño));
			break;
		case NINGUNO:
		default:
			break;
		}
	}

	public void ejecutar(Personaje usuario, ArrayList<Personaje> objetivos) {
		for (Personaje objetivo : objetivos) {
			ejecutar(usuario, objetivo);
		}
	}
}