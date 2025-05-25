package dao;

import util.DBConexion;
import modelo.ReporteVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para reportes.
 */
public class ReporteDAO {
    public List<ReporteVenta> obtenerReporte(String tipo) {
        List<ReporteVenta> lista = new ArrayList<>();
        String sql;

        if (tipo.equals("semanal")) {
            sql = "SELECT id, fecha, total FROM ventas WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
        } else {
            sql = "SELECT id, fecha, total FROM ventas WHERE MONTH(fecha) = MONTH(CURDATE())";
        }

        try (Connection con = DBConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ReporteVenta(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
