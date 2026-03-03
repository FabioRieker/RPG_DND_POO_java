package personajes;

import armas.Arma;
import estado.Estado;
import java.util.ArrayList;

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

	}

	public void mostrarInfo() {
		System.out.println("------------------------------------------");
		System.out.println("PERSONAJE: " + nombre + " [" + tipoClase + " " + raza + "]");
		System.out.println("--- Estadísticas ---");
		System.out.println(
				"FUE: " + fuerza + " | DES: " + destreza + " | CON: " + constitucion + " | INT: " + inteligencia);
		System.out.println("--- Barras de Recursos ---");
		System.out.println("Vida: " + vidaActual + "/" + vidaMax);
		System.out.println("Maná: " + manaActual + "/" + manaMax);
		System.out.println("Energía: " + energiaActual + "/" + energiaMax);
		System.out.println("--- Estado de Combate ---");
		System.out.println("Defensa Base: " + defensaBase);
		System.out.println("Arma: " + (armaEquipada != null ? armaEquipada.getNombre() : "Desarmado"));
		System.out.println(" Vivo: " + (vivo ? "SÍ" : "NO"));
		System.out.println("------------------------------------------");
	}

	public void recibirDaño(int cantidad) {

		int mitigacion = this.defensaBase / 2;
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
			System.out.println(this.nombre + " recibe " + dañoFinal + " de daño. (Vida: " + this.vidaActual + "/"
					+ this.vidaMax + ")");
		}
	}

	protected Arma armaEquipada;

	public void equiparArma(Arma arma) {
		this.armaEquipada = arma;
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

	// he creado después la de tiene estado para q no aplique el mismo varias veces
	public void aplicarEstado(Estado nuevoEstado) {
		this.estadosActivos.add(nuevoEstado);
	}

	// compruebo si un personae ya tiene aplicado un estado con el mismo nombre
	public boolean tieneEstado(String nombreEstado) {
		for (Estado e : estadosActivos) {
			if (e.getNombre().equals(nombreEstado)) {
				return true;
			}
		}
		return false;
	}

	//uso iterator en vez de bucle for para no generar problemas al borrar (por si por ejemplo se cura un estado antes de tiempo)
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
	
	public void atacar(Personaje objetivo) {
	   //compruebo q ambos estén vivos
		if (!this.vivo || !objetivo.estaVivo()) {
	        return;
	    }

	    // calculo daño
	    int daño;
	    if (this.armaEquipada != null) {
	        daño = this.armaEquipada.calcularDaño(this, objetivo);
	    } else {
	        daño = this.fuerza / 2;
	    }

	    String mensaje = "\n" + this.nombre + " ataca a " + objetivo.getNombre();
	    if (this.armaEquipada != null) {
	        mensaje += " con " + this.armaEquipada.getNombre();
	    } else {
	        mensaje += " a puñetazos";
	    }
	    System.out.println(mensaje);
	    
	    // aplico daño al objetivo
	    objetivo.recibirDaño(daño);

	    // para cuando configuremos mejor q arma tiene qué efecto
	    if (this.armaEquipada != null) {
	        this.aplicarEfectoDeArma(objetivo);
	    }
	}

	//hasta q esté lo de las armas, de momento pongo aquí esto para forzar el veneno en la prueba
	private void aplicarEfectoDeArma(Personaje objetivo) {
	    if (!objetivo.tieneEstado("Veneno")) {
	        objetivo.aplicarEstado(new estado.EstadoVeneno(3, 5));
	        System.out.println("-- Se ha envenenado al enemigo!");
	    } else {
	        System.out.println("-- " + objetivo.getNombre() + " ya está evnvenenado");
	    }
	}
}
