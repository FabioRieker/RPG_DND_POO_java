package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class JefeGigante extends Jefe {

  public JefeGigante(String nombre, int fue, int des, int con, int intel, int defBase) {
    super(nombre, Raza.BESTIA, TipoJefe.GIGANTE, fue, des, con, intel, defBase);

    this.armasPermitidas.add(CategoriaArma.melee);
    this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
  }

  @Override
  public void habilidadEspecial() {
    System.out.println(nombre + " aplasta todo a su paso!");
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " lanza rocas!");
  }
}
