package vista;

import modelo.Dispositivo;
import controlador.Ctrl_Dispositivo;
import controlador.Ctrl_Historial;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import util.ColorUtils;

public class CardCamara extends JPanel {

    private Dispositivo dispositivo;
    private final Ctrl_Dispositivo ctrl = new Ctrl_Dispositivo();
    private final Ctrl_Historial ctrlHistorial = new Ctrl_Historial();

    private JLabel lblNombre;
    private JLabel lblUbicacion;
    private JLabel lblEstado;
    private JToggleButton tglEstado;
    private JButton btnVerVistaPrevia;
    private JButton btnGrabar;
    private JButton btnEditar;
    private JButton btnEliminar;

    private JPanel panelVistaPrevia;
    private JLabel lblVistaPrevia;
    private Timer timerVistaPrevia;

    public CardCamara(Dispositivo d) {
        this.dispositivo = d;
        initComponents();
        configurarComponentes();
        cargarDatos();
        agregarBotonEditar();
    }

    private void agregarBotonEditar() {
        btnEditar = new JButton("✏️");
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEditar.setBackground(new Color(59, 130, 246));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setBorderPainted(false);
        btnEditar.setPreferredSize(new Dimension(35, 25));
        btnEditar.setToolTipText("Editar cámara");
        btnEditar.addActionListener(e -> abrirDialogoEditar());

    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        setPreferredSize(new Dimension(400, 160));

        // ===== PANEL SUPERIOR (Info) =====
        JPanel panelInfo = new JPanel(new BorderLayout(10, 0));
        panelInfo.setOpaque(false);

        JLabel lblIcono = new JLabel("📹");
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        panelInfo.add(lblIcono, BorderLayout.WEST);

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setOpaque(false);

        lblNombre = new JLabel("Cámara");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        panelTexto.add(lblNombre);

        lblUbicacion = new JLabel("📍 Ubicación no definida");
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUbicacion.setForeground(new Color(120, 120, 120));
        panelTexto.add(lblUbicacion);

        panelInfo.add(panelTexto, BorderLayout.CENTER);

        // Panel derecho con estado y editar
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelDerecho.setOpaque(false);

        lblEstado = new JLabel("● Desconectado");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstado.setForeground(Color.RED);
        panelDerecho.add(lblEstado);

        // ✅ Botón Editar
        btnEditar = new JButton("✏️");
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEditar.setBackground(new Color(59, 130, 246));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setBorderPainted(false);
        btnEditar.setPreferredSize(new Dimension(35, 25));
        btnEditar.addActionListener(e -> abrirDialogoEditar());
        panelDerecho.add(btnEditar);

        btnEliminar = new JButton("🗑️");
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(220, 38, 38));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setBorderPainted(false);
        btnEliminar.setPreferredSize(new Dimension(35, 25));
        btnEliminar.setToolTipText("Eliminar cámara");
        btnEliminar.addActionListener(e -> eliminarCamara());

        panelDerecho.add(btnEliminar);

        panelInfo.add(panelDerecho, BorderLayout.EAST);
        add(panelInfo, BorderLayout.NORTH);

        // ===== PANEL DE VISTA PREVIA =====
        panelVistaPrevia = new JPanel(new BorderLayout());
        panelVistaPrevia.setBackground(new Color(40, 40, 40));
        panelVistaPrevia.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        panelVistaPrevia.setPreferredSize(new Dimension(160, 90));

        lblVistaPrevia = new JLabel("📷 Sin señal", SwingConstants.CENTER);
        lblVistaPrevia.setFont(new Font("Arial", Font.PLAIN, 14));
        lblVistaPrevia.setForeground(new Color(150, 150, 150));
        lblVistaPrevia.setBackground(new Color(40, 40, 40));
        lblVistaPrevia.setOpaque(true);
        panelVistaPrevia.add(lblVistaPrevia, BorderLayout.CENTER);

        add(panelVistaPrevia, BorderLayout.CENTER);

