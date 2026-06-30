package vista;

import controlador.Ctrl_Configuracion;
import controlador.Ctrl_Dispositivo;
import controlador.Ctrl_Historial;
import controlador.Sesion;
import modelo.Historial;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.border.TitledBorder;
import util.ColorUtils;
import util.TaskExecuter;

public class DashboardPanel extends JPanel {

    private Timer timerDashboard;
    private Icon iconoOriginal;
    private Color colorSys;
    private boolean activo = false;
    private boolean actualizando = false; // Bandera para evitar actualizaciones concurrente

    private final Ctrl_Configuracion ctrlConfig = new Ctrl_Configuracion();
    private final Ctrl_Dispositivo ctrlDispositivo = new Ctrl_Dispositivo();
    private final Ctrl_Historial ctrlHistorial = new Ctrl_Historial();

    // Cache para reducir consultas a BD
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    private DashboardCard cardLuces, cardSensores, cardCamaras, cardAlarmas;

    public DashboardPanel() {
        initComponents();
        configurarPaneles();
        cargarCards();
        cargarDatosIniciales();
        cargarActividadReciente();
        iniciarActualizacionAutomatica();
    }
    
    public void actualizarTema() {
        configurarPaneles();
        cargarCards();
        cargarAccesosRapidos();
        cargarEstadoSistema();
    }

    private void configurarPaneles() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();

        panelActividad.setLayout(new BoxLayout(panelActividad, BoxLayout.Y_AXIS));
        QuickAccessPanel.setLayout(new GridLayout(0, 1, 0, 10));

        panelActividad.setBackground(bgColor);
        panelTarjetas.setBackground(bgColor);
        QuickAccessPanel.setBackground(bgColor);

