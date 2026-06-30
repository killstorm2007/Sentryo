package modelo;

public class Usuario {

    private int idusuario;
    private String nombre;
    private String apellido;
    private String nickname;
    private String correo;
    private String password;
    private String rol;

    public Usuario() {
        this.idusuario = 0;
        this.nombre = "";
        this.apellido = "";
        this.nickname = "";
        this.password = "";
        this.correo = "";
    }

    public int getID() {
        return idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }
    public String getRol() {
        return rol;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

}
