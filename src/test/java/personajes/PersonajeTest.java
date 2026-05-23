package personajes;

import motor.MotorCombate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonajeTest {

    private Personaje heroeDummy;

    @BeforeEach
    public void setUp() {
        // Instancia anonima de Personaje (clase abstracta)
        // Valores iniciales: Fuerza 10, Destreza 10, Const 10, Int 10, Defensa 10
        heroeDummy = new Personaje("HeroeTest", Raza.HUMANO, TipoClase.GUERRERO, 10, 10, 10, 10, 10) {
        };

        // Restablece el multiplicador de dificultad a 1.0 (Normal)
        MotorCombate.multiplicadorDificultad = 1.0;
    }

    @Test
    public void recibirDañoNoDejaVidaNegativa() {
        // Daño masivo que supera la vida total (55 HP)
        int dañoExcesivo = 1000;

        heroeDummy.recibirDaño(dañoExcesivo, true); // Aplica daño puro (ignora armadura)

        assertEquals(0, heroeDummy.getVidaActual(), "La vida no debería bajar de 0.");
        assertFalse(heroeDummy.estaVivo(), "El héroe debería estar muerto con 0 de vida.");
    }

    @Test
    public void curarNoSuperaVidaMaxima() {
        int vidaMax = heroeDummy.getVidaMax();

        // Reduce la vida antes de intentar curar
        heroeDummy.recibirDaño(10, true);
        assertTrue(heroeDummy.getVidaActual() < vidaMax, "La vida debería haber bajado.");

        // Intenta aplicar curacion por encima del tope
        heroeDummy.curar(500);

        assertEquals(vidaMax, heroeDummy.getVidaActual(), "La cura no debería sobrepasar la vida máxima.");
    }

    @Test
    public void estaVivoDevuelveFalseSiVidaEsCero() {
        assertTrue(heroeDummy.estaVivo(), "Debería empezar vivo.");

        heroeDummy.setVidaActual(0);

        assertFalse(heroeDummy.estaVivo(), "Debería devolver false si la vida es exactamente 0.");
    }

    @Test
    public void recibirDañoAplicaMinimoUnoConDificultad() {
        int dañoBase = 1;

        // Establece dificultad baja para forzar redondeo a 0 (1 * 0.1 = 0.1 -> casting a int = 0)
        MotorCombate.multiplicadorDificultad = 0.1;

        heroeDummy.recibirDaño(dañoBase, true);

        // Verifica que la barrera Math.max(1) impida recibir 0 daño
        int vidaEsperada = heroeDummy.getVidaMax() - 1;

        assertEquals(vidaEsperada, heroeDummy.getVidaActual(),
                "Debería haber recibido 1 punto de daño mínimo garantizado.");
    }
}
