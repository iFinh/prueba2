package main;

import javax.swing.SwingUtilities;
import main.controllers.AppController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AppController();
        });
    }
}