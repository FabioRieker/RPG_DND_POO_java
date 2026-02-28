package motor;

import personajes.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("Iniciando Test del Motor RPG... \n");
    Mago elara = new Mago("Elara", Raza.ELFO, 6, 16, 8, 20, 16);
    Guerrero kaelen = new Guerrero("Kaelen", Raza.ENANO, 18, 10, 18, 6, 16);
    Picaro vex = new Picaro("Vex", Raza.HUMANO, 10, 18, 12, 12, 18);
    Brujo kallista = new Brujo("Kallista", Raza.TIEFLING, 8, 14, 14, 18, 14);
    Paladin cirric = new Paladin("Cirric", Raza.HUMANO, 14, 10, 16, 14, 18);
    Monje milo = new Monje("Milo", Raza.MEDIANO, 14, 18, 12, 8, 16);

    kaelen.mostrarInfo();
    vex.mostrarInfo();
  }
}
