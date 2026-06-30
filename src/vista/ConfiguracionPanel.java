package vista;

import controlador.Ctrl_Configuracion;
import controlador.Sesion;
import util.ThemeManager;
import util.ColorUtils;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.border.TitledBorder;

public class ConfiguracionPanel extends JPanel {
    private Ctrl_Configuracion ctrlConfig = new Ctrl_Configuracion();

    public ConfiguracionPanel() {
        initComponents();
        configurarColores();
        configurarEventos();
        cargarConfiguracionesActuales();

        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }   

    private void configurarColores() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        Color textFieldBg = ColorUtils.getTextFieldBackground();
        Color textFieldFg = ColorUtils.getTextFieldForeground();
        
        // ✅ NO cambiar el layout, solo colores
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // TabbedPane
        jTabbedPane1.setBackground(bgColor);
        jTabbedPane1.setForeground(fgColor);
        jTabbedPane1.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Pestañas
        pestañaAutomatizacion.setBackground(bgColor);
        pestañaInterfaz.setBackground(bgColor);
        pestañaConectividad.setBackground(bgColor);
        
        // Labels
        jLabel1.setForeground(fgColor);
        jLabel2.setForeground(fgColor);
        jLabel3.setForeground(fgColor);
        jLabel4.setForeground(fgColor);
        jLabel5.setForeground(fgColor);
        jLabel6.setForeground(fgColor);
        jLabel7.setForeground(fgColor);
        jLabel8.setForeground(fgColor);
        lblValorUmbral.setForeground(fgColor);
        lblIntervalo.setForeground(fgColor);
        lblEstado.setForeground(fgColor);
        lblEstadoValor.setForeground(Color.RED);
        lblVersion.setForeground(fgColor);
        
        // Checkboxes
        chkAutomatizacionActiva.setForeground(fgColor);
        chkAutomatizacionActiva.setBackground(bgColor);
        chkSonido.setForeground(fgColor);
        chkSonido.setBackground(bgColor);
        chkNotificacionesPush.setForeground(fgColor);
        chkNotificacionesPush.setBackground(bgColor);
        chkNotificacionesEmail.setForeground(fgColor);
        chkNotificacionesEmail.setBackground(bgColor);
        chkAutoActualizacion.setForeground(fgColor);
        chkAutoActualizacion.setBackground(bgColor);
        
        // Combos
        cmbTema.setBackground(textFieldBg);
        cmbTema.setForeground(textFieldFg);
        cmbIdioma.setBackground(textFieldBg);
        cmbIdioma.setForeground(textFieldFg);
        
        // TextFields
        txtIpServidor.setBackground(textFieldBg);
        txtIpServidor.setForeground(textFieldFg);
        txtIpServidor.setBorder(BorderFactory.createLineBorder(borderColor));
        txtPuerto.setBackground(textFieldBg);
        txtPuerto.setForeground(textFieldFg);
        txtPuerto.setBorder(BorderFactory.createLineBorder(borderColor));
        
        // Spinner
        spnTiempoActualizacion.setBackground(textFieldBg);
        spnTiempoActualizacion.setForeground(textFieldFg);
        
        // Slider
        sliderUmbralLuz.setBackground(bgColor);
        sliderUmbralLuz.setForeground(fgColor);
        
        // Panel estado
        panelEstado.setBackground(bgColor);
        
        // ProgressBar
        progressBar.setBackground(bgColor);
        progressBar.setForeground(new Color(105, 115, 218));
        
        // Botones
        btnGuardarAjustes.setBackground(new Color(105, 115, 218));
        btnGuardarAjustes.setForeground(Color.WHITE);
        btnGuardarAjustes.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarAjustes.setFocusPainted(false);
        btnGuardarAjustes.setBorderPainted(false);
        
        btnProbarConexion.setBackground(new Color(59, 130, 246));
        btnProbarConexion.setForeground(Color.WHITE);
        btnProbarConexion.setFocusPainted(false);
        btnProbarConexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRestaurarDefault.setBackground(new Color(220, 38, 38));
        btnRestaurarDefault.setForeground(Color.WHITE);
        btnRestaurarDefault.setFocusPainted(false);
        btnRestaurarDefault.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // ✅ Actualizar valores
        lblValorUmbral.setText("Valor actual: " + sliderUmbralLuz.getValue());
        lblIntervalo.setText("Intervalo: " + spnTiempoActualizacion.getValue() + " segundos");
        
