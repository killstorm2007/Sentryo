package vista;

import controlador.Sesion;
import util.ThemeManager;
import java.awt.*;
import javax.swing.*;
import java.util.prefs.Preferences;

public class mainFrame extends JFrame {

    private CardLayout cardLayout;
    private boolean menuExpandido = true;

    private DashboardPanel dashboardPanel;
    private AgregarDispositivoPanel agregarPanel;
    private DispositivosPanel dispositivosPanel;
    private PerfilPanel perfilPanel;
    private ConfiguracionPanel configuracionPanel;
    private RegistrosPanel registroPanel;
    private CamarasPanel camarasPanel;

    public mainFrame() {

        // ✅ REGISTRAR EL FRAME EN ThemeManager
        ThemeManager.setMainFrame(this);

        // ✅ Aplicar tema guardado ANTES de iniciar componentes
        ThemeManager.aplicarTemaGuardado();

        initComponents();
        setTitle("Panel de Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar shutdown hook
        util.ConnectionManager.setupShutdownHook(this);

        initPaneles();
        btnUserText.setText(Sesion.getNombre());
        mostrarPanel("DASHBOARD");

        // ✅ Agregar botón de cambio de tema en el header
        agregarBotonTema();

        aplicarColoresDinamicos();

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/Sentryo Logo.jpeg"));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("❌ No se pudo cargar el icono: " + e.getMessage());
        }

    }

    private void seleccionarBoton(JButton boton) {
        Color bgColor = UIManager.getColor("Panel.background");
        Color focusColor = UIManager.getColor("Component.focusColor");

        for (JButton b : new JButton[]{btnDashboard, btnDispositivos, btnAgregar,
            btnRegistros, btnPerfil, btnCamaras, btnConfiguracion}) {
            b.setBackground(bgColor);
        }
        if (boton != null) {
            boton.setBackground(focusColor);
        }
    }

    private void aplicarColoresDinamicos() {
        Color bgColor = UIManager.getColor("Panel.background");
        Color fgColor = UIManager.getColor("Panel.foreground");

        panelMain.setBackground(bgColor);
        panelMenu.setBackground(bgColor);
        panelBotones.setBackground(bgColor);
        panelHeader.setBackground(bgColor);
        jPanel2.setBackground(bgColor);
        jPanel3.setBackground(bgColor);
        panelContenido.setBackground(bgColor);
        btnUser.setBackground(bgColor);

        jLabel2.setForeground(fgColor);

        // Botones del menú
        for (JButton btn : new JButton[]{btnDashboard, btnDispositivos, btnAgregar,
            btnRegistros, btnPerfil, btnCamaras, btnConfiguracion, btnLogout}) {
            btn.setBackground(bgColor);
            btn.setForeground(fgColor);
            btn.setOpaque(true);
        }
    }

