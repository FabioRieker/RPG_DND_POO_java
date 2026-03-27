package personajes;

import armas.Arma;
import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.Estado;
import habilidad.AccionCombate;
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
	protected CategoriaArmadura armaduraEquipada = CategoriaArmadura.NADA;
	protected List<CategoriaArma> armasPermitidas;
	protected List<CategoriaArmadura> armadurasPermitidas;
	protected ArrayList<AccionCombate> habilidades = new ArrayList<>();

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

		// formulas de recursos
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
		System.out.println("FUE: " + fuerza + " | DES: " + getDestrezaTotal() + " | CON: " + constitucion + " | INT: "
				+ inteligencia);
		System.out.println("--- Barras de Recursos ---");
		System.out.println("Vida: " + vidaActual + "/" + vidaMax);
		System.out.println("Maná: " + manaActual + "/" + manaMax);
		System.out.println("Energía: " + energiaActual + "/" + energiaMax);
		System.out.println("--- Estado de Combate ---");
		System.out.println("Defensa Base: " + defensaBase);
		System.out.println("Defensa Total: " + getDefensaTotal());
		System.out.println("Armadura: " + armaduraEquipada.nombre);
		System.out.println("Arma: " + (armaEquipada != null ? armaEquipada.getNombre() : "Desarmado"));
		System.out.println(" Vivo: " + (vivo ? "SÍ" : "NO"));
		System.out.println("------------------------------------------");
	}

	// para antes o después de un turno, mostrar solo lo imprescindible
	public void mostrarInfoBreve() {
		System.out.println("   > " + nombre + ": " + vidaActual + "/" + vidaMax + " HP | Estados: "
				+ (estadosActivos.isEmpty() ? "Ninguno" : estadosActivos.get(0).getNombre()));
	}

	public void equiparArma(Arma arma) {
		if (this.armasPermitidas.contains(arma.getCategoria())) {
			this.armaEquipada = arma;
			System.out.println(this.nombre + " se ha equipado " + arma.getNombre());
		} else {
			System.out.println(
					this.tipoClase + " " + this.nombre + " no sabe usar ese tipo de arma: " + arma.getCategoria());
		}
	}

	public void equiparArmadura(CategoriaArmadura categoria) {
		if (this.armadurasPermitidas.contains(categoria)) {
			this.armaduraEquipada = categoria;
			System.out.println(this.nombre + " se ha equipado " + categoria.nombre);
		} else {
			System.out.println(this.tipoClase + " " + this.nombre + " no sabe usar ese tipo de armadura: " + categoria);
		}
	}

	public int getDefensaTotal() {
		return defensaBase + armaduraEquipada.bonoDefensa;
	}

	public int getDestrezaTotal() {
		return destreza + armaduraEquipada.modificadorDestreza;
	}

	public Arma getArma() {
		return this.armaEquipada;
	}

	public int getFuerza() {
		return fuerza;
	}

	public int getDestreza() {
		return destreza;
	}

	public int getInteligencia() {
		return inteligencia;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean estaVivo() {
		return this.vidaActual > 0;
	}

	public void atacar(Personaje objetivo) {
		// compruebo q ambos personajes estén vivos
		if (this.vivo == false || objetivo.estaVivo() == false) {
			return;
		}

		// compruebo si sufre algun estado que le impide atacar
		if (tieneEstado("Aturdimiento") || tieneEstado("Congelado")) {
			System.out.println("! " + this.nombre + " intenta atacar pero está incapacitado.");
			return;
		}

		// calculo daño
		int daño = 0;
		// para habilidades o estados que afectan al daño (como furia)
		int bonoDeEstados = this.getBonoDañoTotal();
		if (this.armaEquipada != null) {
			daño = this.armaEquipada.calcularDaño(this, objetivo) + bonoDeEstados;
		} else {
			daño = (this.fuerza / 2) + bonoDeEstados;
		}

		System.out.println("\n" + this.nombre + " ataca a " + objetivo.getNombre());

		// aplico efecto del arma antes del daño (arregla situaciones como q el
		// personaje muera y se envenene después)
		if (this.armaEquipada != null) {
			this.aplicarEfectoDeArma(objetivo);
		}

		// aplico daño
		objetivo.recibirDaño(daño, false);
	}

	public void recibirDaño(int cantidad, boolean esDañoPuro) {
		if (this.vivo == false) {
			return;
		}

		// para estados o ataques especiales q se salten la mitigación
		int dañoFinal;
		if (esDañoPuro == true) {
			dañoFinal = cantidad;
		} else {
			int mitigacion = this.getDefensaTotal() / 2;
			dañoFinal = cantidad - mitigacion;
		}

		// evita q el daño negativo cure al personaje
		if (dañoFinal < 0) {
			dañoFinal = 0;
		}

		this.vidaActual = this.vidaActual - dañoFinal;

		// muestro el daño del ataque
		String prefijo = "";
		if (esDañoPuro == true) {
			prefijo = "[PURO] ";
		}
		System.out.println(prefijo + this.nombre + " recibe " + dañoFinal + " de daño.");

		if (this.vidaActual <= 0) {
			this.vidaActual = 0;
			this.vivo = false;
			System.out.println(">>> " + this.nombre + " ha caído en combate.");
		}
	}

	// -----ESTADOS-----

	// he creado después la de tiene estado para q no aplique el mismo varias veces
	public void aplicarEstado(Estado nuevoEstado) {
		this.estadosActivos.add(nuevoEstado);
	}

	// para aplicar estados alterados (resuelve el problema del veneno siempre
	// aplicándose)
	private void aplicarEfectoDeArma(Personaje objetivo) {
		if (this.armaEquipada != null) {
			this.armaEquipada.aplicarEfectosEspeciales(objetivo);
		}
	}

	// compruebo si un personae ya tiene aplicado un estado con el mismo nombre
	public boolean tieneEstado(String nombreEstado) {
		for (Estado e : estadosActivos) {
			if (e.getNombre().equals(nombreEstado))
				return true;
		}
		return false;
	}

	// uso iterator en vez de bucle for para no generar problemas al borrar (por si
	// por ejemplo se cura un estado antes de tiempo)
	public void pasarTurnoDeEstados() {
		if (!this.vivo || estadosActivos.isEmpty())
			return;

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

	// -----HABILIDADES-----

	public ArrayList<AccionCombate> getHabilidades() {
		return habilidades;
	}

	public void agregarHabilidad(AccionCombate h) {
		this.habilidades.add(h);
	}

	public boolean tieneRecursos(int energia, int mana) {
		return this.energiaActual >= energia && this.manaActual >= mana;
	}

	public void consumirRecursos(int energia, int mana) {
		this.energiaActual -= energia;
		this.manaActual -= mana;
	}

	public ArrayList<Estado> getEstadosActivos() {
		return estadosActivos;
	}

	public int getConstitucion() {
		return constitucion;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public void curar(int cantidad) {
		this.vidaActual += cantidad;
		if (this.vidaActual > vidaMax) {
			this.vidaActual = vidaMax;
		}
		System.out.println(this.nombre + " se ha curado " + cantidad + " puntos de vida.");
	}

	public void recuperarRecursos(int cantidad) {
		this.energiaActual += cantidad;
		this.manaActual += cantidad;
		if (this.energiaActual > energiaMax) {
			this.energiaActual = energiaMax;
		}
		if (this.manaActual > manaMax) {
			this.manaActual = manaMax;
		}
		System.out.println(this.nombre + " recupera " + cantidad + " SP y MP.");
	}

	public int getBonoDañoTotal() {
		int bonoTotal = 0;
		for (Estado e : estadosActivos) {
			bonoTotal += e.getModificadorDaño();
		}
		return bonoTotal;
	}
}
