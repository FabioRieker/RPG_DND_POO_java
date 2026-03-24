package estado;

import personajes.Personaje;

public class EstadoCongelado extends Estado {

  public EstadoCongelado(int turnos, int potencia) {
    super("Congelado", turnos, potencia);
  }

  @Override
  public void alPasarTurnoEstado(Personaje obj) {
    System.out.println("[ESTADO] El frío hiela a " + obj.getNombre());
    obj.recibirDaño(potencia, true);
  }

  @Override
  public void alTerminarEstado(Personaje obj) {
    System.out.println("[ESTADO] " + obj.getNombre() + " se ha descongelado.");
  }
}
