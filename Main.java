package main;

import vista.LoginVista;
import javax.swing.*;

/**
 * Clase principal para iniciar el sistema de ventas.
 * Muestra la ventana de login al arrancar la aplicación.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ejecuta la ventana de login
        SwingUtilities.invokeLater(() -> {
            LoginVista login = new LoginVista();
            login.setVisible(true);
        });
    }
}
