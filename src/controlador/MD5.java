package controlador;

import java.security.MessageDigest;

public class MD5 {
    public static String encriptar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            System.err.println("Error encriptando MD5: " + e.getMessage());
            return null;
        }
    }
}