package vista;

import controlador.Ctrl_Configuracion;
import controlador.Sesion;
import util.ThemeManager;
import util.ColorUtils;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.border.TitledBorder;
import util.DiagnosticoBDDialog;

public class ConfiguracionPanel extends JPanel {
    private Ctrl_Configuracion ctrlConfig = new Ctrl_Configuracion();

    public ConfiguracionPanel() {
        initComponents();
        configurarColores();
        configurarEventos();
        cargarConfiguracionesActuales();
        reestructurarPaneles();

        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }   

    private void reestructurarPaneles() {
        reestructurarAutomatizacion();
        reestructurarInterfaz();
        reestructurarConectividad();
    }

    // ============================================
    // PESTAÑA AUTOMATIZACIÓN
    // ============================================
    private void reestructurarAutomatizacion() {
        pestañaAutomatizacion.removeAll();
        pestañaAutomatizacion.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("⚙️ Configuración de Automatización");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(ColorUtils.getForeground());
        pestañaAutomatizacion.add(lblTitulo, gbc);

        // Separador
        gbc.gridy = 1;
        JSeparator separator = new JSeparator();
        separator.setForeground(ColorUtils.getBorderColor());
        pestañaAutomatizacion.add(separator, gbc);

        // Modo Auto
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        chkAutomatizacionActiva.setFont(new Font("Arial", Font.BOLD, 14));
        pestañaAutomatizacion.add(chkAutomatizacionActiva, gbc);

        // Descripción
        gbc.gridx = 1;
        JLabel lblDesc = new JLabel("(El sistema controlará las luces automáticamente)");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDesc.setForeground(ColorUtils.getForeground().darker());
        pestañaAutomatizacion.add(lblDesc, gbc);

        // Umbral de luz
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        jLabel1.setFont(new Font("Arial", Font.BOLD, 13));
        pestañaAutomatizacion.add(jLabel1, gbc);

        // Slider
        gbc.gridy = 4;
        sliderUmbralLuz.setPreferredSize(new Dimension(500, 50));
        pestañaAutomatizacion.add(sliderUmbralLuz, gbc);

        // Valor del slider
        gbc.gridy = 5;
        lblValorUmbral.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaAutomatizacion.add(lblValorUmbral, gbc);

        pestañaAutomatizacion.revalidate();
        pestañaAutomatizacion.repaint();
    }

    // ============================================
    // PESTAÑA INTERFAZ
    // ============================================
    private void reestructurarInterfaz() {
        pestañaInterfaz.removeAll();
        pestañaInterfaz.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 20, 6, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("🎨 Configuración de Interfaz");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(ColorUtils.getForeground());
        pestañaInterfaz.add(lblTitulo, gbc);

        // Separador
        gbc.gridy = 1;
        JSeparator separator = new JSeparator();
        separator.setForeground(ColorUtils.getBorderColor());
        pestañaInterfaz.add(separator, gbc);

        // Tema
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        jLabel3.setFont(new Font("Arial", Font.BOLD, 13));
        pestañaInterfaz.add(jLabel3, gbc);
        gbc.gridx = 1;
        cmbTema.setPreferredSize(new Dimension(150, 30));
        pestañaInterfaz.add(cmbTema, gbc);

        // Sonido
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        chkSonido.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaInterfaz.add(chkSonido, gbc);

        // Notificaciones Push
        gbc.gridy = 4;
        chkNotificacionesPush.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaInterfaz.add(chkNotificacionesPush, gbc);

        // Notificaciones Email
        gbc.gridy = 5;
        chkNotificacionesEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaInterfaz.add(chkNotificacionesEmail, gbc);

        // Auto actualización
        gbc.gridy = 6;
        chkAutoActualizacion.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaInterfaz.add(chkAutoActualizacion, gbc);

        // Intervalo
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        lblIntervalo.setFont(new Font("Arial", Font.PLAIN, 13));
        pestañaInterfaz.add(lblIntervalo, gbc);
        gbc.gridx = 1;
        spnTiempoActualizacion.setPreferredSize(new Dimension(80, 30));
        pestañaInterfaz.add(spnTiempoActualizacion, gbc);

        pestañaInterfaz.revalidate();
        pestañaInterfaz.repaint();
    }

