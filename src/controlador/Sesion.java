package controlador;

public class Sesion {
    private static Sesion instance;
    private int idUsuario;
    private String nombre;
    private String nickname;
    private String email;
    private String apellidos;
    private String password;

    private Sesion() {}

    public static Sesion getInstance() {
        if (instance == null) {
            instance = new Sesion();
        }
        return instance;
    }

    public static void iniciar(int id, String nom, String nick, String correo, String apellido, String pass) {
        Sesion s = getInstance();
        s.idUsuario = id;
        s.nombre = nom;
        s.nickname = nick;
        s.email = correo;
        s.apellidos = apellido;
        s.password = pass;
    }

    public static void cerrar() {
        instance = null;
    }

    public static int getIdUsuario() { return getInstance().idUsuario; }
    public static String getNombre() { return getInstance().nombre; }
    public static String getNickname() { return getInstance().nickname; }
    public static String getCorreo() { return getInstance().email; }
    public static String getApellido() { return getInstance().apellidos; }
    public static String getPass() { return getInstance().password; }
}