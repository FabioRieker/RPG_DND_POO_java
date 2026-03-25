package personajes;

import armas.CategoriaArma;
import armaduras.CategoriaArmadura;

public class JefeDemonio extends Jefe {

  public JefeDemonio(String nombre, int fue, int des, int con, int intel, int defBase) {
    super(nombre, Raza.TIEFLING, TipoJefe.DEMONIO, fue, des, con, intel, defBase);

    this.armasPermitidas.add(CategoriaArma.melee);
    this.armadurasPermitidas.add(CategoriaArmadura.MEDIA);
  }

  @Override
  public void habilidadEspecial() {
    System.out.println(nombre + " entra en modo berserker!");
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " desata ira infernal!");
  }
}
