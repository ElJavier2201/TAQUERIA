package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

/**
 * Controlador intermedio para la gestión de usuarios.
 * Se encarga de procesar la lógica entre la vista y el DAO.
 *
 * Usado por la interfaz de administrador (UsuariosPanel).
 *
 * @author Javier Lopez
 */
public class UsuarioControlador {
    private final UsuarioDAO usuarioDAO;

    /**
     * Crea un nuevo controlador de usuarios y su conexión DAO.
     */
    public UsuarioControlador() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto Usuario a insertar
     */
    public void insertar(Usuario usuario) {
        usuarioDAO.insertar(usuario);
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param usuario Objeto Usuario con datos actualizados
     */
    public void actualizar(Usuario usuario) {
        usuarioDAO.actualizar(usuario);
    }

    /**
     * Elimina un usuario según su ID.
     *
     * @param id ID del usuario a eliminar
     */
    public void eliminar(int id) {
        usuarioDAO.eliminar(id);
    }

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return Lista de usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioDAO.obtenerTodos();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Usuario encontrado, o null si no existe
     */
    public Usuario obtenerPorId(int id) {
        return usuarioDAO.obtenerPorId(id);
    }
}
