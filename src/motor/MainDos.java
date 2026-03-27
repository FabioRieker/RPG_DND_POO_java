package motor;

import personajes.*;
import java.util.List;

public class MainDos {

  public static void main(String[] args) {

    System.out.println("===========================================");
    System.out.println("    COMBATE: HEROES vs ENEMIGOS + JEFE");
    System.out.println("===========================================\n");

    // LLAMAR HEROES 
    List<Personaje> listaHeroes = FabricaHeroes.crearEquipoInicial();
   
    // OBTENER SALA 6 DE LA FABRICA
    System.out.println("Entrando en la Sala 6...");
    Sala salaActual = FabricaSalas.generarSala(6);
    List<Personaje> listaEnemigos = salaActual.getEnemigos();

    Personaje[] heroes = listaHeroes.toArray(new Personaje[0]);
    Personaje[] enemigos = listaEnemigos.toArray(new Personaje[0]);

    // MOSTRAR INFO
    System.out.println("=== HEROES ===");
    for (Personaje h : heroes) {
      h.mostrarInfo();
    }

    System.out.println("\n=== ENEMIGOS ===");
    for (Personaje e : enemigos) {
      e.mostrarInfo();
    }

    // EJECUTAR COMBATE
    MotorCombate.iniciarCombate(heroes, enemigos);
  }
}