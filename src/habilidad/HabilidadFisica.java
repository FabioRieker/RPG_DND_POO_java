package habilidad;

import personajes.Personaje;
import estado.EstadoVeneno;
import estado.EstadoAturdimiento;
import estado.EstadoSangrado;
import estado.EstadoLisiado;

/**
 * Una categoría de habilidad que solo gasta energía y depende de la Fuerza o
 * Destreza. Pueden provocar efectos de sangre, veneno o aturdimiento.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class HabilidadFisica extends AccionCombate {

	protected String efectoEspecial; // Tipo de efecto secundario

	/**
	 * Crea la base para una habilidad física rápida o fuerte.
	 * 
	 * @param nombre        Nombre de la destreza.
	 * @param costeEnergia  Energía que gasta.
	 * @param dadosCantidad Cuántos dados genera de daño.
	 * @param dadosCaras    Caras de cada dado.
	 * @param estadistica   Puntos de Fuerza / Destreza que escalan el ataque.
	 * @param efecto        Nombre del estado alterado a infligir.
	 */
	public HabilidadFisica(String nombre, int costeEnergia, int dadosCantidad, int dadosCaras, Estadistica estadistica,
			String efecto) {
		super(nombre, costeEnergia, 0, dadosCantidad, dadosCaras, estadistica);
		this.efectoEspecial = efecto;
	}

	/**
	 * Calcula el golpe físico cuando la tirada acierta y daña al objetivo. También
	 * le pasa el estado alterado si toca.
	 * 
	 * @param usuario  Personaje que ataca.
	 * @param objetivo Personaje herido.
	 * @param bono     Daño extra derivado de la tirada.
	 */
	@Override
	protected void aplicarEfectoImpacto(Personaje usuario, Personaje objetivo, int bono) {
		int daño = tirarDados() + bono;
		System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño");
		objetivo.recibirDaño(daño, false);
		aplicarEfectoSecundario(objetivo);
	}

	/**
	 * Comprueba el tipo especial del golpe (ej. VENENO) y fuerza la aplicación del
	 * estado correspondiente en el contrario.
	 * 
	 * @param objetivo Enemigo que sufre las consecuencias.
	 */
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