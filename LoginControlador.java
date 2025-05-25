package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

/**
 * Controlador de la lógica de autenticación.
 * Se comunica con {@link UsuarioDAO} para verificar las credenciales.
 *
 * Este controlador representa la capa intermedia entre la vista (Swing) y el modelo de datos.
 *
 * @author Javier Lopez
 */
public class LoginControlador {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     *
     * @param usuario nombre de usuario
     * @param contraseña contraseña del usuario
     * @return una instancia de {@link Usuario} si es válido, o {@code null} en caso contrario
     */
    public Usuario login(String usuario, String contraseña) {
        return usuarioDAO.verificarCredenciales(usuario, contraseña);
    }
}

