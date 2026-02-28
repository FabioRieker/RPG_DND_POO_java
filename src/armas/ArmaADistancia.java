package armas;

import personajes.Personaje;

public class ArmaADistancia extends Arma {

  public ArmaADistancia(String nombre, int cantidadDados, int carasDado) {
    super(nombre, cantidadDados, carasDado);
  }

  @Override
  public int calcularDaño(Personaje atacante, Personaje defensor) {
    // Tirada de arma + Modificador de Destreza
    int dañoBase = tirarDados();
    int bonoDestreza = atacante.getDestreza() / 2;

    System.out.println(atacante.getNombre() + " ataca a distancia con " + this.nombre);
    return dañoBase + bonoDestreza;
  }
}
