package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Auditoria;
import modelo.Proyecto;
import negocio.ProyectoService;
import negocio.ReporteService;

public class FrmReportesProyectos extends JFrame {
    private ProyectoService proyectoService;
    private ReporteService reporteService;

    public FrmReportesProyectos(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
        this.reporteService = new ReporteService();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Módulo 3: Reportes Consolidados y Auditoría - Gobernación de La Paz");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstiloUI.FONDO_CLARO);
        setLayout(new BorderLayout(10, 10));

        // Cabecera
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(EstiloUI.GRIS_PIZARRA);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblHead = new JLabel("MÓDULO 03  |  REPORTES CONSOLIDADOS Y TRAZABILIDAD DE AUDITORÍA");
        lblHead.setForeground(Color.WHITE);
        lblHead.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTop.add(lblHead, BorderLayout.WEST);
        add(pnlTop, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pestanas.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        Proyecto[] activos = proyectoService.obtenerProyectosActivos();

        Object[][] datosEstado = reporteService.generarResumenPorEstado(activos);
        String[] colsEstado = {"Estado del Proyecto", "Cantidad de Proyectos", "Presupuesto Asignado (Bs.)", "Presupuesto Ejecutado (Bs.)"};
        pestanas.addTab("Resumen por Estado", crearPanelTabla(datosEstado, colsEstado));

        Object[][] datosUnidad = reporteService.generarResumenPorUnidad(activos);
        String[] colsUnidad = {"Unidad Ejecutora", "Cantidad de Proyectos", "Presupuesto Asignado (Bs.)", "Presupuesto Ejecutado (Bs.)"};
        pestanas.addTab("Por Unidad Ejecutora", crearPanelTabla(datosUnidad, colsUnidad));

        Object[][] datosFuente = reporteService.generarResumenPorFuente(activos);
        String[] colsFuente = {"Fuente de Financiamiento", "Cantidad de Proyectos", "Presupuesto Asignado (Bs.)", "Presupuesto Ejecutado (Bs.)"};
        pestanas.addTab("Por Fuente Financiamiento", crearPanelTabla(datosFuente, colsFuente));

        Auditoria[] historial = proyectoService.obtenerHistorialAuditoria();
        Object[][] datosAuditoria = new Object[historial.length][4];
        for (int i = 0; i < historial.length; i++) {
            datosAuditoria[i][0] = historial[i].getFechaHora();
            datosAuditoria[i][1] = historial[i].getUsuario();
            datosAuditoria[i][2] = historial[i].getAccion();
            datosAuditoria[i][3] = historial[i].getDetalle();
        }
        String[] colsAuditoria = {"Fecha y Hora", "Usuario", "Operación", "Detalle del Cambio"};
        pestanas.addTab("Bitácora de Auditoría", crearPanelTabla(datosAuditoria, colsAuditoria));

        add(pestanas, BorderLayout.CENTER);

        // Barra Inferior
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        pnlSur.setBackground(Color.WHITE);
        pnlSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, EstiloUI.BORDE_SUAVE));

        JButton btnExportar = new JButton("Exportar Reporte General a Archivo TXT");
        EstiloUI.estilizarBoton(btnExportar, EstiloUI.AZUL_PRIMARIO, EstiloUI.AZUL_HOVER);

        JButton btnCerrar = new JButton("Cerrar");
        EstiloUI.estilizarBotonSecundario(btnCerrar);

        pnlSur.add(btnExportar);
        pnlSur.add(btnCerrar);
        add(pnlSur, BorderLayout.SOUTH);

        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarReporteTXT();
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JScrollPane crearPanelTabla(Object[][] datos, String[] columnas) {
        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(model);
        EstiloUI.estilizarTabla(tabla);
        return new JScrollPane(tabla);
    }

    private void exportarReporteTXT() {
        BufferedWriter bw = null;
        try {
            String nombreArchivo = "datos/REPORTE_CONSOLIDADO_GOBERNACION.txt";
            bw = new BufferedWriter(new FileWriter(nombreArchivo));
            bw.write("==========================================================================");
            bw.newLine();
            bw.write("GOBIERNO AUTÓNOMO DEPARTAMENTAL DE LA PAZ - REPORTE GENERAL DE PROYECTOS");
            bw.newLine();
            bw.write("==========================================================================");
            bw.newLine();
            bw.newLine();

            Proyecto[] activos = proyectoService.obtenerProyectosActivos();
            double totalPresupuesto = 0;
            for (int i = 0; i < activos.length; i++) {
                bw.write((i + 1) + ". " + activos[i].generarResumenReporte());
                bw.newLine();
                bw.write("   Unidad: " + activos[i].getUnidadEjecutora() + " | Fuente: " + activos[i].getFuenteFinanciamiento().getSigla());
                bw.newLine();
                bw.write("   " + activos[i].getDetalleEspecifico() + " | Avance Físico: " + activos[i].getAvanceFisico() + "%");
                bw.newLine();
                bw.newLine();
                totalPresupuesto += activos[i].obtenerMontoComprometido();
            }

            bw.write("--------------------------------------------------------------------------");
            bw.newLine();
            bw.write("TOTAL PROYECTOS ACTIVOS: " + activos.length);
            bw.newLine();
            bw.write("MONTO TOTAL COMPROMETIDO: Bs. " + String.format("%.2f", totalPresupuesto));
            bw.newLine();

            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente en:\n" + nombreArchivo);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException ignored) {
            }
        }
    }
}