package consumibles;

import personajes.Personaje;
import java.util.Random;

public class CuchilloArrojadizo {

  private String nombre = "Cuchillo Arrojadizo";
  private int cantidad;
  private Random dado = new Random();

  public CuchilloArrojadizo(int cantidad) {
    this.cantidad = cantidad;
  }

  public void usar(Personaje objetivo) {
    if (cantidad <= 0) {
      System.out.println("No quedan " + nombre + ".");
      return;
    }

    cantidad--;

    int tirada = dado.nextInt(20) + 1;
    int daño = dado.nextInt(4) + 1;

    System.out.println("Se lanza " + nombre + " a " + objetivo.getNombre() + "!");
    System.out.println("Tirada: " + tirada);

    if (tirada >= objetivo.getDefensaTotal()) {
      System.out.println("IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de dano.");
      objetivo.recibirDaño(daño, false);
    } else {
      System.out.println("FALLO! El cuchillo no alcanza el objetivo.");
    }
  }

  public int getCantidad() {
    return cantidad;
  }
}
