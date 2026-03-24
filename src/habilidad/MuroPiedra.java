package habilidad;

import personajes.Personaje;

// Crea un muro de piedra protector
// Daño: ninguno, Coste: 15 PM
// Da +4 de defensa al objetivo durante 3 turnos
public class MuroPiedra extends HechizoMagico {

  public MuroPiedra() {
    super("Muro de Piedra", 15, 0, 0, null);
  }

  @Override
  public void ejecutar(Personaje usuario, Personaje objetivo) {
    if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
      System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
      return;
    }

    usuario.consumirRecursos(costeEnergia, costeMana);

    System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
    System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe +4 de defensa por 3 turnos!");
    System.out.println("-- ¡" + objetivo.getNombre() + " está protegido por un muro de piedra!");
  }
}
