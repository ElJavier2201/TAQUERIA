package controlador;
import dao.ReporteDAO;
import modelo.ReporteVenta;
import java.util.List;

/**
 * Controlador para manejar lógica de reportes.
 */
public class ReporteControlador {

    private ReporteDAO dao = new ReporteDAO();

    public List<ReporteVenta> obtenerReporte(String tipo) {
        return dao.obtenerReporte(tipo);
    }
}