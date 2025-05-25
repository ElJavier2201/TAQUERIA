package vista;

import controlador.LoginControlador;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de login para el sistema de ventas de la taquería.
 * Incluye campos de entrada de usuario, contraseña, logo y diseño estilizado.
 * Realiza redirección al panel correspondiente según el rol.
 *
 * Roles posibles: admin, mesero, cajero, cocinero.
 *
 * Requiere la clase {@link LoginControlador} para validar credenciales.
 * @author Javier Lopez
 */
public class LoginVista extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private final LoginControlador loginControlador = new LoginControlador();

    /**
     * Constructor que inicializa la interfaz del login.
     */
    public LoginVista() {
        setTitle("Sistema de Ventas - Taquería");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    /**
     * Inicializa y organiza los componentes gráficos del login.
     */
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Logo
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon("src/assets/logo.png")); // Ruta al logo
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblLogo);

        // Encabezado
        JLabel lblTitulo = new JLabel("Ingrese sus credenciales");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblTitulo);

        // Campo usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsuario = new JTextField(20);
        txtUsuario.setMaximumSize(new Dimension(300, 30));

        // Campo contraseña
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtContraseña = new JPasswordField(20);
        txtContraseña.setMaximumSize(new Dimension(300, 30));

        // Botón ingresar con estilo
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setBackground(new Color(0, 153, 76));
        btnIngresar.setForeground(Color.BLACK);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIngresar.setMaximumSize(new Dimension(150, 40));
        btnIngresar.addActionListener(e -> validarLogin());

        // Añadir componentes
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblContraseña);
        panel.add(txtContraseña);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnIngresar);

        add(panel);
    }

    /**
     * Verifica las credenciales y redirige al panel correspondiente según el rol.
     */
    private void validarLogin() {
        String usuario = txtUsuario.getText();
        String contraseña = new String(txtContraseña.getPassword());

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
            return;
        }

        Usuario u = loginControlador.login(usuario, contraseña);

        if (u != null) {
            //SesionUsuario.setUsuarioActual(u);
            JOptionPane.showMessageDialog(this, "Bienvenido, " + u.getNombreUsuario());

            switch (u.getRol()) {
                case "admin":
                    new AdminPanel(u).setVisible(true);
                    break;
                //case "mesero":
                 //   dispose();
                  //  new MeseroPanel().setVisible(true);
                 //   break;
               // case "cajero":
                  //  dispose();
                   // new CajeroPanel();
                  //  break;
                //case "cocinero":
                  //  dispose();
                  //  new CocineroPanel();
                  //  break;
                default:
                    JOptionPane.showMessageDialog(this, "Rol no reconocido.");
                    return;
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }
}