        // ===== PANEL DE CONTROLES =====
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelControles.setOpaque(false);

        tglEstado = new JToggleButton("OFF");
        tglEstado.setFont(new Font("Arial", Font.BOLD, 12));
        tglEstado.setBackground(new Color(220, 38, 38));
        tglEstado.setForeground(Color.WHITE);
        tglEstado.setFocusPainted(false);
        tglEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tglEstado.addActionListener(this::toggleEstado);
        panelControles.add(tglEstado);

        btnVerVistaPrevia = new JButton("👁️ Ver");
        btnVerVistaPrevia.setFont(new Font("Arial", Font.PLAIN, 11));
        btnVerVistaPrevia.setBackground(new Color(59, 130, 246));
        btnVerVistaPrevia.setForeground(Color.WHITE);
        btnVerVistaPrevia.setFocusPainted(false);
        btnVerVistaPrevia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerVistaPrevia.addActionListener(e -> abrirVistaPrevia());
        panelControles.add(btnVerVistaPrevia);

        btnGrabar = new JButton("🔴 Grabar");
        btnGrabar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnGrabar.setBackground(new Color(220, 38, 38));
        btnGrabar.setForeground(Color.WHITE);
        btnGrabar.setFocusPainted(false);
        btnGrabar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGrabar.addActionListener(e -> iniciarGrabacion());
        panelControles.add(btnGrabar);

