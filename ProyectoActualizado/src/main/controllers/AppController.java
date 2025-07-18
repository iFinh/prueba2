package main.controllers;
import main.controllers.AdminControllers.LoginAdminController;
import main.controllers.UserControllers.LoginController;
import main.models.AdminModel;
import main.views.App.AppView;
import main.views.Usuario.LoginView;
import javax.swing.*;

public class AppController {

    private AppView view;
    private AdminModel model;

    public AppController() {
        this.view = new AppView();
        this.model = new AdminModel();
        setupControllers();
        view.setVisible(true);
    }

    private void setupControllers() {
        view.getBtnAdmin().addActionListener(e -> handleAdminAccess());
        view.getBtnUser().addActionListener(e -> handleUserAccess());
    }

    private void handleAdminAccess() {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(
            view.getFrame(),
            passwordField,
            "Ingrese la contraseña de administrador",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            if (model.validateAdminPassword(new String(passwordField.getPassword()))) {
                view.getFrame().dispose();
                new LoginAdminController(); 
            } else {
                JOptionPane.showMessageDialog(
                    view.getFrame(),
                    "Contraseña incorrecta",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void handleUserAccess() {
        view.getFrame().dispose();
        new LoginController(); 
    }
}

