package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardCard extends JPanel {

    private JPanel panelIcono;
    private JLabel lblIcono;
    private JLabel lblTitulo;
    private JLabel lblValor;

    public DashboardCard(
            Icon icono,
            String titulo,
            String valor,
            Color colorIcono) {

        initComponents();

        lblIcono.setIcon(icono);
        lblTitulo.setText(titulo);
        lblValor.setText(valor);

        panelIcono.setBackground(colorIcono);
    }

    private void initComponents() {

        setLayout(new BorderLayout(15, 0));

        setOpaque(true);
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225,225,225),1,true),
                new EmptyBorder(18,18,18,18)));

        setPreferredSize(new Dimension(230,120));

        //-------------------------------------------------
        // PANEL ICONO
        //-------------------------------------------------

        panelIcono = new JPanel(new GridBagLayout());

        panelIcono.setPreferredSize(new Dimension(60,60));

        panelIcono.putClientProperty(
                "JComponent.roundRect",
                true);

        lblIcono = new JLabel();

        panelIcono.add(lblIcono);

        //-------------------------------------------------
        // PANEL TEXTO
        //-------------------------------------------------

        JPanel panelTexto = new JPanel();

        panelTexto.setOpaque(false);

        panelTexto.setLayout(new BoxLayout(
                panelTexto,
                BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Titulo");

        lblTitulo.setFont(
                new Font("Arial",
                        Font.BOLD,
                        16));

        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblValor = new JLabel("Valor");

        lblValor.setFont(
                new Font("Arial",
                        Font.PLAIN,
                        14));

        lblValor.setForeground(
                new Color(120,120,120));

        lblValor.setBorder(
                new EmptyBorder(8,0,0,0));

        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelTexto.add(lblTitulo);
        panelTexto.add(lblValor);

        //-------------------------------------------------

        add(panelIcono, BorderLayout.WEST);
        add(panelTexto, BorderLayout.CENTER);

    }

    //-------------------------------------------------
    // Métodos para actualizar la tarjeta
    //-------------------------------------------------

    public void setTitulo(String titulo){

        lblTitulo.setText(titulo);

    }

    public void setValor(String valor){

        lblValor.setText(valor);

    }

    public void setIcono(Icon icono){

        lblIcono.setIcon(icono);

    }

    public void setColorIcono(Color color){

        panelIcono.setBackground(color);

    }

}