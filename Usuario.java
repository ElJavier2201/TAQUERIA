package modelo;

/**
 * Clase que representa un usuario del sistema de ventas.
 * Contiene información sobre el ID, nombre de usuario, contraseña y rol.
 *
 * @author Javier Lopez
 */
public class Usuario {
    private int id;
    private String nombreUsuario;
    private String contraseña;
    private String rol;

    /**
     * Constructor completo para la clase Usuario.
     * @param id el ID del usuario
     * @param nombreUsuario el nombre de usuario
     * @param contraseña la contraseña del usuario
     * @param rol el rol asignado (admin, mesero, cajero, cocinero)
     */
    public Usuario(int id, String nombreUsuario, String contraseña, String rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public Usuario() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
