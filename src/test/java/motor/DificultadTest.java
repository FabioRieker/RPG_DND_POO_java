package motor;

import personajes.Personaje;
import personajes.Raza;
import personajes.TipoClase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DificultadTest {

    @BeforeEach
    public void setUp() {
        // Restablece la dificultad a valor por defecto antes de cada prueba
        MotorCombate.multiplicadorDificultad = 1.0;
    }

    @Test
    public void dificultadFacilReduceVidaDeEnemigos() {
        // Genera un jefe con multiplicador estandar (x1.0) para registrar vida base
        MotorCombate.multiplicadorDificultad = 1.0;
        Sala salaNormal = FabricaSalas.generarSala(4); // Sala 4 contiene Jefe Grog
        Personaje jefeNormal = salaNormal.getEnemigos().get(0);
        int vidaBase = jefeNormal.getVidaMax();

        // Genera el mismo jefe bajo multiplicador de dificultad baja (x0.6)
        MotorCombate.multiplicadorDificultad = 0.6;
        Sala salaFacil = FabricaSalas.generarSala(4);
        Personaje jefeFacil = salaFacil.getEnemigos().get(0);

        // Calcula el valor esperado aplicando la reduccion del 40%
        int vidaEsperada = (int) (vidaBase * 0.6);
        vidaEsperada = Math.max(1, vidaEsperada);

        assertEquals(vidaEsperada, jefeFacil.getVidaMax(),
                "La vida del jefe en Fácil debería haberse reducido un 40%.");
    }

    @Test
    public void dificultadNormalMantieneValoresBase() {
        MotorCombate.multiplicadorDificultad = 1.0;

        Sala sala = FabricaSalas.generarSala(1); // Genera Goblins comunes
        Personaje goblin = sala.getEnemigos().get(0);

        // Verifica que el multiplicador 1.0 no provoque alteraciones
        assertTrue(goblin.getVidaMax() > 1, "En normal, el Goblin debe conservar su vida base estándar.");
    }

    @Test
    public void dificultadDificilAumentaVidaDeEnemigos() {
        // Registra salud de jefe en dificultad normal
        MotorCombate.multiplicadorDificultad = 1.0;
        Sala salaNormal = FabricaSalas.generarSala(20); // Jefe Dragon
        int vidaBase = salaNormal.getEnemigos().get(0).getVidaMax();

        // Regenera al mismo jefe en dificultad alta (x1.5)
        MotorCombate.multiplicadorDificultad = 1.5;
        Sala salaDificil = FabricaSalas.generarSala(20);
        int vidaDificil = salaDificil.getEnemigos().get(0).getVidaMax();

        int vidaEsperada = (int) (vidaBase * 1.5);

        assertEquals(vidaEsperada, vidaDificil, "La vida del jefe en Difícil debería haber aumentado un 50%.");
    }

    @Test
    public void dificultadFacilReduceDanioRecibidoPorHeroes() {
        Personaje heroe = new Personaje("Tanque", Raza.ENANO, TipoClase.GUERRERO, 10, 10, 10, 10, 10) {
        };
        int vidaMax = heroe.getVidaMax();

        // Selecciona dificultad facil
        MotorCombate.multiplicadorDificultad = 0.6;

        int dañoBaseDelAtaque = 50;
        heroe.recibirDaño(dañoBaseDelAtaque, true);

        // Calcula el daño recibido esperado (50 * 0.6 = 30)
        int dañoEsperado = 30;
        int vidaEsperada = vidaMax - dañoEsperado;

        assertEquals(vidaEsperada, heroe.getVidaActual(),
                "El héroe debería haber recibido daño mitigado por jugar en Fácil.");
    }
}
