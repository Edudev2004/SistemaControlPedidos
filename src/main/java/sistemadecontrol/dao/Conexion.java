package sistemadecontrol.dao;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

// Clase encargada de conectarse a la base de datos de Supabase
public class Conexion {

    // Leemos las variables del archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    private static final String HOST = dotenv.get("DB_HOST");
    private static final String PORT = dotenv.get("DB_PORT");
    private static final String DB_NAME = dotenv.get("DB_NAME");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Armamos la URL de conexion JDBC con SSL para Supabase
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?sslmode=require";

    // Metodo para obtener una conexion a la base de datos
    public static Connection getConnection() {
        Connection conexion = null;
        try {
            // Cargar el driver JDBC
            Class.forName("org.postgresql.Driver");

            // Establecer conexion con los parametros del .env
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a Supabase");
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}
