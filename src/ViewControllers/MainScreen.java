package ViewControllers;

import Database.DatabaseConnector;
import Model.Appointments;
import Model.Customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Main screen class contains the main GUI, links to all other GUIS, and displays all appointments in database through a tableview. */
public class MainScreen implements Initializable {

    /** Datepicker to select view */
    @FXML
    private DatePicker datePicker;

    /** Tab toggle to view all */
    @FXML
    private ToggleButton allTab;

    /** Tab toggle to view weekly */
    @FXML
    private ToggleButton weeklyTab;

    /** Tab toggle to view monthly */
    @FXML
    private ToggleButton monthlyTab;

    /** Tableview for main table */
    @FXML
    private TableView<Appointments> mainTableView;

    /** Appt Id column for appointments */
    @FXML
    private TableColumn<Appointments, Integer> apptIdCol;

    /** Customer Name column for appointments */
    @FXML
    private TableColumn<Appointments, String> nameCol;

    /** Title column for appointments */
    @FXML
    private TableColumn<Appointments, String> titleCol;

    /** Description column for appointments */
    @FXML
    private TableColumn<Appointments, String> descCol;

    /** Location column for appointments */
    @FXML
    private TableColumn<Appointments, String> locationCol;

    /** Date column for appointments */
    @FXML
    private TableColumn<Appointments, Date> dateColumn;

    /** Contact column for appointments */
    @FXML
    private TableColumn<Appointments, String> contactCol;

    /** Type column for appointments */
    @FXML
    private TableColumn<Appointments, String> typeCol;

    /** Start time column for appointments */
    @FXML
    private TableColumn<Appointments, LocalDateTime> startCol;

    /** End Time column for appointments */
    @FXML
    private TableColumn<Appointments, LocalDateTime> endCol;

    /** Cust Id column for appointments */
    @FXML
    private TableColumn<Customers, Integer> custidCol;

    /** Country column for appointments */
    @FXML
    private TableColumn<Customers, String> countryCol;

    /** Text for current local user date */
    @FXML
    private TextField currentDateField;

    /** Button to go to add customer screen */
    @FXML
    private Button addCustomerButton;

    /** Button to go to modify customer screen */
    @FXML
    private Button modifyCustomerButton;

    /** Button to go to add appointment screen */
    @FXML
    private Button addAppointmentButton;

    /** Button to go to modify appointment screen */
    @FXML
    private Button modifyAppointmentButton;

    /** Button to go to reports screen */
    @FXML
    private Button reportsButton;

    /** Button to exit */
    @FXML
    private Button exitButton;

    /** Toggle group to switch view between weekly, monthly, and all */
    @FXML
    private ToggleGroup calendarView;

    /** Boolean group to switch weekly*/
    private boolean isWeekly;

    /** Boolean group to switch monthly*/
    private boolean isMonthly;

    /** Local date time converter switches input time to UTC for the system and converts UTC to local time for tableviews*/
    LocalDateTimeConverter convert = (String dateTime) -> {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt =  LocalDateTime.parse(dateTime, format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return ldt;
    };

    /** Intercface created to incorporate a lambda expression to change UTC times in database to local time in table views*/
    public interface LocalDateTimeConverter {

        LocalDateTime stringToLocalDateTime(String dateTime);

    }

    /** Initializes data into tableview and uses local date time lambda to change utc time to local time in tableview. */
    //java.sql.SQLException: Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY.
    //   java.sql.SQLException: Column 'Contact' not found.
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        LocalDateTimeConverter convert = (String dateTime) -> { //Lambda used to convert the UTC datetime from the database to the user's localdatetime
//            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//            LocalDateTime ldt =  LocalDateTime.parse(dateTime, format).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
//            return ldt;
//        };

        ResultSet rs = accessDB();

        //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
        //needed to capitalize ID in appointments variable.

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("Contact_ID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        custidCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));


        java.util.Date date = Calendar.getInstance().getTime();
        System.out.println(date);

        currentDateField.setText(String.valueOf(date));


        ObservableList<Appointments> apptList = FXCollections.observableArrayList();

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {



                Integer appointment_id = rs.getInt("Appointment_ID"); //parameter is the column name in the database
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                Integer contact = rs.getInt("Contact_ID");
                String type = rs.getString("Type");

                LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                String fixedStartString = fixedStart.toString().substring(11,16);
                String fixedEndString = fixedEnd.toString().substring(11,16);

//                Time start = rs.getTime("Start");
//                Time end = rs.getTime("End");
                Date startDate = rs.getDate("StartDate");
                Integer customer_id = rs.getInt("Customer_ID");
                String customer_name = rs.getString("Customer_Name");
                String country = rs.getString("Country");
                Integer userID = rs.getInt("User_ID");
                Appointments tr = new Appointments(appointment_id, title, description, location, contact, type, fixedStartString, fixedEndString, startDate, customer_id, customer_name, country, userID);

                apptList.add(tr);
            }
            mainTableView.setItems(apptList);
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /** Result set access DB  queries the data needed to place in main table view.*/
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
                rs = stmt.executeQuery("SELECT appointment_ID, title, description, location, contacts.contact_ID, type, start, end, DATE(start) StartDate, appointments.customer_ID, customer_Name, country, user_ID"
                        + " FROM appointments, customers, contacts, first_level_divisions, countries " +
                        "WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID");
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }


            }catch(
            SQLException ex)

            {
              ex.printStackTrace();
          }
        return rs;
}