    // ============================================
    // PESTAÑA CONECTIVIDAD (MEJORADA)
    // ============================================
    private void reestructurarConectividad() {
        pestañaConectividad.removeAll();
        pestañaConectividad.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== TÍTULO =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("🌐 Configuración de Conectividad");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(ColorUtils.getForeground());
        pestañaConectividad.add(lblTitulo, gbc);

        // Separador
        gbc.gridy = 1;
        JSeparator separator = new JSeparator();
        separator.setForeground(ColorUtils.getBorderColor());
        pestañaConectividad.add(separator, gbc);

        // ===== PANEL DE ESTADO DEL SERVIDOR =====
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel panelEstadoServidor = new JPanel(new BorderLayout(10, 5));
        panelEstadoServidor.setBackground(ColorUtils.getBackground());
        panelEstadoServidor.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ColorUtils.getBorderColor()),
            "🔗 Estado del Servidor",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            ColorUtils.getForeground()
        ));
        
        // Subpanel para estado y botón
        JPanel panelEstadoInterno = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelEstadoInterno.setBackground(ColorUtils.getBackground());
        
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEstado.setText("Estado:");
        panelEstadoInterno.add(lblEstado);
        
        lblEstadoValor.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstadoValor.setForeground(Color.RED);
        lblEstadoValor.setText("● Desconectado");
        panelEstadoInterno.add(lblEstadoValor);
        
        btnProbarConexion.setPreferredSize(new Dimension(140, 30));
        btnProbarConexion.setBackground(new Color(59, 130, 246));
        btnProbarConexion.setForeground(Color.WHITE);
        btnProbarConexion.setFocusPainted(false);
        btnProbarConexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelEstadoInterno.add(btnProbarConexion);
        
        panelEstadoServidor.add(panelEstadoInterno, BorderLayout.NORTH);
        
        // Mensaje de ayuda
        JLabel lblAyuda = new JLabel("ℹ️ Si la conexión falla, verifica que el servidor esté encendido");
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAyuda.setForeground(ColorUtils.getForeground().darker());
        lblAyuda.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 0));
        panelEstadoServidor.add(lblAyuda, BorderLayout.SOUTH);
        
        pestañaConectividad.add(panelEstadoServidor, gbc);

        // ===== PANEL DE BASE DE DATOS =====
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 20, 8, 20);
        JPanel panelBD = new JPanel(new BorderLayout(10, 5));
        panelBD.setBackground(ColorUtils.getBackground());
        panelBD.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ColorUtils.getBorderColor()),
            "🗄️ Base de Datos",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            ColorUtils.getForeground()
        ));
        
        JPanel panelBDInterno = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBDInterno.setBackground(ColorUtils.getBackground());
        
        JButton btnDiagnostico = new JButton("🔍 Diagnóstico de BD");
        btnDiagnostico.setBackground(new Color(105, 115, 218));
        btnDiagnostico.setForeground(Color.WHITE);
        btnDiagnostico.setFocusPainted(false);
        btnDiagnostico.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDiagnostico.setPreferredSize(new Dimension(160, 30));
        btnDiagnostico.addActionListener(e -> {
            DiagnosticoBDDialog dialog = new DiagnosticoBDDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this)
            );
            dialog.setVisible(true);
        });
        panelBDInterno.add(btnDiagnostico);
        
        JLabel lblAyudaBD = new JLabel("ℹ️ Ejecuta config.bat si necesitas cambiar los datos de conexión");
        lblAyudaBD.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAyudaBD.setForeground(ColorUtils.getForeground().darker());
        panelBDInterno.add(lblAyudaBD);
        
        panelBD.add(panelBDInterno, BorderLayout.NORTH);
        pestañaConectividad.add(panelBD, gbc);

        // ===== PROGRESS BAR =====
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 20, 8, 20);
        progressBar.setPreferredSize(new Dimension(500, 20));
        progressBar.setBackground(ColorUtils.getBackground());
        progressBar.setForeground(new Color(105, 115, 218));
        pestañaConectividad.add(progressBar, gbc);

        // ===== BOTÓN RESTAURAR =====
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 20, 10, 20);
        btnRestaurarDefault.setPreferredSize(new Dimension(250, 35));
        btnRestaurarDefault.setBackground(new Color(220, 38, 38));
        btnRestaurarDefault.setForeground(Color.WHITE);
        btnRestaurarDefault.setFocusPainted(false);
        btnRestaurarDefault.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pestañaConectividad.add(btnRestaurarDefault, gbc);

        // ===== VERSIÓN =====
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 20, 10, 20);
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 11));
        lblVersion.setForeground(ColorUtils.getForeground().darker());
        pestañaConectividad.add(lblVersion, gbc);

        // Espaciador final
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        pestañaConectividad.add(new JPanel(), gbc);

        pestañaConectividad.revalidate();
        pestañaConectividad.repaint();
    }

    // ============================================
    // CONFIGURACIÓN DE COLORES
    // ============================================
    private void configurarColores() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        Color textFieldBg = ColorUtils.getTextFieldBackground();
        Color textFieldFg = ColorUtils.getTextFieldForeground();

        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        jTabbedPane1.setBackground(bgColor);
        jTabbedPane1.setForeground(fgColor);
        jTabbedPane1.setFont(new Font("Arial", Font.PLAIN, 13));

        pestañaAutomatizacion.setBackground(bgColor);
        pestañaInterfaz.setBackground(bgColor);
        pestañaConectividad.setBackground(bgColor);

        jLabel1.setForeground(fgColor);
        jLabel3.setForeground(fgColor);
        lblValorUmbral.setForeground(fgColor);
        lblIntervalo.setForeground(fgColor);
        lblVersion.setForeground(fgColor);
        lblEstado.setForeground(fgColor);

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

        cmbTema.setBackground(textFieldBg);
        cmbTema.setForeground(textFieldFg);

        spnTiempoActualizacion.setBackground(textFieldBg);
        spnTiempoActualizacion.setForeground(textFieldFg);

        sliderUmbralLuz.setBackground(bgColor);
        sliderUmbralLuz.setForeground(fgColor);

        progressBar.setBackground(bgColor);
        progressBar.setForeground(new Color(105, 115, 218));

        btnGuardarAjustes.setBackground(new Color(105, 115, 218));
        btnGuardarAjustes.setForeground(Color.WHITE);
        btnGuardarAjustes.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarAjustes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarAjustes.setFocusPainted(false);
        btnGuardarAjustes.setBorderPainted(false);

        lblValorUmbral.setText("Valor actual: " + sliderUmbralLuz.getValue());
        lblIntervalo.setText("Intervalo: " + spnTiempoActualizacion.getValue() + " segundos");

        this.revalidate();
        this.repaint();
    }

    // ============================================
    // EVENTOS
    // ============================================
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

    // ============================================
    // FUNCIONES
    // ============================================
    private void probarConexion() {
        lblEstadoValor.setText("⏳ Probando...");
        lblEstadoValor.setForeground(Color.ORANGE);
        btnProbarConexion.setEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
                SwingUtilities.invokeLater(() -> {
                    lblEstadoValor.setText("✅ Conectado");
                    lblEstadoValor.setForeground(new Color(34, 197, 94));
                    btnProbarConexion.setEnabled(true);
                });
            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    btnProbarConexion.setEnabled(true);
                });
            }
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
            chkSonido.setSelected(true);
            chkNotificacionesPush.setSelected(true);
            chkNotificacionesEmail.setSelected(false);
            chkAutoActualizacion.setSelected(true);
            spnTiempoActualizacion.setValue(30);
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

                chkSonido.setSelected((boolean) config.getOrDefault("sonido_confirmacion", true));
            }
        } catch (Exception e) {
            System.err.println("❌ Error cargando configuraciones: " + e.getMessage());
        }
    }

    public void actualizarTema() {
        configurarColores();
        cmbTema.setSelectedItem(ThemeManager.getCurrentTheme());
        this.revalidate();
        this.repaint();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        chkSonido = new javax.swing.JCheckBox();
        cmbTema = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        chkNotificacionesPush = new javax.swing.JCheckBox();
        chkNotificacionesEmail = new javax.swing.JCheckBox();
        chkAutoActualizacion = new javax.swing.JCheckBox();
        lblIntervalo = new javax.swing.JLabel();
        spnTiempoActualizacion = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        pestañaConectividad = new javax.swing.JPanel();
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
                .addGap(122, 122, 122)
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
    }// </editor-fold>                        

    private void cmbTemaActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void btnGuardarAjustesActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        int idUsuario = Sesion.getIdUsuario();
        
        boolean autoActiva = chkAutomatizacionActiva.isSelected();
        int umbralSensor = sliderUmbralLuz.getValue();
        String tema = cmbTema.getSelectedItem().toString();
        boolean sonido = chkSonido.isSelected();

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        btnGuardarAjustes.setEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    ctrlConfig.guardarConfiguracionCompleta(idUsuario, autoActiva, umbralSensor, 
                                                            tema , sonido);
                    ThemeManager.aplicarTema(tema);
                    progressBar.setVisible(false);
                    btnGuardarAjustes.setEnabled(true);
                    JOptionPane.showMessageDialog(this, "✅ Configuración guardada correctamente.");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }                                                 

    private void chkSonidoActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         


    // Variables declaration - do not modify                     
    private javax.swing.JButton btnGuardarAjustes;
    private javax.swing.JButton btnProbarConexion;
    private javax.swing.JButton btnRestaurarDefault;
    private javax.swing.JCheckBox chkAutoActualizacion;
    private javax.swing.JCheckBox chkAutomatizacionActiva;
    private javax.swing.JCheckBox chkNotificacionesEmail;
    private javax.swing.JCheckBox chkNotificacionesPush;
    private javax.swing.JCheckBox chkSonido;
    private javax.swing.JComboBox<String> cmbTema;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
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
    // End of variables declaration                   
}
