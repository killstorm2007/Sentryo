package conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexion {
    
    public static Connection conectar() {
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public static void cerrar(Connection cn) {
        if (cn != null) {
            ConnectionPool.closeConnection(cn);
        }
    }
    
    public static void cerrarTodo() {
        ConnectionPool.shutdown();
    }
    
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
}