    /** List to gather data retrieved from query.*/
    ObservableList<Appointments> appointmentSched = FXCollections.observableArrayList();

    /** Void to set up if statements to distinguish monthly, weekly, all*/
    @FXML private void handleDatePicked (ActionEvent event) throws IOException {
        if (isWeekly) {
            viewWeeklySelected(); //View by Week is previously selected
        }
        else if (isMonthly) {
            viewMonthlySelected(); //View by Month is previously selected
        }
        else {
            viewAllSelected(); //View All is previously selected
        }
    }

//java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual
// that corresponds to your MySQL server version for the right syntax to use near 'AND appointments.Contact_ID = contacts.Contact_ID

    /** View all sets the tableview to display all data that pertains to the section*/
    public void viewAllSelected() {
        calendarView = new ToggleGroup();
        this.allTab.setToggleGroup(calendarView);
        this.weeklyTab.setToggleGroup(calendarView);
        this.monthlyTab.setToggleGroup(calendarView);

        isWeekly = false;
        isMonthly = false;

        Connection con;
        try {
            appointmentSched.clear();
            con = DatabaseConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT Appointment_ID, title, description, location, contacts.contact_ID, type, start, end, DATE(start) StartDate, appointments.Customer_ID, Customer_Name, country, user_ID" +
                    " FROM appointments, customers, contacts, first_level_divisions, countries" +
                    " WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID " +
                    " ORDER BY start;");
            while (rs.next()) {

                LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                String fixedStartString = fixedStart.toString().substring(11,16);
                String fixedEndString = fixedEnd.toString().substring(11,16);

                appointmentSched.add(new Appointments(rs.getInt("Appointment_ID"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getInt("Contact_ID"),
                        rs.getString("type"),
                        fixedStartString,
                        fixedEndString,
                        rs.getDate("StartDate"),
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("country"),
                        rs.getInt("User_ID")));


            }
           mainTableView.setItems(appointmentSched);

        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /** View weekly sets the tableview to display weekly data that pertains to the section in that week */
    void viewWeeklySelected() {

        if(datePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Date picker cannot be left blank to search by weekly.");
            alert.showAndWait();
            allTab.setSelected(true);
            return;
        }

        calendarView = new ToggleGroup();
        this.allTab.setToggleGroup(calendarView);
        this.weeklyTab.setToggleGroup(calendarView);
        this.monthlyTab.setToggleGroup(calendarView);

        isWeekly = true;
        isMonthly = false;

        LocalDate datePicked = datePicker.getValue();
        String yearPicked = datePicker.getValue().toString().substring(0,4);
        WeekFields weekFields = WeekFields.of(Locale.US);
        int weekNumber = datePicked.get(weekFields.weekOfWeekBasedYear());
        String weekString = Integer.toString(weekNumber);

        System.out.println("week number: " + weekString + " year: " + yearPicked);

        Connection con;
        try {
            appointmentSched.clear();
            con = DatabaseConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT Appointment_ID, title, description, location, contacts.contact_ID, type, start, end, DATE(start) StartDate, appointments.Customer_ID, Customer_Name, country, user_ID " +
                    "FROM appointments, customers, contacts, first_level_divisions, countries WHERE WEEK(DATE(start))+1 = '%s' AND YEAR(start) = '%s' AND appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID ORDER BY start", weekString, yearPicked));
            while (rs.next()) {

                LocalDateTime fixedStart = convert.stringToLocalDateTime(rs.getString("start"));
                LocalDateTime fixedEnd = convert.stringToLocalDateTime(rs.getString("end"));
                String fixedStartString = fixedStart.toString().substring(11,16);
                String fixedEndString = fixedEnd.toString().substring(11,16);

                appointmentSched.add(new Appointments(rs.getInt("Appointment_ID"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getInt("Contact_ID"),
                        rs.getString("type"),
                        fixedStartString,
                        fixedEndString,
//                        rs.getTime("start"),
//                        rs.getTime("end"),
                        rs.getDate("StartDate"),
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("country"),
                        rs.getInt("User_ID")));

            }
                mainTableView.setItems(appointmentSched);

            if (appointmentSched.isEmpty()) ;

        } catch (SQLException e) {
            System.out.println("Something wrong with SQL: " + e.getMessage());
        }
    }

//Caused by: java.lang.NullPointerException
//	at ViewControllers.MainScreen.viewMonthlySelected(MainScreen.java:279)
//	at ViewControllers.MainScreen.monthlyTab(MainScreen.java:321)
//
//Something wrong with SQL: Column 'Customer_Id' in field list is ambiguous

    /** View monthly sets the tableview to display monthly data that pertains to the section in that month */
    void viewMonthlySelected() {

        if(datePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Date picker cannot be left blank to search by month.");
            alert.showAndWait();
            allTab.setSelected(true);
            return;
        }

        calendarView = new ToggleGroup();
        this.allTab.setToggleGroup(calendarView);
        this.weeklyTab.setToggleGroup(calendarView);
        this.monthlyTab.setToggleGroup(calendarView);

        isWeekly = false;
        isMonthly = true;

        LocalDate datePicked = datePicker.getValue();
        System.out.println("date" + datePicked);
        String monthPicked = datePicked.toString().substring(5,7);
        System.out.println(monthPicked);
        String yearPicked = datePicked.toString().substring(0,4);

        System.out.println("month number: " + monthPicked + " year: " + yearPicked);

        Connection con;
        try {
            appointmentSched.clear();
            con = DatabaseConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery(String.format("SELECT Appointment_ID, title, description, location, contacts.contact_ID, type, start, end, DATE(start) StartDate, appointments.Customer_Id, Customer_Name, country, user_ID " +
                    "FROM appointments, customers, contacts, first_level_divisions, countries WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.Contact_ID = contacts.Contact_ID AND customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID AND MONTH(start) = '%s' AND YEAR(start) " +
                    "ORDER BY start ", monthPicked, yearPicked));

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
//                    rs.getTime("start"),
//                    rs.getTime("end"),
                    rs.getDate("StartDate"),
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("country"),
                    rs.getInt("User_ID")));
            }
            mainTableView.setItems(appointmentSched);

