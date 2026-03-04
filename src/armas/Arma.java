package armas;

import personajes.Personaje;
import java.util.Random;

import armaduras.CategoriaArmadura;

public abstract class Arma {
  protected String nombre;
  protected int cantidadDados;
  protected int carasDado;
  protected Random dado;
  protected CategoriaArma categoria;

  public Arma(String nombre, int cantidadDados, int carasDado, CategoriaArma categoria) {
    this.nombre = nombre;
    this.cantidadDados = cantidadDados;
    this.carasDado = carasDado;
    this.dado = new Random();
  }

  public String getNombre() {
    return nombre;
  }

  public CategoriaArma getCategoria() {
    return categoria;
  }

  protected int tirarDados() {
    int total = 0;
    for (int i = 0; i < cantidadDados; i++) {
      total += dado.nextInt(carasDado) + 1;
    }
    return total;
  }

  public abstract int calcularDaño(Personaje atacante, Personaje defensor);
}
