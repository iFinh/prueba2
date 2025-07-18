package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import main.controllers.UserControllers.DashboardUserController;
import main.models.Sesion;
import main.models.Usuario;

public class DashboardUserControllerTest {

    private DashboardUserController controller;
    private Usuario testUser;

    @Before
    public void setUp() {
        testUser = new Usuario("testuser", 100.0);
        Sesion.iniciarSesion(testUser);
        controller = new DashboardUserController();
    }

    @Test
    public void testInicializacionConSesion() throws Exception {
        Field viewField = DashboardUserController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        assertNotNull(viewField.get(controller));
    }

    @Test
    public void testSinSesionActiva() throws Exception {
        Sesion.cerrarSesion();
        DashboardUserController noSessionController = new DashboardUserController();
        Field viewField = DashboardUserController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        assertNull(viewField.get(noSessionController));
    }

    @Test
    public void testConsultarMenu() throws Exception {
        // Obtener método
        var method = DashboardUserController.class.getDeclaredMethod("consultarMenu");
        method.setAccessible(true);
        
        // Verificar que no lanza excepciones
        method.invoke(controller);
        
        // Verificación indirecta: si no hay excepción, se considera exitoso
        assertTrue(true);
    }

    @Test
    public void testCerrarSesion() throws Exception {
        // Obtener método
        var method = DashboardUserController.class.getDeclaredMethod("cerrarSesion");
        method.setAccessible(true);
        
        // Ejecutar
        method.invoke(controller);
        
        // Verificar solo lo que podemos comprobar directamente
        assertNull(Sesion.getUsuarioActual());
    }
}