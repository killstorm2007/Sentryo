package vista;

import controlador.Ctrl_Dispositivo;
import controlador.Sesion;
import modelo.Dispositivo;
import util.ColorUtils;
import javax.swing.*;
import java.awt.*;

public class AgregarDispositivoPanel extends JPanel {
    private final Ctrl_Dispositivo ctrl = new Ctrl_Dispositivo();
    
    private JTextField txtUrlCamara;
    private JLabel lblUrlCamara;

    public AgregarDispositivoPanel() {
        System.out.println("Usuario logueado: " + Sesion.getIdUsuario());
        initComponents();
        configurarColores();
        agregarCampoUrlCamara();
    }
    
    public void actualizarTema() {
        configurarColores();
    }

    private void configurarColores() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        
        // Fondo del panel
        panelPrincipal.setBackground(bgColor);
        this.setBackground(bgColor);
        
        // Labels
        jLabel2.setForeground(fgColor);
        jLabel6.setForeground(fgColor);
        jLabel7.setForeground(fgColor);
        jLabel8.setForeground(fgColor);
        jLabel9.setForeground(fgColor);
        jLabel4.setForeground(fgColor);
        title.setForeground(fgColor);
        
        // TextFields
        txtNombre.setBackground(bgColor);
        txtNombre.setForeground(fgColor);
        txtUbicacion.setBackground(bgColor);
        txtUbicacion.setForeground(fgColor);
        txtDescripcion.setBackground(bgColor);
        txtDescripcion.setForeground(fgColor);
        txtIP.setBackground(bgColor);
        txtIP.setForeground(fgColor);
        
        if (lblUrlCamara != null) {
            lblUrlCamara.setForeground(fgColor);
        }
        
        // ComboBoxes
        cmbTipo.setBackground(bgColor);
        cmbTipo.setForeground(fgColor);
        cmbPin.setBackground(bgColor);
        cmbPin.setForeground(fgColor);
        
