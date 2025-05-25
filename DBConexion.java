package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de manejar la conexión a la base de datos MySQL.
 * Utiliza parámetros estáticos para establecer la conexión con el servidor.
 *
 * @author Javier Lopez
 */
public class DBConexion {

    private static final String URL = "jdbc:mysql://localhost:3306/taqueria_db";
    private static final String USER = "root"; // Cambiar si tienes otro usuario
    private static final String PASSWORD = "220105"; // Cambiar si tienes contraseña

    /**
     * Retorna una conexión activa con la base de datos.
     *
     * @return una instancia {@link Connection} a la base de datos MySQL
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
