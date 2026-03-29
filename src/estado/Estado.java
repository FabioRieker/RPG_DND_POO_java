package estado;

import personajes.Personaje;

/**
 * Clase base abstracta que centraliza la lógica de los estados alterados.
 * Permite que los personajes sufran efectos a lo largo del tiempo.
 * 
 * @author Ricardo Crespo y Fabio Rieker
 */
public abstract class Estado {
	protected String nombre;
	protected int turnosRestantes;
	protected int potencia;

	/**
	 * Constructor base para crear un estado alterado.
	 * 
	 * @param nombre   Nombre descriptivo del estado.
	 * @param turnos   Número de turnos que permanecerá activo.
	 * @param potencia Valor numérico del efecto (daño por turno, curación o
	 *                 bonificación).
	 */
	public Estado(String nombre, int turnos, int potencia) {
		this.nombre = nombre;
		this.turnosRestantes = turnos;
		this.potencia = potencia;
	}

	/**
	 * Se ejecuta una vez por turno mientras el estado está activo.
	 * 
	 * @param obj El personaje que sufre el efecto.
	 */
	public abstract void alPasarTurnoEstado(Personaje obj);

	/**
	 * Se ejecuta cuando el contador de turnos llega a cero y el estado desaparece.
	 * 
	 * @param obj El personaje que se libera del efecto.
	 */
	public abstract void alTerminarEstado(Personaje obj);

	/**
	 * Comprueba si el estado sigue vigente.
	 * 
	 * @return true si quedan turnos restantes, false si ha caducado.
	 */
	public boolean estaActivo() {
		// Un estado caduca cuando su duración baja a cero
		return turnosRestantes > 0;
	}

	/**
	 * Resta un turno a la duración total del estado.
	 */
	public void reducirTurno() {
		turnosRestantes--;
	}

	/**
	 * Obtiene el identificador del estado.
	 * 
	 * @return El nombre del estado en formato texto.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Permite a algunos estados alterar el daño base del personaje.
	 * 
	 * @return Cantidad de daño extra a sumar al ataque.
	 */
	public int getModificadorDaño() {
		return 0; // Por defecto, un estado genérico no aplica modificaciones de daño
	}
}