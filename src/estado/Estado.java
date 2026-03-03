package estado;

import personajes.Personaje;

public abstract class Estado {
    protected String nombre;
    protected int turnosRestantes;
    protected int potencia;

    public Estado(String nombre, int turnos, int potencia) {
        this.nombre = nombre;
        this.turnosRestantes = turnos;
        this.potencia = potencia;
    }

    public abstract void alPasarTurnoEstado(Personaje obj);
    public abstract void alTerminarEstado(Personaje obj);

    
    public boolean estaActivo() {
        return turnosRestantes > 0;
    }

    public void reducirTurno() {
        turnosRestantes--;
    }
    
    public String getNombre() {
        return nombre;
    }
}