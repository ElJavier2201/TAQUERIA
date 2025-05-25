package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductosDialog extends JDialog {
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnCerrar;

    public ProductosDialog(JFrame parent, String categoria) {
        super(parent, "Productos - " + categoria, true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock"}, 0);
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        add(scrollPane, BorderLayout.CENTER);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}

