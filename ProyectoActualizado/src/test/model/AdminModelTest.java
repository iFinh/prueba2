package test.model; 

import main.models.AdminModel;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;

public class AdminModelTest {
    private AdminModel model;
    private final String TEST_ADMINS_FILE = "test_admins.txt";
    private final String TEST_COSTOS_FILE = "test_costos.txt";
    private final String TEST_PERIODO = "2023-10";
    
    @Before
    public void setUp() throws Exception {
        // Create test files
        createTestFile(TEST_ADMINS_FILE, "admin123,securePass456\n");
        createTestFile(TEST_COSTOS_FILE, "");
        
        model = new AdminModel();
        
        // Set test file paths using reflection
        setPrivateField(model, "adminFile", new File(TEST_ADMINS_FILE));
        setPrivateField(model, "costosFile", new File(TEST_COSTOS_FILE));
    }

    private void setPrivateField(Object target, String fieldName, Object value) 
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void createTestFile(String filename, String content) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
    }

    @Test
    public void testValidarPasswordAdmin() {
        assertTrue(model.validateAdminPassword("admin123"));
        assertFalse(model.validateAdminPassword("wrongpass"));
    }

    @Test
    public void testVerificarCredenciales() {
        assertTrue(model.verificarCredenciales("admin123", "securePass456"));
        assertFalse(model.verificarCredenciales("admin123", "wrongpass"));
        assertFalse(model.verificarCredenciales("unknown", "securePass456"));
    }

    @Test
    public void testExisteCredencial() {
        assertTrue(model.existeCredencial("admin123"));
        assertFalse(model.existeCredencial("nonexistent"));
    }

    @Test
    public void testGuardarAdmin() throws IOException {
        assertTrue(model.guardarAdmin("newadmin", "newpass"));
        assertTrue(model.existeCredencial("newadmin"));
        assertTrue(model.verificarCredenciales("newadmin", "newpass"));
        
        // Verify file content
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_ADMINS_FILE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("newadmin,newpass")) {
                    found = true;
                    break;
                }
            }
            assertTrue("New admin not found in file", found);
        }
    }

    @Test
    public void testCalcularCCB_NormalCase() {
        double cf = 5000.0;  // Fixed costs
        double cv = 3000.0;  // Variable costs
        int nb = 1000;       // Number of trays
        double merma = 5.0;  // 5% waste
        
        double expected = ((5000 + 3000) / 1000) * (1 + (5.0/100));
        assertEquals(expected, model.calcularCCB(cf, cv, nb, merma), 0.001);
    }

    @Test
    public void testCalcularCCB_ZeroTrays() {
        // Division by zero case
        assertEquals(0.0, model.calcularCCB(5000, 3000, 0, 5), 0.001);
        
        // All zeros case
        assertEquals(0.0, model.calcularCCB(0, 0, 0, 0), 0.001);
    }

    @Test
    public void testCalcularCCB_NoWaste() {
        assertEquals(8.0, model.calcularCCB(5000, 3000, 1000, 0), 0.001);
    }

    @Test
    public void testCalcularCCB_HighWaste() {
        double expected = ((5000 + 3000) / 1000) * (1 + (20.0/100));
        assertEquals(expected, model.calcularCCB(5000, 3000, 1000, 20), 0.001);
    }
//
    @Test
    public void testCalcularCCB_EdgeCases() {
        // Negative costs (should handle gracefully)
        assertEquals(0.0, model.calcularCCB(-1000, -500, 100, 5), 0.001);
        
        // Negative waste (should handle gracefully)
        double result = model.calcularCCB(1000, 500, 100, -5);
        assertTrue("Should handle negative waste", result >= 0);
        
        // Single tray
        assertEquals(8400, model.calcularCCB(5000, 3000, 1, 5), 0.001);
    }

    @Test
    public void testCalcularCCB_DecimalPrecision() {
        double cf = 1234.56;
        double cv = 789.01;
        int nb = 50;
        double merma = 7.5;
        
        double expected = ((1234.56 + 789.01) / 50) * 1.075;
        assertEquals(expected, model.calcularCCB(cf, cv, nb, merma), 0.0001);
    }

    @Test
    public void testGuardarCostos() throws IOException {
        double ccbValue = 8.4;
        assertTrue(model.guardarCostos(TEST_PERIODO, ccbValue));
        
        // Verify file content
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_COSTOS_FILE))) {
            String line = reader.readLine();
            assertEquals(TEST_PERIODO + "," + ccbValue, line);
        }
        
        // Test multiple records
        assertTrue(model.guardarCostos("2023-11", 9.2));
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_COSTOS_FILE))) {
            assertEquals(TEST_PERIODO + "," + ccbValue, reader.readLine());
            assertEquals("2023-11,9.2", reader.readLine());
        }
    }

    @After
    public void tearDown() {
        // Clean up test files
        new File(TEST_ADMINS_FILE).delete();
        new File(TEST_COSTOS_FILE).delete();
    }
}