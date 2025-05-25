package vista;

import controlador.UsuarioControlador;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel que permite la gestión de usuarios:
 * visualización, adición, edición y eliminación.
 * Este panel solo debe ser accedido por administradores.
 *
 * Se conecta con la base de datos a través del controlador UsuarioController.
 *
 * @author Javier Lopez
 */
public class UsuariosPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private final UsuarioControlador controlador;

    /**
     * Crea e inicializa el panel de gestión de usuarios.
     */
    public UsuariosPanel() {
        setLayout(new BorderLayout());
        controlador = new UsuarioControlador();
        JLabel lblTitulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        initTable();
        initButtons();
        cargarUsuarios();
    }

    /**
     * Inicializa la tabla que muestra los usuarios.
     */
    private void initTable() {
        model = new DefaultTableModel(new String[]{"ID", "Usuario", "Rol"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Inicializa los botones para operaciones CRUD.
     */
    private void initButtons() {
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga todos los usuarios desde la base de datos a la tabla.
     */
    private void cargarUsuarios() {
        model.setRowCount(0); // Limpiar tabla
        List<Usuario> lista = controlador.obtenerTodos();

        for (Usuario u : lista) {
            model.addRow(new Object[]{u.getId(), u.getNombreUsuario(), u.getRol()});
        }
    }

    /**
     * Agrega un nuevo usuario usando un formulario de entrada.
     */
    private void agregarUsuario() {
        UsuarioForm form = new UsuarioForm(null); // modo agregar
        int resultado = JOptionPane.showConfirmDialog(this, form, "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            Usuario nuevo = form.getUsuario();
            controlador.insertar(nuevo);
            cargarUsuarios();
        }
    }

    /**
     * Edita el usuario seleccionado en la tabla.
     */
    private void editarUsuario() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.");
            return;
        }

        int id = (int) model.getValueAt(fila, 0);
        Usuario existente = controlador.obtenerPorId(id);

        UsuarioForm form = new UsuarioForm(existente); // modo editar
        int resultado = JOptionPane.showConfirmDialog(this, form, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (resultado == JOptionPane.OK_OPTION) {
            Usuario actualizado = form.getUsuario();
            actualizado.setId(id);
            controlador.actualizar(actualizado);
            cargarUsuarios();
        }
    }

    /**
     * Elimina el usuario seleccionado en la tabla.
     */
    private void eliminarUsuario() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
            return;
        }

        int id = (int) model.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controlador.eliminar(id);
            cargarUsuarios();
        }
    }
}

