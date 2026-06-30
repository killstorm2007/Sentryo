package vista;

import modelo.Dispositivo;
import controlador.Ctrl_Dispositivo;
import controlador.Ctrl_Historial;
import controlador.Sesion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardDispositivo extends JPanel {

    private Dispositivo dispositivo;
    private final Ctrl_Dispositivo ctrl = new Ctrl_Dispositivo();
    private final Ctrl_Historial ctrlHistorial = new Ctrl_Historial();

    private JButton btnEditar;
    private JButton btnEliminar;  // ✅ Nuevo botón

    public CardDispositivo(Dispositivo d) {
        this.dispositivo = d;
        initComponents();
        configurarComponentes();
        cargarDatos();
        agregarBotonesAccion();
    }

    private void configurarComponentes() {
        tglEstado.putClientProperty("JButton.buttonType", "roundRect");
        sliderBrillo.putClientProperty("JSlider.paintThumbArrowShape", Boolean.TRUE);

        if (!dispositivo.getTipo().equalsIgnoreCase("LUZ")) {
            sliderBrillo.setVisible(false);
            jLabel2.setVisible(false);
            lblBrillo.setVisible(false);
        }
    }

    private void cargarDatos() {
        lblNombre.setText(dispositivo.getNombre());
        String ubicacion = dispositivo.getUbicacion();
        lblUbicacion.setText((ubicacion == null || ubicacion.isBlank())
                ? "Ubicación no definida" : "📍 " + ubicacion);

        tglEstado.setSelected(dispositivo.getEstado().equalsIgnoreCase("ON"));
        sliderBrillo.setValue(dispositivo.getBrillo());
        lblBrillo.setText(dispositivo.getBrillo() + "%");
        actualizarColor();
        actualizarTextoBoton();
    }

    /**
     * ✅ Agrega los botones de Editar y Eliminar
     */
    private void agregarBotonesAccion() {
        // Panel para botones (se agregará al panel de información)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotones.setOpaque(false);

        // ✅ Botón Editar
        btnEditar = new JButton("✏️");
        btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEditar.setBackground(new Color(59, 130, 246));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setBorderPainted(false);
        btnEditar.setPreferredSize(new Dimension(35, 25));
        btnEditar.setToolTipText("Editar dispositivo");
        btnEditar.addActionListener(e -> abrirDialogoEditar());
        panelBotones.add(btnEditar);

        // ✅ Botón Eliminar
        btnEliminar = new JButton("🗑️");
        btnEliminar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEliminar.setBackground(new Color(220, 38, 38));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setBorderPainted(false);
        btnEliminar.setPreferredSize(new Dimension(35, 25));
        btnEliminar.setToolTipText("Eliminar dispositivo");
        btnEliminar.addActionListener(e -> eliminarDispositivo());
        panelBotones.add(btnEliminar);

        // Agregar el panel de botones al panel de información
        panelnfo.setLayout(new BorderLayout(10, 0));

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setOpaque(false);
        panelTexto.add(lblNombre);
        panelTexto.add(lblUbicacion);

        panelnfo.add(panelTexto, BorderLayout.CENTER);
        panelnfo.add(panelBotones, BorderLayout.EAST);

        panelnfo.revalidate();
        panelnfo.repaint();
    }

    /**
     * ✅ Eliminar dispositivo con confirmación
     */
    private void eliminarDispositivo() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar el dispositivo:\n"
                + "📌 " + dispositivo.getNombre() + "\n"
                + "📍 " + (dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "Ubicación no definida")
                + "\n\n⚠️ Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = ctrl.eliminar(dispositivo.getId(), dispositivo.getIdUsuario());

            if (eliminado) {
                // ✅ Guardar en historial
                ctrlHistorial.guardarEvento(
                        dispositivo.getIdUsuario(),
                        dispositivo.getId(),
                        dispositivo.getTipo(),
                        "Dispositivo eliminado - " + dispositivo.getNombre(),
                        "Se eliminó el dispositivo '" + dispositivo.getNombre()
                        + "' ubicado en '" + (dispositivo.getUbicacion() != null ? dispositivo.getUbicacion() : "ubicación no definida") + "'"
                );

                JOptionPane.showMessageDialog(this,
                        "✅ Dispositivo eliminado correctamente.",
                        "Eliminado",
                        JOptionPane.INFORMATION_MESSAGE);

                // ✅ Actualizar la lista de dispositivos
                mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    frame.actualizarDispositivos();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ No se pudo eliminar el dispositivo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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

        String[] tipos = {"ALARMA", "LUZ", "CAMARA", "TEMPERATURA", "HUMEDAD", "HUMO"};
        JComboBox<String> cmbTipo = new JComboBox<>(tipos);
        cmbTipo.setSelectedItem(dispositivo.getTipo());
        cmbTipo.setEnabled(false);

        String[] pines = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        JComboBox<String> cmbPin = new JComboBox<>(pines);
        cmbPin.setSelectedItem(String.valueOf(dispositivo.getPin()));

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Ubicación:"));
        panel.add(txtUbicacion);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtDescripcion);
        panel.add(new JLabel("IP/URL:"));
        panel.add(txtIP);
        panel.add(new JLabel("Tipo:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("PIN:"));
        panel.add(cmbPin);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "✏️ Editar dispositivo: " + dispositivo.getNombre(),
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
                JOptionPane.showMessageDialog(this, "✅ Dispositivo actualizado correctamente.");
                this.dispositivo = d;
                lblNombre.setText(d.getNombre());
                String ubicacion = d.getUbicacion();
                lblUbicacion.setText((ubicacion == null || ubicacion.isBlank())
                        ? "Ubicación no definida" : "📍 " + ubicacion);

                mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    frame.actualizarDispositivos();
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo actualizar el dispositivo.");
            }
        }
    }

    private void actualizarColor() {
        Color bgColor = tglEstado.isSelected()
                ? new Color(220, 252, 231) : new Color(243, 244, 246);
        Color btnColor = tglEstado.isSelected()
                ? new Color(34, 197, 94) : new Color(220, 38, 38);

        panelPrincipal.setBackground(bgColor);
        panelBrillo.setBackground(bgColor);
        panelnfo.setBackground(bgColor);
        tglEstado.setBackground(btnColor);
        tglEstado.setForeground(Color.WHITE);
        repaint();
    }

    private void actualizarTextoBoton() {
        tglEstado.setText(tglEstado.isSelected() ? "ON" : "OFF");
    }

    private void cambiarEstado() {
        String estado = tglEstado.isSelected() ? "ON" : "OFF";
        String accion = tglEstado.isSelected() ? "encendido" : "apagado";

        dispositivo.setEstado(estado);
        actualizarTextoBoton();
        actualizarColor();

        if (ctrl.actualizarEstado(dispositivo.getId(), estado, dispositivo.getIdUsuario())) {
            guardarEventoHistorial(accion);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.");
            tglEstado.setSelected(!tglEstado.isSelected());
            actualizarTextoBoton();
            actualizarColor();
        }
    }

    private void guardarEventoHistorial(String accion) {
        String ubicacion = dispositivo.getUbicacion();
        if (ubicacion == null || ubicacion.isBlank()) {
            ubicacion = "una ubicación no definida";
        }

        String nombreDispositivo = obtenerNombreSegunTipo();
        String descripcion = construirDescripcion(accion, ubicacion);

        ctrlHistorial.guardarEvento(
                dispositivo.getIdUsuario(),
                dispositivo.getId(),
                dispositivo.getTipo(),
                "Cambio de Estado - " + dispositivo.getNombre(),
                descripcion
        );
    }

    private String obtenerNombreSegunTipo() {
        String tipo = dispositivo.getTipo().toUpperCase();
        switch (tipo) {
            case "LUZ":
                return "la luz";
            case "ALARMA":
                return "la alarma";
            case "CAMARA":
                return "la cámara";
            case "TEMPERATURA":
                return "el sensor de temperatura";
            case "HUMEDAD":
                return "el sensor de humedad";
            case "HUMO":
                return "el detector de humo";
            default:
                return "el dispositivo";
        }
    }

    private String construirDescripcion(String accion, String ubicacion) {
        String tipo = dispositivo.getTipo().toUpperCase();
        String nombreDispositivo = obtenerNombreSegunTipo();

        if (tipo.equals("TEMPERATURA") || tipo.equals("HUMEDAD") || tipo.equals("HUMO")) {
            return nombreDispositivo + " en '" + ubicacion + "' se ha " + accion + ".";
        }
        if (tipo.equals("LUZ")) {
            return nombreDispositivo + " de '" + ubicacion + "' se ha " + accion + ".";
        }
        return nombreDispositivo + " en '" + ubicacion + "' se ha " + accion + ".";
    }

    private void actualizarBrillo() {
        int brillo = sliderBrillo.getValue();
        lblBrillo.setText(brillo + "%");

        if (!sliderBrillo.getValueIsAdjusting()) {
            dispositivo.setBrillo(brillo);
            if (!ctrl.actualizarBrillo(dispositivo.getId(), brillo, dispositivo.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el brillo.");
            }
        }
    }

    // ==================== GENERATED CODE ====================
    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        tglEstado = new javax.swing.JToggleButton();
        panelnfo = new javax.swing.JPanel();
        lblUbicacion = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        panelBrillo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sliderBrillo = new javax.swing.JSlider();
        lblBrillo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMaximumSize(new java.awt.Dimension(32767, 130));
        setLayout(new java.awt.BorderLayout());

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setLayout(new java.awt.BorderLayout());

        tglEstado.setBackground(new java.awt.Color(231, 76, 60));
        tglEstado.setFont(new java.awt.Font("Arial", 1, 14));
        tglEstado.setText("On");
        tglEstado.addActionListener(this::tglEstadoActionPerformed);
        panelPrincipal.add(tglEstado, java.awt.BorderLayout.EAST);

        panelnfo.setBackground(new java.awt.Color(255, 255, 255));

        lblUbicacion.setFont(new java.awt.Font("Arial", 1, 18));
        lblUbicacion.setText("Sala de Estar");

        lblNombre.setFont(new java.awt.Font("Arial", 1, 18));
        lblNombre.setText("Luz");

        // ✅ IMPORTANTE: Los componentes se agregan en agregarBotonesAccion()
        // No agregamos nada directamente a panelnfo aquí
        panelPrincipal.add(panelnfo, java.awt.BorderLayout.WEST);
        add(panelPrincipal, java.awt.BorderLayout.NORTH);

        panelBrillo.setBackground(new java.awt.Color(255, 255, 255));
        panelBrillo.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14));
        jLabel2.setText("Brillo");
        panelBrillo.add(jLabel2, java.awt.BorderLayout.NORTH);

        sliderBrillo.setBackground(new java.awt.Color(153, 153, 153));
        sliderBrillo.addChangeListener(this::sliderBrilloStateChanged);
        panelBrillo.add(sliderBrillo, java.awt.BorderLayout.CENTER);

        lblBrillo.setFont(new java.awt.Font("Arial", 1, 14));
        lblBrillo.setText("50%");
        panelBrillo.add(lblBrillo, java.awt.BorderLayout.EAST);

        add(panelBrillo, java.awt.BorderLayout.CENTER);
    }

    // ==================== EVENTOS ====================
    private void tglEstadoActionPerformed(java.awt.event.ActionEvent evt) {
        this.cambiarEstado();
    }

    private void sliderBrilloStateChanged(javax.swing.event.ChangeEvent evt) {
        this.actualizarBrillo();
    }

    // Variables declaration
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblBrillo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblUbicacion;
    private javax.swing.JPanel panelBrillo;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelnfo;
    private javax.swing.JSlider sliderBrillo;
    private javax.swing.JToggleButton tglEstado;
}
