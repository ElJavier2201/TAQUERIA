package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import java.util.List;

public class ProductoControlador {
    private final ProductoDAO productoDAO;

    public ProductoControlador() {
        this.productoDAO = new ProductoDAO();
    }

    /**
     * Obtiene productos según su categoría.
     */
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return productoDAO.listarPorCategoria(categoria);
    }

    /**
     * Obtiene un producto por su ID.
     */
    public Producto obtenerProductoPorId(int id) {
        return productoDAO.obtenerPorId(id);
    }

    /**
     * Obtiene todos los productos.
     */
    public List<Producto> obtenerTodos() {
        return productoDAO.obtenerTodos();
    }
}


