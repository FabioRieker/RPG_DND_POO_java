package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Paladin extends Personaje {

  public Paladin(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
    super(nombre, raza, TipoClase.PALADIN, fue, des, con, intel, defBase);
    // Armas a melee
    this.armasPermitidas.add(CategoriaArma.melee);
    // armaduras pesadas
    this.armadurasPermitidas.add(CategoriaArmadura.PESADA);

  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está cargado su fuerza...");
  }
}