    private void agregarBotonTema() {
        JButton btnThemeToggle = new JButton();
        btnThemeToggle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btnThemeToggle.setBorder(null);
        btnThemeToggle.setOpaque(false);
        btnThemeToggle.setFocusPainted(false);
        btnThemeToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemeToggle.setBackground(new Color(0, 0, 0, 0));

        actualizarIconoTema(btnThemeToggle);

        btnThemeToggle.addActionListener(e -> {
            ThemeManager.toggleTheme();
            actualizarIconoTema(btnThemeToggle);
            aplicarColoresDinamicos();

            // Actualizar combo en configuración si está visible
            if (configuracionPanel != null) {
                actualizarComboTema();
            }
        });

        // Agregar al panel header (jPanel3, al inicio)
        jPanel3.add(btnThemeToggle, 0);
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void actualizarIconoTema(JButton btn) {
        btn.setText(ThemeManager.isDarkMode() ? "☀️" : "🌙");
        btn.setToolTipText(ThemeManager.isDarkMode() ? "Cambiar a tema claro" : "Cambiar a tema oscuro");
    }

    private void actualizarComboTema() {
        Component[] components = configuracionPanel.getComponents();
        for (Component c : components) {
            if (c instanceof JPanel) {
                JPanel panel = (JPanel) c;
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JComboBox) {
                        JComboBox<?> combo = (JComboBox<?>) comp;
                        String selected = (String) combo.getSelectedItem();
                        if ("Claro".equals(selected) || "Oscuro".equals(selected)) {
                            combo.setSelectedItem(ThemeManager.getCurrentTheme());
                            break;
                        }
                    }
                }
            }
        }
    }

    private void initPaneles() {
        cardLayout = (CardLayout) panelContenido.getLayout();

        dashboardPanel = new DashboardPanel();
        agregarPanel = new AgregarDispositivoPanel();
        dispositivosPanel = new DispositivosPanel();
        perfilPanel = new PerfilPanel();
        configuracionPanel = new ConfiguracionPanel();
        registroPanel = new RegistrosPanel();
        camarasPanel = new CamarasPanel();

        panelContenido.add(dashboardPanel, "DASHBOARD");
        panelContenido.add(dispositivosPanel, "DISPOSITIVOS");
        panelContenido.add(perfilPanel, "PERFIL");
        panelContenido.add(agregarPanel, "AGREGAR");
        panelContenido.add(configuracionPanel, "CONFIG");
        panelContenido.add(registroPanel, "REGISTRO");
        panelContenido.add(camarasPanel, "CAMARAS");
    }

    public void mostrarPanel(String nombre) {
        cardLayout.show(panelContenido, nombre);
    }

    public void actualizarDispositivos() {
        dispositivosPanel.recargarDispositivos();
    }

    private void toggleMenu() {
        int width = menuExpandido ? 70 : 220;
        panelMenu.setPreferredSize(new Dimension(width, panelMenu.getHeight()));

        String[] textos = menuExpandido
                ? new String[]{"", "", "", "", "", "", "", ""}
                : new String[]{"Dashboard", "Dispositivos", "Agregar", "Registros", "Perfil", "Configuración", "Camaras", "Cerrar Sesión"};

        btnDashboard.setText(textos[0]);
        btnDispositivos.setText(textos[1]);
        btnAgregar.setText(textos[2]);
        btnRegistros.setText(textos[3]);
        btnPerfil.setText(textos[4]);
        btnConfiguracion.setText(textos[5]);
        btnCamaras.setText(textos[6]);
        btnLogout.setText(textos[7]);

        menuExpandido = !menuExpandido;
        panelMenu.revalidate();
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            Preferences prefs = Preferences.userRoot();
            prefs.putBoolean("sesionActiva", false);
            prefs.remove("ID_Usuario");
            Sesion.cerrar();

            new Login().setVisible(true);
            dispose();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        panelBotones = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        btnDispositivos = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnRegistros = new javax.swing.JButton();
        btnPerfil = new javax.swing.JButton();
        btnConfiguracion = new javax.swing.JButton();
        btnCamaras = new javax.swing.JButton();
        panelContenido = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnMenu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnUser = new javax.swing.JPanel();
        btnUserText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 700));

        panelMain.setLayout(new java.awt.BorderLayout());

        panelMenu.setBackground(new java.awt.Color(255, 255, 255));
        panelMenu.setLayout(new java.awt.BorderLayout());

        btnLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout icon.png"))); // NOI18N
        btnLogout.setText("Cerrar Sesion");
        btnLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setIconTextGap(15);
        btnLogout.setOpaque(true);
        btnLogout.addActionListener(this::btnLogoutActionPerformed);
        panelMenu.add(btnLogout, java.awt.BorderLayout.PAGE_END);

        panelBotones.setBackground(new java.awt.Color(255, 255, 255));
        panelBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        panelBotones.setPreferredSize(new java.awt.Dimension(220, 0));
        panelBotones.setLayout(new java.awt.GridLayout(7, 1));

        btnDashboard.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home icon.png"))); // NOI18N
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnDashboard.setBorderPainted(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDashboard.setIconTextGap(15);
        btnDashboard.setOpaque(true);
        btnDashboard.setPreferredSize(new java.awt.Dimension(220, 45));
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);
        panelBotones.add(btnDashboard);

        btnDispositivos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDispositivos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/disp icon.png"))); // NOI18N
        btnDispositivos.setText("Dispositivos");
        btnDispositivos.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnDispositivos.setBorderPainted(false);
        btnDispositivos.setFocusPainted(false);
        btnDispositivos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDispositivos.setIconTextGap(15);
        btnDispositivos.setOpaque(true);
        btnDispositivos.addActionListener(this::btnDispositivosActionPerformed);
        panelBotones.add(btnDispositivos);

        btnAgregar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add icon.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnAgregar.setBorderPainted(false);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregar.setIconTextGap(15);
        btnAgregar.setOpaque(true);
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);
        panelBotones.add(btnAgregar);

        btnRegistros.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRegistros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/registro icon.png"))); // NOI18N
        btnRegistros.setText("Registros");
        btnRegistros.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRegistros.setBorderPainted(false);
        btnRegistros.setFocusPainted(false);
        btnRegistros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRegistros.setIconTextGap(15);
        btnRegistros.setOpaque(true);
        btnRegistros.addActionListener(this::btnRegistrosActionPerformed);
        panelBotones.add(btnRegistros);

        btnPerfil.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/profile icon.png"))); // NOI18N
        btnPerfil.setText("Perfil");
        btnPerfil.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnPerfil.setBorderPainted(false);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPerfil.setIconTextGap(15);
        btnPerfil.setOpaque(true);
        btnPerfil.addActionListener(this::btnPerfilActionPerformed);
        panelBotones.add(btnPerfil);

        btnConfiguracion.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/settings icon.png"))); // NOI18N
        btnConfiguracion.setText("Configuracion");
        btnConfiguracion.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnConfiguracion.setBorderPainted(false);
        btnConfiguracion.setFocusPainted(false);
        btnConfiguracion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnConfiguracion.setIconTextGap(15);
        btnConfiguracion.setOpaque(true);
        btnConfiguracion.addActionListener(this::btnConfiguracionActionPerformed);
        panelBotones.add(btnConfiguracion);

        btnCamaras.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCamaras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/camara icon.png"))); // NOI18N
        btnCamaras.setText("Camaras");
        btnCamaras.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCamaras.setBorderPainted(false);
        btnCamaras.setFocusPainted(false);
        btnCamaras.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCamaras.setIconTextGap(15);
        btnCamaras.setOpaque(true);
        btnCamaras.addActionListener(this::btnCamarasActionPerformed);
        panelBotones.add(btnCamaras);

        panelMenu.add(panelBotones, java.awt.BorderLayout.CENTER);

        panelMain.add(panelMenu, java.awt.BorderLayout.LINE_START);

        panelContenido.setLayout(new java.awt.CardLayout());
        panelMain.add(panelContenido, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelMain, java.awt.BorderLayout.CENTER);

        panelHeader.setBackground(new java.awt.Color(255, 255, 255));
        panelHeader.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu icon.png"))); // NOI18N
        btnMenu.setBorder(null);
        btnMenu.addActionListener(this::btnMenuActionPerformed);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Panel de Control");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMenu)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelHeader.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnUser.setBackground(new java.awt.Color(255, 255, 255));

        btnUserText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUserText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user_icon.png"))); // NOI18N
        btnUserText.setText("Usuario");
        btnUserText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserTextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUserTextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUserTextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnUserLayout = new javax.swing.GroupLayout(btnUser);
        btnUser.setLayout(btnUserLayout);
        btnUserLayout.setHorizontalGroup(
            btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnUserText, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
        );
        btnUserLayout.setVerticalGroup(
            btnUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnUserLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUserText))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        panelHeader.add(jPanel3, java.awt.BorderLayout.EAST);

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        int width = menuExpandido ? 70 : 220;
        panelMenu.setPreferredSize(new Dimension(width, panelMenu.getHeight()));

        String[] textos = menuExpandido
                ? new String[]{"", "", "", "", "", "", "", ""}
                : new String[]{"Dashboard", "Dispositivos", "Agregar", "Registros", "Perfil", "Configuración", "Camaras", "Cerrar Sesión"};

        btnDashboard.setText(textos[0]);
        btnDispositivos.setText(textos[1]);
        btnAgregar.setText(textos[2]);
        btnRegistros.setText(textos[3]);
        btnPerfil.setText(textos[4]);
        btnConfiguracion.setText(textos[5]);
        btnCamaras.setText(textos[6]);
        btnLogout.setText(textos[7]);

        menuExpandido = !menuExpandido;
        panelMenu.revalidate();
        panelMenu.repaint();
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        mostrarPanel("PERFIL");
        seleccionarBoton(btnPerfil);

    }//GEN-LAST:event_btnPerfilActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        mostrarPanel("DASHBOARD");
        seleccionarBoton(btnDashboard);
        dashboardPanel.actualizarDashboard();

    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnDispositivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDispositivosActionPerformed
        dispositivosPanel.recargarDispositivos();
        mostrarPanel("DISPOSITIVOS");
        seleccionarBoton(btnDispositivos);
    }//GEN-LAST:event_btnDispositivosActionPerformed

    private void btnConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfiguracionActionPerformed
        mostrarPanel("CONFIG");
        seleccionarBoton(btnConfiguracion);

    }//GEN-LAST:event_btnConfiguracionActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        this.cerrarSesion();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrosActionPerformed
        mostrarPanel("REGISTRO");
        seleccionarBoton(btnRegistros);
        registroPanel.cargarDatos();

    }//GEN-LAST:event_btnRegistrosActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        mostrarPanel("AGREGAR");
        seleccionarBoton(btnAgregar);

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnUserTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserTextMouseExited
        btnUser.setBackground(UIManager.getColor("Panel.background"));
    }//GEN-LAST:event_btnUserTextMouseExited

    private void btnUserTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserTextMouseEntered
        btnUser.setBackground(new Color(243, 244, 246));
    }//GEN-LAST:event_btnUserTextMouseEntered

    private void btnUserTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserTextMouseClicked
        mostrarPanel("PERFIL");
        seleccionarBoton(btnPerfil);
        camarasPanel.cargarCamaras();

    }//GEN-LAST:event_btnUserTextMouseClicked

    private void btnCamarasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCamarasActionPerformed
        mostrarPanel("CAMARAS");
        seleccionarBoton(btnCamaras);
    }//GEN-LAST:event_btnCamarasActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            new mainFrame().setVisible(true);
        });


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new mainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCamaras;
    private javax.swing.JButton btnConfiguracion;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDispositivos;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnRegistros;
    private javax.swing.JPanel btnUser;
    private javax.swing.JLabel btnUserText;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelMenu;
    // End of variables declaration//GEN-END:variables
}
