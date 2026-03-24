package habilidad;

import personajes.Personaje;
import estado.EstadoQuemadura;

// Lanza una bola de fuego
// Daño: 3d6, Coste: 25 PM
// Aplica Quemadura y sinergiza con Veneno
public class BolaFuego extends HechizoMagico {

    public BolaFuego() {
        super("Bola de Fuego", 25, 3, 6, "QUEMA");
    }

    @Override
    public void ejecutar(Personaje usuario, Personaje objetivo) {
        if (!usuario.tieneRecursos(costeEnergia, costeMana)) {
            System.out.println(usuario.getNombre() + " no tiene suficientes recursos.");
            return;
        }

        usuario.consumirRecursos(costeEnergia, costeMana);

        int tirada = dado.nextInt(20) + 1;
        int bono = usuario.getInteligencia() / 2;
        int totalImpacto = tirada + bono;

        System.out.println(usuario.getNombre() + " lanza " + nombre + "...");
        System.out.println("Tirada: " + tirada + " + " + bono + " = " + totalImpacto);

        if (totalImpacto >= objetivo.getDefensaTotal()) {
            int daño = tirarDados() + bono;

            // Sinergia fuego + veneno
            boolean sinergiaVeneno = objetivo.tieneEstado("Veneno");
            if (sinergiaVeneno) {
                daño *= 2;
                System.out.println("¡SINERGIA! Fuego contra Veneno - DAÑO x2!");
            }

            System.out.println("¡IMPACTO! " + objetivo.getNombre() + " recibe " + daño + " de daño.");
            objetivo.recibirDaño(daño, false);

            // Aplica quemadura
            if (!objetivo.tieneEstado("Quemadura")) {
                objetivo.aplicarEstado(new EstadoQuemadura(3, 5));
                System.out.println("-- ¡" + objetivo.getNombre() + " está en llamas!");
            }
        } else {
            System.out.println("¡FALLO! El hechizo no atraviesa la defensa.");
        }
    }
}
