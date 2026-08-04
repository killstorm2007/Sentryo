package vista;

import controlador.Ctrl_Historial;
import controlador.Sesion;
import modelo.Historial;
import util.ColorUtils;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RegistrosPanel extends JPanel {
    
    private DefaultTableModel modelo;  // ✅ DECLARADO AQUÍ

    private final Ctrl_Historial ctrlHistorial = new Ctrl_Historial();
    private List<Historial> historialCompleto = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");


    public RegistrosPanel() {
        initComponents();
        configurarComponentes();
        cargarDatos();
    }
    public void actualizarTema() {
        configurarComponentes();
        cargarDatos();
    }
    
    private void configurarComponentes() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setLayout(new BorderLayout(10, 10));
        
        // ========== PANEL SUPERIOR (Título y estadísticas) ==========
        JPanel panelTitulo = new JPanel(new BorderLayout(10, 0));
        panelTitulo.setBackground(bgColor);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("📋 Registros del Sistema");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(fgColor);
        panelTitulo.add(lblTitulo, BorderLayout.WEST);
        
        lblEstadisticas = new JLabel("📊 Cargando estadísticas...");
        lblEstadisticas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstadisticas.setForeground(fgColor);
        panelTitulo.add(lblEstadisticas, BorderLayout.EAST);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // ========== PANEL DE FILTROS ==========
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFiltros.setBackground(bgColor);
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor),
            "🔍 Filtros",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            fgColor
        ));
        
        // Buscar por texto
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setForeground(fgColor);
        panelFiltros.add(lblBuscar);
        
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Buscar por título o descripción");
        txtBuscar.setBackground(ColorUtils.getTextFieldBackground());
        txtBuscar.setForeground(ColorUtils.getTextFieldForeground());
        txtBuscar.setBorder(BorderFactory.createLineBorder(borderColor));
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
        panelFiltros.add(txtBuscar);
        
        // Filtro por tipo
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setForeground(fgColor);
        panelFiltros.add(lblTipo);
        
        cmbFiltroTipo = new JComboBox<>(new String[]{
            "Todos", "LUZ", "ALARMA", "CAMARA", "TEMPERATURA", "HUMEDAD", "HUMO", "SISTEMA"
        });
        cmbFiltroTipo.setBackground(ColorUtils.getComboBoxBackground());
        cmbFiltroTipo.setForeground(ColorUtils.getComboBoxForeground());
        cmbFiltroTipo.addActionListener(e -> filtrar());
        panelFiltros.add(cmbFiltroTipo);
        
        // Filtro por fecha (selector de día)
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setForeground(fgColor);
        panelFiltros.add(lblFecha);
        
        // ✅ CAMBIADO: ComboBox con opciones de fecha
        cmbFiltroFecha = new JComboBox<>(new String[]{
            "Todos", 
            "Hoy", 
            "Ayer", 
            "Seleccionar fecha..."
        });
        cmbFiltroFecha.setBackground(ColorUtils.getComboBoxBackground());
        cmbFiltroFecha.setForeground(ColorUtils.getComboBoxForeground());
        cmbFiltroFecha.addActionListener(e -> {
            if (cmbFiltroFecha.getSelectedItem().equals("Seleccionar fecha...")) {
                seleccionarFechaEspecifica();
            } else {
                filtrar();
            }
        });
        panelFiltros.add(cmbFiltroFecha);
        
        // Botones
        btnRefrescar = new JButton("🔄 Recargar");
        btnRefrescar.setBackground(new Color(105, 115, 218));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnRefrescar);
        
        btnLimpiarFiltros = new JButton("🧹 Limpiar");
        btnLimpiarFiltros.setBackground(new Color(100, 100, 100));
        btnLimpiarFiltros.setForeground(Color.WHITE);
        btnLimpiarFiltros.setFocusPainted(false);
        btnLimpiarFiltros.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiarFiltros.addActionListener(e -> limpiarFiltros());
        panelFiltros.add(btnLimpiarFiltros);
        
        btnExportar = new JButton("📤 Exportar CSV");
        btnExportar.setBackground(new Color(46, 125, 50));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportar.addActionListener(e -> exportarDatos());
        panelFiltros.add(btnExportar);
        
        
        add(panelFiltros, BorderLayout.NORTH);
        
        // ========== PANEL TABLA ==========
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(bgColor);
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor),
            "📊 Eventos",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            fgColor
        ));
        
        // Crear tabla
        String[] columnas = {"ID", "Fecha/Hora", "Tipo", "Título", "Descripción", "Dispositivo"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(ColorUtils.getBackground());
        tabla.getTableHeader().setForeground(ColorUtils.getForeground());
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setBackground(ColorUtils.getTableBackground());
        tabla.setForeground(ColorUtils.getTableForeground());
        tabla.setSelectionBackground(ColorUtils.getTableSelectionBackground());
        tabla.setSelectionForeground(ColorUtils.getTableSelectionForeground());
        
        // Renderizador personalizado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String tipo = (String) table.getValueAt(row, 2);
                    if (tipo != null) {
                        switch (tipo) {
                            case "LUZ": 
                                c.setBackground(new Color(255, 248, 230)); 
                                c.setForeground(Color.BLACK);
                                break;
                            case "ALARMA": 
                                c.setBackground(new Color(255, 230, 230)); 
                                c.setForeground(Color.BLACK);
                                break;
                            case "SISTEMA": 
                                c.setBackground(new Color(230, 245, 230)); 
                                c.setForeground(Color.BLACK);
                                break;
                            default: 
                                c.setBackground(ColorUtils.getTableBackground());
                                c.setForeground(ColorUtils.getTableForeground());
                        }
                    }
                }
                return c;
            }
        });
        
        // Ajustar columnas
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setMaxWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(400);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // Listener para detalle
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(ColorUtils.getBackground());
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        add(panelTabla, BorderLayout.CENTER);
        
        // ========== PANEL INFERIOR ==========
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(bgColor);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        lblTotalRegistros = new JLabel("Total: 0 registros");
        lblTotalRegistros.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotalRegistros.setForeground(fgColor);
        panelInferior.add(lblTotalRegistros, BorderLayout.SOUTH);
        
        // Panel de detalle
        panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setBackground(bgColor);
        panelDetalle.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor),
            "📝 Detalle del evento seleccionado",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12),
            fgColor
        ));
        panelDetalle.setPreferredSize(new Dimension(0, 120));
        
        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtDetalle.setBackground(ColorUtils.getTextFieldBackground());
        txtDetalle.setForeground(ColorUtils.getTextFieldForeground());
        txtDetalle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtDetalle.setText("Selecciona un evento para ver los detalles aquí.");
        
        JScrollPane detalleScroll = new JScrollPane(txtDetalle);
        detalleScroll.setBorder(null);
        detalleScroll.getViewport().setBackground(ColorUtils.getTextFieldBackground());
        panelDetalle.add(detalleScroll, BorderLayout.CENTER);
        
        panelInferior.add(panelDetalle, BorderLayout.CENTER);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void seleccionarFechaEspecifica() {
        // Crear un selector de fecha con JDateChooser o usar un JDialog simple
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JSpinner spinnerDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        JSpinner spinnerMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JSpinner spinnerAnio = new JSpinner(new SpinnerNumberModel(2024, 2000, 2100, 1));
        
        // Obtener fecha actual por defecto
        Calendar cal = Calendar.getInstance();
        spinnerDia.setValue(cal.get(Calendar.DAY_OF_MONTH));
        spinnerMes.setValue(cal.get(Calendar.MONTH) + 1);
        spinnerAnio.setValue(cal.get(Calendar.YEAR));
        
        panel.add(new JLabel("Día:"));
        panel.add(spinnerDia);
        panel.add(new JLabel("Mes:"));
        panel.add(spinnerMes);
        panel.add(new JLabel("Año:"));
        panel.add(spinnerAnio);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Seleccionar fecha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int dia = (int) spinnerDia.getValue();
            int mes = (int) spinnerMes.getValue();
            int anio = (int) spinnerAnio.getValue();
            
            String fechaSeleccionada = String.format("%02d/%02d/%04d", dia, mes, anio);
            cmbFiltroFecha.addItem(fechaSeleccionada);
            cmbFiltroFecha.setSelectedItem(fechaSeleccionada);
            filtrar();
        } else {
            // Volver a "Todos" si cancela
            cmbFiltroFecha.setSelectedItem("Todos");
        }
    }
    
    public void cargarDatos() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        modelo.setRowCount(0);
        
        new Thread(() -> {
            try {
                // ✅ OBTENER TODOS LOS REGISTROS SIN LÍMITE
                historialCompleto = ctrlHistorial.listarTodosEventos(Sesion.getIdUsuario());
                
                SwingUtilities.invokeLater(() -> {
                    filtrar();
                    actualizarEstadisticas();
                    setCursor(Cursor.getDefaultCursor());
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, 
                        "Error al cargar datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    setCursor(Cursor.getDefaultCursor());
                });
            }
        }).start();
    }
    
    private void filtrar() {
        modelo.setRowCount(0);
        
        String busqueda = txtBuscar.getText().toLowerCase().trim();
        String tipoFiltro = cmbFiltroTipo.getSelectedItem().toString();
        String fechaFiltro = cmbFiltroFecha.getSelectedItem().toString();
        
        int contador = 0;
        
        for (Historial h : historialCompleto) {
            // Filtro por tipo
            if (!tipoFiltro.equals("Todos") && !h.getTipoEvento().equals(tipoFiltro)) {
                continue;
            }
            
            // ✅ FILTRO POR FECHA ESPECÍFICA
            if (!fechaFiltro.equals("Todos")) {
                String fechaEvento = sdfFecha.format(h.getFecha());
                
                if (fechaFiltro.equals("Hoy")) {
                    String fechaHoy = sdfFecha.format(new Date());
                    if (!fechaEvento.equals(fechaHoy)) continue;
                } else if (fechaFiltro.equals("Ayer")) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    String fechaAyer = sdfFecha.format(cal.getTime());
                    if (!fechaEvento.equals(fechaAyer)) continue;
                } else {
                    // Fecha específica seleccionada
                    if (!fechaEvento.equals(fechaFiltro)) continue;
                }
            }
            
            // Filtro por búsqueda
            if (!busqueda.isEmpty()) {
                String titulo = h.getTitulo().toLowerCase();
                String desc = h.getDescripcion().toLowerCase();
                if (!titulo.contains(busqueda) && !desc.contains(busqueda)) {
                    continue;
                }
            }
            
            Vector<Object> fila = new Vector<>();
            fila.add(h.getIdHistorial());
            fila.add(sdf.format(h.getFecha()));
            fila.add(h.getTipoEvento());
            fila.add(h.getTitulo());
            fila.add(h.getDescripcion());
            fila.add(h.getIdDispositivo() != null ? h.getIdDispositivo().toString() : "Sistema");
            
            modelo.addRow(fila);
            contador++;
        }
        
        lblTotalRegistros.setText("Total: " + contador + " registros (de " + historialCompleto.size() + " totales)");
        
        if (tabla.getSelectedRow() < 0) {
            txtDetalle.setText("Selecciona un evento para ver los detalles aquí.");
        }
    }
    
    private void limpiarFiltros() {
        txtBuscar.setText("");
        cmbFiltroTipo.setSelectedIndex(0);
        cmbFiltroFecha.setSelectedIndex(0);
        // Eliminar fechas personalizadas del combo
        for (int i = cmbFiltroFecha.getItemCount() - 1; i >= 0; i--) {
            String item = cmbFiltroFecha.getItemAt(i);
            if (!item.equals("Todos") && !item.equals("Hoy") && 
                !item.equals("Ayer") && !item.equals("Seleccionar fecha...")) {
                cmbFiltroFecha.removeItemAt(i);
            }
        }
        filtrar();
    }

    private void mostrarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("📌 DETALLE DEL EVENTO\n");
            sb.append("═".repeat(50)).append("\n\n");
            sb.append("  ID: ").append(tabla.getValueAt(fila, 0)).append("\n");
            sb.append("  Fecha: ").append(tabla.getValueAt(fila, 1)).append("\n");
            sb.append("  Tipo: ").append(tabla.getValueAt(fila, 2)).append("\n");
            sb.append("  Título: ").append(tabla.getValueAt(fila, 3)).append("\n");
            sb.append("  Descripción: ").append(tabla.getValueAt(fila, 4)).append("\n");
            sb.append("  Dispositivo: ").append(tabla.getValueAt(fila, 5)).append("\n");
            txtDetalle.setText(sb.toString());
            txtDetalle.setCaretPosition(0);
        }
    }

    private void actualizarEstadisticas() {
        int total = historialCompleto.size();
        int luces = 0, alarmas = 0, sensores = 0, sistemas = 0;
        
        for (Historial h : historialCompleto) {
            switch (h.getTipoEvento()) {
                case "LUZ": luces++; break;
                case "ALARMA": alarmas++; break;
                case "SISTEMA": sistemas++; break;
                default: sensores++;
            }
        }
        
        lblEstadisticas.setText(String.format(
            "📊 Total: %d | 💡 Luces: %d | 🔔 Alarmas: %d | 📡 Sensores: %d | ⚙️ Sistema: %d",
            total, luces, alarmas, sensores, sistemas
        ));
    }

    private void exportarDatos() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay datos para exportar.",
                "Exportar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar registros");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        chooser.setSelectedFile(new File("registros_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
        
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            String nombre = archivo.getAbsolutePath();
            
            if (!nombre.endsWith(".csv")) {
                nombre += ".csv";
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombre))) {
                writer.println("ID,Fecha/Hora,Tipo,Título,Descripción,Dispositivo");
                
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        Object valor = modelo.getValueAt(i, j);
                        String texto = valor != null ? valor.toString().replace(",", ";") : "";
                        writer.print(texto);
                        if (j < modelo.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Datos exportados exitosamente:\n" + nombre,
                    "Exportación completada", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al exportar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenedorPrincipal = new javax.swing.JPanel();
        pnlNorte = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblEstadisticas = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        pnlFiltros = new javax.swing.JPanel();
        lblBuscara = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        lblTipo = new javax.swing.JLabel();
        cmbFiltroTipo = new javax.swing.JComboBox<>();
        lblFecha = new javax.swing.JLabel();
        cmbFiltroFecha = new javax.swing.JComboBox<>();
        btnRefrescar = new javax.swing.JButton();
        btnLimpiarFiltros = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        pnlTabla = new javax.swing.JPanel();
        scrollTabla = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        panelDetalle = new javax.swing.JPanel();
        scrollDetalle = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        pnlSur = new javax.swing.JPanel();
        lblTotalRegistros = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        pnlContenedorPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlContenedorPrincipal.setLayout(new java.awt.BorderLayout());

        pnlNorte.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblTitulo.setText("Registros del Sistema");
        pnlNorte.add(lblTitulo);

        lblEstadisticas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEstadisticas.setText("Total: 0");
        pnlNorte.add(lblEstadisticas);

        pnlContenedorPrincipal.add(pnlNorte, java.awt.BorderLayout.NORTH);

        pnlFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        lblBuscara.setText("Buscar:");
        pnlFiltros.add(lblBuscara);

        txtBuscar.addActionListener(this::txtBuscarActionPerformed);
        pnlFiltros.add(txtBuscar);

        lblTipo.setText("Tipo:");
        pnlFiltros.add(lblTipo);

        cmbFiltroTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "LUZ", "ALARMA", "CAMARA", "TEMPERATURA", "HUMEDAD", "HUMO", "SISTEMA" }));
        pnlFiltros.add(cmbFiltroTipo);

        lblFecha.setText("Periodo:");
        pnlFiltros.add(lblFecha);

        cmbFiltroFecha.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todo", "Hoy", "Ultima Semana", "Ultimo Mes", "Ultimo Año" }));
        pnlFiltros.add(cmbFiltroFecha);

        btnRefrescar.setText("Recargar");
        pnlFiltros.add(btnRefrescar);

        btnLimpiarFiltros.setText("Limpiar");
        pnlFiltros.add(btnLimpiarFiltros);

        btnExportar.setText("Exportar");
        pnlFiltros.add(btnExportar);

        pnlCentro.add(pnlFiltros);

        pnlTabla.setLayout(new java.awt.BorderLayout());

        tabla.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.setRowHeight(35);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollTabla.setViewportView(tabla);

        pnlTabla.add(scrollTabla, java.awt.BorderLayout.CENTER);

        pnlCentro.add(pnlTabla);

        panelDetalle.setLayout(new java.awt.BorderLayout());

        txtDetalle.setEditable(false);
        txtDetalle.setColumns(50);
        txtDetalle.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txtDetalle.setRows(4);
        scrollDetalle.setViewportView(txtDetalle);

        panelDetalle.add(scrollDetalle, java.awt.BorderLayout.CENTER);

        pnlCentro.add(panelDetalle);

        pnlContenedorPrincipal.add(pnlCentro, java.awt.BorderLayout.CENTER);

        lblTotalRegistros.setText("Total Registros: 0 registros");
        pnlSur.add(lblTotalRegistros);

        pnlContenedorPrincipal.add(pnlSur, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlContenedorPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 336, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlContenedorPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportar;
    private javax.swing.JButton btnLimpiarFiltros;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> cmbFiltroFecha;
    private javax.swing.JComboBox<String> cmbFiltroTipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBuscara;
    private javax.swing.JLabel lblEstadisticas;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JPanel panelDetalle;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlContenedorPrincipal;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JPanel pnlNorte;
    private javax.swing.JPanel pnlSur;
    private javax.swing.JPanel pnlTabla;
    private javax.swing.JScrollPane scrollDetalle;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextArea txtDetalle;
    // End of variables declaration//GEN-END:variables
}
