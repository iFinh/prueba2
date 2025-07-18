package main.controllers.UserControllers;

import main.models.UserModel;
import main.controllers.AppController;
import main.models.AdminModel;
import main.models.Sesion;
import main.models.Usuario;
import main.views.Usuario.LoginView;
import main.views.App.AppView;
import javax.swing.*;

public class LoginController {
    private final UserModel userModel;
    private final LoginView view;

    public LoginController(UserModel userModel, LoginView view) {
        this.userModel = userModel;
        this.view = view;
        setupListeners();
    }

    public LoginController() {
        this(new UserModel(), new LoginView());
    }

    private void setupListeners() {
        view.setLoginListener(e -> intentarLogin());
        view.setVolverMainListener(e -> volverAlMenuPrincipal());
        view.setVolverRegistroListener(e -> irARegistro());
    }

    public void intentarLogin() {
        String credencial = view.getCredencial();
        String contrasena = view.getContrasena();

        if (!userModel.esCredencialValida(credencial)) {
            view.mostrarError("La cédula no es válida.");
            return;
        }

        if (!userModel.usuarioYaRegistrado(credencial)) {
            view.mostrarError("El usuario no está registrado.");
            return;
        }

        if (!userModel.esClaveValida(contrasena)) {
            view.mostrarError("Contraseña debe ser de al menos 6 caracteres.");
            return;
        }

        if (!userModel.autenticarUsuario(credencial, contrasena)) {
            view.mostrarError("Contraseña incorrecta.");
            return;
        }

        Sesion.iniciarSesion(new Usuario(credencial, userModel.obtenerSaldo(credencial)));
        view.mostrarMensaje("Inicio de sesión exitoso.");
        view.dispose();
        new DashboardUserController();
    }

    public void irARegistro() {
        view.dispose();
        new RegistroController();
    }

    public void volverAlMenuPrincipal() {
        view.dispose();
        new AppController();
    }
}