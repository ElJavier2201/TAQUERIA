package vista;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import controlador.ReporteControlador;
import modelo.ReporteVenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Panel para mostrar reportes de ventas semanales y mensuales,
 * y generar PDF desde ellos.
 */
public class ReportesPanel extends JPanel {

    private DefaultTableModel modelo;
    private final ReporteControlador controlador;

    /**
     * Constructor del panel de reportes.
     */
    public ReportesPanel() {
        controlador = new ReporteControlador();
        initComponents();
    }

    /**
     * Inicializa la interfaz gráfica.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Reportes de Ventas", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Fecha", "Total"}, 0);
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botonera inferior
        JPanel panelBotones = new JPanel();

        JButton btnSemanal = new JButton("Reporte Semanal");
        JButton btnMensual = new JButton("Reporte Mensual");
        JButton btnPDF = new JButton("Generar PDF");

        panelBotones.add(btnSemanal);
        panelBotones.add(btnMensual);
        panelBotones.add(btnPDF);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnSemanal.addActionListener(e -> cargarDatos("semanal"));
        btnMensual.addActionListener(e -> cargarDatos("mensual"));
        btnPDF.addActionListener(e -> generarPDF());
    }

    /**
     * Carga los datos de la base de datos en la tabla.
     *
     * @param tipo "semanal" o "mensual"
     */
    private void cargarDatos(String tipo) {
        modelo.setRowCount(0);
        List<ReporteVenta> reportes = controlador.obtenerReporte(tipo);
        for (ReporteVenta r : reportes) {
            modelo.addRow(new Object[]{r.id(), r.fecha(), r.total()});
        }
    }

    /**
     * Genera un PDF con el contenido actual de la tabla.
     */
    private void generarPDF() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
            return;
        }

        try {
            Document documento = new Document();
            String path = "reporte_ventas.pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(path));
            documento.open();

            documento.add(new Paragraph("Reporte de Ventas", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            documento.add(new Paragraph(" ")); // espacio

            PdfPTable pdfTable = new PdfPTable(3);
            pdfTable.addCell("ID");
            pdfTable.addCell("Fecha");
            pdfTable.addCell("Total");

            for (int i = 0; i < modelo.getRowCount(); i++) {
                pdfTable.addCell(modelo.getValueAt(i, 0).toString());
                pdfTable.addCell(modelo.getValueAt(i, 1).toString());
                pdfTable.addCell(modelo.getValueAt(i, 2).toString());
            }

            documento.add(pdfTable);
            documento.close();

            JOptionPane.showMessageDialog(this, "PDF generado correctamente en: " + path);
            Desktop.getDesktop().open(new java.io.File(path));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
        }
    }
}
