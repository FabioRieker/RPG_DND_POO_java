package habilidad;

import personajes.Personaje;

// Ataque rápido de pícaro
// Daño: 1d6, Coste: 20 SP, Escalado: DES
public class AtaqueFurtivo extends HabilidadFisica {
    public AtaqueFurtivo() {
        super("Ataque Furtivo", 20, 1, 6, Estadistica.DESTREZA, null);
    }

    @Override
    public void ejecutar(Personaje usuario, Personaje objetivo) {
        if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
            System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
            return;
        }

        usuario.consumirRecursos(costeEnergia, costeMana);

        int tirada = dado.nextInt(20) + 1;
        int bono = getModificador(usuario) / 2;
        int totalImpacto = tirada + bono;

        System.out.println(usuario.getNombre() + " usa " + nombre + "...");
        System.out.println("Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

        // Bonus: daño doble si el objetivo está aturdido
        boolean objetivoAturdido = objetivo.tieneEstado("Aturdimiento");

        if (totalImpacto >= objetivo.getDefensaTotal()) {
            int daño = tirarDados() + bono;
            if (objetivoAturdido) {
                daño *= 2;
                System.out.println("¡GOLPE CRÍTICO! Objetivo aturdido - DAÑO x2!");
            }
            System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
            objetivo.recibirDaño(daño, false);
        } else {
            System.out.println("¡FALLO! El ataque no atraviesa la defensa del objetivo.");
        }
    }
}
