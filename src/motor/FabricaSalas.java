package motor;

import personajes.*;
import armas.ArmaCuerpoACuerpo;
import armas.Armeria;

public class FabricaSalas {
    
    private static Armeria armeria = new Armeria();

    public static Sala generarSala(int numero) {
        Sala sala = new Sala(numero);

        switch (numero) {
            case 6:
                // defino los enemigos fijos de la Sala 6 (sacados de MainCombate)
                Monstruo orco = new Monstruo("Grumsh", Raza.ORCO, 14, 8, 14, 6, 10);
                orco.equiparArma(armeria.get("Maza"));

                Monstruo goblin = new Monstruo("Snitch", Raza.GOBLIN, 8, 14, 8, 6, 8);
                goblin.equiparArma(armeria.get("Arco"));

                Monstruo bestia = new Monstruo("Garra", Raza.BESTIA, 12, 12, 12, 4, 10);
                bestia.equiparArma(new ArmaCuerpoACuerpo("Garras", 1, 6));

                JefeDragon dragon = new JefeDragon("Valdrax el Devorador", 18, 10, 20, 12, 14);
                dragon.equiparArma(new ArmaCuerpoACuerpo("Colmillos de Dragón", 2, 8));

                sala.agregarEnemigo(orco);
                sala.agregarEnemigo(goblin);
                sala.agregarEnemigo(bestia);
                sala.agregarEnemigo(dragon);
                break;

            default:
                System.out.println("La sala " + numero + " aún no ha sido diseñada.");
                break;
        }

        return sala;
    }
}