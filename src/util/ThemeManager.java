package util;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import vista.*;
import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class ThemeManager {
    private static final String PREF_THEME = "theme";
    private static final String LIGHT = "Claro";
    private static final String DARK = "Oscuro";
    private static String currentTheme = LIGHT;
    private static JFrame mainFrame;
    private static boolean isMaximized = false;
    private static Rectangle savedBounds = null;

    public static void setMainFrame(JFrame frame) {
        mainFrame = frame;
    }

    public static void aplicarTemaGuardado() {
        Preferences prefs = Preferences.userRoot();
        String theme = prefs.get(PREF_THEME, LIGHT);
        aplicarTema(theme, false);
    }

    public static void aplicarTema(String theme) {
        aplicarTema(theme, true);
    }

    private static void aplicarTema(String theme, boolean guardar) {
        try {
            if (mainFrame != null) {
                isMaximized = (mainFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
                if (!isMaximized) {
                    savedBounds = mainFrame.getBounds();
                }
            }

            // ✅ APLICAR TEMA
            if (DARK.equals(theme)) {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
                currentTheme = DARK;
            } else {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                currentTheme = LIGHT;
            }
            
            if (guardar) {
                Preferences prefs = Preferences.userRoot();
                prefs.put(PREF_THEME, theme);
            }
            
            // ✅ ACTUALIZAR TODAS LAS VENTANAS
            actualizarTodasLasVentanas();
            
        } catch (Exception e) {
            System.err.println("❌ Error aplicando tema: " + e.getMessage());
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (Exception ignored) {}
        }
    }

    /**
     * ✅ Actualiza todas las ventanas y paneles
     */
    private static void actualizarTodasLasVentanas() {
        for (Window window : Window.getWindows()) {
            // Actualizar UI del componente
            SwingUtilities.updateComponentTreeUI(window);
            
            // ✅ Si es el frame principal, actualizar paneles
            if (window == mainFrame && mainFrame != null) {
                actualizarPanelesMainFrame();
            }
            
            // Restaurar estado de la ventana
            if (window instanceof JFrame && window == mainFrame) {
                JFrame frame = (JFrame) window;
                if (isMaximized) {
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else if (savedBounds != null) {
                    frame.setBounds(savedBounds);
                }
                frame.revalidate();
                frame.repaint();
            }
            
            window.revalidate();
            window.repaint();
        }
    }

    /**
     * ✅ Actualiza todos los paneles del mainFrame
     */
    private static void actualizarPanelesMainFrame() {
        if (mainFrame == null) return;
        
        // Buscar todos los paneles en el content pane
        Component[] components = mainFrame.getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof mainFrame) {
                mainFrame main = (mainFrame) comp;
                // Buscar paneles en el contenedor principal
                for (Component c : main.getContentPane().getComponents()) {
                    actualizarPanel(c);
                }
            }
        }
    }

    /**
     * ✅ Actualiza un panel específico
     */
    private static void actualizarPanel(Component comp) {
        if (comp == null) return;
        
        try {
            if (comp instanceof DashboardPanel) {
                ((DashboardPanel) comp).actualizarTema();
            } else if (comp instanceof DispositivosPanel) {
                ((DispositivosPanel) comp).actualizarTema();
            } else if (comp instanceof PerfilPanel) {
                ((PerfilPanel) comp).actualizarTema();
            } else if (comp instanceof ConfiguracionPanel) {
                ((ConfiguracionPanel) comp).actualizarTema();
            } else if (comp instanceof RegistrosPanel) {
                ((RegistrosPanel) comp).actualizarTema();
            } else if (comp instanceof AgregarDispositivoPanel) {
                ((AgregarDispositivoPanel) comp).actualizarTema();
            } else if (comp instanceof JPanel) {
                // Actualizar colores del panel
                comp.setBackground(ColorUtils.getBackground());
                comp.setForeground(ColorUtils.getForeground());
                // Recursivamente actualizar hijos
                if (comp instanceof Container) {
                    for (Component child : ((Container) comp).getComponents()) {
                        actualizarPanel(child);
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar errores de paneles que no tienen actualizarTema
        }
    }

    public static void toggleTheme() {
        String newTheme = currentTheme.equals(LIGHT) ? DARK : LIGHT;
        aplicarTema(newTheme);
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public static boolean isDarkMode() {
        return DARK.equals(currentTheme);
    }
}