package util;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javax.swing.*;
import java.awt.*;

public class MacOSTheme {
    
    public static class MacOSLightTheme extends FlatMacLightLaf {
        @Override
        public String getName() {
            return "macOS Light";
        }
        
        @Override
        public String getDescription() {
            return "Tema macOS Claro Personalizado";
        }
        
        @Override
        public void initialize() {
            super.initialize();
            configurarFuente();
            configurarColoresClaros();
            configurarBordesRedondeados();
        }
    }
    
    public static class MacOSDarkTheme extends FlatMacDarkLaf {
        @Override
        public String getName() {
            return "macOS Dark";
        }
        
        @Override
        public String getDescription() {
            return "Tema macOS Oscuro Personalizado";
        }
        
        @Override
        public void initialize() {
            super.initialize();
            configurarFuente();
            configurarColoresOscuros();
            configurarBordesRedondeados();
        }
    }
    
    private static void configurarFuente() {
        String osName = System.getProperty("os.name").toLowerCase();
        String fontName = osName.contains("mac") ? "San Francisco" : "Segoe UI";
        
        Font baseFont = new Font(fontName, Font.PLAIN, 13);
        Font boldFont = baseFont.deriveFont(Font.BOLD);
        
        UIManager.put("Button.font", baseFont);
        UIManager.put("Label.font", baseFont);
        UIManager.put("TextField.font", baseFont);
        UIManager.put("TextArea.font", baseFont);
        UIManager.put("PasswordField.font", baseFont);
        UIManager.put("ComboBox.font", baseFont);
        UIManager.put("Menu.font", baseFont);
        UIManager.put("MenuItem.font", baseFont);
        UIManager.put("TabbedPane.font", baseFont);
        UIManager.put("Table.font", baseFont);
        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("List.font", baseFont);
        UIManager.put("Tree.font", baseFont);
        UIManager.put("CheckBox.font", baseFont);
        UIManager.put("RadioButton.font", baseFont);
        UIManager.put("ToggleButton.font", baseFont);
        UIManager.put("ProgressBar.font", baseFont);
        UIManager.put("Spinner.font", baseFont);
        UIManager.put("EditorPane.font", baseFont);
        UIManager.put("FormattedTextField.font", baseFont);
        UIManager.put("ScrollPane.font", baseFont);
        UIManager.put("Viewport.font", baseFont);
        UIManager.put("TitledBorder.font", boldFont);
        UIManager.put("TitledBorder.titleFont", boldFont);
        UIManager.put("MenuBar.font", baseFont);
        UIManager.put("PopupMenu.font", baseFont);
        UIManager.put("MenuItem.acceleratorFont", baseFont);
    }
    
    private static void configurarColoresClaros() {
        Color backgroundColor = new Color(242, 242, 242);
        Color foregroundColor = new Color(0, 0, 0);
        Color componentBackground = new Color(255, 255, 255);
        Color borderColor = new Color(199, 199, 199);
        Color selectionColor = new Color(0, 122, 255);
        Color selectionForeground = new Color(255, 255, 255);
        
        // Panel
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Panel.foreground", foregroundColor);
        
        // Botones
        UIManager.put("Button.background", componentBackground);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("Button.borderColor", borderColor);
        UIManager.put("Button.focusedBorderColor", selectionColor);
        UIManager.put("Button.hoverBackground", new Color(240, 240, 240));
        
        // TextFields
        UIManager.put("TextField.background", componentBackground);
        UIManager.put("TextField.foreground", foregroundColor);
        UIManager.put("TextField.borderColor", borderColor);
        UIManager.put("TextField.focusedBorderColor", selectionColor);
        UIManager.put("TextArea.background", componentBackground);
        UIManager.put("TextArea.foreground", foregroundColor);
        UIManager.put("PasswordField.background", componentBackground);
        UIManager.put("PasswordField.foreground", foregroundColor);
        
        // ComboBox
        UIManager.put("ComboBox.background", componentBackground);
        UIManager.put("ComboBox.foreground", foregroundColor);
        UIManager.put("ComboBox.borderColor", borderColor);
        UIManager.put("ComboBox.focusedBorderColor", selectionColor);
        UIManager.put("ComboBox.buttonBackground", componentBackground);
        UIManager.put("ComboBox.buttonHoverBackground", new Color(240, 240, 240));
        
        // TabbedPane
        UIManager.put("TabbedPane.background", backgroundColor);
        UIManager.put("TabbedPane.foreground", foregroundColor);
        UIManager.put("TabbedPane.selectedBackground", componentBackground);
        UIManager.put("TabbedPane.selectedForeground", selectionColor);
        UIManager.put("TabbedPane.borderColor", borderColor);
        UIManager.put("TabbedPane.tabAreaBackground", backgroundColor);
        
        // Tabla
        UIManager.put("Table.background", componentBackground);
        UIManager.put("Table.foreground", foregroundColor);
        UIManager.put("Table.selectionBackground", selectionColor);
        UIManager.put("Table.selectionForeground", selectionForeground);
        UIManager.put("Table.gridColor", new Color(230, 230, 230));
        UIManager.put("TableHeader.background", backgroundColor);
        UIManager.put("TableHeader.foreground", foregroundColor);
        UIManager.put("TableHeader.borderColor", borderColor);
        
        // ScrollBar
        UIManager.put("ScrollBar.thumbInsets", new Insets(3, 3, 3, 3));
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(191, 191, 191));
        UIManager.put("ScrollBar.thumbShadow", new Color(205, 205, 205));
        UIManager.put("ScrollBar.track", backgroundColor);
        UIManager.put("ScrollBar.thumb", new Color(199, 199, 199));
        UIManager.put("ScrollBar.thumbHover", new Color(170, 170, 170));
        
