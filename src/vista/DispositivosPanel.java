package vista;

import controlador.Ctrl_Dispositivo;
import controlador.Sesion;
import modelo.Dispositivo;
import util.ColorUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DispositivosPanel extends JPanel {

    private final Ctrl_Dispositivo ctrl = new Ctrl_Dispositivo();
    private List<Dispositivo> dispositivosCache;

    public DispositivosPanel() {
        initComponents();
        configurarPanel();
        cargarDispositivos();
    }

    private void configurarPanel() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();

        // Panel principal
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setLayout(new BorderLayout(0, 15));

        // ===== PANEL SUPERIOR (Título y filtros) =====
        jPanel1.setBackground(bgColor);
        jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel1.setLayout(new BorderLayout());

        // Título
        headerText.setForeground(fgColor);
        headerText.setFont(new Font("Arial", Font.BOLD, 24));
        headerText.setText("📱 Dispositivos");
        jPanel1.add(headerText, BorderLayout.NORTH);

        // Panel de filtros
        jPanel2.setBackground(bgColor);
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Campo de búsqueda
        txtBuscar.setBackground(ColorUtils.getTextFieldBackground());
        txtBuscar.setForeground(ColorUtils.getTextFieldForeground());
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtBuscar.setPreferredSize(new Dimension(250, 35));
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 13));

        // Combo de filtro
        cmbFiltro.setBackground(ColorUtils.getComboBoxBackground());
        cmbFiltro.setForeground(ColorUtils.getComboBoxForeground());
        cmbFiltro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cmbFiltro.setPreferredSize(new Dimension(150, 35));
        cmbFiltro.setFont(new Font("Arial", Font.PLAIN, 13));

        // Icono de búsqueda
        btnBuscarText.setIcon(new ImageIcon(getClass().getResource("/img/search icon (1).png")));

        // Agregar componentes al panel de filtros
        jPanel2.add(btnBuscarText);
        jPanel2.add(txtBuscar);
        jPanel2.add(cmbFiltro);

        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        add(jPanel1, BorderLayout.NORTH);

        // ===== SCROLL Y CONTENEDOR =====
        jScrollPane1.setBorder(BorderFactory.createLineBorder(borderColor));
        jScrollPane1.getViewport().setBackground(bgColor);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        panelContenedor.setBackground(bgColor);
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jScrollPane1.setViewportView(panelContenedor);
        add(jScrollPane1, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    public void recargarDispositivos() {
        cargarDispositivos();
    }

    private void cargarDispositivos() {
        panelContenedor.removeAll();
        dispositivosCache = ctrl.listar(Sesion.getIdUsuario());

        if (dispositivosCache.isEmpty()) {
            JLabel lblEmpty = new JLabel("📭 No hay dispositivos registrados");
            lblEmpty.setFont(new Font("Arial", Font.PLAIN, 16));
            lblEmpty.setForeground(ColorUtils.getForeground());
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedor.add(lblEmpty);
        } else {
            for (Dispositivo d : dispositivosCache) {
                // ✅ Si es cámara, usar CardCamara
                if (d.getTipo().equalsIgnoreCase("CAMARA")) {
                    panelContenedor.add(new CardCamara(d));
                } else {
                    panelContenedor.add(new CardDispositivo(d));
                }
                panelContenedor.add(Box.createVerticalStrut(10));
            }
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void filtrarPorTexto(String texto) {
        String textoLower = texto.trim().toLowerCase();
        panelContenedor.removeAll();

        boolean encontrado = false;
        for (Dispositivo d : dispositivosCache) {
            if (d.getNombre().toLowerCase().contains(textoLower)) {
                if (d.getTipo().equalsIgnoreCase("CAMARA")) {
                    panelContenedor.add(new CardCamara(d));
                } else {
                    panelContenedor.add(new CardDispositivo(d));
                }
                panelContenedor.add(Box.createVerticalStrut(10));
                encontrado = true;
            }
        }

        if (!encontrado && !textoLower.isEmpty()) {
            JLabel lblNoResult = new JLabel("🔍 No se encontraron dispositivos con: " + texto);
            lblNoResult.setFont(new Font("Arial", Font.PLAIN, 14));
            lblNoResult.setForeground(ColorUtils.getForeground());
            lblNoResult.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoResult.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedor.add(lblNoResult);
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void filtrarPorTipo(String tipo) {
        panelContenedor.removeAll();

        if (tipo.equals("Todos")) {
            cargarDispositivos();
            return;
        }

        String tipoBD = mapearTipo(tipo);
        List<Dispositivo> filtrados = ctrl.buscarPorTipo(tipoBD, Sesion.getIdUsuario());

        if (filtrados.isEmpty()) {
            JLabel lblNoResult = new JLabel("📭 No hay dispositivos de tipo: " + tipo);
            lblNoResult.setFont(new Font("Arial", Font.PLAIN, 14));
            lblNoResult.setForeground(ColorUtils.getForeground());
            lblNoResult.setHorizontalAlignment(SwingConstants.CENTER);
            lblNoResult.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedor.add(lblNoResult);
        } else {
            for (Dispositivo d : filtrados) {
                if (d.getTipo().equalsIgnoreCase("CAMARA")) {
                    panelContenedor.add(new CardCamara(d));
                } else {
                    panelContenedor.add(new CardDispositivo(d));
                }
                panelContenedor.add(Box.createVerticalStrut(10));
            }
        }

        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private String mapearTipo(String tipo) {
        switch (tipo) {
            case "Luces":
                return "LUZ";
            case "Sensores":
                return "TEMPERATURA";
            case "Cámaras":
                return "CAMARA";
            case "Alarmas":
                return "ALARMA";
            default:
                return tipo.toUpperCase();
        }
    }

    public void actualizarTema() {
        configurarPanel();
        cargarDispositivos();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedor = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        headerText = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        cmbFiltro = new javax.swing.JComboBox<>();
        btnBuscarText = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setLayout(new java.awt.BorderLayout(0, 20));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setViewportView(panelContenedor);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.BorderLayout());

        headerText.setBackground(new java.awt.Color(255, 255, 255));
        headerText.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        headerText.setText("Dispositivos");
        jPanel1.add(headerText, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Buscar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N
        txtBuscar.addActionListener(this::txtBuscarActionPerformed);
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        cmbFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Luces", "Sensores", "Cámaras", "Alarmas" }));
        cmbFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tipo:"));
        cmbFiltro.addActionListener(this::cmbFiltroActionPerformed);

        btnBuscarText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnBuscarText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search icon (1).png"))); // NOI18N
        btnBuscarText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarTextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarTextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarTextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBuscarText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 515, Short.MAX_VALUE)
                .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscarText)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarTextMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarTextMouseClicked

    private void btnBuscarTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarTextMouseEntered

    }//GEN-LAST:event_btnBuscarTextMouseEntered

    private void btnBuscarTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarTextMouseExited

    }//GEN-LAST:event_btnBuscarTextMouseExited

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        filtrarPorTexto(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void cmbFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltroActionPerformed
        String tipo = cmbFiltro.getSelectedItem().toString();
        filtrarPorTipo(tipo);
    }//GEN-LAST:event_cmbFiltroActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnBuscarText;
    private javax.swing.JComboBox<String> cmbFiltro;
    private javax.swing.JLabel headerText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
