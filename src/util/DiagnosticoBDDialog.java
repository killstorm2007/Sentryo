package util;

import conexion.Conexion;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DiagnosticoBDDialog extends JDialog {
    
    private JLabel lblHost, lblPort, lblUser, lblDatabase, lblStatus;
    private JButton btnTest, btnCerrar, btnAbrirConfig;
    
    public DiagnosticoBDDialog(JFrame parent) {
        super(parent, "🔍 Diagnóstico de Base de Datos", true);
        setSize(450, 320);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(5, 2, 10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelInfo.add(new JLabel("Host:"));
        lblHost = new JLabel(Conexion.getHost());
        panelInfo.add(lblHost);
        
        panelInfo.add(new JLabel("Puerto:"));
        lblPort = new JLabel(Conexion.getPort());
        panelInfo.add(lblPort);
        
        panelInfo.add(new JLabel("Usuario:"));
        lblUser = new JLabel(Conexion.getUser());
        panelInfo.add(lblUser);
        
        panelInfo.add(new JLabel("Base de Datos:"));
        lblDatabase = new JLabel(Conexion.getDatabase());
        panelInfo.add(lblDatabase);
        
        panelInfo.add(new JLabel("Estado:"));
        boolean conectado = Conexion.testConnection();
        lblStatus = new JLabel(conectado ? "✅ Conectado" : "❌ Desconectado");
        lblStatus.setForeground(conectado ? Color.GREEN : Color.RED);
        panelInfo.add(lblStatus);
        
        add(panelInfo, BorderLayout.CENTER);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnTest = new JButton("🔍 Probar conexión");
        btnTest.addActionListener(e -> probarConexion());
        panelBotones.add(btnTest);
        
        btnAbrirConfig = new JButton("⚙️ Abrir config.bat");
        btnAbrirConfig.setToolTipText("Abre el programa de configuración de la base de datos");
        btnAbrirConfig.addActionListener(e -> abrirConfigBat());
        panelBotones.add(btnAbrirConfig);
        
        btnCerrar = new JButton("❌ Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        
        // Panel de ayuda
        JPanel panelAyuda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAyuda.setBackground(new Color(255, 255, 200));
        JLabel lblAyuda = new JLabel("ℹ️ Si la conexión falla, ejecuta 'config.bat' para actualizar los datos");
        lblAyuda.setFont(new Font("Arial", Font.PLAIN, 11));
        panelAyuda.add(lblAyuda);
        
        add(panelAyuda, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void probarConexion() {
        btnTest.setEnabled(false);
        btnTest.setText("⏳ Probando...");
        lblStatus.setText("⏳ Probando...");
        lblStatus.setForeground(Color.ORANGE);
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
                boolean conectado = Conexion.testConnection();
                
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(conectado ? "✅ Conectado" : "❌ Desconectado");
                    lblStatus.setForeground(conectado ? Color.GREEN : Color.RED);
                    btnTest.setEnabled(true);
                    btnTest.setText("🔍 Probar conexión");
                    
                    if (conectado) {
                        JOptionPane.showMessageDialog(this, 
                            "✅ Conexión exitosa a la base de datos!\n\n" +
                            "📌 Host: " + Conexion.getHost() + "\n" +
                            "📌 Base de datos: " + Conexion.getDatabase(),
                            "Diagnóstico", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "❌ No se pudo conectar a la base de datos.\n\n" +
                            "📌 Verifica que:\n" +
                            "   1. MySQL esté ejecutándose\n" +
                            "   2. Los datos de conexión sean correctos\n" +
                            "   3. Ejecuta 'config.bat' para actualizar la configuración",
                            "Diagnóstico", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("❌ Error");
                    lblStatus.setForeground(Color.RED);
                    btnTest.setEnabled(true);
                    btnTest.setText("🔍 Probar conexión");
                    JOptionPane.showMessageDialog(this,
                        "❌ Error al probar la conexión:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    private void abrirConfigBat() {
        try {
            String configBat = "config.bat";
            
            // Buscar en diferentes ubicaciones
            File[] posiblesRutas = {
                new File(System.getenv("ProgramFiles") + "\\Sentryo\\config.bat"),
                new File(System.getProperty("user.dir") + "\\config.bat"),
                new File("C:\\Sentryo\\config.bat"),
                new File(".\\config.bat")
            };
            
            File configFile = null;
            for (File f : posiblesRutas) {
                if (f.exists()) {
                    configFile = f;
                    break;
                }
            }
            
            if (configFile != null) {
                Runtime.getRuntime().exec("cmd /c start " + configFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this,
                    "✅ Se abrió config.bat\n" +
                    "Configura los datos y vuelve a probar la conexión.",
                    "Diagnóstico", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ No se encontró config.bat\n\n" +
                    "📌 Ejecútalo manualmente desde la carpeta de instalación\n" +
                    "   o desde donde descargaste Sentryo.",
                    "Diagnóstico", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error al abrir config.bat:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}