        // Menú
        UIManager.put("MenuBar.background", backgroundColor);
        UIManager.put("MenuBar.foreground", foregroundColor);
        UIManager.put("Menu.background", componentBackground);
        UIManager.put("Menu.foreground", foregroundColor);
        UIManager.put("MenuItem.background", componentBackground);
        UIManager.put("MenuItem.foreground", foregroundColor);
        UIManager.put("MenuItem.selectionBackground", selectionColor);
        UIManager.put("MenuItem.selectionForeground", selectionForeground);
        
        // Listas
        UIManager.put("List.background", componentBackground);
        UIManager.put("List.foreground", foregroundColor);
        UIManager.put("List.selectionBackground", selectionColor);
        UIManager.put("List.selectionForeground", selectionForeground);
        
        // ✅ CheckBox y RadioButton - CORREGIDO (usar null o dejar que FlatLaf maneje los íconos)
        UIManager.put("CheckBox.background", backgroundColor);
        UIManager.put("CheckBox.foreground", foregroundColor);
        // ⚠️ NO establecer CheckBox.icon - dejar que FlatLaf maneje los íconos
        UIManager.put("RadioButton.background", backgroundColor);
        UIManager.put("RadioButton.foreground", foregroundColor);
        
        // Separadores
        UIManager.put("Separator.background", borderColor);
        UIManager.put("Separator.foreground", borderColor);
        
        // ProgressBar
        UIManager.put("ProgressBar.background", backgroundColor);
        UIManager.put("ProgressBar.foreground", selectionColor);
        UIManager.put("ProgressBar.selectionBackground", selectionForeground);
        UIManager.put("ProgressBar.selectionForeground", selectionForeground);
        
        // ToolTip
        UIManager.put("ToolTip.background", new Color(255, 255, 220));
        UIManager.put("ToolTip.foreground", foregroundColor);
        UIManager.put("ToolTip.borderColor", borderColor);
        
        // Spinner
        UIManager.put("Spinner.background", componentBackground);
        UIManager.put("Spinner.foreground", foregroundColor);
        UIManager.put("Spinner.borderColor", borderColor);
        
        // OptionPane
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("OptionPane.foreground", foregroundColor);
        
