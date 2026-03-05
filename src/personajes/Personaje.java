package personajes;

import armas.Arma;
import armas.CategoriaArma;
import armaduras.Armadura;
import armaduras.CategoriaArmadura;
import estado.Estado;
import java.util.ArrayList;
import java.util.List;

public abstract class Personaje {
	protected String nombre;
	protected Raza raza;
	protected TipoClase tipoClase;

	// Stats y Recursos
	protected int fuerza, destreza, constitucion, inteligencia;
	protected int vidaMax, vidaActual, manaMax, manaActual, energiaMax, energiaActual;
	protected int defensaBase;
	protected boolean vivo;
	
	protected ArrayList<Estado> estadosActivos = new ArrayList<>();
	protected Arma armaEquipada;
	protected Armadura armaduraEquipada;
	protected List<CategoriaArma> armasPermitidas;
	protected List<CategoriaArmadura> armadurasPermitidas;

	public Personaje(String nombre, Raza raza, TipoClase tipoClase, int fue, int des, int con, int intel, int defBase) {
		this.nombre = nombre;
		this.raza = raza;
		this.tipoClase = tipoClase;
		this.fuerza = fue;
		this.destreza = des;
		this.constitucion = con;
		this.inteligencia = intel;
		this.defensaBase = defBase;
		this.vivo = true;

		// Fórmulas de recursos
		this.vidaMax = (this.constitucion * 5) + 50;
		this.vidaActual = this.vidaMax;
		this.manaMax = (this.inteligencia * 5) + 20;
		this.manaActual = this.manaMax;
		this.energiaMax = (this.fuerza * 5) + 20;
		this.energiaActual = this.energiaMax;
		
		this.armasPermitidas = new ArrayList<>();
		this.armadurasPermitidas = new ArrayList<>();
	}

	public void mostrarInfo() {
		System.out.println("------------------------------------------");
		System.out.println("PERSONAJE: " + nombre + " [" + tipoClase + " " + raza + "]");
		System.out.println("--- Estadísticas ---");
		System.out.println("FUE: " + fuerza + " | DES: " + getDestrezaTotal() +
				" | CON: " + constitucion + " | INT: " + inteligencia);
		System.out.println("--- Barras de Recursos ---");
		System.out.println("Vida: " + vidaActual + "/" + vidaMax);
		System.out.println("Maná: " + manaActual + "/" + manaMax);
		System.out.println("Energía: " + energiaActual + "/" + energiaMax);
		System.out.println("--- Estado de Combate ---");
		System.out.println("Defensa Base: " + defensaBase);
		System.out.println("Defensa Total: " + getDefensaTotal());
		System.out.println("Armadura: " + (armaduraEquipada != null ? armaduraEquipada.getNombre() : "Sin armadura"));
		System.out.println("Arma: " + (armaEquipada != null ? armaEquipada.getNombre() : "Desarmado"));
		System.out.println(" Vivo: " + (vivo ? "SÍ" : "NO"));
		System.out.println("------------------------------------------");
	}

	public void recibirDaño(int cantidad) {
		int mitigacion = this.getDefensaTotal() / 2;
		int dañoFinal = cantidad - mitigacion;

		if (dañoFinal < 0) {
			dañoFinal = 0;
		}

		this.vidaActual -= dañoFinal;
		if (this.vidaActual <= 0) {
			this.vidaActual = 0;
			this.vivo = false;
			System.out.println(this.nombre + " ha caído en combate.");
		} else {
			System.out.println(this.nombre + " recibe " + dañoFinal + " de daño. (Vida: " + this.vidaActual + "/" + this.vidaMax + ")");
		}
	}

	public void equiparArma(Arma arma) {
		if (this.armasPermitidas.contains(arma.getCategoria())) {
			this.armaEquipada = arma;
			System.out.println(this.nombre + " se ha equipado " + arma.getNombre());
		} else {
			System.out.println(this.tipoClase + " " + this.nombre + " no sabe usar ese tipo de arma: " + arma.getCategoria());
		}
	}

	public void equiparArmadura(Armadura armadura) {
		if (this.armadurasPermitidas.contains(armadura.getCategoria())) {
			this.armaduraEquipada = armadura;
			System.out.println(this.nombre + " se ha equipado " + armadura.getNombre());
		} else {
			System.out.println(this.tipoClase + " " + this.nombre + " no sabe usar ese tipo de armadura: " + armadura.getCategoria());
		}
	}

	public int getDefensaTotal() {
		return (armaduraEquipada != null) ? defensaBase + armaduraEquipada.getBonoDefensa() : defensaBase;
	}

	public int getDestrezaTotal() {
		return (armaduraEquipada != null) ? destreza - armaduraEquipada.getPenalizacionDestreza() : destreza;
	}

	public Arma getArma() { return this.armaEquipada; }
	public int getFuerza() { return fuerza; }
	public int getDestreza() { return destreza; }
	public int getInteligencia() { return inteligencia; }
	public String getNombre() { return nombre; }
	public boolean estaVivo() { return this.vidaActual > 0; }

	public void aplicarEstado(Estado nuevoEstado) {
		this.estadosActivos.add(nuevoEstado);
	}

	public boolean tieneEstado(String nombreEstado) {
		for (Estado e : estadosActivos) {
			if (e.getNombre().equals(nombreEstado)) return true;
		}
		return false;
	}

	public void pasarTurnoDeEstados() {
		if (!this.vivo || estadosActivos.isEmpty()) return;

		java.util.Iterator<Estado> it = estadosActivos.iterator();
		while (it.hasNext()) {
			Estado e = it.next();
			e.alPasarTurnoEstado(this);
			e.reducirTurno();

			if (!e.estaActivo()) {
				e.alTerminarEstado(this);
				it.remove();
			}
		}
	}

	public void atacar(Personaje objetivo) {
		if (!this.vivo || !objetivo.estaVivo()) return;

		int daño = (this.armaEquipada != null) ? this.armaEquipada.calcularDaño(this, objetivo) : this.fuerza / 2;

		System.out.println("\n" + this.nombre + " ataca a " + objetivo.getNombre() + 
				(this.armaEquipada != null ? " con " + this.armaEquipada.getNombre() : " a puñetazos"));
		
		objetivo.recibirDaño(daño);

		if (this.armaEquipada != null) {
			this.aplicarEfectoDeArma(objetivo);
		}
	}

	private void aplicarEfectoDeArma(Personaje objetivo) {
		if (!objetivo.tieneEstado("Veneno")) {
			objetivo.aplicarEstado(new estado.EstadoVeneno(3, 5));
			System.out.println("-- Se ha envenenado al enemigo!");
		}
	}
}