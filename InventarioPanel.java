package vista;

import dao.ProductoDAO;
import modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de inventario del administrador.
 * Permite ver y modificar productos.
 * @author Javier Lopez
 */
public class InventarioPanel extends JPanel {

    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final ProductoDAO dao = new ProductoDAO();

    public InventarioPanel() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Inventario de Productos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoría", "Precio", "Stock"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        acciones.add(btnAgregar);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        add(acciones, BorderLayout.SOUTH);

        cargarProductos();

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        List<Producto> productos = dao.obtenerTodos();
        for (Producto p : productos) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getCategoria(),p.getPrecio(), p.getStock()});
        }
    }

    private void agregarProducto() {
        ProductoForm form = new ProductoForm(null);
        if (form.mostrar() && dao.agregar(form.getProducto())) {
            cargarProductos();
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            Producto p = new Producto();
            p.setId((Integer) modelo.getValueAt(fila, 0));
            p.setNombre((String) modelo.getValueAt(fila, 1));
            p.setCategoria((String) modelo.getValueAt(fila, 2));
            p.setStock((Integer) modelo.getValueAt(fila, 3));
            p.setPrecio((Double) modelo.getValueAt(fila, 4));

            ProductoForm form = new ProductoForm(p);
            if (form.mostrar() && dao.actualizar(form.getProducto())) {
                cargarProductos();
            }
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (Integer) modelo.getValueAt(fila, 0);
            if (JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dao.eliminar(id);
                cargarProductos();
            }
        }
    }
}
