package modelo;

import java.util.Date;

/**
 * Modelo para representar una venta en el reporte.
 */
public record ReporteVenta(int id, Date fecha, double total) {
}
