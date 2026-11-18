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
import modelo.FuenteFinanciamiento;
import modelo.Presupuesto;
import modelo.Proyecto;
import modelo.ProyectoInfraestructura;
import modelo.ProyectoSocial;
import modelo.UnidadEjecutora;
import modelo.Usuario;
import negocio.ProyectoException;
import negocio.ProyectoService;

public class FrmRegistroProyectos extends JFrame {
    private Usuario usuarioActual;
    private ProyectoService proyectoService;

    private JTextField txtCodigo, txtNombre, txtPresupuesto, txtGestion, txtEspecifico, txtBuscar;
    private JComboBox<String> cmbTipo;
    private JComboBox<UnidadEjecutora> cmbUnidad;
    private JComboBox<FuenteFinanciamiento> cmbFuente;
    private JLabel lblEspecifico;
    private JButton btnRegistrar, btnModificar, btnBaja, btnLimpiar, btnBuscar;
    private JTable tblProyectos;
    private DefaultTableModel modeloTabla;

    public FrmRegistroProyectos(Usuario usuarioActual, ProyectoService proyectoService) {
        this.usuarioActual = usuarioActual;
        this.proyectoService = proyectoService;
        inicializarComponentes();
        aplicarPermisosRol();
        cargarTabla("");
    }

    private void inicializarComponentes() {
        setTitle("Módulo 1: Registro de Proyectos - Gobernación de La Paz");
        setSize(980, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstiloUI.FONDO_CLARO);
        setLayout(new BorderLayout(10, 10));

        // Cabecera de Módulo
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(EstiloUI.AZUL_OSCURO);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lblHead = new JLabel("MÓDULO 01  |  REGISTRO Y ADMINISTRACIÓN DE PROYECTOS DE INVERSIÓN");
        lblHead.setForeground(Color.WHITE);
        lblHead.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTop.add(lblHead, BorderLayout.WEST);

        // Formulario Superior
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 5, 15),
                BorderFactory.createTitledBorder("Datos del Proyecto de Inversión Pública")));
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(new JLabel("Código (*):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtCodigo = new JTextField(12); EstiloUI.estilizarCampo(txtCodigo); pnlForm.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; pnlForm.add(new JLabel("Tipo Proyecto:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0;
        cmbTipo = new JComboBox<>(new String[]{"INFRAESTRUCTURA", "SOCIAL"});
        cmbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlForm.add(cmbTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(new JLabel("Nombre Proyecto (*):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3;
        txtNombre = new JTextField(35);
        EstiloUI.estilizarCampo(txtNombre);
        pnlForm.add(txtNombre, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(new JLabel("Unidad Ejecutora:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        cmbUnidad = new JComboBox<>(new UnidadEjecutora[]{
            new UnidadEjecutora("SEDCAM", "Servicio Departamental de Caminos", "Ing. Carlos Mamani"),
            new UnidadEjecutora("SEDES", "Servicio Departamental de Salud", "Dra. Ana Quispe"),
            new UnidadEjecutora("SEDAG", "Servicio Departamental Agropecuario", "Ing. Luis Condori"),
            new UnidadEjecutora("DIR-EDU", "Dirección Departamental de Educación", "Lic. María Flores")
        });
        cmbUnidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlForm.add(cmbUnidad, gbc);

        gbc.gridx = 2; gbc.gridy = 2; pnlForm.add(new JLabel("Fuente Financiamiento:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        cmbFuente = new JComboBox<>(new FuenteFinanciamiento[]{
            new FuenteFinanciamiento("TGN", "Tesoro General de la Nación"),
            new FuenteFinanciamiento("IDH", "Impuesto Directo a los Hidrocarburos"),
            new FuenteFinanciamiento("REG", "Regalías Departamentales"),
            new FuenteFinanciamiento("TRANS", "Transferencias Institucionales")
        });
        cmbFuente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlForm.add(cmbFuente, gbc);

        gbc.gridx = 0; gbc.gridy = 3; pnlForm.add(new JLabel("Presupuesto Asignado (Bs.):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; txtPresupuesto = new JTextField(12); EstiloUI.estilizarCampo(txtPresupuesto); pnlForm.add(txtPresupuesto, gbc);

        gbc.gridx = 2; gbc.gridy = 3; pnlForm.add(new JLabel("Gestión (Año):"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; txtGestion = new JTextField("2026", 8); EstiloUI.estilizarCampo(txtGestion); pnlForm.add(txtGestion, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        lblEspecifico = new JLabel("Longitud / Magnitud (Km):");
        lblEspecifico.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEspecifico.setForeground(EstiloUI.AZUL_OSCURO);
        pnlForm.add(lblEspecifico, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        txtEspecifico = new JTextField(12);
        EstiloUI.estilizarCampo(txtEspecifico);
        pnlForm.add(txtEspecifico, gbc);

        // Botonera Estilizada
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        pnlAcciones.setBackground(EstiloUI.FONDO_CLARO);

        btnRegistrar = new JButton("Registrar Proyecto");
        btnModificar = new JButton("Guardar Modificación");
        btnBaja = new JButton("Dar de Baja Lógica");
        btnLimpiar = new JButton("Limpiar Campos");

        EstiloUI.estilizarBoton(btnRegistrar, EstiloUI.AZUL_PRIMARIO, EstiloUI.AZUL_HOVER);
        EstiloUI.estilizarBoton(btnModificar, EstiloUI.VERDE_EXITO, EstiloUI.VERDE_HOVER);
        EstiloUI.estilizarBoton(btnBaja, EstiloUI.ROJO_PELIGRO, EstiloUI.ROJO_HOVER);
        EstiloUI.estilizarBotonSecundario(btnLimpiar);

        pnlAcciones.add(btnRegistrar);
        pnlAcciones.add(btnModificar);
        pnlAcciones.add(btnBaja);
        pnlAcciones.add(btnLimpiar);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlTop, BorderLayout.NORTH);
        pnlNorte.add(pnlForm, BorderLayout.CENTER);
        pnlNorte.add(pnlAcciones, BorderLayout.SOUTH);
        add(pnlNorte, BorderLayout.NORTH);

        // Panel Central: Buscador y Tabla
        JPanel pnlTabla = new JPanel(new BorderLayout(5, 8));
        pnlTabla.setBackground(Color.WHITE);
        pnlTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 15, 15, 15),
                BorderFactory.createTitledBorder("Proyectos Activos Registrados (Haga clic en una fila para editar)")));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBusqueda.setBackground(Color.WHITE);
        pnlBusqueda.add(new JLabel("Buscar por Código o Nombre:"));
        txtBuscar = new JTextField(25);
        EstiloUI.estilizarCampo(txtBuscar);
        btnBuscar = new JButton("Filtrar");
        EstiloUI.estilizarBoton(btnBuscar, EstiloUI.GRIS_PIZARRA, EstiloUI.GRIS_HOVER);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlTabla.add(pnlBusqueda, BorderLayout.NORTH);

        String[] columnas = {"Código", "Nombre", "Tipo", "Unidad", "Fuente", "Presupuesto (Bs.)", "Estado", "Detalle Específico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProyectos = new JTable(modeloTabla);
        EstiloUI.estilizarTabla(tblProyectos);
        pnlTabla.add(new JScrollPane(tblProyectos), BorderLayout.CENTER);
        add(pnlTabla, BorderLayout.CENTER);

        cmbTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbTipo.getSelectedItem().equals("INFRAESTRUCTURA")) {
                    lblEspecifico.setText("Longitud / Magnitud (Km):");
                } else {
                    lblEspecifico.setText("Beneficiarios Directos (Personas):");
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrar();
            }
        });

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificar();
            }
        });

        btnBaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                darDeBaja();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTabla(txtBuscar.getText().trim());
            }
        });

        tblProyectos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    private void aplicarPermisosRol() {
        if (usuarioActual.getRol().equalsIgnoreCase("CONSULTA")) {
            btnRegistrar.setEnabled(false);
            btnModificar.setEnabled(false);
            btnBaja.setEnabled(false);
        }
    }

    private Proyecto construirObjetoDesdeFormulario(EstadoProyecto estadoActual, double avanceActual, double ejecutadoActual) throws ProyectoException {
        try {
            String codigo = txtCodigo.getText().trim().toUpperCase();
            String nombre = txtNombre.getText().trim();
            UnidadEjecutora ue = (UnidadEjecutora) cmbUnidad.getSelectedItem();
            FuenteFinanciamiento ff = (FuenteFinanciamiento) cmbFuente.getSelectedItem();
            double montoAsig = Double.parseDouble(txtPresupuesto.getText().trim());
            int gestion = Integer.parseInt(txtGestion.getText().trim());

            Presupuesto pres = new Presupuesto(montoAsig, ejecutadoActual, gestion);
            String tipo = (String) cmbTipo.getSelectedItem();

            if (tipo.equals("INFRAESTRUCTURA")) {
                double km = Double.parseDouble(txtEspecifico.getText().trim());
                if (km <= 0) throw new ProyectoException("La longitud en Km debe ser mayor a cero.");
                return new ProyectoInfraestructura(codigo, nombre, ue, ff, pres, estadoActual, avanceActual, true, km);
            } else {
                int benef = Integer.parseInt(txtEspecifico.getText().trim());
                if (benef <= 0) throw new ProyectoException("La cantidad de beneficiarios debe ser mayor a cero.");
                return new ProyectoSocial(codigo, nombre, ue, ff, pres, estadoActual, avanceActual, true, benef);
            }
        } catch (NumberFormatException ex) {
            throw new ProyectoException("Verifique que Presupuesto, Gestión y el dato específico sean números válidos.");
        } catch (IllegalArgumentException ex) {
            throw new ProyectoException(ex.getMessage());
        }
    }

    private void registrar() {
        try {
            Proyecto nuevo = construirObjetoDesdeFormulario(EstadoProyecto.FORMULACION, 0.0, 0.0);
            proyectoService.registrarProyecto(nuevo, usuarioActual.getUsername());
            JOptionPane.showMessageDialog(this, "Proyecto registrado correctamente.");
            limpiar();
            cargarTabla("");
        } catch (ProyectoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modificar() {
        try {
            String codigo = txtCodigo.getText().trim();
            Proyecto existente = proyectoService.buscarProyectoActivo(codigo);
            if (existente == null) {
                throw new ProyectoException("Seleccione un proyecto de la tabla para modificar.");
            }
            Proyecto actualizado = construirObjetoDesdeFormulario(
                    existente.getEstado(),
                    existente.getAvanceFisico(),
                    existente.getPresupuesto().getMontoEjecutado());
            proyectoService.modificarProyecto(actualizado, usuarioActual.getUsername());
            JOptionPane.showMessageDialog(this, "Proyecto actualizado correctamente.");
            limpiar();
            cargarTabla("");
        } catch (ProyectoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void darDeBaja() {
        try {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) {
                throw new ProyectoException("Seleccione primero un proyecto de la tabla.");
            }
            int conf = JOptionPane.showConfirmDialog(this,
                    "¿Confirma la baja lógica del proyecto " + codigo + "?\n(El registro se conservará en historial pero dejará de estar activo)",
                    "Confirmar Baja Lógica", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                proyectoService.darDeBajaLogica(codigo, usuarioActual.getUsername());
                JOptionPane.showMessageDialog(this, "Baja lógica aplicada exitosamente.");
                limpiar();
                cargarTabla("");
            }
        } catch (ProyectoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarFila() {
        int fila = tblProyectos.getSelectedRow();
        if (fila >= 0) {
            String codigo = modeloTabla.getValueAt(fila, 0).toString();
            Proyecto p = proyectoService.buscarProyectoActivo(codigo);
            if (p != null) {
                txtCodigo.setText(p.getCodigo());
                txtCodigo.setEditable(false);
                txtNombre.setText(p.getNombre());
                cmbTipo.setSelectedItem(p.getTipoProyecto());
                txtPresupuesto.setText(String.valueOf(p.getPresupuesto().getMontoAsignado()));
                txtGestion.setText(String.valueOf(p.getPresupuesto().getGestion()));

                if (p instanceof ProyectoInfraestructura) {
                    txtEspecifico.setText(String.valueOf(((ProyectoInfraestructura) p).getLongitudKm()));
                } else if (p instanceof ProyectoSocial) {
                    txtEspecifico.setText(String.valueOf(((ProyectoSocial) p).getBeneficiariosDirectos()));
                }
            }
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtPresupuesto.setText("");
        txtGestion.setText("2026");
        txtEspecifico.setText("");
        tblProyectos.clearSelection();
    }

    private void cargarTabla(String filtro) {
        modeloTabla.setRowCount(0);
        Proyecto[] activos = proyectoService.obtenerProyectosActivos();
        for (int i = 0; i < activos.length; i++) {
            Proyecto p = activos[i];
            if (filtro.isEmpty()
                    || p.getCodigo().toLowerCase().contains(filtro.toLowerCase())
                    || p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                modeloTabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getTipoProyecto(),
                    p.getUnidadEjecutora().getCodigo(),
                    p.getFuenteFinanciamiento().getSigla(),
                    String.format("%.2f", p.getPresupuesto().getMontoAsignado()),
                    p.getEstado().getDescripcion(),
                    p.getDetalleEspecifico()
                });
            }
        }
    }
}