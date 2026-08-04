
package vista;

import controlador.Ctrl_Dispositivo;
import controlador.Sesion;
import modelo.Dispositivo;
import util.ColorUtils;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class CamarasPanel extends javax.swing.JPanel {
    
    private final Ctrl_Dispositivo ctrl = new Ctrl_Dispositivo();
    private List<Dispositivo> camarasCache;
    private JPanel panelCamaras;
    private JScrollPane scrollPane;
    private JTextField txtBuscar;
    private JButton btnRefrescar;
    private JLabel lblTotalCamaras;
    private JButton btnVerTodas;

    public CamarasPanel() {
        initComponents();  // Esto ahora solo inicializa lo mínimo
        configurarPanel();
        cargarCamaras();
    }
private void configurarPanel() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        
        // ✅ LIMPIAR Y CONFIGURAR EL PANEL PRINCIPAL
        this.removeAll();
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setLayout(new BorderLayout(0, 15));
        
        // ===== PANEL SUPERIOR =====
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(bgColor);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Título
        JLabel lblTitulo = new JLabel("📹 Cámaras de Vigilancia");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(fgColor);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        // Panel derecho (total + botón ver todas)
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setBackground(bgColor);
        
        lblTotalCamaras = new JLabel("Total: 0 cámaras");
        lblTotalCamaras.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTotalCamaras.setForeground(fgColor);
        panelDerecho.add(lblTotalCamaras);
        
        btnVerTodas = new JButton("📹 Ver todas");
        btnVerTodas.setBackground(new Color(220, 38, 38));
        btnVerTodas.setForeground(Color.WHITE);
        btnVerTodas.setFocusPainted(false);
        btnVerTodas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerTodas.addActionListener(e -> verTodasCamaras());
        panelDerecho.add(btnVerTodas);
        
        panelSuperior.add(panelDerecho, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        // ===== PANEL DE FILTROS =====
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBackground(bgColor);
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor),
            "🔍 Buscar cámara",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            fgColor
        ));
        
        txtBuscar = new JTextField(25);
        txtBuscar.setToolTipText("Buscar por nombre o ubicación");
        txtBuscar.setBackground(ColorUtils.getTextFieldBackground());
        txtBuscar.setForeground(ColorUtils.getTextFieldForeground());
        txtBuscar.setBorder(BorderFactory.createLineBorder(borderColor));
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        panelFiltros.add(txtBuscar);
        
        btnRefrescar = new JButton("🔄 Refrescar");
        btnRefrescar.setBackground(new Color(105, 115, 218));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarCamaras());
        panelFiltros.add(btnRefrescar);
        
        add(panelFiltros, BorderLayout.CENTER);
        
        // ===== PANEL DE CÁMARAS =====
        panelCamaras = new JPanel();
        panelCamaras.setBackground(bgColor);
        panelCamaras.setLayout(new BoxLayout(panelCamaras, BoxLayout.Y_AXIS));
        panelCamaras.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(panelCamaras);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.SOUTH);
        
        // ✅ REVALIDAR
        this.revalidate();
        this.repaint();
    }

    /**
     * ✅ CARGA LAS CÁMARAS DESDE LA BD
     */
    public void cargarCamaras() {
        if (panelCamaras == null) return;
        
        panelCamaras.removeAll();
        camarasCache = ctrl.buscarPorTipo("CAMARA", Sesion.getIdUsuario());
        
        if (camarasCache.isEmpty()) {
            JLabel lblEmpty = new JLabel("📭 No hay cámaras registradas");
            lblEmpty.setFont(new Font("Arial", Font.PLAIN, 16));
            lblEmpty.setForeground(ColorUtils.getForeground());
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCamaras.add(lblEmpty);
        } else {
            for (Dispositivo d : camarasCache) {
                CardCamara card = new CardCamara(d);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                panelCamaras.add(card);
                panelCamaras.add(Box.createVerticalStrut(10));
            }
        }
        
        lblTotalCamaras.setText("Total: " + camarasCache.size() + " cámaras");
        panelCamaras.revalidate();
        panelCamaras.repaint();
    }

    /**
     * ✅ FILTRAR CÁMARAS POR TEXTO
     */
    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        panelCamaras.removeAll();
        
        if (camarasCache == null || camarasCache.isEmpty()) {
            cargarCamaras();
            return;
        }
        
        boolean encontrado = false;
        for (Dispositivo d : camarasCache) {
            String nombre = d.getNombre().toLowerCase();
            String ubicacion = d.getUbicacion() != null ? d.getUbicacion().toLowerCase() : "";
            if (nombre.contains(texto) || ubicacion.contains(texto)) {
                CardCamara card = new CardCamara(d);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                panelCamaras.add(card);
                panelCamaras.add(Box.createVerticalStrut(10));
                encontrado = true;
            }
        }
        
        if (!encontrado && !texto.isEmpty()) {
            JLabel lblNoResult = new JLabel("🔍 No se encontraron cámaras con: " + texto);
            lblNoResult.setFont(new Font("Arial", Font.PLAIN, 14));
            lblNoResult.setForeground(ColorUtils.getForeground());
            lblNoResult.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoResult.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCamaras.add(lblNoResult);
        }
        
        panelCamaras.revalidate();
        panelCamaras.repaint();
    }

    /**
     * ✅ VER TODAS LAS CÁMARAS EN VIVO (GRID)
     */
    private void verTodasCamaras() {
    if (camarasCache == null || camarasCache.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "No hay cámaras disponibles para ver.",
            "Sin cámaras",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    JFrame gridFrame = new JFrame("📹 Vista en vivo - Todas las cámaras");
    gridFrame.setSize(900, 700);
    gridFrame.setLocationRelativeTo(this);
    gridFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gridFrame.setLayout(new BorderLayout());
    
    JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    gridPanel.setBackground(Color.BLACK);
    gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    int maxCamaras = Math.min(camarasCache.size(), 4);
    for (int i = 0; i < maxCamaras; i++) {
        VideoPanel videoPanel = new VideoPanel();
        videoPanel.setPreferredSize(new Dimension(400, 300));
        gridPanel.add(videoPanel);
        
        final int index = i;
        new Thread(() -> {
            try {
                Thread.sleep(500 * index);
                
                // ✅ OBTENER IP DE LA BASE DE DATOS
                Dispositivo camara = camarasCache.get(index);
                String ip = camara.getIp();
                String url = "";
                
                if (ip != null && !ip.trim().isEmpty()) {
                    if (!ip.startsWith("http://") && !ip.startsWith("https://")) {
                        url = "http://" + ip + "/video";
                    } else {
                        url = ip;
                    }
                    videoPanel.iniciarCamaraArduino(url);
                } else {
                    // Si no tiene IP, mostrar mensaje en el panel
                    videoPanel.iniciarCamaraPrueba();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    for (int i = maxCamaras; i < 4; i++) {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.BLACK);
        emptyPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        JLabel lblEmpty = new JLabel("📷 Sin cámara", SwingConstants.CENTER);
        lblEmpty.setForeground(Color.GRAY);
        lblEmpty.setFont(new Font("Arial", Font.BOLD, 18));
        emptyPanel.add(lblEmpty);
        gridPanel.add(emptyPanel);
    }
    
    gridFrame.add(gridPanel, BorderLayout.CENTER);
    gridFrame.setVisible(true);
}

    /**
     * ✅ ACTUALIZAR TEMA
     */
    public void actualizarTema() {
        configurarPanel();
        cargarCamaras();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
