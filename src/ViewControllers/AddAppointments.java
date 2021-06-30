package ViewControllers;

import Database.DatabaseConnector;
import Model.Appointments;
import Model.Customers;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Add Appointments control class creates an interface to add appointment value input and save it to the database through sql queries.
 */
public class AddAppointments implements Initializable {

    /**Table view for customers*/
    @FXML
    private TableView<Customers> custNamesTableView;

    /**Id column for customers*/
    @FXML
    private TableColumn<Customers, String> custIdCol;

    /**Name column for customers*/
    @FXML
    private TableColumn<Customers, String> custNameCol;

    /**Text for search bar*/
    @FXML
    private TextField searchText;

    /**Text for appt id*/
    @FXML
    private TextField nextApptID;

    /**Button for search*/
    @FXML
    private Button searchButton;

    /**Button for selecting customer*/
    @FXML
    private Button selectCustomerButton;

    /**Text for id*/
    @FXML
    private TextField idField;

    /**Text for name*/
    @FXML
    private TextField nameText;

    /**Text for title*/
    @FXML
    private TextField titleText;

    /**Text for location*/
    @FXML
    private TextField locationText;

    /**Datepicker for date selection*/
    @FXML
    private DatePicker datePicker;

    /**Text for contact*/
    @FXML
    private TextField contactText;

    /**Text for description*/
    @FXML
    private TextField descriptionText;

    /**Text for user local time*/
    @FXML
    private TextField currentDateField;

    /**Combo box for user id*/
    @FXML
    private ComboBox userIDField;

    /**Combo box for user type*/
    @FXML
    private ComboBox<String> typeComboBox;

    /**Combo box for user contact*/
    @FXML
    private ComboBox<String> contactComboBox;

    /**Combo box for start*/
    @FXML
    private ComboBox<String> startTimeCombo;

    /**Combo box for user end*/
    @FXML
    private ComboBox<String> endTimeCombo;

    /**Button to add*/
    @FXML
    private Button addButton;

    /**Button to clear*/
    @FXML
    private Button clearButton;

    /**Table view for appointments*/
    @FXML
    private TableView<Appointments> apptTableView;

    /**Appt Id column for appointments*/
    @FXML
    private TableColumn<Appointments, String> apptIdCol;

    /**User Id column for appointments*/
    @FXML
    private TableColumn<Appointments, String> userIdCol;

    /**Title column for appointments*/
    @FXML
    private TableColumn<Appointments, String> titleCol;

    /**Description column for appointments*/
    @FXML
    private TableColumn<Appointments, String> descriptionCol;

    /**Location column for appointments*/
    @FXML
    private TableColumn<Appointments, String> locationCol;

    /**Date column for appointments*/
    @FXML
    private TableColumn<Appointments, String> dateColumn;

    /**Contact column for appointments*/
    @FXML
    private TableColumn<Appointments, String> contactCol;

    /**Type column for appointments*/
    @FXML
    private TableColumn<Appointments, String> typeCol;

    /**Start column for appointments*/
    @FXML
    private TableColumn<Appointments, String> startCol;

    /**End Time column for appointments*/
    @FXML
    private TableColumn<Appointments, String> endCol;

    /**Cust ID column for appointments*/
    @FXML
    private TableColumn<Appointments, String> cusIdCol;

    /**Cust Name column for appointments*/
    @FXML
    private TableColumn<Appointments, String> cusNameCol;

    /**Country column for appointments*/
    @FXML
    private TableColumn<Appointments, String> countryCol;

    MainScreen.LocalDateTimeConverter convert = (String dateTime) -> { //Lambda used to convert the UTC datetime from the database to the user's localdatetime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt =  LocalDateTime.parse(dateTime, format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return ldt;
    };

    /**List to set types of appointments*/
    ObservableList<String> apptType = FXCollections.observableArrayList("Planning Session", "De-Briefing");

    /**List to set contacts available*/
    ObservableList<String> contactNames = FXCollections.observableArrayList("Anika Costa", "Daniel Garcia" , "Li Lee" );

    /**List to set time options for start*/
    ObservableList<String> startTimes = FXCollections.observableArrayList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");

    /**List to set time options for end*/
    ObservableList<String> endTimes = FXCollections.observableArrayList( "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");

