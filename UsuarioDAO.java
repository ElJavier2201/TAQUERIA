package dao;

import util.DBConexion;
import modelo.Usuario;
import util.Encriptador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para acceder a los datos de usuarios en la base de datos.
 * Realiza operaciones CRUD sobre la tabla "usuarios".
 *
 * Utiliza SHA-256 para almacenar contraseñas cifradas.
 *
 * @author Javier Lopez
 */
public class UsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto Usuario a insertar
     */
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES (?, ?, ?)";
        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, Encriptador.encriptar(usuario.getContraseña()));
            stmt.setString(3, usuario.getRol());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     * Si la contraseña está vacía, no se actualiza.
     *
     * @param usuario Usuario con datos modificados
     */
    public void actualizar(Usuario usuario) {
        String sql;
        boolean actualizarPassword = usuario.getContraseña() != null && !usuario.getContraseña().isEmpty();

        if (actualizarPassword) {
            sql = "UPDATE usuarios SET nombre_usuario=?, contraseña=?, rol=? WHERE id=?";
        } else {
            sql = "UPDATE usuarios SET nombre_usuario=?, rol=? WHERE id=?";
        }

        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombreUsuario());

            if (actualizarPassword) {
                stmt.setString(2, Encriptador.encriptar(usuario.getContraseña()));
                stmt.setString(3, usuario.getRol());
                stmt.setInt(4, usuario.getId());
            } else {
                stmt.setString(2, usuario.getRol());
                stmt.setInt(3, usuario.getId());
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * @return Lista de objetos Usuario
     */
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setRol(rs.getString("rol"));
                // No se carga la contraseña por seguridad
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Objeto Usuario o null si no existe
     */
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id=?";
        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombreUsuario(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Verifica si las credenciales ingresadas son válidas.
     *
     * @param usuario nombre de usuario
     * @param contraseña contraseña sin encriptar (en texto plano)
     * @return objeto Usuario si las credenciales son válidas, null en caso contrario
     */

    public Usuario verificarCredenciales(String usuario, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?";

        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, Encriptador.encriptar(contraseña)); // ciframos antes de comparar

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setRol(rs.getString("rol"));
                // No se retorna la contraseña
                return u;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // credenciales incorrectas
    }
    }

