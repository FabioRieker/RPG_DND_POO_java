package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class Guerrero extends Personaje {

  public Guerrero(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
    super(nombre, raza, TipoClase.GUERRERO, fue, des, con, intel, defBase);

    // Armas a melee
    this.armasPermitidas.add(CategoriaArma.melee);
    // armaduras media y pesada
    this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);
    this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está cargado su fuerza...");
  }
}
