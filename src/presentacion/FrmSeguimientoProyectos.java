package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.EstadoProyecto;
import modelo.Proyecto;
import modelo.Usuario;
import negocio.ProyectoException;
import negocio.ProyectoService;

public class FrmSeguimientoProyectos extends JFrame {
    private Usuario usuarioActual;
    private ProyectoService proyectoService;

    private JTextField txtCodigoSel, txtNombreSel, txtAsignadoSel, txtEjecutado, txtAvance;
    private JComboBox<EstadoProyecto> cmbEstado;
    private JButton btnActualizar;
    private JTable tblSeguimiento;
    private DefaultTableModel modeloTabla;

    public FrmSeguimientoProyectos(Usuario usuarioActual, ProyectoService proyectoService) {
        this.usuarioActual = usuarioActual;
        this.proyectoService = proyectoService;
        inicializarComponentes();
        if (usuarioActual.getRol().equalsIgnoreCase("CONSULTA")) {
            btnActualizar.setEnabled(false);
        }
        cargarTabla();
    }

    private void inicializarComponentes() {
        setTitle("Módulo 2: Seguimiento y Control de Estados - Gobernación de La Paz");
        setSize(920, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstiloUI.FONDO_CLARO);
        setLayout(new BorderLayout(10, 10));

        // Cabecera
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(EstiloUI.VERDE_EXITO);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblHead = new JLabel("MÓDULO 02  |  SEGUIMIENTO DE CICLO DE VIDA Y EJECUCIÓN DE PROYECTOS");
        lblHead.setForeground(Color.WHITE);
        lblHead.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTop.add(lblHead, BorderLayout.WEST);

        // Panel Superior de Control
        JPanel pnlControl = new JPanel(new GridBagLayout());
        pnlControl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 5, 15),
                BorderFactory.createTitledBorder("Actualización de Estado y Avance del Proyecto")));
        pnlControl.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlControl.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtCodigoSel = new JTextField(10);
        txtCodigoSel.setEditable(false);
        EstiloUI.estilizarCampo(txtCodigoSel);
        pnlControl.add(txtCodigoSel, gbc);

        gbc.gridx = 2; gbc.gridy = 0; pnlControl.add(new JLabel("Proyecto:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        txtNombreSel = new JTextField(25);
        txtNombreSel.setEditable(false);
        EstiloUI.estilizarCampo(txtNombreSel);
        pnlControl.add(txtNombreSel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlControl.add(new JLabel("Estado Ciclo de Vida:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cmbEstado = new JComboBox<>(EstadoProyecto.values());
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlControl.add(cmbEstado, gbc);

        gbc.gridx = 2; gbc.gridy = 1; pnlControl.add(new JLabel("Presupuesto Asignado (Bs.):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1;
        txtAsignadoSel = new JTextField(12);
        txtAsignadoSel.setEditable(false);
        EstiloUI.estilizarCampo(txtAsignadoSel);
        pnlControl.add(txtAsignadoSel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlControl.add(new JLabel("Monto Ejecutado (Bs.):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtEjecutado = new JTextField(12);
        EstiloUI.estilizarCampo(txtEjecutado);
        pnlControl.add(txtEjecutado, gbc);

        gbc.gridx = 2; gbc.gridy = 2; pnlControl.add(new JLabel("Avance Físico (%):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        txtAvance = new JTextField(10);
        EstiloUI.estilizarCampo(txtAvance);
        pnlControl.add(txtAvance, gbc);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlBtn.setBackground(EstiloUI.FONDO_CLARO);
        btnActualizar = new JButton("Registrar Avance y Cambio de Estado");
        EstiloUI.estilizarBoton(btnActualizar, EstiloUI.VERDE_EXITO, EstiloUI.VERDE_HOVER);
        pnlBtn.add(btnActualizar);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlTop, BorderLayout.NORTH);
        pnlNorte.add(pnlControl, BorderLayout.CENTER);
        pnlNorte.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlNorte, BorderLayout.NORTH);

        // Tabla de Seguimiento
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBackground(Color.WHITE);
        pnlCentro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 15, 15, 15),
                BorderFactory.createTitledBorder("Seleccione un proyecto de la lista para actualizar su seguimiento")));

        String[] cols = {"Código", "Nombre", "Estado Actual", "Asignado (Bs.)", "Ejecutado (Bs.)", "% Financiero", "% Físico"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSeguimiento = new JTable(modeloTabla);
        EstiloUI.estilizarTabla(tblSeguimiento);
        pnlCentro.add(new JScrollPane(tblSeguimiento), BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        tblSeguimiento.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblSeguimiento.getSelectedRow();
                if (fila >= 0) {
                    String cod = modeloTabla.getValueAt(fila, 0).toString();
                    Proyecto p = proyectoService.buscarProyectoActivo(cod);
                    if (p != null) {
                        txtCodigoSel.setText(p.getCodigo());
                        txtNombreSel.setText(p.getNombre());
                        cmbEstado.setSelectedItem(p.getEstado());
                        txtAsignadoSel.setText(String.valueOf(p.getPresupuesto().getMontoAsignado()));
                        txtEjecutado.setText(String.valueOf(p.getPresupuesto().getMontoEjecutado()));
                        txtAvance.setText(String.valueOf(p.getAvanceFisico()));
                    }
                }
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarSeguimiento();
            }
        });
    }

    private void actualizarSeguimiento() {
        try {
            String cod = txtCodigoSel.getText().trim();
            if (cod.isEmpty()) {
                throw new ProyectoException("Seleccione primero un proyecto de la tabla.");
            }
            EstadoProyecto nuevoEstado = (EstadoProyecto) cmbEstado.getSelectedItem();
            double ejecutado = Double.parseDouble(txtEjecutado.getText().trim());
            double avance = Double.parseDouble(txtAvance.getText().trim());

            proyectoService.actualizarSeguimiento(cod, nuevoEstado, ejecutado, avance, usuarioActual.getUsername());
            JOptionPane.showMessageDialog(this, "Seguimiento actualizado y registrado en auditoría.");
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para monto y porcentaje.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (ProyectoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Regla de Negocio", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        Proyecto[] activos = proyectoService.obtenerProyectosActivos();
        for (int i = 0; i < activos.length; i++) {
            Proyecto p = activos[i];
            modeloTabla.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getEstado().getDescripcion(),
                String.format("%.2f", p.getPresupuesto().getMontoAsignado()),
                String.format("%.2f", p.getPresupuesto().getMontoEjecutado()),
                String.format("%.1f%%", p.getPresupuesto().calcularPorcentajeEjecucion()),
                String.format("%.1f%%", p.getAvanceFisico())
            });
        }
    }
}