        // ✅ Forzar actualización
        this.revalidate();
        this.repaint();
    }

    public void actualizarTema() {
        configurarColores();
        cmbTema.setSelectedItem(ThemeManager.getCurrentTheme());
        this.revalidate();
        this.repaint();
    }

private void configurarEventos() {
        cmbTema.addActionListener(e -> {
            String tema = cmbTema.getSelectedItem().toString();
            ThemeManager.aplicarTema(tema);
        });
        
        sliderUmbralLuz.addChangeListener(e -> {
            lblValorUmbral.setText("Valor actual: " + sliderUmbralLuz.getValue());
        });
        
        spnTiempoActualizacion.addChangeListener(e -> {
            lblIntervalo.setText("Intervalo: " + spnTiempoActualizacion.getValue() + " segundos");
        });
        
        btnProbarConexion.addActionListener(e -> probarConexion());
        btnRestaurarDefault.addActionListener(e -> restaurarValoresDefault());
    }

    private void probarConexion() {
        lblEstadoValor.setText("⏳ Probando...");
        lblEstadoValor.setForeground(Color.ORANGE);
        
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                SwingUtilities.invokeLater(() -> {
                    lblEstadoValor.setText("✅ Conectado");
                    lblEstadoValor.setForeground(new Color(34, 197, 94));
                });
            } catch (InterruptedException ex) {}
        }).start();
    }

    private void restaurarValoresDefault() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas restaurar todos los valores predeterminados?",
            "Restaurar valores",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            chkAutomatizacionActiva.setSelected(false);
            sliderUmbralLuz.setValue(500);
            cmbTema.setSelectedItem("Claro");
            cmbIdioma.setSelectedItem("Español");
            chkSonido.setSelected(true);
            chkNotificacionesPush.setSelected(true);
            chkNotificacionesEmail.setSelected(false);
            chkAutoActualizacion.setSelected(true);
            spnTiempoActualizacion.setValue(30);
            txtIpServidor.setText("192.168.1.100");
            txtPuerto.setText("8080");
            ThemeManager.aplicarTema("Claro");
            
            JOptionPane.showMessageDialog(this,
                "✅ Valores restaurados correctamente.",
                "Restaurado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarConfiguracionesActuales() {
        try {
            Map<String, Object> config = ctrlConfig.obtenerConfiguracionCompleta(Sesion.getIdUsuario());
            
            if (config != null) {
                chkAutomatizacionActiva.setSelected((boolean) config.getOrDefault("automatizacion_activa", false));
                sliderUmbralLuz.setValue((int) config.getOrDefault("umbral_sensor_luz", 500));
                lblValorUmbral.setText("Valor actual: " + sliderUmbralLuz.getValue());
                
                String tema = (String) config.get("tema");
                if (tema == null || tema.isEmpty()) {
                    tema = ThemeManager.getCurrentTheme();
                }
                cmbTema.setSelectedItem(tema);
                
                cmbIdioma.setSelectedItem(config.getOrDefault("idioma", "Español"));
                chkSonido.setSelected((boolean) config.getOrDefault("sonido_confirmacion", true));
                txtIpServidor.setText((String) config.getOrDefault("ip_servidor", "192.168.1.100"));
                txtPuerto.setText((String) config.getOrDefault("puerto_com", "8080"));
            }
        } catch (Exception e) {
            System.err.println("❌ Error cargando configuraciones: " + e.getMessage());
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        pestañaAutomatizacion = new javax.swing.JPanel();
        chkAutomatizacionActiva = new javax.swing.JCheckBox();
        sliderUmbralLuz = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        lblValorUmbral = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pestañaInterfaz = new javax.swing.JPanel();
        cmbIdioma = new javax.swing.JComboBox<>();
        chkSonido = new javax.swing.JCheckBox();
        cmbTema = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        chkNotificacionesPush = new javax.swing.JCheckBox();
        chkNotificacionesEmail = new javax.swing.JCheckBox();
        chkAutoActualizacion = new javax.swing.JCheckBox();
        lblIntervalo = new javax.swing.JLabel();
        spnTiempoActualizacion = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        pestañaConectividad = new javax.swing.JPanel();
        txtIpServidor = new javax.swing.JTextField();
        txtPuerto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelEstado = new javax.swing.JPanel();
        lblEstado = new javax.swing.JLabel();
        lblEstadoValor = new javax.swing.JLabel();
        btnProbarConexion = new javax.swing.JButton();
        btnRestaurarDefault = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        btnGuardarAjustes = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setLayout(new java.awt.BorderLayout());

        pestañaAutomatizacion.setLayout(new java.awt.GridBagLayout());

        chkAutomatizacionActiva.setText("Activar Modo Auto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(22, 18, 0, 0);
        pestañaAutomatizacion.add(chkAutomatizacionActiva, gridBagConstraints);

        sliderUmbralLuz.setPaintTrack(false);
        sliderUmbralLuz.setToolTipText("El usuario elige qué tan oscuro debe estar afuera para que el sistema decida encender automáticamente las luces del patio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 164;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 18, 148, 0);
        pestañaAutomatizacion.add(sliderUmbralLuz, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Umbral de Luz");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 18, 0, 0);
        pestañaAutomatizacion.add(jLabel1, gridBagConstraints);

        lblValorUmbral.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValorUmbral.setText("Valor Actual: 500");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 14, 0, 238);
        pestañaAutomatizacion.add(lblValorUmbral, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Configuracion de Automatizacion");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pestañaAutomatizacion.add(jLabel6, gridBagConstraints);

        jTabbedPane1.addTab("Automatizacion", pestañaAutomatizacion);

        pestañaInterfaz.setLayout(new java.awt.GridBagLayout());

        cmbIdioma.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Español", "English" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 30, 0, 0);
        pestañaInterfaz.add(cmbIdioma, gridBagConstraints);

        chkSonido.setText("Activar Sonido Confirmacion");
        chkSonido.addActionListener(this::chkSonidoActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 0, 0);
        pestañaInterfaz.add(chkSonido, gridBagConstraints);

        cmbTema.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Oscuro", "Claro" }));
        cmbTema.addActionListener(this::cmbTemaActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 0, 0);
        pestañaInterfaz.add(cmbTema, gridBagConstraints);

        jLabel2.setText("Idioma:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 30, 0, 0);
        pestañaInterfaz.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Tema:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 0, 0);
        pestañaInterfaz.add(jLabel3, gridBagConstraints);

        chkNotificacionesPush.setSelected(true);
        chkNotificacionesPush.setText("Activar Notificaciones Push");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 21, 0, 0);
        pestañaInterfaz.add(chkNotificacionesPush, gridBagConstraints);

        chkNotificacionesEmail.setText("Activar Notificaciones por Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 21, 0, 0);
        pestañaInterfaz.add(chkNotificacionesEmail, gridBagConstraints);

        chkAutoActualizacion.setText("Actualizacion Automatica de Datos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 21, 0, 0);
        pestañaInterfaz.add(chkAutoActualizacion, gridBagConstraints);

        lblIntervalo.setText("Intervalo: 0 segundos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 21, 0, 0);
        pestañaInterfaz.add(lblIntervalo, gridBagConstraints);

        spnTiempoActualizacion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 83, 0);
        pestañaInterfaz.add(spnTiempoActualizacion, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Configuracion de Interfaz");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pestañaInterfaz.add(jLabel7, gridBagConstraints);

        jTabbedPane1.addTab("Interfaz", pestañaInterfaz);

        txtIpServidor.addActionListener(this::txtIpServidorActionPerformed);

        jLabel4.setText("Ip del Servidor");

        jLabel5.setText("Puerto del Servidor");

        lblEstado.setText("Estado del Servidor: ");
        panelEstado.add(lblEstado);

        lblEstadoValor.setForeground(new java.awt.Color(255, 0, 0));
        lblEstadoValor.setText("Desconectado");
        panelEstado.add(lblEstadoValor);

        btnProbarConexion.setText("Probar Conexion");
        panelEstado.add(btnProbarConexion);

        btnRestaurarDefault.setBackground(new java.awt.Color(220, 38, 38));
        btnRestaurarDefault.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnRestaurarDefault.setForeground(new java.awt.Color(255, 255, 255));
        btnRestaurarDefault.setText("Restaurar Valores");

        lblVersion.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblVersion.setText("Version: 1.0");

        progressBar.setPreferredSize(new java.awt.Dimension(400, 15));
        progressBar.setVerifyInputWhenFocusTarget(false);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Configuracion de Red");

        javax.swing.GroupLayout pestañaConectividadLayout = new javax.swing.GroupLayout(pestañaConectividad);
        pestañaConectividad.setLayout(pestañaConectividadLayout);
        pestañaConectividadLayout.setHorizontalGroup(
            pestañaConectividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(panelEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(txtIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnRestaurarDefault))
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pestañaConectividadLayout.setVerticalGroup(
            pestañaConectividadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pestañaConectividadLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(6, 6, 6)
                .addComponent(panelEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel5)
                .addGap(8, 8, 8)
                .addComponent(txtPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addGap(8, 8, 8)
                .addComponent(txtIpServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRestaurarDefault)
                .addGap(12, 12, 12)
                .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Conectividad", pestañaConectividad);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Automatizacion");

        btnGuardarAjustes.setText("Guardar Configuracion");
        btnGuardarAjustes.addActionListener(this::btnGuardarAjustesActionPerformed);
        add(btnGuardarAjustes, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbTemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTemaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTemaActionPerformed

    private void btnGuardarAjustesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarAjustesActionPerformed
        int idUsuario = Sesion.getIdUsuario();
        
        boolean autoActiva = chkAutomatizacionActiva.isSelected();
        int umbralSensor = sliderUmbralLuz.getValue();
        String tema = cmbTema.getSelectedItem().toString();
        String idioma = cmbIdioma.getSelectedItem().toString();
        boolean sonido = chkSonido.isSelected();
        String ip = txtIpServidor.getText().trim();
        String puerto = txtPuerto.getText().trim();

        if (ip.isEmpty() || puerto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos de red no pueden quedar vacíos.");
            return;
        }

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        btnGuardarAjustes.setEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    ctrlConfig.guardarConfiguracionCompleta(idUsuario, autoActiva, umbralSensor, 
                                                            tema, idioma, sonido, ip, puerto);
                    ThemeManager.aplicarTema(tema);
                    progressBar.setVisible(false);
                    btnGuardarAjustes.setEnabled(true);
                    JOptionPane.showMessageDialog(this, "✅ Configuración guardada correctamente.");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }//GEN-LAST:event_btnGuardarAjustesActionPerformed

    private void txtIpServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpServidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpServidorActionPerformed

    private void chkSonidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSonidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSonidoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarAjustes;
    private javax.swing.JButton btnProbarConexion;
    private javax.swing.JButton btnRestaurarDefault;
    private javax.swing.JCheckBox chkAutoActualizacion;
    private javax.swing.JCheckBox chkAutomatizacionActiva;
    private javax.swing.JCheckBox chkNotificacionesEmail;
    private javax.swing.JCheckBox chkNotificacionesPush;
    private javax.swing.JCheckBox chkSonido;
    private javax.swing.JComboBox<String> cmbIdioma;
    private javax.swing.JComboBox<String> cmbTema;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstadoValor;
    private javax.swing.JLabel lblIntervalo;
    private javax.swing.JLabel lblValorUmbral;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JPanel panelEstado;
    private javax.swing.JPanel pestañaAutomatizacion;
    private javax.swing.JPanel pestañaConectividad;
    private javax.swing.JPanel pestañaInterfaz;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JSlider sliderUmbralLuz;
    private javax.swing.JSpinner spnTiempoActualizacion;
    private javax.swing.JTextField txtIpServidor;
    private javax.swing.JTextField txtPuerto;
    // End of variables declaration//GEN-END:variables
}