    /**Initializes the GUI and arranges data into appropriate columns on the tableview for both customers tableview and appointments table view.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResultSet rs = accessDB();
        ResultSet crs = accessDBcustomers();
        ResultSet trs = accessIds();



        //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
        //needed to capitalize ID in appointments variable.

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        cusIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("User_ID"));

        datePicker.setDayCellFactory(picker -> new DateCell() { //Lambda used to disable past dates and weekends from being selected
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
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

            userIDField.setItems(idList);

        } catch (IndexOutOfBoundsException | NullPointerException | SQLException throwables) {
            throwables.printStackTrace();
            userIDField.getSelectionModel().select(-1);

        }

        ObservableList<Appointments> apptList = FXCollections.observableArrayList();

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {

                LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                String fixedStartString = fixedStart.toString().substring(11,16);
                String fixedEndString = fixedEnd.toString().substring(11,16);

                Integer appointment_id = rs.getInt("Appointment_ID"); //parameter is the column name in the database
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                Integer contact = Integer.valueOf(rs.getString("Contact_ID"));
                String type = rs.getString("Type");
//                Time start = rs.getTime("Start");
//                Time end = rs.getTime("End");
                Date startDate = rs.getDate("StartDate");
                Integer customer_id = rs.getInt("Customer_ID");
                String customer_name = rs.getString("Customer_Name");
                String country = rs.getString("Country");
                Integer userid = rs.getInt("User_ID");
                Appointments tr = new Appointments(appointment_id, title, description, location, contact, type, fixedStartString, fixedEndString, startDate, customer_id, customer_name, country, userid);

                apptList.add(tr);
            }
            apptTableView.setItems(apptList);
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        custIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        nextApptID.setText(String.valueOf(apptList.size() + 1));

        ObservableList<Customers> custList = FXCollections.observableArrayList();
        try {
            crs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (crs.next()) {
                Integer customers_ID = crs.getInt("Customer_ID"); //parameter is the column name in the database
                String customers_name = crs.getString("Customer_Name");
                String address = crs.getString("Address");
                String Country = "Country";
                String zip = crs.getString("Postal_Code");
                String phone = crs.getString("Phone");

                Customers tr = new Customers(customers_ID, customers_name, null, null, null, null);

                custList.add(tr);
            }
            custNamesTableView.setItems(custList);
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**Result Set accessDB is used to query data for the initial table view for appointments.*/
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

//            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery("SELECT *, DATE(start) StartDate FROM appointments, customers, contacts, first_level_divisions, countries " +
                        "WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID");
                while (rs.next()) {

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return rs;
    }

