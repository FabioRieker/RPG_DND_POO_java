package personajes;

public class Picaro extends Personaje {

  public Picaro(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {

    super(nombre, raza, TipoClase.PICARO, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está canalizando energía arcana...");
  }

}
