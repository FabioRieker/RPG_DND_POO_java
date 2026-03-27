package estado;

import personajes.Personaje;

// Estado que da bonus de daño temporal
// Se aplica cuando un aliado usa BUFF_ALIADOS
public class EstadoFuria extends Estado {

    public EstadoFuria(int turnos, int potencia) {
        super("Furia", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println("[ESTADO] " + obj.getNombre() + " está enfurecido con +" + potencia + " de daño!");
    }
    
    public int getModificadorDaño() {
        return this.potencia; // 
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println("[ESTADO] La furia de " + obj.getNombre() + " se ha calmado.");
    }
}
