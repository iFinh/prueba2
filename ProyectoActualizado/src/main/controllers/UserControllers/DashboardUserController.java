package main.controllers.UserControllers;

import main.models.Usuario;
import main.models.Sesion;
import main.views.Usuario.DashboardUserView;

import javax.swing.*;
import java.time.LocalDateTime;

public class DashboardUserController {
    private DashboardUserView view;
    private final Usuario usuario;

    public DashboardUserController(){
        this.usuario = Sesion.getUsuarioActual();
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "No hay una sesión activa para el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            new LoginController();
            return;
        }

        this.view = new DashboardUserView(usuario);
        inicializarVista();
        agregarListeners();
        view.setVisible(true);
    }

    private void inicializarVista() {
        view.updateDate(LocalDateTime.now()); 
    }

    private void agregarListeners() {
        view.getConsultarMenuBtn().addActionListener(e -> consultarMenu());
        view.getCerrarSesionBtn().addActionListener(e -> cerrarSesion());
    }

    private void consultarMenu() {
        view.dispose();
        new MenuController();
    }

    private void cerrarSesion() {
        Sesion.cerrarSesion();
        view.dispose();
        new LoginController(); 
    }
}