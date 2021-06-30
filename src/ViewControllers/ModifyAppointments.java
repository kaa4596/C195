package ViewControllers;

import Database.DatabaseConnector;
import Model.Appointments;

import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.LocalDate.parse;

/**Class modify appointments initializes the GUI to update existing appointments into the database. It loads all data related into a tableview.*/
public class ModifyAppointments implements Initializable {

    /**
     * Text that retrieves appt id.
     */
    @FXML
    private TextField idField;

    /**
     * Text that retrieves cust id.
     */
    @FXML
    private TextField custIdField;

    /**
     * Text that retrieves title.
     */
    @FXML
    private TextField titleField;

    /**
     * Text that retrieves location.
     */
    @FXML
    private TextField locationField;

    /**
     * Datepicker selects date.
     */
    @FXML
    private DatePicker datePicker;

    /**
     * Text that retrieves contact.
     */
    @FXML
    private TextField contactField;

    /**
     * Text that retrieves description.
     */
    @FXML
    private TextField descriptionField;

    /**
     * Combo box that retrieves type.
     */
    @FXML
    private ComboBox<String> typeComboBox;

    /**
     * Combo box that retrieves start time.
     */
    @FXML
    private ComboBox<String> startTimeCombo;

    /**
     * Combo box that retrieves end time.
     */
    @FXML
    private ComboBox<String> endTimeCombo;

    /**
     * Text that shows current date.
     */
    @FXML
    private TextField currentDateField;

    /**
     * Button that saves
     */
    @FXML
    private Button saveButton;

    /**
     * Button that cancels
     */
    @FXML
    private Button cancelButton;

    /**
     * Tableview that shows appointments
     */
    @FXML
    private TableView<Appointments> appointmentTableView;

    /**
     * Appt ID column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> apptIdCol;

    /**
     * User ID column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> userIdCol;

    /**
     * Cust ID column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> custCol;

    /**
     * Title column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> titleCol;

    /**
     * Location column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> locCol;

    /**
     * Start column for appointments
     */
    @FXML
    private TableColumn<Appointments, Time> startCol;

    /**
     * End column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> endCol;

    /**
     * Date column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> dateColumn;

    /**
     * Type column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> typeCol;

    /**
     * Contact column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> contactCol;

    /**
     * Description column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> descCol;

    /**
     * Cust ID column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> custIdCol;

    /**
     * Country column for appointments
     */
    @FXML
    private TableColumn<Appointments, String> countryCol;

    /**
     * Combo box to show contacts available
     */
    @FXML
    private ComboBox<String> contactComboBox;

    /**
     * Button for main GUI
     */
    @FXML
    private Button mainButton;

    /**
     * Text for search term
     */
    @FXML
    private TextField searchField;

    /**
     * Combo box to show user ids available
     */
    @FXML
    private ComboBox userdIdField;

    /**
     * Button for search
     */
    @FXML
    private Button searchButton;

    /**
     * Button for loading selected data into fields
     */
    @FXML
    private Button editButton;

    /**
     * Button for deleting selected
     */
    @FXML
    private Button deleteButton;

    /**
     * Pulls lambda expression from main to convert UTC database time to lovcal date time in tableview
     */
    MainScreen.LocalDateTimeConverter convert = (String dateTime) -> { //Lambda used to convert the UTC datetime from the database to the user's localdatetime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(dateTime, format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return ldt;
    };

    /**
     * List to retrieve all types for combo box.
     */
    ObservableList<String> apptType = FXCollections.observableArrayList("Planning Session", "De-Briefing");

    /**
     * List to retrieve all contacts for combo box.
     */
    ObservableList<String> contactNames = FXCollections.observableArrayList("Anika Costa", "Daniel Garcia", "Li Lee");

    /**
     * List to retrieve all start times for combo box.
     */
    ObservableList<String> startTimes = FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");

    /**
     * List to retrieve all end times for combo box.
     */
    ObservableList<String> endTimes = FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");


    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

    //  Database credentials
    final String DBUSER = "U08BQC";
    final String DBPASS = "53689240206";
    ResultSet rs = null;
    Statement stmt;

    /**
     * List to retrieve all data from query.
     */
    ObservableList<Appointments> apptList = FXCollections.observableArrayList();
    Connection conn;

