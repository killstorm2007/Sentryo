package modelo;

public class Dispositivo {

    private int id;

    private String nombre;

    private String tipo;

    private String estado;

    private int brillo;

    private String ubicacion;

    private String descripcion;

    private String ip;

    private int pin;
    
    private int idUsuario;

    public Dispositivo() {
    }

    public Dispositivo(int id, String nombre, String tipo,
            String estado, int brillo,
            String ubicacion,
            String descripcion,
            String ip,
            int pin,
            int idUsuario) {

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.brillo = brillo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.ip = ip;
        this.pin = pin;
        this.idUsuario = idUsuario;
    }

    //=========================
    // GETTERS
    //=========================

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public int getBrillo() {
        return brillo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIp() {
        return ip;
    }

    public int getPin() {
        return pin;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    //=========================
    // SETTERS
    //=========================

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setBrillo(int brillo) {
        this.brillo = brillo;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

}