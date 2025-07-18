package main.controllers.AdminControllers;

import main.models.AdminModel;
import main.views.Admin.RegistroAdminView;
import main.controllers.AppController;

public class RegistroAdminController {
    private final RegistroAdminView view;
    private final AdminModel model;

    public RegistroAdminController() {
        this.view = new RegistroAdminView();
        this.model = new AdminModel();
        setupListeners();
    }

    public void setupListeners() {
        view.setRegistroListener(e -> registrarAdmin());
        view.setVolverLoginListener(e -> volverLogin());
        view.setVolverMainListener(e -> volverMain());
    }

    public void registrarAdmin() {
        String credencial = view.getCredencial();
        String password = view.getContrasena();
        String confirmacion = view.getConfirmacion();

        if (credencial.isEmpty() || password.isEmpty() || confirmacion.isEmpty()) {
            view.mostrarError("Todos los campos son obligatorios.");
            return;
        }

        if (!model.esClaveValida(password)) {
            view.mostrarError("Contraseña debe ser de al menos 6 caracteres.");
            return;
        }

        if (!password.equals(confirmacion)) {
            view.mostrarError("Las contraseñas no coinciden.");
            return;
        }

        if (model.existeCredencial(credencial)) {
            view.mostrarError("La credencial ya está registrada.");
            return;
        }

        if (model.guardarAdmin(credencial, password)) {
            view.mostrarMensaje("Administrador registrado con éxito.");
            view.dispose();
            new LoginAdminController();
        } else {
            view.mostrarError("Error al guardar el administrador.");
        }

    }

    public void volverLogin() {
        view.dispose();
        new LoginAdminController();
    }

    public void volverMain() {
        view.dispose();
        new AppController();
    }
}