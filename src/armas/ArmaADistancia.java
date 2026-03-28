package armas;

import personajes.Personaje;

public class ArmaADistancia extends Arma {

  public ArmaADistancia(String nombre, int cantidadDados, int carasDado) {
    super(nombre, cantidadDados, carasDado, CategoriaArma.distancia);
  }

  @Override
  public int calcularDaño(Personaje atacante, Personaje defensor) {
    // Tirada de arma + Modificador de Destreza
    int dañoBase = tirarDados();
    int bonoDestreza = atacante.getDestreza() / 2;

    return dañoBase + bonoDestreza;
  }
}
