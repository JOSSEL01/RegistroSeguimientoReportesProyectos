package presentacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class EstiloUI {
    // Paleta Institucional Gobernación de La Paz
    public static final Color AZUL_OSCURO = new Color(15, 44, 76);
    public static final Color AZUL_PRIMARIO = new Color(25, 118, 210);
    public static final Color AZUL_HOVER = new Color(21, 101, 192);
    public static final Color VERDE_EXITO = new Color(0, 121, 107);
    public static final Color VERDE_HOVER = new Color(0, 96, 84);
    public static final Color ROJO_PELIGRO = new Color(198, 40, 40);
    public static final Color ROJO_HOVER = new Color(165, 28, 28);
    public static final Color GRIS_PIZARRA = new Color(69, 90, 100);
    public static final Color GRIS_HOVER = new Color(55, 71, 79);
    public static final Color FONDO_CLARO = new Color(240, 244, 248);
    public static final Color BORDE_SUAVE = new Color(206, 214, 224);

    public static void estilizarBoton(final JButton btn, final Color colorBase, final Color colorHover) {
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(colorHover);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(colorBase);
                }
            }
        });
    }

    public static void estilizarBotonSecundario(final JButton btn) {
        final Color base = new Color(226, 232, 240);
        final Color hover = new Color(203, 213, 225);
        btn.setBackground(base);
        btn.setForeground(new Color(30, 41, 59));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(base);
            }
        });
    }

    public static void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(28);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(230, 235, 240));
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setSelectionForeground(new Color(15, 23, 42));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(AZUL_OSCURO);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getWidth(), 32));

        // Renderizador para cabecera oscura legible en Windows y filas cebra
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(AZUL_OSCURO);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerRenderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                return c;
            }
        });
    }
}