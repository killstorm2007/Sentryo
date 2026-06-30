package vista;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class QuickAccessCard extends JPanel {

    private JLabel lblIcono;
    private JLabel lblTitulo;
    private JLabel lblDescripcion;
    private JLabel lblFlecha;

    private Runnable accion;

    public QuickAccessCard(
            Icon icono,
            String titulo,
            String descripcion) {

        initComponents();

        lblIcono.setIcon(icono);
        lblTitulo.setText(titulo);
        lblDescripcion.setText(descripcion);

        eventosMouse();
    }

    private void initComponents() {

        setLayout(new BorderLayout(15, 0));

        setBackground(Color.WHITE);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225,225,225),1,true),
                new EmptyBorder(15,15,15,15)));

        setPreferredSize(new Dimension(450,70));

        //---------------------------------------------------
        // IZQUIERDA
        //---------------------------------------------------

        JPanel panelIzq = new JPanel(new BorderLayout(10,0));

        panelIzq.setOpaque(false);

        lblIcono = new JLabel();

        panelIzq.add(lblIcono, BorderLayout.WEST);

        JPanel panelTexto = new JPanel();

        panelTexto.setOpaque(false);

        panelTexto.setLayout(new BoxLayout(
                panelTexto,
                BoxLayout.Y_AXIS));

        lblTitulo = new JLabel();

        lblTitulo.setFont(
                new Font("Arial", Font.BOLD,16));

        lblDescripcion = new JLabel();

        lblDescripcion.setFont(
                new Font("Arial",Font.PLAIN,13));

        lblDescripcion.setForeground(
                new Color(120,120,120));

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(5));
        panelTexto.add(lblDescripcion);

        panelIzq.add(panelTexto, BorderLayout.CENTER);

        //---------------------------------------------------
        // DERECHA
        //---------------------------------------------------

        lblFlecha = new JLabel("➜");

        lblFlecha.setFont(
                new Font("Arial",Font.BOLD,20));

        lblFlecha.setForeground(
                new Color(160,160,160));

        //---------------------------------------------------

        add(panelIzq,BorderLayout.CENTER);
        add(lblFlecha,BorderLayout.EAST);

    }

    private void eventosMouse(){

        MouseAdapter mouse = new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent e){

                setBackground(new Color(245,248,255));

                setCursor(
                        Cursor.getPredefinedCursor(
                                Cursor.HAND_CURSOR));

            }

            @Override
            public void mouseExited(MouseEvent e){

                setBackground(Color.WHITE);

            }

            @Override
            public void mouseClicked(MouseEvent e){

                if(accion != null){

                    accion.run();

                }

            }

        };

        addMouseListener(mouse);

    }

    public void setAccion(Runnable accion){

        this.accion = accion;

    }

}