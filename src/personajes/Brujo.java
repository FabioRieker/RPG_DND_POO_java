package personajes;

public class Brujo extends Personaje {

  public Brujo(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

    super(nombre, raza, TipoClase.BRUJO, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está canalizando energía demoniaca...");
  }

}
