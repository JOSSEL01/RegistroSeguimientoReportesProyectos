package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import modelo.Usuario;
import negocio.ProyectoException;
import negocio.ProyectoService;
import negocio.UsuarioService;

public class FrmLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnSalir;
    private UsuarioService usuarioService;

    public FrmLogin() {
        usuarioService = new UsuarioService();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Acceso Institucional - Gobernación de La Paz");
        setSize(470, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Encabezado Institucional con franja dorada/roja sutil
        JPanel pnlSuperior = new JPanel(new BorderLayout(0, 4));
        pnlSuperior.setBackground(EstiloUI.AZUL_OSCURO);
        pnlSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(212, 175, 55)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));

        JLabel lblTitulo = new JLabel("GOBERNACIÓN DEL DEPARTAMENTO DE LA PAZ", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblSubtitulo = new JLabel("Registro, Seguimiento y Reportes de Proyectos", SwingConstants.CENTER);
        lblSubtitulo.setForeground(new Color(191, 219, 254));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pnlSuperior.add(lblTitulo, BorderLayout.NORTH);
        pnlSuperior.add(lblSubtitulo, BorderLayout.SOUTH);
        add(pnlSuperior, BorderLayout.NORTH);

        // Panel Central
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setBackground(Color.WHITE);
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(20, 35, 15, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCentro.add(lblUser, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtUsuario = new JTextField(15);
        txtUsuario.setText("admin");
        EstiloUI.estilizarCampo(txtUsuario);
        pnlCentro.add(txtUsuario, gbc);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlCentro.add(lblPass, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(15);
        txtPassword.setText("1234");
        EstiloUI.estilizarCampo(txtPassword);
        pnlCentro.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel lblAyuda = new JLabel("<html><center>Usuarios disponibles (clave <b>1234</b>): admin | operador | consulta</center></html>", SwingConstants.CENTER);
        lblAyuda.setForeground(new Color(100, 116, 139));
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        pnlCentro.add(lblAyuda, gbc);

        add(pnlCentro, BorderLayout.CENTER);

        // Panel Inferior
        JPanel pnlBotones = new JPanel();
        pnlBotones.setBackground(EstiloUI.FONDO_CLARO);
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        btnIngresar = new JButton("Ingresar al Sistema");
        EstiloUI.estilizarBoton(btnIngresar, EstiloUI.AZUL_PRIMARIO, EstiloUI.AZUL_HOVER);

        btnSalir = new JButton("Salir");
        EstiloUI.estilizarBotonSecundario(btnSalir);

        pnlBotones.add(btnIngresar);
        pnlBotones.add(btnSalir);
        add(pnlBotones, BorderLayout.SOUTH);

        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void iniciarSesion() {
        try {
            String user = txtUsuario.getText();
            String pass = new String(txtPassword.getPassword());
            Usuario autenticado = usuarioService.autenticar(user, pass);
            ProyectoService proyectoService = new ProyectoService();

            FrmPrincipal principal = new FrmPrincipal(autenticado, proyectoService);
            principal.setVisible(true);
            this.dispose();
        } catch (ProyectoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Acceso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmLogin().setVisible(true);
            }
        });
    }
}