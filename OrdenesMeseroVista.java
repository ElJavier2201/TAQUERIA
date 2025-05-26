package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Ventana para ver la ventana de ordenes que el mesero crea con
 * el menu
 * @author Javier Lopez
 */

public class OrdenesMeseroVista extends JFrame {

    private final JTable tablaOrdenes;
    private final DefaultTableModel modelo;
    private final int meseroId;
    private final JComboBox<String> comboEstado;

    /**
     * Inicializamos nuestros componentes de la interfaz grafica
     * @param meseroId
     */
    public OrdenesMeseroVista(int meseroId) {
        this.meseroId = meseroId;

        setTitle("Órdenes del Mesero");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel();
        tablaOrdenes = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        add(scrollPane, BorderLayout.CENTER);

        modelo.addColumn("Orden");
        modelo.addColumn("Mesa");
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Estado");

        cargarOrdenes();

        // Panel inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout());

        comboEstado = new JComboBox<>(new String[]{"Pendiente", "En preparación", "Entregado"});
        JButton btnActualizarEstado = new JButton("Actualizar Estado");

        panelInferior.add(new JLabel("Nuevo Estado:"));
        panelInferior.add(comboEstado);
        panelInferior.add(btnActualizarEstado);

        add(panelInferior, BorderLayout.SOUTH);

        // Acción del botón
        btnActualizarEstado.addActionListener(e -> actualizarEstadoOrden());

        setVisible(true);
    }

    /**
     * Cargamos las ordenes
     */

    private void cargarOrdenes() {
        modelo.setRowCount(0); // Limpiar tbla

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/taqueria_db", "root", "220105")) {
            String sql = """
              SELECT o.id, o.id_m, p.nombre AS producto, od.cantidad, o.estado
                 FROM ordenes o
                 JOIN orden_detalle od ON o.id_orden = od.id_orden
                 JOIN productos p ON od.id_producto = p.id
                 WHERE o.mesero_id = ?
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, meseroId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("id_mesa"),
                        rs.getString("producto"),
                        rs.getInt("cantidad"),
                        rs.getString("estado")
                });
                System.out.println("Orden: " + rs.getInt("id_orden") + ", Mesa: " + rs.getInt("mesa_id"));
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
            String sql = "UPDATE ordenes SET estado = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idOrden);
            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                JOptionPane.showMessageDialog(this, "Estado actualizado exitosamente.");
                cargarOrdenes(); // Recargar tabla
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

