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

    // ESTA ES LA ÚNICA PARTE QUE NECESITAS (Convertir a array para el resto del Main)
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

    // COMBATE POR TURNOS
    System.out.println("\n===========================================");
    System.out.println("            ¡COMBATE COMIENZA!");
    System.out.println("===========================================\n");

    int turno = 1;
    while (hayVivos(heroes) && hayVivos(enemigos) && turno <= 10) {
      System.out.println("======= TURNO " + turno + " =======");

      // Proceso de estados de heroes
      for (Personaje h : heroes) {
        if (h.estaVivo()) {
          h.pasarTurnoDeEstados();
        }
      }

      // Proceso de estados de enemigos
      for (Personaje e : enemigos) {
        if (e.estaVivo()) {
          e.pasarTurnoDeEstados();
        }
      }

      // Turno de heroes
      System.out.println("\n--- Turno de Heroes ---");
      for (int i = 0; i < heroes.length; i++) {
        Personaje heroe = heroes[i];
        if (heroe.estaVivo() && hayVivos(enemigos)) {
          Personaje objetivo = obtenerObjetivoAleatorio(enemigos);
          heroe.atacar(objetivo);
        }
      }

      // Turno de enemigos
      System.out.println("\n--- Turno de Enemigos ---");
      for (int i = 0; i < enemigos.length; i++) {
        Personaje enemigo = enemigos[i];
        if (enemigo.estaVivo() && hayVivos(heroes)) {
          Personaje objetivo = obtenerObjetivoAleatorio(heroes);
          
          // El ataque normal ya comprueba estados de incapacidad internamente
          enemigo.atacar(objetivo);

          // Si es jefe, usar habilidad especial (Corregido con objetivo y chequeo de estado)
          if (enemigo instanceof Jefe && Math.random() < 0.3) {
            // Comprobamos si el Jefe puede actuar (Problema #1)
            if (!enemigo.tieneEstado("Aturdimiento") && !enemigo.tieneEstado("Congelado")) {
                ((Jefe) enemigo).habilidadEspecial(objetivo);
            }
          }
        }
      }

      // Mostrar estado
      System.out.println("\n--- Estado tras turno " + turno + " ---");
      System.out.println("Heroes:");
      for (Personaje h : heroes) {
        h.mostrarInfoBreve();
      }
      System.out.println("Enemigos:");
      for (Personaje e : enemigos) {
        e.mostrarInfoBreve();
      }

      System.out.println("");
      turno++;
    }

    // RESULTADO
    System.out.println("===========================================");
    System.out.println("            RESULTADO");
    System.out.println("===========================================");

    if (hayVivos(heroes)) {
      System.out.println("¡VICTORIA! Los heroes han ganado.");
    } else if (hayVivos(enemigos)) {
      System.out.println("¡DERROTA! Los enemigos han ganado.");
    } else {
      System.out.println("¡EMPATE!");
    }

    System.out.println("\n--- Estado Final ---");
    for (Personaje h : heroes) {
      h.mostrarInfo();
    }
    for (Personaje e : enemigos) {
      e.mostrarInfo();
    }

    System.out.println("\n===========================================");
    System.out.println("         COMBATE FINALIZADO");
    System.out.println("===========================================\n");
  }

  static boolean hayVivos(Personaje[] grupo) {
    for (Personaje p : grupo) {
      if (p.estaVivo()) {
        return true;
      }
    }
    return false;
  }

  static Personaje obtenerObjetivoAleatorio(Personaje[] grupo) {
    Personaje[] vivos = new Personaje[grupo.length];
    int count = 0;
    for (Personaje p : grupo) {
      if (p.estaVivo()) {
        vivos[count++] = p;
      }
    }
    if (count == 0) return null;
    return vivos[(int) (Math.random() * count)];
  }
}