    /**
     * Initialize retrieves all data from database and places into tableview. Uses lambda local date time to convert UTC time to local
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResultSet trs = accessIds();
        ResultSet rs = accessDB();

        //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
        //needed to capitalize ID in appointments variable.

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        custCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("User_ID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));

        datePicker.setDayCellFactory(picker -> new DateCell() { //Lambda used to disable past dates and weekends from being selected
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                    setDisable(true);
            }
        });

        java.util.Date date = Calendar.getInstance().getTime();
        System.out.println(date);

        currentDateField.setText(String.valueOf(date));

        typeComboBox.setItems(apptType);
        contactComboBox.setItems(contactNames);
        startTimeCombo.setItems(startTimes);
        endTimeCombo.setItems(endTimes);

        ObservableList<String> idList = FXCollections.observableArrayList();

        try {
            trs.beforeFirst();
            while (trs.next()) {
                String ids = trs.getString("User_ID");

                idList.add(ids);
            }

            userdIdField.setItems(idList);

        } catch (IndexOutOfBoundsException | NullPointerException | SQLException throwables) {
            throwables.printStackTrace();
            userdIdField.getSelectionModel().select(-1);

        }

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {

                LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                String fixedStartString = fixedStart.toString().substring(11, 16);
                String fixedEndString = fixedEnd.toString().substring(11, 16);

                Integer appointment_id = rs.getInt("Appointment_ID"); //parameter is the column name in the database
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                Integer contact = Integer.valueOf(rs.getString("Contact_ID"));
                String type = rs.getString("Type");

//                Time start = rs.getTime("Start");
//                Time end = rs.getTime("End");
                Date startdate = rs.getDate("StartDate");
                Integer customer_id = rs.getInt("Customer_ID");
                String customer_name = rs.getString("Customer_Name");
                String country = rs.getString("Country");
                Integer user_ID = rs.getInt("User_ID");


                Appointments tr = new Appointments(appointment_id, title, description, location, contact, type, fixedStartString, fixedEndString, startdate, customer_id, customer_name, country, user_ID);

                apptList.add(tr);
            }
            appointmentTableView.setItems(apptList);
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Result set accessDB queries to retrieve all data for appointment tableview
     */
    public ResultSet accessDB() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet rs = null;

