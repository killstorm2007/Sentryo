package modelo;

import java.sql.Timestamp;

public class Historial {

    private int id;
    private int idUsuario;
    private Integer idDispositivo;

    private String tipoEvento;
    private String titulo;
    private String descripcion;

    private Timestamp fecha;

    public int getIdHistorial() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdDispositivo() {
        return idDispositivo;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setIdHistorial(int id) {
        this.id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdDispositivo(Integer idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    

}
