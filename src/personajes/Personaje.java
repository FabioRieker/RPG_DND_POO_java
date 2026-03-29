package personajes;

import armas.Arma;
import armas.CategoriaArma;
import armaduras.CategoriaArmadura;
import estado.Estado;
import habilidad.AccionCombate;
import consumibles.Consumible;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para todos los personajes del juego (héroes y enemigos). Define
 * sus atributos, equipo, recursos y acciones básicas en combate.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class Personaje {
	// --- ATRIBUTOS DE IDENTIDAD ---
	protected String nombre;
	protected Raza raza;
	protected TipoClase tipoClase;
	protected boolean vivo;
	protected String mensajePreparacion;
	protected boolean posturaDefensiva = false;
	protected boolean muroActivo = false;

	// --- STATS Y RECURSOS ---
	protected int fuerza, destreza, constitucion, inteligencia;
	protected int vidaMax, vidaActual, manaMax, manaActual, energiaMax, energiaActual;
	protected int defensaBase;

	// --- EQUIPO Y COLECCIONES ---
	protected Arma armaEquipada;
	protected CategoriaArmadura armaduraEquipada = CategoriaArmadura.NADA;
	protected List<CategoriaArma> armasPermitidas;
	protected List<CategoriaArmadura> armadurasPermitidas;
	protected ArrayList<Estado> estadosActivos = new ArrayList<>();
	protected ArrayList<AccionCombate> habilidades = new ArrayList<>();
	protected ArrayList<Consumible> inventario = new ArrayList<>();

	// --- CONSTRUCTOR ---
	/**
	 * Constructor principal para crear cualquier personaje.
	 * 
	 * @param nombre    Nombre del personaje.
	 * @param raza      Raza a la que pertenece.
	 * @param tipoClase Rol o clase principal (ej. Guerrero, Jefe).
	 * @param fue       Puntos iniciales de Fuerza.
	 * @param des       Puntos iniciales de Destreza.
	 * @param con       Puntos iniciales de Constitución.
	 * @param intel     Puntos iniciales de Inteligencia.
	 * @param defBase   Defensa pura inicial sin armaduras.
	 */
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

		// Fórmulas de recursos adaptadas de D&D
		this.vidaMax = (this.constitucion * 3) + 25;
		this.vidaActual = this.vidaMax;
		this.manaMax = (this.inteligencia * 3) + 15;
		this.manaActual = this.manaMax;
		this.energiaMax = (this.fuerza * 3) + 15;
		this.energiaActual = this.energiaMax;

		this.armasPermitidas = new ArrayList<>();
		this.armadurasPermitidas = new ArrayList<>();

		this.mensajePreparacion = "está preparándose...";
	}

	// --- SECCIÓN: INFORMACIÓN ---
	/**
	 * Muestra toda la ficha de estadísticas del personaje en la consola.
	 */
	public void mostrarInfo() {
		String B = motor.MotorCombate.ANSI_BEIGE;
		String R = motor.MotorCombate.ANSI_RESET;

		String arma = (armaEquipada != null) ? armaEquipada.getNombre() : "Desarmado";

		System.out.println(B + "╔════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("║  FICHA DE PERSONAJE                                                        ║");
		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  Nombre: " + nombre + "  |  Clase: " + tipoClase + "  |  Raza: " + raza);
		System.out.println("║  Estado: " + (vivo ? "VIVO" : "MUERTO"));
		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Recursos ---");
		System.out.println("║  Vida:    " + vidaActual + "/" + vidaMax);
		System.out.println("║  Mana:    " + manaActual + "/" + manaMax);
		System.out.println("║  Energia: " + energiaActual + "/" + energiaMax);
		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Estadisticas ---");
		System.out.println("║  FUE: " + fuerza + "  |  DES: " + getDestrezaTotal() + "  |  CON: " + constitucion
				+ "  |  INT: " + inteligencia);
		System.out.println("║  Defensa: " + getDefensaTotal() + " (base " + defensaBase + ")");
		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Equipo ---");
		System.out.println("║  Arma:     " + arma);
		System.out.println("║  Armadura: " + armaduraEquipada.nombre);
		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Habilidades ---");

		if (habilidades.isEmpty()) {
			System.out.println("║  (ninguna)");
		} else {
			for (int i = 0; i < habilidades.size(); i++) {
				System.out.println("║  " + (i + 1) + ". " + habilidades.get(i).getNombre());
			}
		}

		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Estados Activos ---");

		if (estadosActivos.isEmpty()) {
			System.out.println("║  (sin estados)");
		} else {
			for (int i = 0; i < estadosActivos.size(); i++) {
				System.out.println("║  - " + estadosActivos.get(i).getNombre());
			}
		}

		System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("║  --- Inventario ---");

		if (inventario.isEmpty()) {
			System.out.println("║  (vacio)");
		} else {
			for (int i = 0; i < inventario.size(); i++) {
				System.out.println("║  " + (i + 1) + ". " + inventario.get(i).getNombre());
			}
		}

		System.out.println("╚════════════════════════════════════════════════════════════════════════════╝" + R);
	}

	/**
	 * Imprime una pequeña barra de vida y el estado del personaje. Pensado para
	 * usarse rápido durante el combate.
	 */
	public void mostrarInfoBreve() {
		int celdasTotales = 10;
		int celdasLlenas = vidaMax > 0 ? (int) Math.round(((double) vidaActual / vidaMax) * celdasTotales) : 0;
		if (celdasLlenas == 0 && vidaActual > 0)
			celdasLlenas = 1;

		StringBuilder barraHp = new StringBuilder("[");
		for (int i = 0; i < celdasTotales; i++) {
			if (i < celdasLlenas) {
				barraHp.append("█");
			} else {
				barraHp.append(" ");
			}
		}
		barraHp.append("]");

		String estadoVivo = vivo ? "VIVO" : "MUERTO";
		String ident = nombre + " [" + tipoClase + "]";

		System.out.println(motor.MotorCombate.ANSI_BEIGE
				+ "╔════════════════════════════════════════════════════════════════════════════╗");
		System.out.printf("║ %-20s HP: %-15s %-9s STR: %-4d CON: %-4d ║%n", ident, barraHp.toString(),
				vidaActual + "/" + vidaMax, fuerza, constitucion);
		System.out.printf("║ %-20s ENERGIA: %-7s MANA: %-7s DEX: %-4d INT: %-4d ║%n", estadoVivo,
				energiaActual + "/" + energiaMax, manaActual + "/" + manaMax, destreza, inteligencia);
		System.out.println("╚════════════════════════════════════════════════════════════════════════════╝"
				+ motor.MotorCombate.ANSI_RESET);
	}

	// --- SECCIÓN: EQUIPO ---
	/**
	 * Permite que el personaje equipe un arma si su clase lo permite.
	 * 
	 * @param arma Arma a equipar.
	 */
	public void equiparArma(Arma arma) {
		if (this.armasPermitidas.contains(arma.getCategoria())) {
			this.armaEquipada = arma;
			System.out.println("[SISTEMA] " + this.nombre + " se ha equipado " + arma.getNombre());
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] " + this.tipoClase + " " + this.nombre
					+ " no sabe usar ese tipo de arma: " + arma.getCategoria() + motor.MotorCombate.ANSI_RESET);
		}
	}

	/**
	 * Equipa una armadura comprobando que el personaje sepa usarla.
	 * 
	 * @param categoria Tipo de armadura a poner.
	 */
	public void equiparArmadura(CategoriaArmadura categoria) {
		if (this.armadurasPermitidas.contains(categoria)) {
			this.armaduraEquipada = categoria;
			System.out.println("[SISTEMA] " + this.nombre + " se ha equipado " + categoria.nombre);
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] " + this.tipoClase + " " + this.nombre
					+ " no sabe usar ese tipo de armadura: " + categoria + motor.MotorCombate.ANSI_RESET);
		}
	}

	public Arma getArma() {
		return this.armaEquipada;
	}

	public List<CategoriaArma> getArmasPermitidas() {
		return this.armasPermitidas;
	}

	// --- SECCIÓN: ESTADÍSTICAS Y RECURSOS ---
	public int getDefensaTotal() {
		int total = defensaBase + armaduraEquipada.bonoDefensa;
		if (this.posturaDefensiva) {
			total += 15; // Bono de postura defensiva
		}
		if (this.muroActivo) {
			total += 30; // Bono del muro de piedra
		}
		return total;
	}

	public int getDestrezaTotal() {
		return destreza + armaduraEquipada.modificadorDestreza;
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

	public int getConstitucion() {
		return constitucion;
	}

	public String getNombre() {
		return nombre;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public TipoClase getTipoClase() {
		return this.tipoClase;
	}

	public boolean esEnemigo() {
		return this.tipoClase == TipoClase.MONSTRUO || this.tipoClase == TipoClase.JEFE;
	}

	public boolean esHeroe() {
		return !esEnemigo();
	}

	/**
	 * Comprueba si el personaje tiene puntos de vida.
	 * 
	 * @return true si tiene más de 0 HP.
	 */
	public boolean estaVivo() {
		return this.vidaActual > 0;
	}

	/**
	 * Cambia el estado del personaje a una postura para recibir menos daño.
	 */
	public void defenderse() {
		this.posturaDefensiva = true;
		System.out.println(motor.MotorCombate.ANSI_AMARILLO + "\n[DEFENSA] " + this.nombre
				+ " adopta una postura defensiva firme." + motor.MotorCombate.ANSI_RESET);
	}

	public void reiniciarDefensa() {
		this.posturaDefensiva = false;
		this.muroActivo = false;
	}

	public void aplicarMuroPiedra() {
		this.muroActivo = true;
	}

	public boolean isMuroActivo() {
		return this.muroActivo;
	}

	/**
	 * Cura vida al personaje sin sobrepasar su máximo de puntos.
	 * 
	 * @param cantidad Puntos de vida a sumar.
	 */
	public void curar(int cantidad) {
		this.vidaActual += cantidad;
		if (this.vidaActual > vidaMax) {
			this.vidaActual = vidaMax;
		}
		System.out.println(motor.MotorCombate.ANSI_MORADO + "[CURA] " + this.nombre + " recupera " + cantidad
				+ " puntos de vida." + motor.MotorCombate.ANSI_RESET);
	}

	/**
	 * Restaura maná y energía al mismo tiempo.
	 * 
	 * @param cantidad Puntos a rellenar en ambas estadísticas.
	 */
	public void recuperarRecursos(int cantidad) {
		this.energiaActual += cantidad;
		this.manaActual += cantidad;
		if (this.energiaActual > energiaMax) {
			this.energiaActual = energiaMax;
		}
		if (this.manaActual > manaMax) {
			this.manaActual = manaMax;
		}
		System.out.println(motor.MotorCombate.ANSI_AZUL + "[CURA] " + this.nombre + " recupera " + cantidad
				+ " SP y MP." + motor.MotorCombate.ANSI_RESET);
	}

	public boolean tieneRecursos(int energia, int mana) {
		return this.energiaActual >= energia && this.manaActual >= mana;
	}

	public void consumirRecursos(int energia, int mana) {
		this.energiaActual -= energia;
		this.manaActual -= mana;
	}

	// --- SECCIÓN: COMBATE ---
	/**
	 * Realiza un ataque físico básico contra un objetivo. Si tira un 20 en un dado
	 * virtual, es un golpe crítico y hace el doble de daño.
	 * 
	 * @param objetivo Enemigo que recibe el ataque.
	 */
	public void atacar(Personaje objetivo) {
		// Comprueba que ambos personajes estén con vida
		if (this.vivo == false || objetivo.estaVivo() == false) {
			return;
		}

		// Comprueba si sufre algún estado que le impide atacar

		// Cálculo del daño base
		int daño = 0;
		// Suma bonificaciones de daño de estados como la Furia
		int bonoDeEstados = this.getBonoDañoTotal();

		// Variable auxiliar para mostrar el mensaje de ataque
		String modo = "a puñetazos";

		if (this.armaEquipada != null) {
			daño = this.armaEquipada.calcularDaño(this, objetivo) + bonoDeEstados;

			// Detecta si el arma es cuerpo a cuerpo o a distancia
			String tipo = "cuerpo a cuerpo";
			if (this.armaEquipada.getCategoria() == armas.CategoriaArma.distancia) {
				tipo = "a distancia";
			}
			modo = tipo + " con " + this.armaEquipada.getNombre();
		} else {
			daño = (this.fuerza / 2) + bonoDeEstados;
		}

		int tiradaCritico = (int) (Math.random() * 20) + 1;
		if (tiradaCritico == 20) {
			String colorCrit = objetivo.esHeroe() ? motor.MotorCombate.ANSI_ROJO : motor.MotorCombate.ANSI_VERDE_OSCURO;
			System.out.println(colorCrit + "[CRÍTICO] " + this.nombre + " ha encontrado un punto vital."
					+ motor.MotorCombate.ANSI_RESET);
			daño = daño * 2;
		}

		// Mensaje unificado de ataque
		System.out.println("\n[ATAQUE] " + this.nombre + " ataca " + modo + " a " + objetivo.getNombre());

		// Aplica el efecto del arma antes de calcular la vida final
		if (this.armaEquipada != null) {
			this.aplicarEfectoDeArma(objetivo);
		}

		// Aplica la reducción de vida
		objetivo.recibirDaño(daño, false);
	}

	/**
	 * Resta puntos a la vida del personaje, reducidos por su nivel de defensa.
	 * Muestra si ha muerto en el proceso.
	 * 
	 * @param cantidad   Los puntos base de daño del ataque o efecto.
	 * @param esDañoPuro Si es verdadero, el ataque ignora toda reducción de
	 *                   armadura.
	 */
	public void recibirDaño(int cantidad, boolean esDañoPuro) {
		if (this.vivo == false) {
			return;
		}

		// Permite que el daño puro ignore la mitigación de la armadura
		int dañoFinal;
		if (esDañoPuro == true) {
			dañoFinal = cantidad;
		} else {
			int mitigacion = this.getDefensaTotal() / 4;
			dañoFinal = cantidad - mitigacion;
		}

		// Evita que un número negativo de daño cure al personaje
		if (dañoFinal < 0) {
			dañoFinal = 0;
		}

		this.vidaActual = this.vidaActual - dañoFinal;

		// Añade un prefijo especial para indicar la procedencia del daño
		String prefijo = "";
		if (esDañoPuro == true) {
			prefijo = "[PURO] ";
		}

		String colorDaño = this.esHeroe() ? motor.MotorCombate.ANSI_ROJO : motor.MotorCombate.ANSI_VERDE_OSCURO;
		System.out.println(colorDaño + "[DAÑO] " + prefijo + this.nombre + " recibe " + dañoFinal + " de daño."
				+ motor.MotorCombate.ANSI_RESET);

		if (this.vidaActual <= 0) {
			this.vidaActual = 0;
			this.vivo = false;
			System.out.println(colorDaño + "[ALERTA FATAL] " + this.nombre + " ha caído en combate."
					+ motor.MotorCombate.ANSI_RESET);
		}
	}

	public int getBonoDañoTotal() {
		int bonoTotal = 0;
		for (Estado e : estadosActivos) {
			bonoTotal += e.getModificadorDaño();
		}
		return bonoTotal;
	}

	// --- SECCIÓN: ESTADOS ---
	/**
	 * Agrega un estado alterado como veneno o quemadura si no lo tiene puesto.
	 * 
	 * @param nuevoEstado Estado a aplicar al personaje.
	 */
	public void aplicarEstado(Estado nuevoEstado) {
		if (!tieneEstado(nuevoEstado.getNombre())) {
			this.estadosActivos.add(nuevoEstado);
		}
	}

	// Llama a la lógica de estado que tienen algunas armas
	private void aplicarEfectoDeArma(Personaje objetivo) {
		if (this.armaEquipada != null) {
			this.armaEquipada.aplicarEfectosEspeciales(objetivo);
		}
	}

	// Comprueba que no tenga ya un estado con el mismo nombre para no duplicarlo
	public boolean tieneEstado(String nombreEstado) {
		for (Estado e : estadosActivos) {
			if (e.getNombre().equals(nombreEstado))
				return true;
		}
		return false;
	}

	/**
	 * Descuenta un turno a todos los estados activos y borra los caducados al
	 * inicio de cada ronda.
	 */
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

	public ArrayList<Estado> getEstadosActivos() {
		return estadosActivos;
	}

	// --- SECCIÓN: HABILIDADES ---
	public ArrayList<AccionCombate> getHabilidades() {
		return habilidades;
	}

	public void agregarHabilidad(AccionCombate h) {
		this.habilidades.add(h);
	}

	public void lanzarHechizo() {
		System.out.println(nombre + " " + mensajePreparacion);
	}

	public void agregarItem(Consumible item) {
		if (this.inventario.size() < 10) {
			this.inventario.add(item);
		} else {
			System.out.println(motor.MotorCombate.ANSI_AMARILLO + "[SISTEMA] El inventario de " + this.nombre
					+ " está lleno." + motor.MotorCombate.ANSI_RESET);
		}
	}

	public ArrayList<Consumible> getInventario() {
		return inventario;
	}

	public int getManaActual() {
		return manaActual;
	}

	public int getManaMax() {
		return manaMax;
	}

	public int getEnergiaActual() {
		return energiaActual;
	}

	public int getEnergiaMax() {
		return energiaMax;
	}
}