        try {


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery("SELECT *, DATE(start) StartDate FROM appointments, customers, contacts, first_level_divisions, countries" +
                        " WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID");
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    /**
     * Result set accessIds queries to retrieve user ids to place in combo box
     */
    public ResultSet accessIds() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet trs = null;

        try {


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                trs = stmt.executeQuery(String.format("SELECT User_ID FROM users ORDER BY User_ID"));
                while (trs.next()) {
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return trs;
    }

    /**
     * Main action returns to main GUI
     */
    @FXML
    void mainPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**
     * Delete appt query to remove selected data from database
     */
    public static void deleteAppointment(Integer id) throws SQLException {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";
        ResultSet rs = null;
        Statement stmt;
        Connection conn;
        conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        try {
            conn.createStatement().executeUpdate(String.format("DELETE FROM appointments WHERE Appointment_ID='%s'", id));

        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }
//java.lang.IllegalStateException: Cannot read from unreadable property Contact

    /**
     * Delete action retrieves selected data and passes it to the delete query. It reloads the page and sets of alerts for errors
     */
    @FXML
    private void deleteButtonPushed(ActionEvent event) throws IOException {

        boolean error = true;

        if (appointmentTableView.getSelectionModel().isEmpty()) {
            Alert e = new Alert(Alert.AlertType.ERROR);
            e.setTitle("No Selection");
            e.setContentText("There is no appointment selected to delete.");
            e.showAndWait();
            return;
        }

        Appointments appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();

        Integer deleteApptId = appointmentToDelete.getAppointment_ID();
        String custName = appointmentToDelete.getCustomer_Name();
        String typeAppt = appointmentToDelete.getType();

        try {


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Appointment?");
            alert.setHeaderText("Please Confirm...");
            alert.setContentText("Are you sure you want to delete Appointment ID #" + deleteApptId + " for customer " + custName + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                //User selected OK to delete
                deleteAppointment(deleteApptId);

                //Refreshes screen, shows updated table (post-delete)
                ResultSet rs = accessDB();

                //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
                //needed to capitalize ID in appointments variable.

                apptIdCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
                titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
                descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
                locCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
                contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
                typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
                startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
                endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
                custCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
                custIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
                userIdCol.setCellValueFactory(new PropertyValueFactory<>("User_ID"));

                ObservableList<Appointments> apptList = FXCollections.observableArrayList();

                try {
                    rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
                    while (rs.next()) {

                        LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                        LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                        String fixedStartString = fixedStart.toString().substring(11, 16);
                        String fixedEndString = fixedEnd.toString().substring(11, 16);

                        Integer appointment_id = rs.getInt("Appointment_ID"); //parameter is the column name in the database
                        String title = rs.getString("Title");
                        String description = rs.getString("Description");
                        String location = rs.getString("Location");
                        Integer contact = rs.getInt("Contact_ID");
                        String type = rs.getString("Type");
//                        Time start = rs.getTime("Start");
//                        Time end = rs.getTime("End");
                        Date startDate = rs.getDate("StartDate");
                        Integer customer_id = rs.getInt("Customer_ID");
                        String customer_name = rs.getString("Customer_Name");
                        String country = rs.getString("Country");
                        Integer user_ID = rs.getInt("User_ID");

                        Appointments tr = new Appointments(appointment_id, title, description, location, contact, type, fixedStartString, fixedEndString, startDate, customer_id, customer_name, country, user_ID);

                        apptList.add(tr);
                    }
                    appointmentTableView.setItems(apptList);


                } catch (SQLException ex) {
                    System.out.println(ex);
                    Alert e = new Alert(Alert.AlertType.ERROR);
                    e.setTitle("Error");
                    e.setContentText("Appointment ID " + deleteApptId + " type " + typeAppt + " was not deleted.");
                    e.showAndWait();

                    Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }

                Parent parent = FXMLLoader.load(getClass().getResource("../ViewControllers/ModifyAppointments.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            }

        } catch (RuntimeException | IOException | SQLException e) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, e);
            error = false;
            Alert f = new Alert(Alert.AlertType.ERROR);
            f.setTitle("Error");
            f.setContentText(deleteApptId + " " + typeAppt + " was not deleted.");
            f.showAndWait();
            return;


        }
        if (error == true) {
            Alert e = new Alert(Alert.AlertType.INFORMATION);
            e.setTitle("Success");
            e.setContentText("Appointment ID " + deleteApptId + ", " + typeAppt + ", was deleted successfully.");
            e.showAndWait();
        }
    }

    /**
     * Edit Appt query to update the data in the database.
     */
    public void editAppointment(Integer id, Integer custID, String description, String type, Integer contact, String location, String startDateTime, String endDateTime, Integer userID) throws SQLException {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";
        ResultSet rs = null;
        Statement stmt;
        Connection conn;
        conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

        appointmentTableView.getSelectionModel().getSelectedItems();


        try {

            id = Integer.parseInt(idField.getText());

            conn.createStatement().executeUpdate(String.format("UPDATE appointments SET Customer_ID = '%s', description = '%s', contact_ID ='%s', type = '%s', location = '%s', start = '%s', end = '%s', user_ID = '%s', last_Update = '%s', last_Updated_By = '%s' WHERE Appointment_ID='%s'", custID, description, contact, type, location, startDateTime, endDateTime, userID, LocalDateTime.now(), Users.getUserName(), id));

        } catch (Exception e) {
            System.out.println("Error saving appointment: " + e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid customer ID, please try again with a valid customer ID.");
            alert.showAndWait();
            return;


        }
    }

    /**
     * Appointment warning sets of an alert if the user has an appointment within 15 min
     */
    public static boolean appointmentWarning() {

        try {
//            1. Change startTime  (00:00:00) , endTime (00:00:00), and date (YYYY-MM-DD) to "YYYY-MM-DD 00:00:00"

           String start = LocalDateTime.now(ZoneOffset.UTC).toString();
            String startstring = start.substring(0,19);
           String plus15 = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15).toString();
            String plusstring = plus15.substring(0,19);

            String startArray[] = startstring.split("T");
            String plusArray[] = plusstring.split("T");


            String UTCstart = startArray[0] +  " " + startArray[1];

            String UTCend = plusArray[0] + " " + plusArray[1];

            String localStart = (UTCstart.replace("T", " "));
            String localEnd = (UTCend.replace("T", " "));
            System.out.println(UTCstart);
            System.out.println(UTCend);
            System.out.println(Users.getUserID());

            ResultSet earliestAppt = DatabaseConnector.conn.createStatement().executeQuery(String.format("SELECT * "
                            + "FROM appointments a, users u WHERE a.user_ID=u.user_ID "
                            + "AND a.user_ID='%s' AND start BETWEEN '%s' AND '%s'",
                    Users.getUserID(), localStart, localEnd));
            earliestAppt.next();

            System.out.println(earliestAppt.getString("start"));
            String name = earliestAppt.getString("customer_ID");
            String id = earliestAppt.getString("appointment_ID");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Appointment");
                alert.setHeaderText("Appointment Coming Up!");
                alert.setContentText("You have an appointment ID " + id + " within the next 15 minutes with customer ID "+ name +"!");
                alert.showAndWait();


            return true;
        } catch (SQLException e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Appointment");
            alert.setHeaderText("No recent Appointments");
            alert.setContentText("You have no appointments coming up.");
            alert.showAndWait();
            System.out.println("You don't have an appointment soon.");
            return false;
        }
    }




    /**Boolean check for duplicate uses a query to see if an appointment overlaps with another*/
    public static boolean checkForDuplicate(String startTime, String endTime, String dateGiven) {
        try {

//            1. Change startTime  (00:00:00) , endTime (00:00:00), and date (YYYY-MM-DD) to "YYYY-MM-DD 00:00:00"
            LocalDateTime localStart = stringToLDT_UTC(startTime + ":00", dateGiven);
            LocalDateTime localEnd = stringToLDT_UTC(endTime + ":00", dateGiven);
            String UTCstart = localStart.toString();
            String UTCend = localEnd.toString();
            System.out.println(UTCend);

            //3. Look for overlap
            ResultSet getOverlap = DatabaseConnector.conn.createStatement().executeQuery(String.format(
                    "SELECT start, end, customer_Name FROM appointments a, customers c WHERE a.customer_ID = c.customer_ID " +
                            "AND ('%s' >= start AND '%s' <= end) " +
                            "OR ('%s' <= start AND '%s' >= end) " +
                            "OR ('%s' <= start AND '%s' >= start) " +
                            "OR ('%s' <= end AND '%s' >= end)", UTCstart, UTCend, UTCstart, UTCend, UTCstart, UTCstart, UTCend, UTCend));
            getOverlap.next();
            System.out.println("APPOINTMENT Interferes: " + getOverlap.getString("customer_Name"));
            return false;
        } catch (SQLException e) {
            System.out.println("There are no appointment conflicts.");
            return true;
        }
    }

    /**Boolean is it open checks if the attempted input for start and end time is within business hours (13:00-03:00 UTC)*/
    //Checks to make sure the start and end times the user selected are within business hours (13:00-03:00 UTC)
    public static boolean isItOpen(String startTime, String endTime, String date) {


        //convert start and end times selected to UTC equivalents
        LocalDateTime localStart = ModifyAppointments.stringToLDT_UTC(startTime + ":00", date);
        LocalDateTime localEnd = ModifyAppointments.stringToLDT_UTC(endTime + ":00", date);
//        String UTCstart = localStart.toString().substring(11,16);
//        String UTCend = localEnd.toString().substring(11,16);



        //Compare by using LocalTime datatypes
//        LocalTime enteredStart = LocalTime.parse(UTCstart + ":00");
//        LocalTime enteredEnd = LocalTime.parse(UTCend + ":00");
        LocalDate today = LocalDate.parse(date);
        LocalDate tomorrow = LocalDate.parse(date).plusDays(1);
        LocalTime open = LocalTime.parse("12:59:00");
        LocalTime close = LocalTime.parse("03:01:00");

        LocalDateTime openingHour = LocalDateTime.of(today, open);
        LocalDateTime closingHour = LocalDateTime.of(tomorrow, close);


//        System.out.println("start" + enteredStart);
//        System.out.println("end" + enteredEnd);

        Boolean startTimeAllowed = localStart.isAfter(openingHour);
        Boolean endTimeAllowed = localEnd.isBefore(closingHour);


        if (startTimeAllowed && endTimeAllowed ) {
            return true;
        }
        else {
            return false;
        }
    }

    /**Local date time formatter to change UTC to local date time*/
    public static LocalDateTime stringToLDT_UTC (String time, String date){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(date + " " + time, format).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        return ldt;


    }

    /**Save pushed action retrieves all data inseted into text fields/combo boxes converts it to data formats and sends it through a query. Alerts are checked based on booleans set for overlap and business hours.*/
        @FXML private void saveButtonPushed (ActionEvent event) throws IOException, SQLException, ParseException {


            try {
                //Gather user-entered data from textfields
                Integer id = Integer.parseInt(idField.getText());
                Integer custID = Integer.parseInt(custIdField.getText());
                String editDescription = descriptionField.getText();
                Integer contact = Integer.parseInt(contactField.getText());
                String editType = typeComboBox.getValue().toString();
                String editContact = contactComboBox.getValue().toString();
                String editLocation = locationField.getText();

                String startTime = startTimeCombo.getValue().toString();
                String startdate = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String endTime = endTimeCombo.getValue().toString();


                LocalDateTime localStart = stringToLDT_UTC(startTime + ":00", startdate);


                LocalDateTime localEnd = stringToLDT_UTC(endTime + ":00", startdate);
                String UTCstart = localStart.toString();
                String UTCend = localEnd.toString();



                System.out.println("Converted date and start time (UTC): " + UTCstart);


                int userID = Integer.parseInt(userdIdField.getValue().toString());


                int startCheck = localStart.compareTo(localEnd);

                if (idField.getText().isEmpty() || custIdField.getText().isEmpty() || editDescription.isEmpty() || contactField.getText().isEmpty() || editType.isEmpty() || editContact.isEmpty() || editLocation.isEmpty() || datePicker.getValue() == null || userdIdField.getValue() == null || endTimeCombo.getValue() == null || startTimeCombo.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Fields cannot be left blank before a save. Please fill in ALL fields.");
                    alert.showAndWait();
                    return;

                } else if (startCheck == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Start date cannot equal end date.");
                    alert.showAndWait();
                    return;

                } else if (startCheck > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Start date cannot be after an end date.");
                    alert.showAndWait();
                    return;

                } else if (!checkForDuplicate(startTime, endTime, startdate)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment Interferes");
                alert.setContentText("Appointment cannot be at the same time, with the same customers, and same users as another appointment.");
                alert.showAndWait();
                return;

                }else if(!isItOpen(startTime, endTime, startdate)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Not Open");
                    alert.setContentText("Appointment must be within business hours of 8:00 am est to 10:00 pm est.");
                    alert.showAndWait();
                    return;

                }else if (isItOpen(startTime, endTime, startdate)) {
//                    //Adds new appointment to the database
                    editAppointment(id, custID, editDescription, editType, contact, editLocation, UTCstart, UTCend, userID);

                    //Refreshes screen, shows the new data in the table
                    System.out.println("Edit successful! Refresh page.");
                    Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/ModifyAppointments.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Edited");
                    alert.setContentText("Appointment " + id + " was edited successfully and does not interfere with other appointments.");
                    alert.showAndWait();
                    return;
                }
                    } catch(NumberFormatException | IOException | SQLException e){

                        System.out.println(e);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Fields have incorrect values or are left blank. Please fix fields and try again.");
                        alert.showAndWait();
                        return;

                 }
             }


//Caused by: java.lang.NullPointerException
//	at ViewControllers.ModifyAppointments.editPushed(ModifyAppointments.java:439)

    /**Action edit pushed retrieves selected data from tableview and inserts it into text fields and combo boxes.*/
    @FXML
    void editPushed(ActionEvent event) throws IOException {

    if (appointmentTableView.getSelectionModel().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Nothing is selected.");
        alert.showAndWait();
        return;

    }
    else{
            String startlocaldate;
            LocalDate endlocaldate;

            ObservableList<Appointments> selectedItems = appointmentTableView.getSelectionModel().getSelectedItems();

            int appId = selectedItems.get(0).getAppointment_ID();
            int custId = selectedItems.get(0).getCustomer_ID();
            String title = selectedItems.get(0).getTitle();
            String location = selectedItems.get(0).getLocation();
            String start = selectedItems.get(0).getStart();
            String end =  selectedItems.get(0).getEnd();
            Date startDate = selectedItems.get(0).getStartDate();
            startlocaldate = startDate.toString();
            String apptType = selectedItems.get(0).getType();
            int userID = selectedItems.get(0).getUser_ID();


            idField.setText(String.valueOf(appId));
            Integer contact = selectedItems.get(0).getContact_ID();
            String description = selectedItems.get(0).getDescription();
            custIdField.setText(String.valueOf(custId));
            titleField.setText(title);
            locationField.setText(location);
            contactField.setText(String.valueOf(contact));
            descriptionField.setText(description);
            datePicker.setValue(LocalDate.parse(startlocaldate));

            userdIdField.getSelectionModel().select(selectedItems.get(0).getUser_ID());

             String a = String.valueOf(selectedItems.get(0).getStart());
            startTimeCombo.getSelectionModel().select(a.substring(0, a.length()- 0 ));
            String b = String.valueOf(selectedItems.get(0).getEnd());
            endTimeCombo.getSelectionModel().select(b.substring(0, b.length() - 0));
            typeComboBox.getSelectionModel().select(selectedItems.get(0).getType());


            if(contact == 1){
                contactComboBox.getSelectionModel().select(0);
            } else if (contact == 2){
                contactComboBox.getSelectionModel().select(1);
            }else if (contact == 3){
                contactComboBox.getSelectionModel().select(2);
            }
        }

    }
//Caused by: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax;
// check the manual that corresponds to your MySQL server version for the right syntax to use near
// '[id=searchField, styleClass=text-input text-field]' at line 1

//Something wrong with SQL: Not unique table/alias: 'appointments' fixed with space

    /**Search action checks inserted text through the database for a match. If a match is found it is displayed.*/
    //Search button
    @FXML private void searchPushed (ActionEvent event) throws IOException, SQLException {
        ObservableList<Appointments> appointmentSched = FXCollections.observableArrayList();
        Connection con;
        searchField.getText();
        if (searchField.getText().isEmpty()) {
            try {
                appointmentSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * , DATE(start) StartDate FROM appointments, customers, contacts, first_level_divisions, countries" +
                        " WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID AND appointments.Customer_ID = customers.Customer_ID"));

                while (rs.next()) {

                    LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                    LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                    String fixedStartString = fixedStart.toString().substring(11,16);
                    String fixedEndString = fixedEnd.toString().substring(11,16);

                    appointmentSched.add(new Appointments(rs.getInt("Appointment_ID"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getInt("contact_ID"),
                            rs.getString("type"),
                            fixedStartString,
                            fixedEndString,
//                            rs.getTime("start"),
//                            rs.getTime("end"),
                            rs.getDate("StartDate"),
                            rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("country"),
                            rs.getInt("user_ID")));

                }
                appointmentTableView.setItems(appointmentSched);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Blank");
                alert.setContentText("Search field left blank. Returned to view all.");
                alert.showAndWait();
                return;

            } catch (SQLException e) {
                System.out.println("Something wrong with SQL: " + e.getMessage());
            }
        } else {
            try {
                appointmentSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * , DATE(start) StartDate FROM appointments, customers, contacts, first_level_divisions, countries" +
                        " WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID AND appointments.Customer_ID = " + searchField.getText()));

                while (rs.next()) {

                    LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                    LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                    String fixedStartString = fixedStart.toString().substring(11,16);
                    String fixedEndString = fixedEnd.toString().substring(11,16);

                    appointmentSched.add(new Appointments(rs.getInt("Appointment_ID"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getInt("contact_ID"),
                            rs.getString("type"),
                            fixedStartString,
                            fixedEndString,
//                            rs.getTime("start"),
//                            rs.getTime("end"),
                            rs.getDate("StartDate"),
                            rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("country"),
                             rs.getInt("user_ID")));

                }

                appointmentTableView.setItems(appointmentSched);
            } catch (SQLException e) {
                System.out.println("Something wrong with SQL: " + e.getMessage());
            }

                if (appointmentSched.isEmpty()) {


                    Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/ModifyAppointments.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    searchField.setText("");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Blank");
                    alert.setContentText("No appointment found for search. Returned to view all.");
                    alert.showAndWait();
                    }
                }
            }

    /** Combo box selection sets the contact id field to the matching id */
        @FXML
        private void comboContactSelection (ActionEvent event){
                if(contactComboBox.getValue() == "Anika Costa"){
                    contactField.setText(String.valueOf(1));
                }
                if(contactComboBox.getValue() == "Daniel Garcia"){
                    contactField.setText(String.valueOf(2));
                }
                if(contactComboBox.getValue() == "Li Lee"){
                    contactField.setText(String.valueOf(3));
                }

        }

    /** Cancel event clears all entered data */
        @FXML
    private void cancelButton(ActionEvent event) throws IOException{
                custIdField.clear();
                idField.clear();
                titleField.clear();
                locationField.clear();
                contactField.clear();
                descriptionField.clear();
                datePicker.setValue(null);
                typeComboBox.getSelectionModel().select(-1);
                contactComboBox.getSelectionModel().select(-1);
                userdIdField.getSelectionModel().select(-1);


            }

    }


