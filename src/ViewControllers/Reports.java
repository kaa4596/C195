package ViewControllers;

import Database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** Reports class initializes data into the text field on command. Generates different data based on query request.  */
public class Reports implements Initializable{

    /**Choice box to choose report time*/
    @FXML
    private ChoiceBox<String> reportChoiceBox;

    /**Button to generate reports*/
    @FXML
    private Button generateReportButton;

    /**Text area for reports*/
    @FXML
    private TextArea textAreaForReports;

    /**Button to reset*/
    @FXML
    private Button resetButton;

    /**Button to main*/
    @FXML
    private Button mainButton;

    /**List to collect all reports*/
    static ObservableList<String> reports = FXCollections.observableArrayList();

    /**Main action returns to main GUI*/
    @FXML
    void mainPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**Report push action calls a report query based on selection*/
    @FXML private void reportPushed (ActionEvent event) throws IOException, SQLException {
        textAreaForReports.clear();

        String chosenReport = reportChoiceBox.getValue().toString();

        switch (chosenReport) {
            case "Appointment Types by Month":
                textAreaForReports.setText(reportApptTypesByMonth());
                break;
            case "Schedule for Contacts":
                textAreaForReports.setText(reportContacts());
                break;
            case "Appointments per Year":
                textAreaForReports.setText(reportApptsPerYear());
                break;
            default:
                break;

        }
    }

    /**List to add reports based on selection and clear before next call.*/
        public static ObservableList<String> getReports () {
            reports.removeAll(reports); //prevents types from copying themselves to the list
            reports.add("Appointment Types by Month");
            reports.add("Schedule for Contacts");
            reports.add("Appointments per Year");
            return reports;
        }


    /**Report by month queries data to retrieve appointments by type and by month*/
        public String reportApptTypesByMonth () throws SQLException {
            Connection conn = DatabaseConnector.getConnection();
            try {


                StringBuilder text = new StringBuilder();
                text.append("Month _________ # of each Type  \n");


                ResultSet rs = conn.createStatement().executeQuery(String.format("SELECT MONTHNAME(start) as Start, type, COUNT(*) AS Count " +
                        "FROM appointments "+
                        "GROUP BY MONTHNAME(start), type"));
                while (rs.next()) {
                   text.append(rs.getString("Start") + " _________ " + rs.getString("Count") + "   " + rs.getString("type") + "\n");
                }

                return text.toString();

            } catch (SQLException e) {
                System.out.println("Error getting report: " + e.getMessage());
                return "Error with Report";
            }


        }

    /**Initializes the data into the choice box*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        reportChoiceBox.setItems(getReports());
    }



    /**Report by contacts queries data to retrieve contacts and all of their scheduled data*/
    public String reportContacts() throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        try {


            StringBuilder text = new StringBuilder();
            text.append("Contact Schedule\n" +
                    "Contact ___ Apt ID ___ Title ___ Description ___ Start ___ End ___ Cust ID \n");


            ResultSet rs = conn.createStatement().executeQuery(String.format("SELECT contact_Name, appointment_ID, title, type, description, start, end, customer_ID "
                    + "FROM appointments a, contacts c WHERE a.contact_ID = c.contact_ID ORDER BY start;"));


            while (rs.next()) {

                text.append(rs.getString("contact_Name") + " ___ " + rs.getString("appointment_ID") + " ___ " + rs.getString("title") + " ___ " + rs.getString("type") + " ___ " + rs.getString("description")+ " ___ " + rs.getString("start") + " ___ " + rs.getString("end") + " ___ " + rs.getString("customer_ID") + "\n");
//                String date = getSchedule.getString("date");
//                String name = getSchedule.getString("customerName");
//                String title = getSchedule.getString("title");
//                String description = getSchedule.getString("description");
//                String type = getSchedule.getString("type");
//                String location = getSchedule.getString("location");

                text.toString();
            }


            return text.toString();

        } catch (SQLException e) {
            System.out.println("Error getting report: " + e.getMessage());
            return "Didn't work";
        }
    }

    /**Report by year queries data to retrieve the number of appointments by year*/
    private String reportApptsPerYear() throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        try {


            StringBuilder text = new StringBuilder();
            text.append("Year _____ # of Appointments \n");


            ResultSet getApptsPerYear = conn.createStatement().executeQuery(String.format("SELECT YEAR(start) Year, COUNT(*) Count "
                    + "FROM appointments GROUP BY YEAR(start)"));

            //Transform the data by extracting only the numerical month MM (like 01)
            while (getApptsPerYear.next()) {
                String year = getApptsPerYear.getString("Year");
                String count = getApptsPerYear.getString("Count");

                text.append(year + " _____ " + count + "\n");
            }

            return text.toString();

        } catch (SQLException e) {
            System.out.println("Error getting report: " + e.getMessage());
            return "Didn't work";
        }
    }


    /**Clear action clears all data entered in GUI*/
    //reset everything on Reports screen
    @FXML private void clearPushed (ActionEvent event) throws IOException {
        textAreaForReports.clear();
       reportChoiceBox.getSelectionModel().select(-1);

    }



}

