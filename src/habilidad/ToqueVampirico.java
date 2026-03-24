package habilidad;

// Toque que roba vida
// Daño: 1d4, Coste: 10 PE + 15 PM, Escalado: INT
// Cura al usuario según el daño causado
public class ToqueVampirico extends HabilidadHibrida {

  public ToqueVampirico() {
    super("Toque Vampírico", 10, 15, 1, 4, Estadistica.INTELIGENCIA, Efecto.ROBO_VIDA);
  }
}
