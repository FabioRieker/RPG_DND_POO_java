package consumibles;

import personajes.Personaje;
import java.util.Random;

public class Bomba {

    private String nombre = "Bomba de Mano";
    private int cantidad;
    private Random dado = new Random();

    public Bomba(int cantidad) {
        this.cantidad = cantidad;
    }

    public void usar(Personaje objetivo) {
        if (cantidad <= 0) {
            System.out.println("No quedan " + nombre + ".");
            return;
        }

        cantidad--;

        int tirada = dado.nextInt(20) + 1;
        int daño = dado.nextInt(6) + 1 + dado.nextInt(6) + 1;

        System.out.println("Se lanza " + nombre + " a " + objetivo.getNombre() + "!");
        System.out.println("Tirada: " + tirada);

        if (tirada >= objetivo.getDefensaTotal()) {
            System.out.println("IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de dano.");
            objetivo.recibirDaño(daño, false);
        } else {
            System.out.println("FALLO! La bomba no alcanza el objetivo.");
        }
    }

    public int getCantidad() {
        return cantidad;
    }
}
