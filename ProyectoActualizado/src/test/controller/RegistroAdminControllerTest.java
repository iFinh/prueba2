package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import main.controllers.AdminControllers.RegistroAdminController;
import main.models.AdminModel;
import main.views.Admin.RegistroAdminView;

public class RegistroAdminControllerTest {

    private static class TestRegistroAdminView extends RegistroAdminView {
        public String errorMessage;
        public String successMessage;
        public boolean disposed = false;
        public String credencial = "";
        public String contrasena = "";
        public String confirmacion = "";
        
        private ActionListener registroListener;
        private ActionListener volverLoginListener;
        private ActionListener volverMainListener;
        
        public TestRegistroAdminView() {
            // Constructor sin inicialización de UI
        }
        
        @Override public String getCredencial() { return credencial; }
        @Override public String getContrasena() { return contrasena; }
        @Override public String getConfirmacion() { return confirmacion; }
        @Override public void mostrarError(String mensaje) { this.errorMessage = mensaje; }
        @Override public void mostrarMensaje(String mensaje) { this.successMessage = mensaje; }
        @Override public void dispose() { this.disposed = true; }
        @Override public void setVisible(boolean visible) { /* No hacer nada */ }
        
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
        
        public void simularRegistro() {
            if (registroListener != null) {
                registroListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "registro"));
            }
        }
        
        public void simularVolverLogin() {
            if (volverLoginListener != null) {
                volverLoginListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "volverLogin"));
            }
        }
        
        public void simularVolverMain() {
            if (volverMainListener != null) {
                volverMainListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "volverMain"));
            }
        }
    }

    private static class TestAdminModel extends AdminModel {
        public boolean claveValidaResult = false;
        public boolean existeCredencialResult = false;
        public boolean guardarAdminResult = false;
        
        @Override
        public boolean esClaveValida(String contrasena) {
            return claveValidaResult;
        }
        
        @Override
        public boolean existeCredencial(String credencial) {
            return existeCredencialResult;
        }
        
        @Override
        public boolean guardarAdmin(String credencial, String contrasena) {
            return guardarAdminResult;
        }
    }

    private RegistroAdminController controller;
    private TestRegistroAdminView viewStub;
    private TestAdminModel modelStub;

    @Before
    public void setUp() throws Exception {
        // Crear stubs
        viewStub = new TestRegistroAdminView();
        modelStub = new TestAdminModel();
        
        // Crear controlador real
        controller = new RegistroAdminController();
        
        // Inyectar nuestros stubs
        injectPrivateField(controller, "view", viewStub);
        injectPrivateField(controller, "model", modelStub);
        
        // Reconfigurar listeners con nuestros stubs
        controller.getClass().getDeclaredMethod("setupListeners").invoke(controller);
    }

    private void injectPrivateField(Object target, String fieldName, Object value) 
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void testCamposVacios() {
        // Configuración
        viewStub.credencial = "";
        viewStub.contrasena = "";
        viewStub.confirmacion = "";
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("Todos los campos son obligatorios.", viewStub.errorMessage);
    }

    @Test
    public void testClaveInvalida() {
        // Configuración
        viewStub.credencial = "admin";
        viewStub.contrasena = "12345"; // Menos de 6 caracteres
        viewStub.confirmacion = "12345";
        modelStub.claveValidaResult = false;
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("Contraseña debe ser de al menos 6 caracteres.", viewStub.errorMessage);
    }

    @Test
    public void testContrasenasNoCoinciden() {
        // Configuración
        viewStub.credencial = "admin";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password456";
        modelStub.claveValidaResult = true;
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("Las contraseñas no coinciden.", viewStub.errorMessage);
    }

    @Test
    public void testCredencialExistente() {
        // Configuración
        viewStub.credencial = "admin";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.claveValidaResult = true;
        modelStub.existeCredencialResult = true;
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("La credencial ya está registrada.", viewStub.errorMessage);
    }

    @Test
    public void testRegistroExitoso() {
        // Configuración
        viewStub.credencial = "admin";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.claveValidaResult = true;
        modelStub.existeCredencialResult = false;
        modelStub.guardarAdminResult = true;
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("Administrador registrado con éxito.", viewStub.successMessage);
        assertTrue(viewStub.disposed);
    }

    @Test
    public void testErrorAlGuardar() {
        // Configuración
        viewStub.credencial = "admin";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.claveValidaResult = true;
        modelStub.existeCredencialResult = false;
        modelStub.guardarAdminResult = false;
        
        // Ejecución
        viewStub.simularRegistro();
        
        // Verificación
        assertEquals("Error al guardar el administrador.", viewStub.errorMessage);
    }

    @Test
    public void testVolverLogin() {
        // Ejecución
        viewStub.simularVolverLogin();
        
        // Verificación
        assertTrue(viewStub.disposed);
    }

    @Test
    public void testVolverMain() {
        // Ejecución
        viewStub.simularVolverMain();
        
        // Verificación
        assertTrue(viewStub.disposed);
    }
}
