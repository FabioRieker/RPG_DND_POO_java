package habilidad;

import personajes.Personaje;
import java.util.ArrayList;

// Grito motivacional para los aliados
// Daño: ninguno, Coste: 20 PE + 15 PM
// Mejora el daño de los aliados
public class GritoGuerra extends HabilidadHibrida {

  public GritoGuerra() {
    super("Grito de Guerra", 20, 15, 0, 0, Estadistica.FUERZA, Efecto.NINGUNO);
  }

  @Override
  public void ejecutar(Personaje usuario, ArrayList<Personaje> aliados) {
    if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
      System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
      return;
    }

    usuario.consumirRecursos(costeEnergia, costeMana);

    System.out.println(usuario.getNombre() + " lanza un " + nombre + "!");
    System.out.println("Coste: " + costeEnergia + " PE + " + costeMana + " PM");
    System.out.println("--- ALIADOS RECIBEN +3 DE DAÑO ---");
    for (Personaje aliado : aliados) {
      System.out.println("-- " + aliado.getNombre() + " recibe +3 de daño!");
    }
  }
}
