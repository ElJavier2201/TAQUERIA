package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Panel principal del administrador. Muestra encabezado, menú lateral y subpaneles
 * como gestión de usuarios, reportes de ventas y control de inventario.
 * Este panel está accesible solo para usuarios con rol "admin".
 *
 * @author Javier Lopez
 */
public class AdminPanel extends JFrame {

    private JPanel contentPanel; // Área dinámica central

    /**
     * Crea e inicializa el panel del administrador.
     *
     * @param usuario el usuario que inició sesión (debe ser administrador)
     */
    public AdminPanel(Usuario usuario) {
        // Usuario logueado

        setTitle("Panel de Administrador - Taquería");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
    }

    /**
     * Inicializa la interfaz con el encabezado, menú lateral y contenido dinámico.
     */
    private void initUI() {
        // Encabezado
        JLabel lblEncabezado = new JLabel("Sistema de Ventas - Administrador", SwingConstants.CENTER);
        lblEncabezado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEncabezado.setOpaque(true);
        lblEncabezado.setBackground(new Color(0, 153, 76));
        lblEncabezado.setForeground(Color.WHITE);
        lblEncabezado.setPreferredSize(new Dimension(900, 50));
        add(lblEncabezado, BorderLayout.NORTH);

        // Menú lateral
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(230, 230, 230));
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));

        JButton btnUsuarios = new JButton("Ver Usuarios");
        JButton btnReportes = new JButton("Reportes");
        JButton btnInventario = new JButton("Inventario");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        styleButton(btnUsuarios);
        styleButton(btnReportes);
        styleButton(btnInventario);
        styleButton(btnCerrarSesion);

        // Acciones de los botones
        btnUsuarios.addActionListener(e -> mostrarUsuarios());
        btnReportes.addActionListener(e -> mostrarReportes());
        btnInventario.addActionListener(e -> mostrarInventario());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnUsuarios);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnReportes);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnInventario);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnCerrarSesion);

        add(menuPanel, BorderLayout.WEST);

        // Panel dinámico central
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Estiliza un botón del menú lateral.
     *
     * @param button el botón a aplicar estilo
     */
    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    /**
     * Muestra el panel de gestión de usuarios.
     */
    private void mostrarUsuarios() {
        contentPanel.removeAll();
        contentPanel.add(new UsuariosPanel(), BorderLayout.CENTER); // <- clase que generaremos
        contentPanel.revalidate();
        contentPanel.repaint();
    }

     // Muestra el panel de reportes de ventas.
        private void mostrarReportes() {
        contentPanel.removeAll();
        contentPanel.add(new ReportesPanel(), BorderLayout.CENTER); // <- clase que generaremos
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Muestra el panel de inventario.
     */
    private void mostrarInventario() {
        contentPanel.removeAll();
        contentPanel.add(new InventarioPanel(), BorderLayout.CENTER); // <- clase que generaremos
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Cierra sesión y vuelve a la pantalla de login.
     */
    private void cerrarSesion() {
        this.dispose();
        new LoginVista().setVisible(true);
    }
}
