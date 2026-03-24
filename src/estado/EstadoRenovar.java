package estado;

import personajes.Personaje;

public class EstadoRenovar extends Estado {

  public EstadoRenovar(int turnos, int potencia) {
    super("Renovar", turnos, potencia);
  }

  @Override
  public void alPasarTurnoEstado(Personaje obj) {
    System.out.println("[ESTADO] La luz sana a " + obj.getNombre());
    obj.curar(potencia);
  }

  @Override
  public void alTerminarEstado(Personaje obj) {
    System.out.println("[ESTADO] El efecto de renovación termina en " + obj.getNombre());
  }
}
