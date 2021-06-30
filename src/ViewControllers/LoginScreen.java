package ViewControllers;

import Database.DatabaseConnector;
import Model.Users;
import com.sun.javafx.runtime.VersionInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import  javafx.scene.control.TextField;
import javafx.scene.control.Button;


import javafx.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Login screen initializes log in GUI sets and retrieves text input and alters interface for French settings.
 */
public class LoginScreen implements Initializable {

    /** Label that sets the title */
    @FXML
    private Label appointmentManagementSystemLabel;

    /** Label that sets username label */
    @FXML
    private Label usernameLabel;

    /** Label that sets password label */
    @FXML
    private Label passwordLabel;

    /** Text for username */
    @FXML
    private TextField usernameField;

    /** Text for password */
    @FXML
    private PasswordField passwordField;

    /** Button for login */
    @FXML
    private Button loginButton;

    /** Button for reset */
    @FXML
    private Button resetButton;

    /** Button for exit */
    @FXML
    private Button exitButton;


    //Define alert message Strings

    /** String in order to change title of errors depending on user set language */
    private String errorTitle;

    /** String in order to change header of errors for missing content depending on user set language */
    private String errorHeaderMissing;

    /** String in order to change header of errors for incorrect information depending on user set language */
    private String errorHeaderIncorrect;

    /** String in order to change content of errors for missing parts depending on user set language */
    private String errorContentMissing;

    /** String in order to change content of errors for incorrect information depending on user set language */
    private String errorContentIncorrect;

    /** Boolean login used to query system to check if username and password match */
    public static boolean login(String usernameInput, String passwordInput) {
        try {
            DatabaseConnector.startConnection();
            PreparedStatement pst = DatabaseConnector.conn.prepareStatement("SELECT * FROM users WHERE User_Name=? AND Password=?");
            pst.setString(1, usernameInput);
            pst.setString(2, passwordInput);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**  Action reset clears all entered data in GUI */
    @FXML
    private void resetPushed(ActionEvent event) throws IOException {
            usernameField.clear();
            passwordField.clear();
            }

    /** Button login submits data entered into GUI and checks to see if it matches query. If a match is found it loads the main GUI. THis function also logs any attempts made to login. */
    //Validate username and password, move to main screen if credentials are correct
    @FXML private void handleLoginButton (ActionEvent event) throws IOException {

        //Get text for username and password fields
        String usernameInput, passwordInput;
        usernameInput = usernameField.getText();
        passwordInput = passwordField.getText();

        //Validate whether or not the username and password are correct
        if (login(usernameInput, passwordInput)) {

            //Validation successful, record timestamp as a log in the logs.txt file
            String filename = "src/Main/login_activity.txt";
            FileWriter fWriter = new FileWriter(filename, true);
            PrintWriter outputFile = new PrintWriter(fWriter);
            outputFile.println(usernameField.getText() + " logged in on " + LocalDateTime.now());
            System.out.println(usernameField.getText() + " logged in on " + LocalDateTime.now());
            outputFile.close();

            //Create User, you will use this info later
            Connection con;
            try {
                con = DatabaseConnector.getConnection();
                ResultSet getUserInfo = con.createStatement().executeQuery(String.format("SELECT user_ID, user_Name FROM users WHERE user_Name='%s'", usernameInput));
                getUserInfo.next();
                Users currentUser = new Users(getUserInfo.getString("user_Name"), getUserInfo.getString("user_ID"), true);
                System.out.println("Current user_Id: " + Users.getUserID() + " user_Name: " + Users.getUserName());

            } catch (SQLException ex) {
                Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Change screens
            System.out.println("Login Successful! Login Screen -> Main Screen");
            Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            //Alerts the user if they logged in within 15 minutes of a scheduled appointment
            if (ModifyAppointments.appointmentWarning()) {
                System.out.println("User alerted");

            }
            else {
                System.out.println("User not alerted of any appointment soon.");

            }
        }
        else {
            //Username or password field(s) left blank
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle(errorTitle);
                alert.setHeaderText(errorHeaderMissing);
                alert.setContentText(errorContentMissing);
                alert.showAndWait();

                String filename = "src/Main/login_activity.txt";
                FileWriter fWriter = new FileWriter(filename, true);
                PrintWriter outputFile = new PrintWriter(fWriter);
                outputFile.println(usernameField.getText() + " failed with a blank field login on " + LocalDateTime.now());
                System.out.println(usernameField.getText() + " failed with a blank field login on " + LocalDateTime.now());
                outputFile.close();
            }
            //Username or password incorrect
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle(errorTitle);
                alert.setHeaderText(errorHeaderIncorrect);
                alert.setContentText(errorContentIncorrect);
                alert.showAndWait();

                String filename = "src/Main/login_activity.txt";
                FileWriter fWriter = new FileWriter(filename, true);
                PrintWriter outputFile = new PrintWriter(fWriter);
                outputFile.println(usernameField.getText() + " failed with an incorrect login on " + LocalDateTime.now());
                System.out.println(usernameField.getText() + " failed with an incorrect login on " + LocalDateTime.now());
                outputFile.close();
            }
        }
    }


    /** Initialize language changes on GUI depending on user resource bundle language. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Establish language (English or Spanish) for login screen
        try {
            rb = ResourceBundle.getBundle("LanguageCheck/lang", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                appointmentManagementSystemLabel.setText(rb.getString("title"));
                usernameLabel.setText(rb.getString("username"));
                passwordLabel.setText(rb.getString("password"));
                loginButton.setText(rb.getString("loginButton"));
                resetButton.setText(rb.getString("resetButton"));
                errorTitle = rb.getString("errorTitle");
                errorHeaderMissing = rb.getString("errorHeaderMissing");
                errorHeaderIncorrect = rb.getString("errorHeaderIncorrect");
                errorContentMissing= rb.getString("errorContentMissing");
                errorContentIncorrect = rb.getString("errorContentIncorrect");
                exitButton.setText(rb.getString("exitButton"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**Action exit push that ends app.*/
    @FXML
    void exitPushed(ActionEvent event) {

        System.exit(0);

    }
}