        // Borders
        UIManager.put("Border.color", borderColor);
        UIManager.put("TitledBorder.borderColor", borderColor);
    }
    
    private static void configurarColoresOscuros() {
        Color backgroundColor = new Color(28, 28, 30);
        Color foregroundColor = new Color(255, 255, 255);
        Color componentBackground = new Color(44, 44, 46);
        Color borderColor = new Color(70, 70, 75);
        Color selectionColor = new Color(0, 122, 255);
        Color selectionForeground = new Color(255, 255, 255);
        
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Panel.foreground", foregroundColor);
        
        UIManager.put("Button.background", componentBackground);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("Button.borderColor", borderColor);
        UIManager.put("Button.focusedBorderColor", selectionColor);
        UIManager.put("Button.hoverBackground", new Color(56, 56, 58));
        
        UIManager.put("TextField.background", componentBackground);
        UIManager.put("TextField.foreground", foregroundColor);
        UIManager.put("TextField.borderColor", borderColor);
        UIManager.put("TextField.focusedBorderColor", selectionColor);
        UIManager.put("TextArea.background", componentBackground);
        UIManager.put("TextArea.foreground", foregroundColor);
        UIManager.put("PasswordField.background", componentBackground);
        UIManager.put("PasswordField.foreground", foregroundColor);
        
        UIManager.put("ComboBox.background", componentBackground);
        UIManager.put("ComboBox.foreground", foregroundColor);
        UIManager.put("ComboBox.borderColor", borderColor);
        UIManager.put("ComboBox.focusedBorderColor", selectionColor);
        UIManager.put("ComboBox.buttonBackground", componentBackground);
        UIManager.put("ComboBox.buttonHoverBackground", new Color(56, 56, 58));
        
        UIManager.put("TabbedPane.background", backgroundColor);
        UIManager.put("TabbedPane.foreground", foregroundColor);
        UIManager.put("TabbedPane.selectedBackground", componentBackground);
        UIManager.put("TabbedPane.selectedForeground", selectionColor);
        UIManager.put("TabbedPane.borderColor", borderColor);
        UIManager.put("TabbedPane.tabAreaBackground", backgroundColor);
        
        UIManager.put("Table.background", componentBackground);
        UIManager.put("Table.foreground", foregroundColor);
        UIManager.put("Table.selectionBackground", selectionColor);
        UIManager.put("Table.selectionForeground", selectionForeground);
        UIManager.put("Table.gridColor", new Color(56, 56, 58));
        UIManager.put("TableHeader.background", backgroundColor);
        UIManager.put("TableHeader.foreground", foregroundColor);
        UIManager.put("TableHeader.borderColor", borderColor);
        
        UIManager.put("ScrollBar.thumbInsets", new Insets(3, 3, 3, 3));
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbDarkShadow", new Color(70, 70, 75));
        UIManager.put("ScrollBar.thumbShadow", new Color(60, 60, 65));
        UIManager.put("ScrollBar.track", backgroundColor);
        UIManager.put("ScrollBar.thumb", new Color(70, 70, 75));
        UIManager.put("ScrollBar.thumbHover", new Color(90, 90, 95));
        
        UIManager.put("MenuBar.background", backgroundColor);
        UIManager.put("MenuBar.foreground", foregroundColor);
        UIManager.put("Menu.background", componentBackground);
        UIManager.put("Menu.foreground", foregroundColor);
        UIManager.put("MenuItem.background", componentBackground);
        UIManager.put("MenuItem.foreground", foregroundColor);
        UIManager.put("MenuItem.selectionBackground", selectionColor);
        UIManager.put("MenuItem.selectionForeground", selectionForeground);
        
        UIManager.put("List.background", componentBackground);
        UIManager.put("List.foreground", foregroundColor);
        UIManager.put("List.selectionBackground", selectionColor);
        UIManager.put("List.selectionForeground", selectionForeground);
        
        // ✅ CheckBox y RadioButton - CORREGIDO
        UIManager.put("CheckBox.background", backgroundColor);
        UIManager.put("CheckBox.foreground", foregroundColor);
        UIManager.put("RadioButton.background", backgroundColor);
        UIManager.put("RadioButton.foreground", foregroundColor);
        
        UIManager.put("Separator.background", borderColor);
        UIManager.put("Separator.foreground", borderColor);
        
        UIManager.put("ProgressBar.background", backgroundColor);
        UIManager.put("ProgressBar.foreground", selectionColor);
        UIManager.put("ProgressBar.selectionBackground", selectionForeground);
        UIManager.put("ProgressBar.selectionForeground", selectionForeground);
        
        UIManager.put("ToolTip.background", new Color(44, 44, 46));
        UIManager.put("ToolTip.foreground", foregroundColor);
        UIManager.put("ToolTip.borderColor", borderColor);
        
        UIManager.put("Spinner.background", componentBackground);
        UIManager.put("Spinner.foreground", foregroundColor);
        UIManager.put("Spinner.borderColor", borderColor);
        
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("OptionPane.foreground", foregroundColor);
        
        UIManager.put("Border.color", borderColor);
        UIManager.put("TitledBorder.borderColor", borderColor);
    }
    
    private static void configurarBordesRedondeados() {
        UIManager.put("Button.arc", 6);
        UIManager.put("Button.borderWidth", 1);
        UIManager.put("TextField.arc", 5);
        UIManager.put("TextArea.arc", 5);
        UIManager.put("PasswordField.arc", 5);
        UIManager.put("ComboBox.arc", 5);
        UIManager.put("ScrollPane.arc", 5);
        UIManager.put("TabbedPane.tabArc", 6);
        UIManager.put("TabbedPane.tabHeight", 28);
        UIManager.put("ToolTip.arc", 5);
        UIManager.put("ProgressBar.arc", 5);
    }
}