//java.sql.SQLSyntaxErrorException: Not unique table/alias: 'customers'

    /**Result Set accessDBCustomers is used to query data for the initial table view for customers.*/
    public ResultSet accessDBcustomers() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet crs = null;

        try {


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                crs = stmt.executeQuery("SELECT * FROM customers");
                while (crs.next()) {

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            } catch (SQLException ex) {
                ex.printStackTrace();

          }
         return crs;
    }

    /**Action main pushed returns user to main GUI.*/
    @FXML
    void mainPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /**Action edit pushed populates related text fields and combo boxes with data from customer tableview.*/
    @FXML
    void editPushed(ActionEvent event) throws IOException {

        if(custNamesTableView.getSelectionModel().isEmpty()){

            Alert e = new Alert(Alert.AlertType.ERROR);
            e.setTitle("No Selection");
            e.setContentText("There is no customer selected to add.");
            e.showAndWait();
            return;
        }
        if(!custNamesTableView.getSelectionModel().isEmpty()){
        ObservableList<Customers> selectedItems  = custNamesTableView.getSelectionModel().getSelectedItems();

        String name = selectedItems.get(0).getCustomer_Name();
        int id = selectedItems.get(0).getCustomer_ID();

        nameText.setText(name);
        idField.setText(String.valueOf(id));

        }
    }

    /**Action search pushed queries the database for a matching input and returns matching data. If no data matches it returns to view all and triggers alert.*/
    @FXML private void searchPushed (ActionEvent event) throws IOException, SQLException {
        ObservableList<Customers> custSched = FXCollections.observableArrayList();
        Connection con;
        searchText.getText();
        if (searchText.getText().isEmpty()) {
            try {
                custSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * FROM customers"));

                while (rs.next()) {
                    custSched.add(new Customers(rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("address"),
                            null,
                            rs.getString("postal_code"),
                            rs.getString("phone")));


                }
                custNamesTableView.setItems(custSched);

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
                custSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * FROM customers WHERE customers.Customer_ID = " + searchText.getText()));

                while (rs.next()) {
                    custSched.add(new Customers(rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("address"),
                            null,
                            rs.getString("postal_code"),
                            rs.getString("phone")));


                }
                custNamesTableView.setItems(custSched);


            } catch (SQLException e) {
                System.out.println("Something wrong with SQL: " + e.getMessage());
            }
            if (custSched.isEmpty()) {


                Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/AddAppointments.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                searchText.setText("");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Blank");
                alert.setContentText("No customer ID found for search term " + searchText.getText() + ". Returned to view all.");
                alert.showAndWait();
            }
        }
    }

    /**Edit appointment queries the sql to inject user data inputted in the GUI to the database and logs time creation.*/
    @FXML
    public void editAppointment(Integer id, Integer custID, String description, Integer contact, String title, String type, String location, String startDateTime, String endDateTime, Integer userID ) throws SQLException {
            final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

            //  Database credentials
            final String DBUSER = "U08BQC";
            final String DBPASS = "53689240206";
            ResultSet rs = null;
            Statement stmt;
            Connection conn;
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            apptTableView.getSelectionModel().getSelectedItems();



            try {

                id = Integer.parseInt(nextApptID.getText());

                conn.createStatement().executeUpdate(String.format("INSERT INTO appointments (appointment_ID, customer_ID, description, contact_ID, title, type, location, start, end, user_ID)" +
                    "VALUES('%s', '%s', '%s', '%s',  '%s',  '%s',  '%s',  '%s',  '%s',  '%s')", id, custID, description, contact, title, type, location, startDateTime, endDateTime, userID ));

                conn.createStatement().executeUpdate(String.format("INSERT INTO appointments "
                                + "( create_Date, created_By, last_Update, last_Updated_By) " +
                                "VALUES ('%s', '%s', '%s', '%s')",
                        LocalDateTime.now(), Users.getUserName(), LocalDateTime.now(), Users.getUserName()));

            } catch (Exception e) {

                System.out.println("Error saving appointment: " + e.getMessage());




            }
        }

    /**Boolean isItOpen checks to make sure the times inputted are within the dates and business hours of 13:00-03:00 UTC)*/
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

    /**Action saveButton retrieves all data input on the GUI and sends it to editAppointment to be queried.*/
    @FXML
        private void saveButtonPushed (ActionEvent event) throws IOException, SQLException {


            boolean error = true;

            try {
                Integer customerId = Integer.parseInt(idField.getText());
                Integer appointmentId = Integer.parseInt(nextApptID.getText());
                //Gather user-entered data from textfields
                Integer id = Integer.parseInt(nextApptID.getText());
                Integer custID = Integer.parseInt(idField.getText());
                String editDescription = descriptionText.getText();
                Integer contact = Integer.parseInt(contactText.getText());
                String editType = typeComboBox.getValue().toString();
                String editLocation = locationText.getText();
                String editTitle = titleText.getText();
                Integer userID = Integer.valueOf(userIDField.getValue().toString());


                String startTime = startTimeCombo.getValue().toString();
                String startdate = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String endTime = endTimeCombo.getValue().toString();


                LocalDateTime localStart = ModifyAppointments.stringToLDT_UTC(startTime + ":00", startdate);


                LocalDateTime localEnd = ModifyAppointments.stringToLDT_UTC(endTime + ":00", startdate);
                String UTCstart = localStart.toString();
                String UTCend = localEnd.toString();


                int startCheck = startTimeCombo.getValue().compareTo(endTimeCombo.getValue().toString());

                if (idField.getText().isEmpty() || editDescription.isEmpty() || contactText.getText().isEmpty() || editTitle.isEmpty() || editType.isEmpty() || editLocation.isEmpty() || datePicker.getValue() == null || userIDField.getValue() == null) {
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
                } else if (!ModifyAppointments.checkForDuplicate(startTime, endTime, startdate)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment Interferes");
                    alert.setContentText("Appointment cannot be at the same time, with the same customers, and same users as another appointment.");
                    alert.showAndWait();
                    return;
                } else if (!ModifyAppointments.isItOpen(startTime, endTime, startdate)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Not Open");
                    alert.setContentText("Appointment must be within business hours of 8:00 am est to 10:00 pm est.");
                    alert.showAndWait();
                    return;

                } else if (isItOpen(startTime, endTime, startdate)) {


                    //Adds new appointment to the database
//                    if (checkYourself(editStartTime, editEndTime, editDate, custName, apptId) && insideBusinessHours(editStartTime, editEndTime, editDate)) {
                    editAppointment(id, custID, editDescription, contact, editTitle, editType, editLocation, UTCstart, UTCend, userID);


                    Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/AddAppointments.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();


                }
            }
            catch (NumberFormatException| IOException | SQLException e) {
                error = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fields have incorrect values or are left blank. Please fix fields and try again.");
                alert.showAndWait();
                return;
            }
            if (error = true){
                Integer customerId = Integer.parseInt(idField.getText());
                Integer appointmentId = Integer.parseInt(nextApptID.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Customer " + customerId + " saved to new appointment " + appointmentId + ".");
                alert.showAndWait();
                return;
            }
        }

    /**Action contact combo sets the id contact field to the appropriate id based upon selection of a contact.*/
    @FXML
    private void comboContactSelection (ActionEvent event) {
        if (contactComboBox.getValue() == "Anika Costa") {
            contactText.setText(String.valueOf(1));
        }
        if (contactComboBox.getValue() == "Daniel Garcia") {
            contactText.setText(String.valueOf(2));
        }
        if (contactComboBox.getValue() == "Li Lee") {
            contactText.setText(String.valueOf(3));
        }
    }

    /**Result set accessIds connects to the database and sql queries the user ids that are in the system in order to set the user id combo box later.*/
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

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return trs;
    }

    /**Action cancel button clears all data input on the GUI*/
    @FXML
    private void cancelButton(ActionEvent event) throws IOException{
        nameText.clear();
        idField.clear();
        titleText.clear();
        locationText.clear();
        contactText.clear();
        descriptionText.clear();
        datePicker.setValue(null);
        startTimeCombo.getSelectionModel().select(-1);
        endTimeCombo.getSelectionModel().select(-1);
        userIDField.getSelectionModel().select(-1);
        typeComboBox.getSelectionModel().select(-1);
        contactComboBox.getSelectionModel().select(-1);

    }
}
