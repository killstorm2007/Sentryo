package controlador;

import conexion.Conexion;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Ctrl_Configuracion {
    private static final String TABLE = "conf_usuario";

    public Map<String, Object> obtenerConfiguracionCompleta(int idUsuario) {
        Map<String, Object> config = new HashMap<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE ID_Usuario=?";
        
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = Conexion.conectar();
            if (cn == null) return getDefaultConfig();
            
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                config.put("automatizacion_activa", rs.getBoolean("sistema_armado"));
                config.put("umbral_sensor_luz", rs.getInt("umbral_luz"));
                config.put("tema", rs.getString("tema"));
                config.put("sonido_confirmacion", rs.getBoolean("sonido_confirmacion"));
            } else {
                config = getDefaultConfig();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo configuración: " + e.getMessage());
            config = getDefaultConfig();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }
        return config;
    }

    public boolean obtenerEstadoSistema(int idUsuario) {
        String sql = "SELECT sistema_armado FROM " + TABLE + " WHERE ID_Usuario=?";
        
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = Conexion.conectar();
            if (cn == null) return false;
            
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            return rs.next() && rs.getBoolean("sistema_armado");
            
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo estado sistema: " + e.getMessage());
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
        }
    }

    public void guardarEstadoSistema(int idUsuario, boolean estado) {
        String sql = "INSERT INTO " + TABLE + " (ID_Usuario, sistema_armado) VALUES (?, ?) ON DUPLICATE KEY UPDATE sistema_armado=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setBoolean(2, estado);
            ps.setBoolean(3, estado);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("❌ Error guardando estado sistema: " + e.getMessage());
        }
    }

    public void guardarConfiguracionCompleta(int idUsuario, boolean autoActiva, int umbralSensor,
                                             String tema, boolean sonido) {
        String sql = "INSERT INTO " + TABLE + " (ID_Usuario, sistema_armado, umbral_luz, tema , sonido_confirmacion) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE sistema_armado=?, umbral_luz=?, tema=?, sonido_confirmacion=?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setBoolean(2, autoActiva);
            ps.setInt(3, umbralSensor);
            ps.setString(4, tema);
            ps.setBoolean(5, sonido);
            
            ps.setBoolean(6, autoActiva);
            ps.setInt(7, umbralSensor);
            ps.setString(8, tema);
            ps.setBoolean(9, sonido);
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("❌ Error guardando configuración: " + e.getMessage());
        }
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("automatizacion_activa", false);
        config.put("umbral_sensor_luz", 500);
        config.put("tema", "Claro");
        config.put("sonido_confirmacion", true);
        return config;
    }
}