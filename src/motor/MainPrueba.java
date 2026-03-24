package motor;

import personajes.*;
import armas.*;
import habilidad.*;
import consumibles.*;

public class MainPrueba {

  public static void main(String[] args) {

    System.out.println("===========================================");
    System.out.println("   PRUEBA DEL SISTEMA DE COMBATE RPG");
    System.out.println("===========================================");

    Personaje kaelen = new Guerrero("Kaelen", Raza.ENANO, 18, 10, 18, 6, 16);
    Personaje elara = new Mago("Elara", Raza.ELFO, 6, 16, 8, 20, 12);

    kaelen.equiparArma(new ArmaCuerpoACuerpo("Espada Larga", 1, 8));
    elara.equiparArma(new ArmaADistancia("Ballesta", 1, 8));

    kaelen.agregarHabilidad(new HojaPonzoñosa());
    kaelen.agregarHabilidad(new TajoSismico());
    kaelen.agregarHabilidad(new GolpeSanguinario());
    kaelen.agregarHabilidad(new Rompecraneos());
    kaelen.agregarHabilidad(new Ejecucion());
    // kaelen.agregarHabilidad(new AtaqueFurtivo());

    elara.agregarHabilidad(new BolaFuego());
    elara.agregarHabilidad(new RafagaGlacial());
    elara.agregarHabilidad(new LuzSagrada());
    elara.agregarHabilidad(new ToqueVampirico());
    elara.agregarHabilidad(new Purificacion());

    Bomba bomba = new Bomba(3);
    PocionCuracion pocion = new PocionCuracion(2);

    System.out.println("\n--- INFORMACION DE LOS PERSONAJES ---\n");
    kaelen.mostrarInfo();
    elara.mostrarInfo();

    System.out.println("\n===========================================");
    System.out.println("            COMbate de prueba");
    System.out.println("===========================================\n");

    System.out.println(">>> TURNO 1: Kaelen ataca a Elara con ESPADA");
    kaelen.atacar(elara);

    System.out.println("\n>>> TURNO 1: Elara ataca a Kaelen con BALLESTA");
    elara.atacar(kaelen);

    System.out.println("\n--- Estados tras ataque ---");
    kaelen.mostrarInfoBreve();
    elara.mostrarInfoBreve();

    System.out.println("\n>>> TURNO 2: Kaelen usa HOJA PONZOÑOSA");
    AccionCombate hoja = kaelen.getHabilidades().get(0);
    hoja.ejecutar(kaelen, elara);

    // AccionCombate pepe = kaelen.getHabilidades().get(6);
    // pepe.ejecutar(kaelen, elara);

    System.out.println("\n>>> TURNO 2: Elara usa BOLA DE FUEGO");
    AccionCombate bola = elara.getHabilidades().get(0);
    bola.ejecutar(elara, kaelen);

    System.out.println("\n>>> TURNO 2: Elara usa LUZ SAGRADA");
    AccionCombate luz = elara.getHabilidades().get(2);
    luz.ejecutar(elara, elara);

    System.out.println("\n--- Estados activos ---");
    kaelen.mostrarInfoBreve();
    elara.mostrarInfoBreve();

    System.out.println("\n>>> TURNO 3: Proceso de estados");
    kaelen.pasarTurnoDeEstados();
    elara.pasarTurnoDeEstados();

    System.out.println("\n--- Estados tras procesamiento ---");
    kaelen.mostrarInfoBreve();
    elara.mostrarInfoBreve();

    System.out.println("\n>>> TURNO 3: Kaelen usa GOLPE SANGUINARIO (hibrida)");
    AccionCombate golpe = kaelen.getHabilidades().get(2);
    golpe.ejecutar(kaelen, elara);

    System.out.println("\n>>> TURNO 3: Elara usa POCION DE CURACION");
    pocion.usar(elara);

    System.out.println("\n>>> TURNO 3: Kaelen usa BOMBA");
    bomba.usar(elara);

    System.out.println("\n>>> TURNO 4: Elara usa PURIFICACION");
    AccionCombate puri = elara.getHabilidades().get(4);
    puri.ejecutar(elara, kaelen);

    System.out.println("\n===========================================");
    System.out.println("            RESULTADO FINAL");
    System.out.println("===========================================\n");
    kaelen.mostrarInfo();
    elara.mostrarInfo();

    System.out.println("\n===========================================");
    System.out.println("         PRUEBA COMPLETADA");
    System.out.println("===========================================\n");

  }
}
