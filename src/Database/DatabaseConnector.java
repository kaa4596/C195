package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**Database connector class establishes connection between the app and the database.*/
public class DatabaseConnector {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/WJ08BQC";
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    private static final String USERNAME = "U08BQC";
    private static final String PASSWORD = "53689240206";
    private static final String jdbcDRIVER = "com.mysql.jdbc.Driver";
    public static Connection conn = null;

    //Caused by: java.lang.ClassNotFoundException: com.mysql.jdbc.Driver
    //	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:602)
    //	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
    //	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
    //	at java.base/java.lang.Class.forName0(Native Method)
    //	at java.base/java.lang.Class.forName(Class.java:340)
    //	at DatabaseConnector.DatabaseConnection.startConnection(DatabaseConnection.java:23)
    //	at Main.Main.main(Main.java:28)

    /**Start connection initiates a connection to the database by inputting username and password.*/
    public static Connection startConnection() throws ClassNotFoundException, SQLException{

        try {
//        Class.forName(jdbcDRIVER);
        conn = DriverManager.getConnection(jdbcURL, USERNAME, PASSWORD);
//        } catch (ClassNotFoundException e) {
//            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**@return  Gets connection*/
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, USERNAME, PASSWORD);
    }

    /**Close connection ends the connection to the database*/
    public static void closeConnection() throws SQLException {
        try {
            conn.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



