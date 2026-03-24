package estado;

import personajes.Personaje;

public class EstadoLisiado extends Estado {

    public EstadoLisiado(int turnos, int potencia) {
        super("Lisiado", turnos, potencia);
    }

    @Override
    public void alPasarTurnoEstado(Personaje obj) {
        System.out.println("[ESTADO] " + obj.getNombre() + " esta lisiado y se mueve con dificultad.");
    }

    @Override
    public void alTerminarEstado(Personaje obj) {
        System.out.println("[ESTADO] " + obj.getNombre() + " ya no esta lisiado.");
    }
}
