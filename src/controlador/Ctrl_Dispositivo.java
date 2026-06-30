package controlador;

import conexion.Conexion;
import static conexion.ConnectionPool.getConnection;
import modelo.Dispositivo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Ctrl_Dispositivo {

    private static final String TABLE = "dispositivos";

    public boolean editar(Dispositivo d) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();

        try {
            String sql = "UPDATE dispositivos SET "
                    + "nombre=?,"
                    + "tipo=?,"
                    + "estado=?,"
                    + "brillo=?,"
                    + "ubicacion=?,"
                    + "descripcion=?,"
                    + "ip=?,"
                    + "pin=? "
                    + "WHERE id_dispositivo=? AND ID_Usuario = ?";

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, d.getNombre());
            ps.setString(2, d.getTipo());
            ps.setString(3, d.getEstado());
            ps.setInt(4, d.getBrillo());
            ps.setString(5, d.getUbicacion());
            ps.setString(6, d.getDescripcion());
            ps.setString(7, d.getIp());
            ps.setInt(8, d.getPin());
            ps.setInt(9, d.getId());
            ps.setInt(10, d.getIdUsuario());

            respuesta = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al editar dispositivo\n" + e.getMessage());
        }

        return respuesta;
    }

    public List<Dispositivo> listar(int idUsuario) {
        List<Dispositivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE ID_Usuario = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                System.err.println("❌ No se pudo obtener conexión");
                return lista;
            }

            ps = cn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            // ⚠️ IMPORTANTE: Procesar todos los resultados ANTES de cerrar
            while (rs.next()) {
                Dispositivo d = new Dispositivo();
                d.setId(rs.getInt("id_dispositivo"));
                d.setNombre(rs.getString("nombre"));
                d.setTipo(rs.getString("tipo"));
                d.setEstado(rs.getString("estado"));
                d.setBrillo(rs.getInt("brillo"));
                d.setUbicacion(rs.getString("ubicacion"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setIp(rs.getString("ip"));
                d.setPin(rs.getInt("pin"));
                d.setIdUsuario(rs.getInt("ID_Usuario"));
                lista.add(d);
            }

            System.out.println("📊 Dispositivos listados: " + lista.size());

        } catch (Exception e) {
            System.err.println("❌ Error listando: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ✅ Cerrar en orden inverso: rs -> ps -> cn
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ignored) {
            }
            // ⚠️ NO cerramos cn aquí - la reutilizamos
        }
        return lista;
    }

    public List<Dispositivo> buscarPorTipo(String tipo, int idUsuario) {
        List<Dispositivo> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE tipo=? AND ID_Usuario=?";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                return lista;
            }

            ps = cn.prepareStatement(sql);
            ps.setString(1, tipo);
            ps.setInt(2, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                Dispositivo d = new Dispositivo();
                d.setId(rs.getInt("id_dispositivo"));
                d.setNombre(rs.getString("nombre"));
                d.setTipo(rs.getString("tipo"));
                d.setEstado(rs.getString("estado"));
                d.setBrillo(rs.getInt("brillo"));
                d.setUbicacion(rs.getString("ubicacion"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setIp(rs.getString("ip"));
                d.setPin(rs.getInt("pin"));
                d.setIdUsuario(rs.getInt("ID_Usuario"));
                lista.add(d);
            }

        } catch (Exception e) {
            System.err.println("❌ Error buscando por tipo: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    public Dispositivo buscar(int id, int idUsuario) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id_dispositivo=? AND ID_Usuario=?";
        Dispositivo d = null;

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                return null;
            }

            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                d = new Dispositivo();
                d.setId(rs.getInt("id_dispositivo"));
                d.setNombre(rs.getString("nombre"));
                d.setTipo(rs.getString("tipo"));
                d.setEstado(rs.getString("estado"));
                d.setBrillo(rs.getInt("brillo"));
                d.setUbicacion(rs.getString("ubicacion"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setIp(rs.getString("ip"));
                d.setPin(rs.getInt("pin"));
                d.setIdUsuario(rs.getInt("ID_Usuario"));
            }

        } catch (Exception e) {
            System.err.println("❌ Error buscando: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return d;
    }

    public int[] contarTodos() {
        int[] resultados = {0, 0, 0, 0, 0};
        String sql = "SELECT "
                + "SUM(CASE WHEN tipo='LUZ' THEN 1 ELSE 0 END) as total_luces, "
                + "SUM(CASE WHEN tipo='LUZ' AND estado='ON' THEN 1 ELSE 0 END) as luces_on, "
                + "SUM(CASE WHEN tipo IN ('TEMPERATURA','HUMEDAD','HUMO') THEN 1 ELSE 0 END) as sensores, "
                + "SUM(CASE WHEN tipo='ALARMA' THEN 1 ELSE 0 END) as alarmas, "
                + "COUNT(*) as total "
                + "FROM " + TABLE + " WHERE ID_Usuario=?";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                return resultados;
            }

            ps = cn.prepareStatement(sql);
            ps.setInt(1, Sesion.getIdUsuario());
            rs = ps.executeQuery();

            if (rs.next()) {
                resultados[0] = rs.getInt("total_luces");
                resultados[1] = rs.getInt("luces_on");
                resultados[2] = rs.getInt("sensores");
                resultados[3] = rs.getInt("alarmas");
                resultados[4] = rs.getInt("total");
            }

        } catch (Exception e) {
            System.err.println("❌ Error contando: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ignored) {
            }
        }
        return resultados;
    }

    public int contarLuces() {
        return contarTodos()[0];
    }

    public int contarLucesEncendidas() {
        return contarTodos()[1];
    }

    public int contarSensores() {
        return contarTodos()[2];
    }

    public int contarAlarmas() {
        return contarTodos()[3];
    }

    public int contarDispositivos() {
        return contarTodos()[4];
    }

    public boolean actualizarEstado(int id, String estado, int idUsuario) {
        String sql = "UPDATE " + TABLE + " SET estado=? WHERE id_dispositivo=? AND ID_Usuario=?";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, id);
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("❌ Error actualizando estado: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarBrillo(int id, int brillo, int idUsuario) {
        String sql = "UPDATE " + TABLE + " SET brillo=? WHERE id_dispositivo=? AND ID_Usuario=?";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, brillo);
            ps.setInt(2, id);
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("❌ Error actualizando brillo: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarDispositivo(Dispositivo d) {
        try {
            Connection cn = Conexion.conectar();
            System.out.println("Nombre: " + d.getNombre());
            System.out.println("Usuario: " + d.getIdUsuario());
            System.out.println("IP/URL: " + d.getIp());

            String sql = "INSERT INTO dispositivos "
                    + "(nombre,tipo,estado,brillo,ubicacion,descripcion,ip,pin,id_usuario)"
                    + " VALUES(?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, d.getNombre());
            ps.setString(2, d.getTipo());
            ps.setString(3, "OFF");
            ps.setInt(4, 100);
            ps.setString(5, d.getUbicacion());
            ps.setString(6, d.getDescripcion());

            // ✅ Guardar IP o URL completa
            ps.setString(7, d.getIp());

            ps.setInt(8, d.getPin());
            ps.setInt(9, d.getIdUsuario());

            System.out.println("Insertando dispositivo...");
            System.out.println("IP/URL guardada: " + d.getIp());

            ps.executeUpdate();
            ps.close();
            cn.close();
            return true;

        } catch (Exception e) {
            System.err.println("Error agregando dispositivo: " + e.getMessage());
            return false;
        }
    }

    public boolean activarSistema(boolean activo, int idUsuario) {
        String estado = activo ? "ON" : "OFF";
        String sql = "UPDATE " + TABLE + " SET estado=? WHERE tipo IN ('HUMO','TEMPERATURA','HUMEDAD','ALARMA', 'CAMARA') AND ID_Usuario=?";
        try (Connection cn = Conexion.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("❌ Error activando sistema: " + e.getMessage());
            return false;
        }
    }

    public int contarCamaras() {
        int total = 0;
        try {
            Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(
                    "SELECT COUNT(*) FROM dispositivos WHERE tipo='CAMARA' AND ID_Usuario=?"
            );
            ps.setInt(1, Sesion.getIdUsuario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            cn.close();
        } catch (Exception e) {
            System.err.println("❌ Error contando cámaras: " + e.getMessage());
        }
        return total;
    }

    public int contarCamarasEncendidas() {
        int total = 0;
        try {
            Connection cn = Conexion.conectar();
            PreparedStatement ps = cn.prepareStatement(
                    "SELECT COUNT(*) FROM dispositivos WHERE tipo='CAMARA' AND estado='ON' AND ID_Usuario=?"
            );
            ps.setInt(1, Sesion.getIdUsuario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            cn.close();
        } catch (Exception e) {
            System.err.println("❌ Error contando cámaras encendidas: " + e.getMessage());
        }
        return total;
    }

    public boolean eliminar(int id, int idUsuario) {
        boolean respuesta = false;
        Connection cn = null;
        PreparedStatement ps = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.");
                return false;
            }

            // ✅ Primero verificar que el dispositivo pertenece al usuario
            String sqlVerificar = "SELECT id_dispositivo FROM dispositivos WHERE id_dispositivo = ? AND ID_Usuario = ?";
            ps = cn.prepareStatement(sqlVerificar);
            ps.setInt(1, id);
            ps.setInt(2, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "El dispositivo no existe o no pertenece al usuario.");
                rs.close();
                ps.close();
                cn.close();
                return false;
            }
            rs.close();
            ps.close();

            // ✅ Eliminar el dispositivo
            String sqlEliminar = "DELETE FROM dispositivos WHERE id_dispositivo = ? AND ID_Usuario = ?";
            ps = cn.prepareStatement(sqlEliminar);
            ps.setInt(1, id);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            respuesta = filasAfectadas > 0;

            if (respuesta) {
                System.out.println("✅ Dispositivo eliminado correctamente (ID: " + id + ")");
            } else {
                System.out.println("❌ No se pudo eliminar el dispositivo (ID: " + id + ")");
            }

            ps.close();
            cn.close();

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar dispositivo: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar dispositivo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar recursos si no se cerraron
            try {
                if (ps != null) {
                    ps.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }

        return respuesta;
    }

    public boolean eliminarConVerificacion(int id, int idUsuario) {
        boolean respuesta = false;
        Connection cn = null;
        PreparedStatement ps = null;
        PreparedStatement psHistorial = null;

        try {
            cn = Conexion.conectar();
            if (cn == null) {
                JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.");
                return false;
            }

            // ✅ Verificar si el dispositivo tiene registros en el historial
            String sqlHistorial = "SELECT COUNT(*) FROM historial WHERE id_dispositivo = ?";
            psHistorial = cn.prepareStatement(sqlHistorial);
            psHistorial.setInt(1, id);
            ResultSet rsHistorial = psHistorial.executeQuery();
            rsHistorial.next();
            int countHistorial = rsHistorial.getInt(1);
            rsHistorial.close();
            psHistorial.close();

            if (countHistorial > 0) {
                System.out.println("📊 El dispositivo tiene " + countHistorial + " registros en el historial.");

                // Opcional: Preguntar si desea eliminar también el historial
                int confirm = JOptionPane.showConfirmDialog(null,
                        "El dispositivo tiene " + countHistorial + " registros en el historial.\n"
                        + "¿Deseas eliminarlo junto con sus registros?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return false;
                }

                // Eliminar registros del historial
                String sqlEliminarHistorial = "DELETE FROM historial WHERE id_dispositivo = ?";
                psHistorial = cn.prepareStatement(sqlEliminarHistorial);
                psHistorial.setInt(1, id);
                psHistorial.executeUpdate();
                psHistorial.close();
                System.out.println("✅ Registros de historial eliminados");
            }

            // ✅ Eliminar el dispositivo
            String sqlEliminar = "DELETE FROM dispositivos WHERE id_dispositivo = ? AND ID_Usuario = ?";
            ps = cn.prepareStatement(sqlEliminar);
            ps.setInt(1, id);
            ps.setInt(2, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            respuesta = filasAfectadas > 0;

            if (respuesta) {
                System.out.println("✅ Dispositivo eliminado correctamente (ID: " + id + ")");
            }

            ps.close();
            cn.close();

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar dispositivo: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar dispositivo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (psHistorial != null) {
                    psHistorial.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                System.err.println("Error cerrando recursos: " + e.getMessage());
            }
        }

        return respuesta;
    }
}
