package controlador;

import conexion.Conexion;
import modelo.Historial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Ctrl_Historial {
    private static final String TABLE = "historial";

    private Connection getConnection() throws SQLException {
        return Conexion.conectar();
    }

    /**
     * ✅ Guarda un evento en el historial
     */
    public boolean guardarEvento(int idUsuario, Integer idDispositivo, String tipo, String titulo, String descripcion) {
        String sql = "INSERT INTO " + TABLE + "(id_usuario,id_dispositivo,tipo_evento,titulo,descripcion) VALUES(?,?,?,?,?)";
        try (Connection cn = getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            if (idDispositivo == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, idDispositivo);
            }
            ps.setString(3, tipo);
            ps.setString(4, titulo);
            ps.setString(5, descripcion);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error guardando evento: " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ Lista los últimos eventos (con límite)
     */
    public List<Historial> listarUltimosEventos(int idUsuario) {
        return listarUltimosEventos(idUsuario, 3);
    }

    /**
     * ✅ Lista los últimos eventos con límite personalizado
     */
    public List<Historial> listarUltimosEventos(int idUsuario, int limite) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id_usuario=? ORDER BY fecha DESC LIMIT ?";
        return listarEventos(sql, idUsuario, limite);
    }

    /**
     * ✅ Lista TODOS los eventos sin límite
     */
    public List<Historial> listarTodosEventos(int idUsuario) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id_usuario=? ORDER BY fecha DESC";
        return listarEventos(sql, idUsuario);
    }

    /**
     * ✅ Método genérico para listar eventos
     */
    private List<Historial> listarEventos(String sql, Object... params) {
        List<Historial> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = getConnection();
            if (cn == null) return lista;
            
            ps = cn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearHistorial(rs));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error listando eventos: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            // NO cerramos la conexión - se reutiliza
        }
        return lista;
    }

    /**
     * ✅ Mapea un ResultSet a un objeto Historial
     */
    private Historial mapearHistorial(ResultSet rs) throws SQLException {
        Historial h = new Historial();
        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdUsuario(rs.getInt("id_usuario"));
        
        // Manejar null para id_dispositivo
        Object idDispositivo = rs.getObject("id_dispositivo");
        if (idDispositivo != null) {
            h.setIdDispositivo((Integer) idDispositivo);
        } else {
            h.setIdDispositivo(null);
        }
        
        h.setTipoEvento(rs.getString("tipo_evento"));
        h.setTitulo(rs.getString("titulo"));
        h.setDescripcion(rs.getString("descripcion"));
        h.setFecha(rs.getTimestamp("fecha"));
        return h;
    }
}