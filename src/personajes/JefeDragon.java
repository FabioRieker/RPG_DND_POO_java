package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class JefeDragon extends Jefe {

  public JefeDragon(String nombre, int fue, int des, int con, int intel, int defBase) {
    super(nombre, Raza.DRACONIDO, TipoJefe.DRAGON, fue, des, con, intel, defBase);

    this.armasPermitidas.add(CategoriaArma.melee);
    this.armadurasPermitidas.add(CategoriaArmadura.PESADA);
  }

  @Override
  public void habilidadEspecial() {
    System.out.println(nombre + " escupe fuego destructor!");
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " usa Aliento de Dragón!");
  }
}
