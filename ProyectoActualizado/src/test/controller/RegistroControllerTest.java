package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import main.controllers.UserControllers.RegistroController;
import main.models.Sesion;
import main.models.UserModel;
import main.models.Usuario;
import main.views.Usuario.RegistroView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class RegistroControllerTest {
    private RegistroViewStub viewStub;
    private UserModelStub modelStub;
    RegistroController controller;

    // Stub para RegistroView
    private static class RegistroViewStub extends RegistroView {
        public String credencial;
        public String contrasena;
        public String confirmacion;
        public String mensajeError;
        public String mensajeExito;
        public boolean disposed;
        public ActionListener registroListener;
        public ActionListener volverLoginListener;
        public ActionListener volverMainListener;

        @Override
        public String getCredencial() {
            return credencial;
        }

        @Override
        public String getContrasena() {
            return contrasena;
        }

        @Override
        public String getConfirmacion() {
            return confirmacion;
        }

        @Override
        public void mostrarError(String mensaje) {
            this.mensajeError = mensaje;
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            this.mensajeExito = mensaje;
        }

        @Override
        public void dispose() {
            this.disposed = true;
        }

        @Override
        public void setRegistroListener(ActionListener listener) {
            this.registroListener = listener;
        }

        @Override
        public void setVolverLoginListener(ActionListener listener) {
            this.volverLoginListener = listener;
        }

        @Override
        public void setVolverMainListener(ActionListener listener) {
            this.volverMainListener = listener;
        }
    }

    // Stub para UserModel
    private static class UserModelStub extends UserModel {
        public boolean credencialValida = true;
        public boolean credencialExiste = true;
        public boolean usuarioYaRegistrado = false;
        public boolean claveValida = true;
        public boolean guardarExitoso = true;

        @Override
        public boolean esCredencialValida(String credencial) {
            return credencialValida;
        }

        @Override
        public boolean credencialExiste(String credencial) {
            return credencialExiste;
        }

        @Override
        public boolean usuarioYaRegistrado(String credencial) {
            return usuarioYaRegistrado;
        }

        @Override
        public boolean esClaveValida(String clave) {
            return claveValida;
        }

        @Override
        public boolean guardarUsuario(String credencial, String clave, double saldo) {
            return guardarExitoso;
        }
    }

    @Before
    public void setUp() {
        viewStub = new RegistroViewStub();
        modelStub = new UserModelStub();
        controller = new RegistroController(viewStub, modelStub);
        Sesion.cerrarSesion(); // Asegurar sesión limpia
    }

    @After
    public void tearDown() {
        Sesion.cerrarSesion();
    }

    @Test
    public void testRegistroExitoso() {
        // Configurar stub
        viewStub.credencial = "123456";
        viewStub.contrasena = "clave123";
        viewStub.confirmacion = "clave123";
        
        // Ejecutar
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        // Verificar
        assertNotNull("Debería haber iniciado sesión", Sesion.getUsuarioActual());
        assertEquals("123456", Sesion.getUsuarioActual().getCredencial());
        assertTrue("Debería mostrar mensaje de éxito", viewStub.mensajeExito.contains("éxito"));
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }

    @Test
    public void testCamposVacios() {
        viewStub.credencial = "";
        viewStub.contrasena = "";
        viewStub.confirmacion = "";
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Todos los campos son obligatorios", viewStub.mensajeError);
        assertNull("No debería haber sesión iniciada", Sesion.getUsuarioActual());
    }

    @Test
    public void testCredencialInvalida() {
        viewStub.credencial = "123";
        viewStub.contrasena = "clave123";
        viewStub.confirmacion = "clave123";
        modelStub.credencialValida = false;
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Cédula inválida.", viewStub.mensajeError);
    }

    @Test
    public void testContrasenasNoCoinciden() {
        viewStub.credencial = "123456";
        viewStub.contrasena = "clave123";
        viewStub.confirmacion = "clave456";
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Las contraseñas no coinciden.", viewStub.mensajeError);
    }

    @Test
    public void testVolverLogin() {
        viewStub.volverLoginListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }

    @Test
    public void testVolverMain() {
        viewStub.volverMainListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }
}
