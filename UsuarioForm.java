package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Formulario de entrada para agregar o editar un usuario.
 * Usado en el panel de gestión de usuarios (UsuariosPanel).
 *
 * Si se pasa un objeto Usuario al constructor, lo usa en modo edición.
 * Si no, actúa en modo agregar.
 *
 * @author ChatGPT
 */
public class UsuarioForm extends JPanel {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JComboBox<String> cmbRol;
    private final Usuario usuario;

    /**
     * Crea un nuevo formulario para agregar o editar un usuario.
     *
     * @param usuario Usuario a editar, o null para modo agregar.
     */
    public UsuarioForm(Usuario usuario) {
        this.usuario = usuario;

        setLayout(new GridLayout(3, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        add(new JLabel("Rol:"));
        cmbRol = new JComboBox<>(new String[]{"admin", "mesero", "cocinero", "cajero"});
        add(cmbRol);

        if (usuario != null) {
            cargarDatos();
        }
    }

    /**
     * Carga los datos del usuario al formulario en modo edición.
     */
    private void cargarDatos() {
        txtUsername.setText(usuario.getNombreUsuario());
        txtPassword.setText(""); // Por seguridad, dejar vacío
        cmbRol.setSelectedItem(usuario.getRol());
    }

    /**
     * Obtiene el usuario ingresado o modificado en el formulario.
     *
     * @return Objeto Usuario con los datos del formulario.
     */
    public Usuario getUsuario() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cmbRol.getSelectedItem();

        Usuario u = new Usuario();
        u.setNombreUsuario(username);
        u.setContraseña(password);
        u.setRol(rol);

        return u;
    }
}

