package armas;

import personajes.Personaje;
import estado.EstadoQuemadura;

public class HojaFenix extends ArmaCuerpoACuerpo {
    public HojaFenix() {
        super("Hoja Fénix", 1, 8);
    }

    @Override
    public void aplicarEfectosEspeciales(Personaje defensor) {
        if (!defensor.tieneEstado("Quemadura")) {
            // aplicamos estado quemadura: 3 turnos, 4 de daño por turno
            defensor.aplicarEstado(new EstadoQuemadura(3, 4));
            System.out.println("La Hoja Fénix prende fuego a " + defensor.getNombre());
        }
    }
}