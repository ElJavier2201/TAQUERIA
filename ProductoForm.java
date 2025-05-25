package vista;

import modelo.Producto;
import javax.swing.*;
import java.awt.*;

/**
 * Formulario para agregar o editar productos en el inventario.
 */
public class ProductoForm extends JDialog {

    private final JTextField txtNombre;
    private final JTextField txtCategoria;
    private final JTextField txtStock;
    private final JTextField txtPrecio;
    private boolean confirmado = false;
    private final Producto producto;

    public ProductoForm(Producto productoExistente) {
        this.producto = (productoExistente != null) ? productoExistente : new Producto();

        setTitle(productoExistente == null ? "Agregar Producto" : "Editar Producto");
        setModal(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel campos = new JPanel(new GridLayout(5, 2, 10, 10));
        campos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        campos.add(txtNombre);

        campos.add(new JLabel("Categoría:"));
        txtCategoria = new JTextField();
        campos.add(txtCategoria);

        campos.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        campos.add(txtStock);

        campos.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        campos.add(txtPrecio);

        add(campos, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Rellenar datos si es edición
        if (productoExistente != null) {
            txtNombre.setText(producto.getNombre());
            txtCategoria.setText(producto.getCategoria());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtStock.setText(String.valueOf(producto.getStock()));
        }

        // Acciones
        btnGuardar.addActionListener(e -> {
            if (validarCampos()) {
                producto.setNombre(txtNombre.getText().trim());
                producto.setCategoria(txtCategoria.getText().trim());
                producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                producto.setStock(Integer.parseInt(txtStock.getText().trim()));
                confirmado = true;
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Muestra el formulario y devuelve si se confirmó.
     */
    public boolean mostrar() {
        setVisible(true);
        return confirmado;
    }

    /**
     * Devuelve el producto creado o editado.
     */
    public Producto getProducto() {
        return producto;
    }

    private boolean validarCampos() {
        try {
            Integer.parseInt(txtStock.getText().trim());
            Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock y Precio deben ser valores numéricos.");
            return false;
        }
        if (txtNombre.getText().trim().isEmpty() || txtCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }
}