        panelActividad.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Actividad reciente",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                fgColor
        ));

        QuickAccessPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor),
                "Accesos rápidos",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14),
                fgColor
        ));

        colorSys = desarmSystem.getBackground();
        iconoOriginal = sysIcon.getIcon();
        panelStats.setBackground(bgColor);
        panelContenido.setBackground(bgColor);
    }

    private void cargarDatosIniciales() {
        cargarCards();
        cargarAccesosRapidos();
        cargarEstadoSistema();

        new Thread(() -> {
            cargarDatosDashboard();
            SwingUtilities.invokeLater(this::actualizarUI);
        }).start();
    }

    private void cargarDatosDashboard() {
        try {
            cache.put("estadoSistema", ctrlConfig.obtenerEstadoSistema(Sesion.getIdUsuario()));

            int luces = ctrlDispositivo.contarLuces();
            int lucesOn = ctrlDispositivo.contarLucesEncendidas();
            int sensores = ctrlDispositivo.contarSensores();
            int alarmas = ctrlDispositivo.contarAlarmas();
            int totalCamaras = ctrlDispositivo.contarCamaras();
            int camarasOn = ctrlDispositivo.contarCamarasEncendidas();

            cache.put("luces", luces);
            cache.put("lucesOn", lucesOn);
            cache.put("sensores", sensores);
            cache.put("alarmas", alarmas);
            cache.put("totalCamaras", totalCamaras);
            cache.put("camarasOn", camarasOn);

            List<Historial> eventos = ctrlHistorial.listarUltimosEventos(Sesion.getIdUsuario());
            cache.put("eventos", eventos);

        } catch (Exception e) {
            System.err.println("❌ Error cargando datos: " + e.getMessage());
        }
    }

    private void actualizarUI() {
        activo = (boolean) cache.getOrDefault("estadoSistema", false);

        if (activo) {
            sysText1.setText("Sistema Armado");
            sysText2.setText("Protección activa - Todos los sensores vigilando");
            btnSys.setText("Desarmar");
            sysIcon.setIcon(new ImageIcon(getClass().getResource("/img/desarm icon.png")));
            desarmSystem.setBackground(new Color(240, 16, 35));
        } else {
            sysText1.setText("Sistema Desarmado");
            sysText2.setText("Listo para armar - Sistema en espera");
            btnSys.setText("Armar Sistema");
            sysIcon.setIcon(iconoOriginal);
            desarmSystem.setBackground(colorSys);
        }

        if (cardLuces != null) {
            int luces = (int) cache.getOrDefault("luces", 0);
            int lucesOn = (int) cache.getOrDefault("lucesOn", 0);
            int sensores = (int) cache.getOrDefault("sensores", 0);
            int alarmas = (int) cache.getOrDefault("alarmas", 0);
            int totalCamaras = (int) cache.getOrDefault("totalCamaras", 0);
            int camarasOn = (int) cache.getOrDefault("camarasOn", 0);

            cardLuces.setValor(lucesOn + " / " + luces + " Encendidas");
            cardSensores.setValor(sensores + " Registrados");
            cardAlarmas.setValor(alarmas + " Registradas");
            cardCamaras.setValor(camarasOn + " / " + totalCamaras + " Activas");
        }

        cargarActividadReciente();
        cargarAccesosRapidos();
    }

    private void cargarActividadReciente() {
        panelActividad.removeAll();

        @SuppressWarnings("unchecked")
        List<Historial> eventos = (List<Historial>) cache.getOrDefault("eventos", List.of());

        if (eventos.isEmpty()) {
            JLabel lblEmpty = new JLabel("📭 No hay actividad reciente");
            lblEmpty.setFont(new Font("Arial", Font.PLAIN, 13));
            lblEmpty.setForeground(ColorUtils.getForeground());
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelActividad.add(lblEmpty);
        } else {
            for (Historial h : eventos) {
                ImageIcon icono = obtenerIconoPorTipo(h.getTipoEvento());
                ActivityCard tarjeta = new ActivityCard(
                        icono,
                        h.getTitulo(),
                        h.getDescripcion(),
                        h.getFecha().toString().substring(0, 16)
                );
                tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
                panelActividad.add(tarjeta);
                panelActividad.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        panelActividad.add(Box.createVerticalGlue());
        panelActividad.revalidate();
        panelActividad.repaint();
    }

    private ImageIcon obtenerIconoPorTipo(String tipo) {
        String path;
        switch (tipo) {
            case "LUZ":
                path = "/img/disp icon.png";
                break;
            case "ALARMA":
                path = "/img/alarm icon.png";
                break;
            case "CAMARA":
                path = "/img/camara icon.png";
                break;
            case "HUMO":
            case "TEMPERATURA":
            case "HUMEDAD":
                path = "/img/sensor icon.png";
                break;
            default:
                path = "/img/registro icon.png";
                break;
        }
        java.net.URL url = getClass().getResource(path);
        return url != null ? new ImageIcon(url) : new ImageIcon();
    }

    private void iniciarActualizacionAutomatica() {
        timerDashboard = new Timer(30000, e -> {
            if (actualizando) return;
            actualizando = true;
            new Thread(() -> {
                try {
                    cargarDatosDashboard();
                    SwingUtilities.invokeLater(() -> {
                        actualizarUI();
                        actualizando = false;
                    });
                } catch (Exception ex) {
                    System.err.println("❌ Error en actualización: " + ex.getMessage());
                    actualizando = false;
                }
            }).start();
        });
        timerDashboard.start();
    }

    public void actualizarDashboard() {
        if (!actualizando) {
            new Thread(() -> {
                cargarDatosDashboard();
                SwingUtilities.invokeLater(() -> {
                    actualizarUI();
                    // Notificar al frame principal
                    mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                    if (frame != null) {
                        frame.actualizarDispositivos();
                    }
                });
            }).start();
        }
    }

    private void cargarAccesosRapidos() {
        QuickAccessPanel.removeAll();

        QuickAccessCard agregar = new QuickAccessCard(
                new ImageIcon(getClass().getResource("/img/add icon.png")),
                "Agregar dispositivo",
                "Registrar un nuevo dispositivo");
        agregar.setAccion(() -> {
            mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) frame.mostrarPanel("AGREGAR");
        });
        QuickAccessPanel.add(agregar);

        QuickAccessCard dispositivos = new QuickAccessCard(
                new ImageIcon(getClass().getResource("/img/disp icon.png")),
                "Dispositivos",
                "Administrar dispositivos");
        dispositivos.setAccion(() -> {
            mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) frame.mostrarPanel("DISPOSITIVOS");
        });
        QuickAccessPanel.add(dispositivos);

        QuickAccessCard configuracion = new QuickAccessCard(
                new ImageIcon(getClass().getResource("/img/settings icon.png")),
                "Configuración",
                "Modificar preferencias");
        configuracion.setAccion(() -> {
            mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) frame.mostrarPanel("CONFIG");
        });
        QuickAccessPanel.add(configuracion);

        QuickAccessCard camaras = new QuickAccessCard(
                new ImageIcon(getClass().getResource("/img/camara icon.png")),
                "Cámaras",
                "Ver cámaras de vigilancia");
        camaras.setAccion(() -> {
            mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) frame.mostrarPanel("CAMARAS");
        });
        QuickAccessPanel.add(camaras);

        QuickAccessPanel.revalidate();
        QuickAccessPanel.repaint();
    }

    private void cargarCards() {
        panelTarjetas.removeAll();

        cardLuces = new DashboardCard(
                new ImageIcon(getClass().getResource("/img/disp icon.png")),
                "Luces", "Cargando...", new Color(255, 244, 214));

        cardSensores = new DashboardCard(
                new ImageIcon(getClass().getResource("/img/sensor icon.png")),
                "Sensores", "Cargando...", new Color(220, 252, 231));

        cardAlarmas = new DashboardCard(
                new ImageIcon(getClass().getResource("/img/alarm icon.png")),
                "Alarmas", "Cargando...", new Color(254, 226, 226));

        cardCamaras = new DashboardCard(
                new ImageIcon(getClass().getResource("/img/camara icon.png")),
                "Cámaras", "Cargando...", new Color(230, 240, 255));

        panelTarjetas.add(cardLuces);
        panelTarjetas.add(cardSensores);
        panelTarjetas.add(cardAlarmas);
        panelTarjetas.add(cardCamaras);
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    public void actualizarCards() {
        if (cardLuces == null) return;

        int luces = ctrlDispositivo.contarLuces();
        int lucesOn = ctrlDispositivo.contarLucesEncendidas();
        cardLuces.setValor(lucesOn + " / " + luces + " Encendidas");

        int sensores = ctrlDispositivo.contarSensores();
        cardSensores.setValor(sensores + " Registrados");

        int alarmas = ctrlDispositivo.contarAlarmas();
        cardAlarmas.setValor(alarmas + " Registradas");

        int totalCamaras = ctrlDispositivo.contarCamaras();
        int camarasOn = ctrlDispositivo.contarCamarasEncendidas();
        cardCamaras.setValor(camarasOn + " / " + totalCamaras + " Activas");
    }

    private void cargarEstadoSistema() {
        activo = ctrlConfig.obtenerEstadoSistema(Sesion.getIdUsuario());

        if (activo) {
            sysText1.setText("Sistema Armado");
            sysText2.setText("Protección activa - Todos los sensores vigilando");
            btnSys.setText("Desarmar");
            sysIcon.setIcon(new ImageIcon(getClass().getResource("/img/desarm icon.png")));
            desarmSystem.setBackground(new Color(240, 16, 35));
        } else {
            sysText1.setText("Sistema Desarmado");
            sysText2.setText("Listo para armar - Sistema en espera");
            btnSys.setText("Armar Sistema");
            sysIcon.setIcon(iconoOriginal);
            desarmSystem.setBackground(colorSys);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        desarmSystem = new javax.swing.JPanel();
        sysText1 = new javax.swing.JLabel();
        sysText2 = new javax.swing.JLabel();
        sysIcon = new javax.swing.JLabel();
        btnSys = new javax.swing.JButton();
        scrollDashboard = new javax.swing.JScrollPane();
        panelContenido = new javax.swing.JPanel();
        panelStats = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelActividad = new javax.swing.JPanel();
        QuickAccessPanel = new javax.swing.JPanel();
        panelTarjetas = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        desarmSystem.setBackground(new java.awt.Color(0, 185, 123));
        desarmSystem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        desarmSystem.setFocusTraversalPolicyProvider(true);

        sysText1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        sysText1.setForeground(new java.awt.Color(255, 255, 255));
        sysText1.setText("Sistema Desarmado");

        sysText2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        sysText2.setForeground(new java.awt.Color(255, 255, 255));
        sysText2.setText("Listo para armar - Sistema en Espera");

        sysIcon.setBackground(new java.awt.Color(255, 255, 255));
        sysIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arm icon.png"))); // NOI18N

        btnSys.setBackground(new java.awt.Color(242, 242, 242));
        btnSys.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnSys.setText("Armar SIstema\n");
        btnSys.setBorder(null);
        btnSys.addActionListener(this::btnSysActionPerformed);

        javax.swing.GroupLayout desarmSystemLayout = new javax.swing.GroupLayout(desarmSystem);
        desarmSystem.setLayout(desarmSystemLayout);
        desarmSystemLayout.setHorizontalGroup(
            desarmSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(desarmSystemLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(sysIcon)
                .addGap(37, 37, 37)
                .addGroup(desarmSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sysText2)
                    .addComponent(sysText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
                .addComponent(btnSys, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        desarmSystemLayout.setVerticalGroup(
            desarmSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(desarmSystemLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(desarmSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sysIcon)
                    .addGroup(desarmSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnSys, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(desarmSystemLayout.createSequentialGroup()
                            .addComponent(sysText1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(sysText2))))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        add(desarmSystem, java.awt.BorderLayout.NORTH);

        panelContenido.setBackground(new java.awt.Color(255, 255, 255));
        panelContenido.setLayout(new java.awt.GridBagLayout());

        panelStats.setBackground(new java.awt.Color(255, 255, 255));
        panelStats.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelStats.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(panelActividad);

        panelStats.add(jScrollPane1, new java.awt.GridBagConstraints());
        panelStats.add(QuickAccessPanel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panelContenido.add(panelStats, gridBagConstraints);

        panelTarjetas.setBackground(new java.awt.Color(255, 255, 255));
        panelTarjetas.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelTarjetas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        panelContenido.add(panelTarjetas, gridBagConstraints);

        scrollDashboard.setViewportView(panelContenido);

        add(scrollDashboard, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSysActionPerformed
        activo = !activo;

        // Guardar en BD en segundo plano
        TaskExecuter.execute(() -> {
            ctrlConfig.guardarEstadoSistema(Sesion.getIdUsuario(), activo);
            ctrlDispositivo.activarSistema(activo, Sesion.getIdUsuario());

            String titulo = activo ? "Sistema armado" : "Sistema desarmado";
            String desc = activo ? "Todos los sensores fueron activados" : "El sistema fue desactivado";
            ctrlHistorial.guardarEvento(Sesion.getIdUsuario(), null, "SISTEMA", titulo, desc);

            SwingUtilities.invokeLater(() -> {
                // Actualizar UI
                if (activo) {
                    sysText1.setText("Sistema Armado");
                    sysText2.setText("Protección activa - Todos los sensores vigilando");
                    btnSys.setText("Desarmar");
                    sysIcon.setIcon(new ImageIcon(getClass().getResource("/img/desarm icon.png")));
                    desarmSystem.setBackground(new Color(240, 16, 35));
                } else {
                    sysText1.setText("Sistema Desarmado");
                    sysText2.setText("Listo para armar - Sistema en espera");
                    btnSys.setText("Armar Sistema");
                    sysIcon.setIcon(iconoOriginal);
                    desarmSystem.setBackground(colorSys);
                }

                // Actualizar cards y actividad
                TaskExecuter.execute(() -> {
                    cargarDatosDashboard();
                    SwingUtilities.invokeLater(() -> {
                        actualizarUI();
                        // Notificar al frame principal
                        mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                        if (frame != null) {
                            frame.actualizarDispositivos();
                        }
                    });
                });
            });
        });

    }//GEN-LAST:event_btnSysActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel QuickAccessPanel;
    private javax.swing.JButton btnSys;
    private javax.swing.JPanel desarmSystem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelActividad;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelStats;
    private javax.swing.JPanel panelTarjetas;
    private javax.swing.JScrollPane scrollDashboard;
    private javax.swing.JLabel sysIcon;
    private javax.swing.JLabel sysText1;
    private javax.swing.JLabel sysText2;
    // End of variables declaration//GEN-END:variables
}