        add(panelControles, BorderLayout.SOUTH);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(new Color(248, 250, 252));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(Color.WHITE);
            }
        });
    }

    private void eliminarCamara() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar la cámara:\n"
                + "📹 " + dispositivo.getNombre() + "\n"
                + "📍 " + (dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "Ubicación no definida")
                + "\n\n⚠️ Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = ctrl.eliminar(dispositivo.getId(), dispositivo.getIdUsuario());

            if (eliminado) {
                ctrlHistorial.guardarEvento(
                        dispositivo.getIdUsuario(),
                        dispositivo.getId(),
                        dispositivo.getTipo(),
                        "Cámara eliminada - " + dispositivo.getNombre(),
                        "Se eliminó la cámara '" + dispositivo.getNombre()
                        + "' ubicada en '" + (dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "ubicación no definida") + "'"
                );

                JOptionPane.showMessageDialog(this, "✅ Cámara eliminada correctamente.");

                mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    frame.actualizarDispositivos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar la cámara.");
            }
        }
    }

    private void abrirDialogoEditar() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtNombre = new JTextField(dispositivo.getNombre(), 20);
        JTextField txtUbicacion = new JTextField(dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "", 20);
        JTextField txtDescripcion = new JTextField(dispositivo.getDescripcion() != null ? dispositivo.getDescripcion() : "", 20);
        JTextField txtIP = new JTextField(dispositivo.getIp() != null ? dispositivo.getIp() : "", 20);

        String[] pines = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        JComboBox<String> cmbPin = new JComboBox<>(pines);
        cmbPin.setSelectedItem(String.valueOf(dispositivo.getPin()));

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ubicación:"));
        panel.add(txtUbicacion);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("URL/IP:"));
        panel.add(txtIP);
        panel.add(new JLabel("PIN:"));
        panel.add(cmbPin);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "✏️ Editar cámara: " + dispositivo.getNombre(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }

            Dispositivo d = new Dispositivo();
            d.setId(dispositivo.getId());
            d.setNombre(txtNombre.getText().trim());
            d.setUbicacion(txtUbicacion.getText().trim());
            d.setDescripcion(txtDescripcion.getText().trim());
            d.setIp(txtIP.getText().trim());
            d.setTipo(dispositivo.getTipo());
            d.setPin(Integer.parseInt(cmbPin.getSelectedItem().toString()));
            d.setEstado(dispositivo.getEstado());
            d.setBrillo(dispositivo.getBrillo());
            d.setIdUsuario(dispositivo.getIdUsuario());

            if (ctrl.editar(d)) {
                JOptionPane.showMessageDialog(this, "✅ Cámara actualizada correctamente.");
                this.dispositivo = d;
                lblNombre.setText(d.getNombre());
                lblUbicacion.setText("📍 " + d.getUbicacion());

                mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    frame.actualizarDispositivos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo actualizar la cámara.");
            }
        }
    }

    private void configurarComponentes() {
        if (!dispositivo.getTipo().equalsIgnoreCase("CAMARA")) {
            btnVerVistaPrevia.setVisible(false);
            btnGrabar.setVisible(false);
        }
    }

    private void cargarDatos() {
        lblNombre.setText(dispositivo.getNombre());
        String ubicacion = dispositivo.getUbicacion();
        lblUbicacion.setText((ubicacion == null || ubicacion.isBlank())
                ? "📍 Ubicación no definida" : "📍 " + ubicacion);

        boolean encendido = dispositivo.getEstado().equalsIgnoreCase("ON");
        tglEstado.setSelected(encendido);
        actualizarEstado();
    }

    private void actualizarEstado() {
        if (tglEstado.isSelected()) {
            tglEstado.setText("ON");
            tglEstado.setBackground(new Color(34, 197, 94));
            lblEstado.setText("● Conectado");
            lblEstado.setForeground(new Color(34, 197, 94));
            simularVistaPrevia(true);
        } else {
            tglEstado.setText("OFF");
            tglEstado.setBackground(new Color(220, 38, 38));
            lblEstado.setText("● Desconectado");
            lblEstado.setForeground(Color.RED);
            simularVistaPrevia(false);
        }
        repaint();
    }

    private void simularVistaPrevia(boolean activa) {
        if (activa) {
            lblVistaPrevia.setText("📹 Transmitiendo...");
            lblVistaPrevia.setForeground(new Color(34, 197, 94));

            if (timerVistaPrevia == null) {
                timerVistaPrevia = new Timer(500, (ActionEvent e) -> {
                    String texto = lblVistaPrevia.getText();
                    if (texto.endsWith("...")) {
                        lblVistaPrevia.setText("📹 Transmitiendo");
                    } else if (texto.endsWith("ndo")) {
                        lblVistaPrevia.setText("📹 Transmitiendo.");
                    } else if (texto.endsWith(".")) {
                        lblVistaPrevia.setText("📹 Transmitiendo..");
                    } else {
                        lblVistaPrevia.setText("📹 Transmitiendo...");
                    }
                });
                timerVistaPrevia.start();
            }
        } else {
            if (timerVistaPrevia != null) {
                timerVistaPrevia.stop();
                timerVistaPrevia = null;
            }
            lblVistaPrevia.setText("📷 Sin señal");
            lblVistaPrevia.setForeground(new Color(150, 150, 150));
        }
    }

    private void toggleEstado(ActionEvent e) {
        String estado = tglEstado.isSelected() ? "ON" : "OFF";
        String accion = tglEstado.isSelected() ? "encendida" : "apagada";

        dispositivo.setEstado(estado);
        actualizarEstado();

        if (ctrl.actualizarEstado(dispositivo.getId(), estado, dispositivo.getIdUsuario())) {
            guardarEventoHistorial(accion);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo cambiar el estado de la cámara.");
            tglEstado.setSelected(!tglEstado.isSelected());
            actualizarEstado();
        }
    }

    private void guardarEventoHistorial(String accion) {
        String ubicacion = dispositivo.getUbicacion();
        if (ubicacion == null || ubicacion.isBlank()) {
            ubicacion = "ubicación no definida";
        }

        ctrlHistorial.guardarEvento(
                dispositivo.getIdUsuario(),
                dispositivo.getId(),
                dispositivo.getTipo(),
                "Cámara " + accion + " - " + dispositivo.getNombre(),
                "La cámara en '" + ubicacion + "' se ha " + accion + "."
        );
    }

    private void abrirVistaPrevia() {
        if (!tglEstado.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "La cámara está apagada. Enciéndela primero.",
                    "Cámara apagada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFrame vistaFrame = new JFrame("📹 Vista previa - " + dispositivo.getNombre());
        vistaFrame.setSize(680, 560);
        vistaFrame.setLocationRelativeTo(this);
        vistaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        vistaFrame.setLayout(new BorderLayout());

        VideoPanel videoPanel = new VideoPanel();
        vistaFrame.add(videoPanel, BorderLayout.CENTER);

        // ✅ USAR LA URL EXACTA QUE FUNCIONA EN EL NAVEGADOR
        String ip = dispositivo.getIp();
        String url = "";

        if (ip != null && !ip.trim().isEmpty()) {
            // ✅ Si la IP ya tiene http://, usarla tal cual
            if (ip.startsWith("http://") || ip.startsWith("https://")) {
                url = ip;
            } else {
                // ✅ Si solo es IP, agregar http://
                url = "http://" + ip;
            }

            // ✅ Si la URL no termina en /video, agregarlo
            if (!url.endsWith("/video") && !url.endsWith("/video.mjpg") && !url.endsWith("/snapshot")) {
                url = url + "/video";
            }

            System.out.println("🔗 URL exacta: " + url);

            // ✅ Preguntar al usuario si la URL es correcta
            int confirm = JOptionPane.showConfirmDialog(vistaFrame,
                    "URL de la cámara:\n" + url + "\n\n"
                    + "¿Es esta la URL que usas en el navegador?\n"
                    + "(Si no, cámbiala en la base de datos)",
                    "Confirmar URL",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                videoPanel.iniciarCamaraArduino(url);
            } else {
                // ✅ Permitir al usuario ingresar la URL manualmente
                String manualUrl = JOptionPane.showInputDialog(vistaFrame,
                        "Ingresa la URL completa de la cámara:\n"
                        + "(Ej: http://192.168.1.100:8080/video)",
                        url);

                if (manualUrl != null && !manualUrl.trim().isEmpty()) {
                    videoPanel.iniciarCamaraArduino(manualUrl.trim());
                } else {
                    videoPanel.iniciarCamaraPrueba();
                }
            }

        } else {
            JOptionPane.showMessageDialog(vistaFrame,
                    "La cámara no tiene IP configurada.\n"
                    + "Usando modo de prueba.",
                    "Sin IP configurada",
                    JOptionPane.WARNING_MESSAGE);

            videoPanel.iniciarCamaraPrueba();
        }

        vistaFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                videoPanel.detenerCamara();
            }
        });

        vistaFrame.setVisible(true);
    }

    private void iniciarGrabacion() {
        if (!tglEstado.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "La cámara está apagada. Enciéndela primero.",
                    "Cámara apagada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas iniciar la grabación de la cámara: " + dispositivo.getNombre() + "?",
                "Iniciar grabación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            btnGrabar.setText("🔴 Grabando...");
            btnGrabar.setBackground(new Color(200, 0, 0));

            Timer timer = new Timer(3000, (ActionEvent e) -> {
                btnGrabar.setText("🔴 Grabar");
                btnGrabar.setBackground(new Color(220, 38, 38));
                JOptionPane.showMessageDialog(this,
                        "✅ Grabación finalizada: " + dispositivo.getNombre() + ".mp4",
                        "Grabación completada",
                        JOptionPane.INFORMATION_MESSAGE);
            });
            timer.setRepeats(false);
            timer.start();

            ctrlHistorial.guardarEvento(
                    dispositivo.getIdUsuario(),
                    dispositivo.getId(),
                    dispositivo.getTipo(),
                    "Grabación iniciada - " + dispositivo.getNombre(),
                    "Se inició la grabación de la cámara en '"
                    + (dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "ubicación no definida") + "'"
            );
        }
    }

    public void actualizarTema() {
        Color bgColor = ColorUtils.getBackground();
        setBackground(bgColor);
    }
}
