package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

public class VideoPanel extends JPanel {
    
    private JLabel lblVideo;
    private boolean isRunning = false;
    private Thread videoThread;
    private JLabel lblEstado;
    private String currentUrl = "";
    private long frameCount = 0;
    private long lastFpsUpdate = 0;
    private int fps = 0;
    private JLabel lblFps;

    public VideoPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(400, 300));
        
        // Panel principal de video
        lblVideo = new JLabel("📹 Conectando...", SwingConstants.CENTER);
        lblVideo.setFont(new Font("Arial", Font.BOLD, 18));
        lblVideo.setForeground(Color.WHITE);
        lblVideo.setBackground(Color.BLACK);
        lblVideo.setOpaque(true);
        add(lblVideo, BorderLayout.CENTER);
        
        // Panel de estado inferior
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(Color.BLACK);
        panelEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Estado de conexión
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelInfo.setBackground(Color.BLACK);
        
        lblEstado = new JLabel("🔴 Desconectado");
        lblEstado.setForeground(Color.RED);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        panelInfo.add(lblEstado);
        
        // FPS
        lblFps = new JLabel("FPS: 0");
        lblFps.setForeground(Color.WHITE);
        lblFps.setFont(new Font("Arial", Font.PLAIN, 11));
        panelInfo.add(lblFps);
        
        panelEstado.add(panelInfo, BorderLayout.WEST);
        
        // Calidad / Resolución
        JLabel lblCalidad = new JLabel("📹 640x480");
        lblCalidad.setForeground(Color.WHITE);
        lblCalidad.setFont(new Font("Arial", Font.PLAIN, 11));
        panelEstado.add(lblCalidad, BorderLayout.EAST);
        
        add(panelEstado, BorderLayout.SOUTH);
    }

    /**
     * ✅ Inicia la cámara con optimización de rendimiento
     */
    public void iniciarCamaraArduino(String url) {
        if (isRunning) detenerCamara();
        
        this.currentUrl = url;
        isRunning = true;
        frameCount = 0;
        lastFpsUpdate = System.currentTimeMillis();
        actualizarEstado("📡 Conectando...", Color.ORANGE);
        
        System.out.println("🔗 Conectando a: " + url);
        
        lblVideo.setText("📡 Conectando a:\n" + url);
        lblVideo.setForeground(Color.ORANGE);
        lblVideo.setHorizontalAlignment(SwingConstants.CENTER);
        
        videoThread = new Thread(() -> {
            int erroresConsecutivos = 0;
            
            while (isRunning) {
                try {
                    long inicioFrame = System.currentTimeMillis();
                    
                    // ✅ Usar MJPEG para stream continuo
                    String[] urlsToTry = {
                        url,
                        url + "/video.mjpg",
                        url + "/video",
                        url + "/snapshot"
                    };
                    
                    boolean imagenRecibida = false;
                    
                    for (String testUrl : urlsToTry) {
                        if (imagenRecibida) break;
                        
                        try {
                            URL imageUrl = new URL(testUrl);
                            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                            connection.setConnectTimeout(2000);
                            connection.setReadTimeout(2000);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                            connection.setRequestProperty("Connection", "keep-alive");
                            connection.connect();
                            
                            int responseCode = connection.getResponseCode();
                            
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStream inputStream = connection.getInputStream();
                                BufferedImage image = ImageIO.read(inputStream);
                                inputStream.close();
                                connection.disconnect();
                                
                                if (image != null) {
                                    // ✅ Calcular FPS
                                    frameCount++;
                                    long ahora = System.currentTimeMillis();
                                    if (ahora - lastFpsUpdate >= 1000) {
                                        fps = (int) (frameCount * 1000 / (ahora - lastFpsUpdate));
                                        frameCount = 0;
                                        lastFpsUpdate = ahora;
                                        SwingUtilities.invokeLater(() -> {
                                            lblFps.setText("FPS: " + fps);
                                        });
                                    }
                                    
                                    erroresConsecutivos = 0;
                                    imagenRecibida = true;
                                    
                                    // ✅ Mostrar imagen
                                    SwingUtilities.invokeLater(() -> {
                                        actualizarEstado("✅ Conectado", new Color(34, 197, 94));
                                        
                                        int ancho = getWidth() > 0 ? getWidth() : 400;
                                        int alto = getHeight() > 0 ? getHeight() - 40 : 260;
                                        
                                        // ✅ Escalar con calidad balanceada
                                        Image scaledImage = image.getScaledInstance(ancho, alto, Image.SCALE_FAST);
                                        ImageIcon icon = new ImageIcon(scaledImage);
                                        
                                        lblVideo.setIcon(icon);
                                        lblVideo.setText("");
                                    });
                                    break;
                                }
                            }
                            connection.disconnect();
                            
                        } catch (Exception e) {
                            // Silencioso para no llenar consola
                        }
                    }
                    
                    if (!imagenRecibida) {
                        erroresConsecutivos++;
                        if (erroresConsecutivos > 3) {
                            SwingUtilities.invokeLater(() -> {
                                actualizarEstado("⏳ Reconectando...", Color.ORANGE);
                                lblVideo.setIcon(null);
                                lblVideo.setText("📡 Reconectando...\n" + url);
                                lblVideo.setForeground(Color.ORANGE);
                            });
                            erroresConsecutivos = 0;
                        }
                    }
                    
                    // ✅ Espera dinámica para mantener FPS estable
                    long tiempoProcesamiento = System.currentTimeMillis() - inicioFrame;
                    long espera = Math.max(0, 33 - tiempoProcesamiento); // 33ms ≈ 30 fps
                    Thread.sleep(espera);
                    
                } catch (Exception e) {
                    erroresConsecutivos++;
                    if (erroresConsecutivos > 10) {
                        SwingUtilities.invokeLater(() -> {
                            actualizarEstado("❌ Error", Color.RED);
                            lblVideo.setIcon(null);
                            lblVideo.setText("❌ Error de conexión\n" + url);
                            lblVideo.setForeground(Color.RED);
                        });
                        erroresConsecutivos = 0;
                    }
                    
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        break;
                    }
                }
            }
        });
        videoThread.start();
    }

    /**
     * ✅ MODO PRUEBA - Optimizado
     */
    public void iniciarCamaraPrueba() {
        if (isRunning) detenerCamara();
        
        isRunning = true;
        actualizarEstado("📹 Modo prueba", new Color(34, 197, 94));
        
        videoThread = new Thread(() -> {
            int frame = 0;
            while (isRunning) {
                try {
                    long inicio = System.currentTimeMillis();
                    
                    BufferedImage image = new BufferedImage(400, 270, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics();
                    
                    // Fondo
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(20, 20, 40), 
                                                              400, 270, new Color(10, 10, 30));
                    g.setPaint(gradient);
                    g.fillRect(0, 0, 400, 270);
                    
                    g.setColor(new Color(34, 197, 94));
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(10, 10, 380, 250);
                    
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString("📹 MODO PRUEBA", 120, 60);
                    
                    g.setFont(new Font("Arial", Font.PLAIN, 14));
                    g.setColor(Color.GRAY);
                    g.drawString("Frame: " + frame++, 150, 100);
                    
                    g.setColor(new Color(100, 200, 255));
                    g.drawString("📡 Conecta con Arduino", 130, 150);
                    
                    String ip = currentUrl != null && !currentUrl.isEmpty() ? 
                                currentUrl : "Sin IP configurada";
                    g.setColor(Color.LIGHT_GRAY);
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                    g.drawString("IP: " + ip, 130, 180);
                    
                    int x = (int)(100 + 200 * Math.sin(frame / 25.0));
                    int y = (int)(200 + 40 * Math.cos(frame / 20.0));
                    g.setColor(new Color(34, 197, 94, 150));
                    g.fillOval(x, y, 30, 30);
                    
                    if (frame % 60 < 30) {
                        g.setColor(Color.RED);
                        g.fillOval(370, 15, 15, 15);
                    }
                    
                    g.dispose();
                    
                    ImageIcon icon = new ImageIcon(image);
                    SwingUtilities.invokeLater(() -> {
                        lblVideo.setIcon(icon);
                        lblVideo.setText("");
                        
                        // ✅ Actualizar FPS en modo prueba
                        frameCount++;
                        long ahora = System.currentTimeMillis();
                        if (ahora - lastFpsUpdate >= 1000) {
                            fps = (int) (frameCount * 1000 / (ahora - lastFpsUpdate));
                            frameCount = 0;
                            lastFpsUpdate = ahora;
                            lblFps.setText("FPS: " + fps);
                        }
                    });
                    
                    // ✅ Espera dinámica para ~30 fps
                    long tiempo = System.currentTimeMillis() - inicio;
                    long espera = Math.max(0, 33 - tiempo);
                    Thread.sleep(espera);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        videoThread.start();
    }

    public void detenerCamara() {
        isRunning = false;
        if (videoThread != null) {
            videoThread.interrupt();
            videoThread = null;
        }
        lblVideo.setIcon(null);
        lblVideo.setText("📷 Cámara detenida");
        lblVideo.setForeground(Color.WHITE);
        lblVideo.setHorizontalAlignment(SwingConstants.CENTER);
        actualizarEstado("🔴 Desconectado", Color.RED);
        lblFps.setText("FPS: 0");
    }

    private void actualizarEstado(String texto, Color color) {
        if (lblEstado != null) {
            lblEstado.setText(texto);
            lblEstado.setForeground(color);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}