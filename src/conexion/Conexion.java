package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Clase para la gestión de conexiones a la base de datos
 * @author Sistema de Seguridad
 * @version 2.0
 */
public class Conexion {
    
    // ============================================
    // CONFIGURACIÓN DE LA BASE DE DATOS
    // ============================================
    
    // ✅ CONEXIÓN A LA MÁQUINA VIRTUAL (CAMBIAR POR LA IP CORRECTA)
    private static final String DB_HOST = "100.83.163.64";   // IP de tu VM
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "SistemaSeguridad";
    private static final String DB_USER = "esp32";
    private static final String DB_PASSWORD = "12345";
    
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + 
                                      "?useSSL=false&serverTimezone=UTC&connectTimeout=15000&socketTimeout=15000&autoReconnect=true";
    
    // ============================================
    // CONEXIÓN ÚNICA (Singleton)
    // ============================================
    private static Connection singleConnection = null;
    private static long lastActivity = 0;
    
    // ============================================
    // MÉTODOS DE CONEXIÓN
    // ============================================
    
    /**
     * Obtiene una conexión a la base de datos.
     * Reutiliza la misma conexión si está activa.
     * 
     * @return Connection activa o null si hay error
     */
    public static Connection conectar() {
        try {
            return getConnection();
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cierra la conexión (la libera para ser reutilizada)
     * 
     * @param cn Conexión a cerrar
     */
    public static void cerrar(Connection cn) {
        if (cn != null) {
            closeConnection(cn);
        }
    }
    
    /**
     * Cierra definitivamente la conexión
     */
    public static void cerrarTodo() {
        shutdown();
    }
    
    /**
     * Prueba la conexión a la base de datos
     * 
     * @return true si la conexión es exitosa
     */
    public static boolean testConnection() {
        try {
            Connection cn = conectar();
            if (cn != null && !cn.isClosed()) {
                cerrar(cn);
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ Test de conexión falló: " + e.getMessage());
        }
        return false;
    }
    
    // ============================================
    // GETTERS (PARA EL DIAGNÓSTICO)
    // ============================================
    
    /**
     * Obtiene el host de la base de datos
     */
    public static String getHost() {
        return DB_HOST;
    }
    
    /**
     * Obtiene el puerto de la base de datos
     */
    public static String getPort() {
        return DB_PORT;
    }
    
    /**
     * Obtiene el usuario de la base de datos
     */
    public static String getUser() {
        return DB_USER;
    }
    
    /**
     * Obtiene la contraseña de la base de datos
     */
    public static String getPassword() {
        return DB_PASSWORD;
    }
    
    /**
     * Obtiene el nombre de la base de datos
     */
    public static String getDatabase() {
        return DB_NAME;
    }
    
    /**
     * Obtiene la URL completa de conexión
     */
    public static String getURL() {
        return URL;
    }
    
    // ============================================
    // CONNECTION POOL (Interno)
    // ============================================
    
    private static synchronized Connection getConnection() throws SQLException {
        if (singleConnection != null && !singleConnection.isClosed()) {
            try {
                if (singleConnection.isValid(2)) {
                    lastActivity = System.currentTimeMillis();
                    return singleConnection;
                }
            } catch (SQLException e) {
                singleConnection = null;
            }
        }
        
        try {
            System.out.println("🔄 Creando conexión a BD...");
            System.out.println("   📌 Host: " + DB_HOST);
            System.out.println("   📌 Base de datos: " + DB_NAME);
            System.out.println("   📌 Usuario: " + DB_USER);
            long inicio = System.currentTimeMillis();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            singleConnection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
            singleConnection.setAutoCommit(true);
            
            long tiempo = System.currentTimeMillis() - inicio;
            System.out.println("✅ Conexión creada en " + tiempo + "ms");
            lastActivity = System.currentTimeMillis();
            return singleConnection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage());
        }
    }
    
    private static void closeConnection(Connection cn) {
        // No hacer nada - reutilizar
    }
    
    private static void shutdown() {
        if (singleConnection != null) {
            try {
                if (!singleConnection.isClosed()) {
                    singleConnection.close();
                    System.out.println("🔒 Conexión cerrada");
                }
            } catch (SQLException ignored) {}
            singleConnection = null;
        }
    }
    
    // ============================================
    // CONNECTION MANAGER (Shutdown Hook)
    // ============================================
    
    /**
     * Configura el cierre automático de la conexión al cerrar la aplicación
     * 
     * @param frame El JFrame principal de la aplicación
     */
    public static void setupShutdownHook(JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("🔄 Cerrando conexiones...");
                shutdown();
                System.out.println("✅ Conexiones cerradas");
            }
        });
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🔄 Shutdown hook: cerrando conexiones...");
            shutdown();
        }));
    }
}