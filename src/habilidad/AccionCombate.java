package habilidad;

import java.util.Random;
import personajes.Personaje;

public abstract class AccionCombate {
	protected String nombre;
	protected int costeEnergia;
	protected int costeMana;
	protected int dadosCantidad;
	protected int dadosCaras;
	protected Estadistica estadisticaClave;
	protected Random dado = new Random();

	public enum Estadistica {
		FUERZA, DESTREZA, INTELIGENCIA, CONSTITUCION
	}

	public AccionCombate(String nombre, int costeEnergia, int costeMana, int dadosCantidad, int dadosCaras,
			Estadistica estadisticaClave) {
		this.nombre = nombre;
		this.costeEnergia = costeEnergia;
		this.costeMana = costeMana;
		this.dadosCantidad = dadosCantidad;
		this.dadosCaras = dadosCaras;
		this.estadisticaClave = estadisticaClave;
	}

	protected int tirarDados() {
		int total = 0;
		for (int i = 0; i < dadosCantidad; i++) {
			total += dado.nextInt(dadosCaras) + 1;
		}
		return total;
	}

	protected int getModificador(Personaje p) {
		switch (estadisticaClave) {
		case FUERZA:
			return p.getFuerza();
		case DESTREZA:
			return p.getDestreza();
		case INTELIGENCIA:
			return p.getInteligencia();
		case CONSTITUCION:
			return p.getConstitucion();
		default:
			return 0;
		}
	}

	public abstract void ejecutar(Personaje usuario, Personaje objetivo);

	public String getNombre() {
		return nombre;
	}

	public int getCosteEnergia() {
		return costeEnergia;
	}

	public int getCosteMana() {
		return costeMana;
	}
}
