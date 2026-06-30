package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    private static final String URL = "jdbc:mysql://100.83.163.64/SistemaSeguridad?useSSL=false&serverTimezone=UTC&connectTimeout=10000&socketTimeout=10000&autoReconnect=true";
    private static final String USER = "esp32";
    private static final String PASSWORD = "12345";
    
    private static Connection singleConnection = null;
    private static long lastActivity = 0;

    public static synchronized Connection getConnection() throws SQLException {
        // Reutilizar si existe y está activa
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
            long inicio = System.currentTimeMillis();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            singleConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            singleConnection.setAutoCommit(true);
            
            long tiempo = System.currentTimeMillis() - inicio;
            System.out.println("✅ Conexión creada en " + tiempo + "ms");
            lastActivity = System.currentTimeMillis();
            return singleConnection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection cn) {
        // No hacer nada - reutilizar
    }

    public static void shutdown() {
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
}