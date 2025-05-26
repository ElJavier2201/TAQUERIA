package vista;

import util.DBConexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MeseroPanel extends JFrame {
    private JPanel panelLateral, panelContenido;
    private JButton btnEntradas, btnPlatos, btnPostres, btnBebidas;
    private JTable tabla;
    private JScrollPane scrollPane;

    private JComboBox<String> comboProductos;
    private JComboBox<String> comboMesas;
    private DefaultListModel<String> modeloListaOrden;
    private JList<String> listaOrden;
    private JButton btnAgregarProducto;
    private JButton btnGuardarOrden;
    private JButton btnVerOrdenes;

    private int meseroId;

    public MeseroPanel(int meseroId, String nombreMesero) {
        this.meseroId = meseroId;

        setTitle("Panel del Mesero - " + nombreMesero);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado
        JLabel lblEncabezado = new JLabel("Gestión de órdenes - Mesero: " + nombreMesero, SwingConstants.CENTER);
        lblEncabezado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEncabezado.setOpaque(true);
        lblEncabezado.setBackground(new Color(0, 153, 76));
        lblEncabezado.setForeground(Color.WHITE);
        lblEncabezado.setPreferredSize(new Dimension(900, 50));
        add(lblEncabezado, BorderLayout.NORTH);

        // Panel lateral con botones de categoría
        panelLateral = new JPanel();
        panelLateral.setLayout(new GridLayout(8, 1, 5, 5));
        panelLateral.setBackground(new Color(30, 30, 30));
        panelLateral.setPreferredSize(new Dimension(200, getHeight()));

        btnEntradas = new JButton("Entradas");
        btnPlatos = new JButton("Platos");
        btnPostres = new JButton("Postres");
        btnBebidas = new JButton("Bebidas");

        for (JButton btn : new JButton[]{btnEntradas, btnPlatos, btnPostres, btnBebidas}) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 255, 255, 207));
            btn.setForeground(Color.BLACK);
            panelLateral.add(btn);
        }

        add(panelLateral, BorderLayout.WEST);

        // Panel contenido principal (dividido en 2 verticalmente)
        panelContenido = new JPanel(new BorderLayout());

        // Panel superior con combos y botones para agregar producto a la orden
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        comboProductos = new JComboBox<>();
        comboMesas = new JComboBox<>();

        modeloListaOrden = new DefaultListModel<>();
        listaOrden = new JList<>(modeloListaOrden);

        btnAgregarProducto = new JButton("Agregar Producto");
        btnAgregarProducto.addActionListener(e -> {
            String prod = (String) comboProductos.getSelectedItem();
            if (prod != null && !prod.isEmpty()) modeloListaOrden.addElement(prod);
        });

        btnGuardarOrden = new JButton("Guardar Orden");
        btnGuardarOrden.addActionListener(e -> guardarOrden());

        btnVerOrdenes = new JButton("Ver Órdenes Actuales");
        btnVerOrdenes.addActionListener(e -> new OrdenesMeseroVista(meseroId).setVisible(true));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginVista().setVisible(true);
        });

        panelSuperior.add(new JLabel("Producto:"));
        panelSuperior.add(comboProductos);
        panelSuperior.add(new JLabel("Mesa:"));
        panelSuperior.add(comboMesas);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnGuardarOrden);
        panelBotones.add(btnVerOrdenes);
        panelBotones.add(btnCerrarSesion);

        JPanel panelOrden = new JPanel(new BorderLayout());
        panelOrden.add(panelSuperior, BorderLayout.NORTH);
        panelOrden.add(new JScrollPane(listaOrden), BorderLayout.CENTER);
        panelOrden.add(panelBotones, BorderLayout.SOUTH);

        // Tabla para mostrar productos filtrados por categoría
        tabla = new JTable();
        scrollPane = new JScrollPane(tabla);

        // Dividir el panel contenido en dos partes horizontales
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, panelOrden);
        splitPane.setDividerLocation(300);
        panelContenido.add(splitPane, BorderLayout.CENTER);

        add(panelContenido, BorderLayout.CENTER);

        // Cargar datos iniciales
        cargarProductos();
        cargarMesas();

        // Conectar eventos de botones categoría
        conectarEventos();

        setVisible(true);
    }

    private void conectarEventos() {
        btnEntradas.addActionListener(e -> mostrarProductosPorCategoria("entrada"));
        btnPlatos.addActionListener(e -> mostrarProductosPorCategoria("plato"));
        btnPostres.addActionListener(e -> mostrarProductosPorCategoria("postre"));
        btnBebidas.addActionListener(e -> mostrarProductosPorCategoria("bebida"));
    }

    private void mostrarProductosPorCategoria(String categoria) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        try (Connection conn = DBConexion.getConnection()) {
            String sql = "SELECT id, nombre, precio, stock FROM productos WHERE LOWER(categoria) = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, categoria.toLowerCase());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Object[] fila = {
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    };
                    modelo.addRow(fila);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }

        tabla.setModel(modelo);
    }

    private void cargarProductos() {
        comboProductos.removeAllItems();
        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT nombre FROM productos WHERE stock > 0")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboProductos.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarMesas() {
        comboMesas.removeAllItems();
        try (Connection conn = DBConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, numero FROM mesas WHERE estado = 'libre'")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboMesas.addItem(rs.getInt("id") + " - Mesa " + rs.getInt("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarOrden() {
        if (modeloListaOrden.isEmpty() || comboMesas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione productos y una mesa.");
            return;
        }

        try (Connection conn = DBConexion.getConnection()) {
            conn.setAutoCommit(false);

            // Obtener ID de mesa
            String itemMesa = (String) comboMesas.getSelectedItem();
            int idMesa = Integer.parseInt(itemMesa.split(" ")[0]);

            // Insertar orden
            PreparedStatement insertOrden = conn.prepareStatement(
                    "INSERT INTO ordenes (id_mesero, id_mesa, estado) VALUES (?, ?, 'abierta')",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertOrden.setInt(1, meseroId);
            insertOrden.setInt(2, idMesa);
            insertOrden.executeUpdate();
            ResultSet rsOrden = insertOrden.getGeneratedKeys();
            rsOrden.next();
            int idOrden = rsOrden.getInt(1);

            double total = 0.0;
            for (int i = 0; i < modeloListaOrden.size(); i++) {
                String nombreProd = modeloListaOrden.get(i);

                // Obtener ID y precio
                PreparedStatement getProd = conn.prepareStatement("SELECT id, precio FROM productos WHERE nombre = ?");
                getProd.setString(1, nombreProd);
                ResultSet rsProd = getProd.executeQuery();
                if (rsProd.next()) {
                    int idProd = rsProd.getInt("id");
                    double precio = rsProd.getDouble("precio");
                    int cantidad = 1;

                    double subtotal = precio * cantidad;
                    total += subtotal;

                    // Insertar detalle
                    PreparedStatement insertDetalle = conn.prepareStatement(
                            "INSERT INTO orden_detalle (id_orden, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)"
                    );
                    insertDetalle.setInt(1, idOrden);
                    insertDetalle.setInt(2, idProd);
                    insertDetalle.setInt(3, cantidad);
                    insertDetalle.setDouble(4, subtotal);
                    insertDetalle.executeUpdate();

                    // Actualizar stock
                    PreparedStatement updateStock = conn.prepareStatement("UPDATE productos SET stock = stock - ? WHERE id = ?");
                    updateStock.setInt(1, cantidad);
                    updateStock.setInt(2, idProd);
                    updateStock.executeUpdate();
                }
            }

            // Actualizar total orden
            PreparedStatement updateTotal = conn.prepareStatement("UPDATE ordenes SET total = ? WHERE id = ?");
            updateTotal.setDouble(1, total);
            updateTotal.setInt(2, idOrden);
            updateTotal.executeUpdate();

            // Cambiar estado mesa
            PreparedStatement updateMesa = conn.prepareStatement("UPDATE mesas SET estado = 'ocupada' WHERE id = ?");
            updateMesa.setInt(1, idMesa);
            updateMesa.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(this, "Orden guardada exitosamente.");
            modeloListaOrden.clear();
            cargarMesas();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la orden: " + e.getMessage());
        }
    }
}

