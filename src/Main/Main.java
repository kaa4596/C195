package Main;

import Database.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * @author Kaitlynn Abshire
 * JAVADOCS in File JavaDoc
 * Class main begins the application and directs the screen to the login GUI. It establishes a database connection as well.
 */

public class Main extends Application {
/**
* Start initiates connection to the login screen GUI and sets title of application.
*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../ViewControllers/LoginScreen.fxml"));
        primaryStage.setTitle("Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Void main initiates connection to the database and closes connection on exit.
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DatabaseConnector.startConnection();
        launch(args);
        DatabaseConnector.closeConnection();
    }
}
