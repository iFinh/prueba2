package main.controllers.UserControllers;

import main.models.Sesion;
import main.models.Usuario;
import main.views.Usuario.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController {
    public MenuView view;
    private Usuario usuario;

    public MenuController() {
        this.usuario = Sesion.getUsuarioActual();
        this.view = new MenuView();
        this.view.setSaldo(usuario.getSaldo());

        initController();
        view.showMealMenu("Desayuno");
    }

    public MenuController(MenuView view) {
        this.usuario = Sesion.getUsuarioActual();
        this.view = view;
        this.view.setSaldo(usuario.getSaldo());
        initController();
        view.showMealMenu("Desayuno");
    }
    public void initController() {
        view.setDesayunoListener(e -> view.showMealMenu("Desayuno"));
        view.setAlmuerzoListener(e -> view.showMealMenu("Almuerzo"));

        view.setVolverListener(e -> {
            new DashboardUserController(); 
            view.dispose();
        });
    }
}
