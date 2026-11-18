package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import modelo.EstadoProyecto;
import modelo.Proyecto;
import modelo.Usuario;
import negocio.ProyectoService;

public class FrmPrincipal extends JFrame {
    private Usuario usuarioActual;
    private ProyectoService proyectoService;
    private JLabel lblKpiTotal, lblKpiPresupuesto, lblKpiEjecucion, lblKpiAvance;

    public FrmPrincipal(Usuario usuarioActual, ProyectoService proyectoService) {
        this.usuarioActual = usuarioActual;
        this.proyectoService = proyectoService;
        inicializarComponentes();
        actualizarIndicadores();
    }

    private void inicializarComponentes() {
        setTitle("REGISTRO SEGUIMIENTO Y REPORTES DE PROYECTOS - Gobernación de La Paz");
        setSize(860, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Encabezado Institucional
        JPanel pnlHeader = new JPanel(new BorderLayout(0, 5));
        pnlHeader.setBackground(EstiloUI.AZUL_OSCURO);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(212, 175, 55)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        JLabel lblTitulo = new JLabel("REGISTRO, SEGUIMIENTO Y REPORTES DE PROYECTOS", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 19));

        JLabel lblInstitucion = new JLabel("Gobierno Autónomo Departamental de La Paz - Bolivia", SwingConstants.CENTER);
        lblInstitucion.setForeground(new Color(191, 219, 254));
        lblInstitucion.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlHeader.add(lblTitulo, BorderLayout.NORTH);
        pnlHeader.add(lblInstitucion, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // Contenedor Central (KPIs + Tarjetas de Módulos)
        JPanel pnlCuerpo = new JPanel(new BorderLayout(0, 20));
        pnlCuerpo.setBackground(EstiloUI.FONDO_CLARO);
        pnlCuerpo.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Fila Superior: 4 Tarjetas Resumen (KPIs)
        JPanel pnlKpis = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlKpis.setOpaque(false);

        lblKpiTotal = new JLabel("0", SwingConstants.CENTER);
        lblKpiPresupuesto = new JLabel("Bs. 0.00", SwingConstants.CENTER);
        lblKpiEjecucion = new JLabel("0", SwingConstants.CENTER);
        lblKpiAvance = new JLabel("0.0%", SwingConstants.CENTER);

        pnlKpis.add(crearTarjetaKpi("PROYECTOS ACTIVOS", lblKpiTotal, EstiloUI.AZUL_PRIMARIO));
        pnlKpis.add(crearTarjetaKpi("PRESUPUESTO TOTAL", lblKpiPresupuesto, EstiloUI.VERDE_EXITO));
        pnlKpis.add(crearTarjetaKpi("EN EJECUCIÓN", lblKpiEjecucion, new Color(217, 119, 6)));
        pnlKpis.add(crearTarjetaKpi("AVANCE PROMEDIO", lblKpiAvance, EstiloUI.GRIS_PIZARRA));

        pnlCuerpo.add(pnlKpis, BorderLayout.NORTH);

        // Fila Central: 3 Tarjetas de Módulos Principales
        JPanel pnlModulos = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlModulos.setOpaque(false);

        JPanel cardRegistro = crearTarjetaModulo(
                "01",
                "REGISTRO DE PROYECTOS",
                "Alta, modificación, consulta y baja lógica de proyectos de inversión.",
                EstiloUI.AZUL_PRIMARIO, EstiloUI.AZUL_HOVER,
                new Runnable() {
                    @Override
                    public void run() {
                        FrmRegistroProyectos frm = new FrmRegistroProyectos(usuarioActual, proyectoService);
                        frm.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                actualizarIndicadores();
                            }
                        });
                        frm.setVisible(true);
                    }
                });

        JPanel cardSeguimiento = crearTarjetaModulo(
                "02",
                "SEGUIMIENTO DE PROYECTOS",
                "Control del ciclo de estados, ejecución financiera y avance físico.",
                EstiloUI.VERDE_EXITO, EstiloUI.VERDE_HOVER,
                new Runnable() {
                    @Override
                    public void run() {
                        FrmSeguimientoProyectos frm = new FrmSeguimientoProyectos(usuarioActual, proyectoService);
                        frm.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                actualizarIndicadores();
                            }
                        });
                        frm.setVisible(true);
                    }
                });

        JPanel cardReportes = crearTarjetaModulo(
                "03",
                "REPORTES Y AUDITORÍA",
                "Consolidados por estado, unidad, fuente y bitácora de cambios.",
                EstiloUI.GRIS_PIZARRA, EstiloUI.GRIS_HOVER,
                new Runnable() {
                    @Override
                    public void run() {
                        new FrmReportesProyectos(proyectoService).setVisible(true);
                    }
                });

        pnlModulos.add(cardRegistro);
        pnlModulos.add(cardSeguimiento);
        pnlModulos.add(cardReportes);
        pnlCuerpo.add(pnlModulos, BorderLayout.CENTER);

        add(pnlCuerpo, BorderLayout.CENTER);

        // Barra Inferior de Sesión
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, EstiloUI.BORDE_SUAVE),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        JLabel lblSesion = new JLabel("Funcionario: " + usuarioActual.getNombreCompleto()
                + "   |   Rol activo: " + usuarioActual.getRol());
        lblSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSesion.setForeground(new Color(30, 41, 59));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        EstiloUI.estilizarBotonSecundario(btnCerrarSesion);

        pnlFooter.add(lblSesion, BorderLayout.WEST);
        pnlFooter.add(btnCerrarSesion, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmLogin().setVisible(true);
                dispose();
            }
        });
    }

    private JPanel crearTarjetaKpi(String titulo, JLabel lblValor, Color colorAcento) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, colorAcento),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTit.setForeground(new Color(100, 116, 139));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValor.setForeground(new Color(15, 23, 42));

        card.add(lblTit, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearTarjetaModulo(String numero, String titulo, String descripcion,
            final Color colorBase, final Color colorHover, final Runnable accion) {
        final JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(colorBase);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(22, 18, 22, 18));

        JLabel lblNum = new JLabel("MÓDULO " + numero, SwingConstants.CENTER);
        lblNum.setForeground(new Color(224, 242, 254));
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JLabel lblTit = new JLabel("<html><center>" + titulo + "</center></html>", SwingConstants.CENTER);
        lblTit.setForeground(Color.WHITE);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>", SwingConstants.CENTER);
        lblDesc.setForeground(new Color(241, 245, 249));
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        card.add(lblNum, BorderLayout.NORTH);
        card.add(lblTit, BorderLayout.CENTER);
        card.add(lblDesc, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(colorBase);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });

        return card;
    }

    public void actualizarIndicadores() {
        Proyecto[] activos = proyectoService.obtenerProyectosActivos();
        int total = activos.length;
        double sumaPresupuesto = 0;
        int enEjecucion = 0;
        double sumaAvance = 0;

        for (int i = 0; i < total; i++) {
            sumaPresupuesto += activos[i].getPresupuesto().getMontoAsignado();
            if (activos[i].getEstado() == EstadoProyecto.EJECUCION) {
                enEjecucion++;
            }
            sumaAvance += activos[i].getAvanceFisico();
        }

        double promedioAvance = total > 0 ? (sumaAvance / total) : 0.0;
        lblKpiTotal.setText(String.valueOf(total));
        lblKpiPresupuesto.setText("Bs. " + String.format("%.2f", sumaPresupuesto));
        lblKpiEjecucion.setText(String.valueOf(enEjecucion));
        lblKpiAvance.setText(String.format("%.1f%%", promedioAvance));
    }
}