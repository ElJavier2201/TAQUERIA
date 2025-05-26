package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class CocinaVista extends JFrame {

    private final JTable tablaOrdenes;
    private final DefaultTableModel modelo;
    private final JComboBox<String> comboEstado;
    private final JComboBox<String> comboFiltro;

    public CocinaVista() {
        setTitle("Panel de Cocina - Órdenes en preparación");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel();
        tablaOrdenes = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        add(scrollPane, BorderLayout.CENTER);

        modelo.addColumn("ID Orden");
        modelo.addColumn("Mesa");
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Estado");

        // Panel superior (filtro)
        JPanel panelSuperior = new JPanel(new FlowLayout());
        comboFiltro = new JComboBox<>(new String[]{"Todas", "Pendiente", "En preparación", "Lista para entrega"});
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnActualizar = new JButton("Actualizar Tabla");

        btnFiltrar.addActionListener(e -> cargarOrdenes((String) Objects.requireNonNull(comboFiltro.getSelectedItem())));
        btnActualizar.addActionListener(e -> cargarOrdenes("Todas"));

        panelSuperior.add(new JLabel("Filtrar por Estado:"));
        panelSuperior.add(comboFiltro);
        panelSuperior.add(btnFiltrar);
        panelSuperior.add(btnActualizar);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel inferior (actualización de estado)
        JPanel panelInferior = new JPanel(new FlowLayout());
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "En preparación", "Lista para entrega"});
        JButton btnActualizarEstado = new JButton("Actualizar Estado");

        btnActualizarEstado.addActionListener(e -> actualizarEstadoOrden());

        panelInferior.add(new JLabel("Nuevo Estado:"));
        panelInferior.add(comboEstado);
        panelInferior.add(btnActualizarEstado);

        add(panelInferior, BorderLayout.SOUTH);

        cargarOrdenes("Todas");
        setVisible(true);
    }

    private void cargarOrdenes(String estadoFiltro) {
        modelo.setRowCount(0); // Limpiar tabla

        String url = "jdbc:mysql://localhost:3306/taqueria_db";
        String user = "root";
        String password = "220105";

        String sql = """
                SELECT o.id, o.id_mesa, p.nombre AS productos, od.cantidad, o.estado
                FROM ordenes o
                JOIN orden_detalle od ON o.id = od.id
                JOIN productos p ON od.id = p.nombre
                """ + (estadoFiltro.equals("Todas") ? "" : "WHERE o.estado = ?");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!estadoFiltro.equals("Todas")) {
                stmt.setString(1, estadoFiltro);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id_orden"),
                        rs.getInt("mesa_id"),
                        rs.getString("producto"),
                        rs.getInt("cantidad"),
                        rs.getString("estado")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstadoOrden() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una orden para actualizar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idOrden = (int) modelo.getValueAt(filaSeleccionada, 0);
        String nuevoEstado = (String) comboEstado.getSelectedItem();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/taqueria_db", "root", "220105")) {
            String sql = "UPDATE ordenes SET estado = ? WHERE id_orden = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idOrden);
            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Estado actualizado exitosamente.");
                cargarOrdenes((String) Objects.requireNonNull(comboFiltro.getSelectedItem()));
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CocinaVista::new);
    }
}
