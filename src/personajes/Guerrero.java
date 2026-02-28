package personajes;

public class Guerrero extends Personaje {

  public Guerrero(String nombre, Raza raza, int fue, int des, int con, int intel, int defBase) {
    super(nombre, raza, TipoClase.GUERRERO, fue, des, con, intel, defBase);
  }

  public void lanzarHechizo() {
    System.out.println(nombre + " está cargado su fuerza...");
  }
}
