package estado;

import java.util.Random;

import personajes.Personaje;

public class EstadoVeneno extends Estado {
    
    public EstadoVeneno(int turnos, int potencia) {
    	super("Veneno", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println("[ESTADO] El veneno afecta a " + obj.getNombre());
        obj.recibirDaño(potencia, true); 
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println("[ESTADO] El veneno se ha disipado de " + obj.getNombre());
    }
}