            if (appointmentSched.isEmpty());
//                noResults("month and year");

        } catch (SQLException e) {
            System.out.println("Something wrong with SQL: " + e.getMessage());
        }

    }

    /** Action weekly tab is selected switches to weekly view */
    //Weekly Radio Button - changes the calendar label and calendar data
    @FXML private void weeklyTab (ActionEvent event) throws IOException { viewWeeklySelected(); }

    /** Action monthly tab is selected switches to monthly view */
    //Monthly Radio Button - changes the calendar label and calender data
    @FXML private void monthlyTab (ActionEvent event) throws IOException { viewMonthlySelected(); }

    /** Action all tab is selected switches to all view */
    //View All Radio Button - changes the calendar label and calendar data
    @FXML private void allTab (ActionEvent event) throws IOException { viewAllSelected(); }

    /** Action add customer pushed changes to add customer GUI */
    @FXML
    void addCustomerPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/AddCustomers.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /** Action mod customer pushed changes to mod customer GUI */
    @FXML
    void modifyCustomerPushed (ActionEvent event) throws IOException {


        Stage stage;
        Parent root;
        stage = (Stage) modifyCustomerButton.getScene().getWindow();
//Load up OTHER FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewControllers/ModifyCustomers.fxml"));

        root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** Action add appointment pushed changes to add appointment GUI */
    @FXML
    void addAppointmentPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/AddAppointments.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /** Action mod appointments pushed changes to mod appointment GUI */
    @FXML
    void modifyAppointmentsPushed(ActionEvent event) throws IOException {

//        if (mainTableView.getSelectionModel().isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("There is no appointment selected to modify.");
//            alert.showAndWait();
//            return;
//        }

        Stage stage;
        Parent root;
        stage = (Stage) modifyCustomerButton.getScene().getWindow();
//Load up OTHER FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewControllers/ModifyAppointments.fxml"));

        root = loader.load();
//        ModifyCustomers controller = loader.getController();
//        Customers person = mainTableView.getSelectionModel().getSelectedItem();
//        int index = mainTableView.getSelectionModel().getSelectedIndex();

//        if (person != null) {
//            controller.setP(part, index);
//        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** Action report pushed changes to report GUI */
    @FXML
    void reportsPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/Reports.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /** Action exit closes app */
    @FXML
    void exitPushed(ActionEvent event) throws IOException {

        System.exit(0);

    }

}


