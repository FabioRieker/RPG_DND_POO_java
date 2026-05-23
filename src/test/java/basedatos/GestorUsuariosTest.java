package basedatos;

import basedatos.gestores.GestorUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorUsuariosTest {

    private GestorUsuarios gestor;
    private String nombreAleatorio;

    @BeforeEach
    public void setUp() {
        gestor = new GestorUsuarios();
        // Genera identificador unico mediante timestamp para evitar que el test falle
        // por usuario duplicado
        nombreAleatorio = "TestUser_" + System.currentTimeMillis();
    }

    @Test
    public void registrarUsuarioPermiteLoginPosterior() {
        String pwd = "PasswordSegura123";
        String email = nombreAleatorio + "@test.com";

        // Ejecuta registro en la base de datos, devuelve -1 en caso de error de
        // conexion
        int idRegistro = gestor.registrarUsuario(nombreAleatorio, pwd, email);

        // Evita fallos si el motor de bases de datos se encuentra apagado
        if (idRegistro == -1) {
            fail("No se pudo registrar el usuario. Comprueba si MySQL / XAMPP está encendido.");
            return;
        }

        assertTrue(idRegistro > 0, "El ID de registro debería ser un número positivo válido.");

        // Ejecuta inicio de sesion usando las credenciales recien creadas
        int idLogin = gestor.validarLogin(nombreAleatorio, pwd);

        // Comprueba que los datos del usuario se mantienen tras el registro y el login
        assertEquals(idRegistro, idLogin, "El ID devuelto por el login debe coincidir con el del registro.");
    }
}
