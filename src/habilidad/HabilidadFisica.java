package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;
import estado.EstadoAturdimiento;
import estado.EstadoSangrado;
import estado.EstadoLisiado;

// Habilidades físicas: consumen energia, escalan con FUE o DES
// Incluyen efectos como Veneno, Aturdir, Sangrado, Lisiado
public abstract class HabilidadFisica extends AccionCombate {

	protected String efectoEspecial; // Tipo de efecto secundario

	public HabilidadFisica(String nombre, int costeEnergia, int dadosCantidad, int dadosCaras, Estadistica estadistica,
			String efecto) {
		super(nombre, costeEnergia, 0, dadosCantidad, dadosCaras, estadistica);
		this.efectoEspecial = efecto;
	}

	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño");
		objetivo.recibirDaño(daño, false);
		aplicarEfectoSecundario(objetivo);
	}

	// Aplica el efecto secundario según el tipo
	protected void aplicarEfectoSecundario(Personaje objetivo) {
		if (efectoEspecial == null)
			return;

		switch (efectoEspecial) {
		case "VENENO":
			if (!objetivo.tieneEstado("Veneno")) {
				objetivo.aplicarEstado(new EstadoVeneno(3, 5));
				System.out.println("-- ¡" + objetivo.getNombre() + " está envenenado!");
			}
			break;
		case "ATURDIR":
			if (!objetivo.tieneEstado("Aturdimiento")) {
				objetivo.aplicarEstado(new EstadoAturdimiento(2));
				System.out.println("-- ¡" + objetivo.getNombre() + " está aturdido!");
			}
			break;
		case "SANGRADO":
			if (!objetivo.tieneEstado("Sangrado")) {
				objetivo.aplicarEstado(new EstadoSangrado(3, 4));
				System.out.println("-- ¡" + objetivo.getNombre() + " está sangrando!");
			}
			break;
		case "LISIADO":
			if (!objetivo.tieneEstado("Lisiado")) {
				objetivo.aplicarEstado(new EstadoLisiado(2, 5));
				System.out.println("-- ¡" + objetivo.getNombre() + " está lisiado!");
			}
			break;
		}
	}

	public void ejecutar(Personaje usuario, java.util.ArrayList<Personaje> objetivos) {
		for (Personaje objetivo : objetivos) {
			ejecutar(usuario, objetivo);
		}
	}
}