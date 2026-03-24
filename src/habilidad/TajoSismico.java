package habilidad;

// Corte sísmico en el suelo
// Daño: 2d8, Coste: 30 PE, Escalado: FUE
// Hace sangrar al objetivo
public class TajoSismico extends HabilidadFisica {
  public TajoSismico() {
    super("Tajo Sísmico", 30, 2, 8, Estadistica.FUERZA, "SANGRADO");
  }
}