        if (txtUrlCamara != null) {
            txtUrlCamara.setBackground(bgColor);
            txtUrlCamara.setForeground(fgColor);
            txtUrlCamara.setBorder(BorderFactory.createLineBorder(borderColor));
        }
        
    }
    private void agregarCampoUrlCamara() {
        // Obtener el GridBagLayout del panelPrincipal
        GridBagLayout layout = (GridBagLayout) panelPrincipal.getLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        // ✅ Label para URL de cámara
        lblUrlCamara = new JLabel("URL Cámara (solo para CAMARA):");
        lblUrlCamara.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 11; // Después de IP
        gbc.gridwidth = 2;
        panelPrincipal.add(lblUrlCamara, gbc);
        
        // ✅ TextField para URL de cámara
        txtUrlCamara = new JTextField();
        txtUrlCamara.setPreferredSize(new Dimension(300, 40));
        txtUrlCamara.setToolTipText("Ej: http://192.168.1.100:8080/video");
        txtUrlCamara.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panelPrincipal.add(txtUrlCamara, gbc);
        
        // ✅ Ajustar el botón "Agregar" a la siguiente fila
        gbc.gridy = 13;
        panelPrincipal.add(btnAgregar, gbc);
        
        // ✅ Listener para mostrar/ocultar el campo según el tipo seleccionado
        cmbTipo.addActionListener(e -> {
            String tipo = cmbTipo.getSelectedItem().toString();
            boolean esCamara = tipo.equals("CAMARA");
            lblUrlCamara.setVisible(esCamara);
            txtUrlCamara.setVisible(esCamara);
            if (!esCamara) {
                txtUrlCamara.setText("");
            }
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        });
        
        // Por defecto, ocultar el campo si no es cámara
        lblUrlCamara.setVisible(false);
        txtUrlCamara.setVisible(false);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtUbicacion.setText("");
        txtDescripcion.setText("");
        txtIP.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbPin.setSelectedIndex(0);
    }

    private void actualizarPanelDispositivos() {
        mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.actualizarDispositivos();
        }
    }

    private boolean validarCampos() {
        String ip = txtIP.getText().trim();
        String tipo = cmbTipo.getSelectedItem().toString();
        
        // ✅ Validar IP
        if (!ip.matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) {
            JOptionPane.showMessageDialog(this, "Formato de IP inválido.\nEj: 192.168.1.100");
            return false;
        }
        
        // ✅ Validar URL de cámara si es de tipo CAMARA
        if (tipo.equals("CAMARA")) {
            String url = txtUrlCamara.getText().trim();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Para cámaras, debes ingresar la URL completa.\n" +
                    "Ej: http://192.168.1.100:8080/video",
                    "URL requerida",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                JOptionPane.showMessageDialog(this, 
                    "La URL debe comenzar con http:// o https://\n" +
                    "Ej: http://192.168.1.100:8080/video",
                    "URL inválida",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }

    private void agregarDispositivo() {
        if (!validarCampos()) return;

        Dispositivo d = new Dispositivo();
        d.setNombre(txtNombre.getText().trim());
        d.setTipo(cmbTipo.getSelectedItem().toString());
        d.setUbicacion(txtUbicacion.getText().trim());
        d.setDescripcion(txtDescripcion.getText().trim());
        d.setPin(Integer.parseInt(cmbPin.getSelectedItem().toString()));
        d.setIdUsuario(Sesion.getIdUsuario());
        
        // ✅ Guardar IP y URL
        d.setIp(txtIP.getText().trim());
        
        // ✅ Si es cámara, guardar la URL completa
        String tipo = cmbTipo.getSelectedItem().toString();
        if (tipo.equals("CAMARA")) {
            String urlCompleta = txtUrlCamara.getText().trim();
            // Guardar la URL completa en el campo IP (o podrías agregar un campo nuevo)
            d.setIp(urlCompleta); // Guardamos la URL completa en IP
        } else {
            d.setIp(txtIP.getText().trim());
        }

        if (ctrl.agregarDispositivo(d)) {
            JOptionPane.showMessageDialog(this, "Dispositivo agregado correctamente.");
            limpiarCampos();
            actualizarPanelDispositivos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar el dispositivo.");
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelPrincipal = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        cmbPin = new javax.swing.JComboBox<>();
        cmbTipo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        txtIP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        title = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 80, 30, 80));
        panelPrincipal.setMaximumSize(new java.awt.Dimension(700, 2147483647));
        panelPrincipal.setPreferredSize(new java.awt.Dimension(700, 650));
        panelPrincipal.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Nombre:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel2, gridBagConstraints);

        txtNombre.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        txtNombre.setPreferredSize(new java.awt.Dimension(300, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(txtNombre, gridBagConstraints);

        cmbPin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" }));
        cmbPin.setPreferredSize(new java.awt.Dimension(300, 40));
        cmbPin.addActionListener(this::cmbPinActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(cmbPin, gridBagConstraints);

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ALARMA", "LUZ", "CAMARA", "TEMPERATURA", "HUMEDAD", "HUMO" }));
        cmbTipo.setPreferredSize(new java.awt.Dimension(300, 40));
        cmbTipo.addActionListener(this::cmbTipoActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(cmbTipo, gridBagConstraints);

        jLabel6.setText("PIN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel6, gridBagConstraints);

        jLabel9.setText("Tipo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel9, gridBagConstraints);

        jLabel7.setText("Ubicacion:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel7, gridBagConstraints);

        txtUbicacion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        txtUbicacion.setMinimumSize(new java.awt.Dimension(300, 40));
        txtUbicacion.addActionListener(this::txtUbicacionActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(txtUbicacion, gridBagConstraints);

        jLabel8.setText("Descripcion:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel8, gridBagConstraints);

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        txtDescripcion.setPreferredSize(new java.awt.Dimension(300, 40));
        jScrollPane1.setViewportView(txtDescripcion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jScrollPane1, gridBagConstraints);

        txtIP.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(220, 220, 220), 1, true));
        txtIP.setPreferredSize(new java.awt.Dimension(300, 40));
        txtIP.addActionListener(this::txtIPActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(txtIP, gridBagConstraints);

        jLabel4.setText("IP:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(jLabel4, gridBagConstraints);

        title.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Agregar Dispositivo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(title, gridBagConstraints);

        btnAgregar.setBackground(new java.awt.Color(105, 115, 218));
        btnAgregar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("+ Agregar");
        btnAgregar.setPreferredSize(new java.awt.Dimension(220, 45));
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        panelPrincipal.add(btnAgregar, gridBagConstraints);

        jScrollPane2.setViewportView(panelPrincipal);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarDispositivo();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void txtIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIPActionPerformed

    private void cmbPinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPinActionPerformed

    private void txtUbicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUbicacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUbicacionActionPerformed

    private void cmbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbPin;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JLabel title;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
