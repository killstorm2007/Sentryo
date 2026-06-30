package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ActivityCard extends JPanel {

    private JLabel lblIcono;
    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JLabel lblHora;

    public ActivityCard(
            Icon icono,
            String titulo,
            String descripcion,
            String hora) {

        initComponents();

        lblIcono.setIcon(icono);
        lblTitulo.setText(titulo);
        lblDescripcion.setText(descripcion);
        lblHora.setText(hora);

        agregarHover();
    }

    private void initComponents() {

        setLayout(new BorderLayout(15, 0));

        setBackground(Color.WHITE);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 230, 230), 1, true),
                new EmptyBorder(12, 12, 12, 12)));

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        setPreferredSize(new Dimension(450, 70));

        //---------------------------------------------------
        // ICONO
        //---------------------------------------------------

        JPanel panelIcono = new JPanel(new GridBagLayout());

        panelIcono.setPreferredSize(new Dimension(45,45));
        panelIcono.setBackground(new Color(240,244,248));

        lblIcono = new JLabel();

        panelIcono.add(lblIcono);

        //---------------------------------------------------
        // TEXTO
        //---------------------------------------------------

        JPanel panelTexto = new JPanel();

        panelTexto.setOpaque(false);

        panelTexto.setLayout(new BoxLayout(
                panelTexto,
                BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Título");

        lblTitulo.setFont(
                new Font("Arial",
                        Font.BOLD,
                        15));

        lblDescripcion = new JLabel("Descripción");

        lblDescripcion.setFont(
                new Font("Arial",
                        Font.PLAIN,
                        13));

        lblDescripcion.setForeground(
                new Color(120,120,120));

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblDescripcion);

        //---------------------------------------------------
        // HORA
        //---------------------------------------------------

        lblHora = new JLabel("Ahora");

        lblHora.setFont(
                new Font("Arial",
                        Font.PLAIN,
                        12));

        lblHora.setForeground(
                new Color(150,150,150));

        //---------------------------------------------------

        add(panelIcono, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);
        add(lblHora, BorderLayout.EAST);

    }

    private void agregarHover() {

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                setBackground(new Color(248,250,252));

                setCursor(
                        Cursor.getPredefinedCursor(
                                Cursor.HAND_CURSOR));

            }

            @Override
            public void mouseExited(MouseEvent e) {

                setBackground(Color.WHITE);

            }

        };

        addMouseListener(mouse);

    }

    //=========================
    // SETTERS
    //=========================

    public void setTitulo(String titulo) {

        lblTitulo.setText(titulo);

    }

    public void setDescripcion(String descripcion) {

        lblDescripcion.setText(descripcion);

    }

    public void setHora(String hora) {

        lblHora.setText(hora);

    }

    public void setIcono(Icon icono) {

        lblIcono.setIcon(icono);

    }

}