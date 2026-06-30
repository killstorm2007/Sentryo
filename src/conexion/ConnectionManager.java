package util;

import conexion.Conexion;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectionManager {
    
    public static void setupShutdownHook(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("🔄 Cerrando conexiones...");
                Conexion.cerrarTodo();
                System.out.println("✅ Conexiones cerradas");
            }
        });
        
        // También cerrar al matar la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🔄 Shutdown hook: cerrando conexiones...");
            Conexion.cerrarTodo();
        }));
    }
}