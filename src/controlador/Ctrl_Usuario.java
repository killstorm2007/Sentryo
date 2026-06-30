package controlador;

import conexion.Conexion;
import modelo.Usuario;
import java.sql.*;
import javax.swing.JOptionPane;

public class Ctrl_Usuario {
    private static final String TABLE = "Usuarios";

    private Connection getConnection() throws SQLException {
        return Conexion.conectar();
    }

    public Usuario loginUser(Usuario objeto) {
        String sql = "SELECT * FROM " + TABLE + " WHERE nickname=? AND password=?";
        try (Connection cn = getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, objeto.getNickname());
            ps.setString(2, MD5.encriptar(objeto.getPassword()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE ID_Usuario=?";
        try (Connection cn = getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener usuario: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarUsuario(Usuario objeto) {
        String sql = "INSERT INTO " + TABLE + " (nombre, apellidos, nickname, password, correo) VALUES (?,?,?,?,?)";
        try (Connection cn = getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, objeto.getNombre());
            ps.setString(2, objeto.getApellido());
            ps.setString(3, objeto.getNickname());
            ps.setString(4, MD5.encriptar(objeto.getPassword()));
            ps.setString(5, objeto.getCorreo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage());
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdusuario(rs.getInt("ID_Usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellidos"));
        u.setNickname(rs.getString("nickname"));
        u.setCorreo(rs.getString("correo"));
        u.setPassword(rs.getString("password"));
        return u;
    }
}