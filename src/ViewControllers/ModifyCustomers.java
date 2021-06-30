package ViewControllers;

import Database.DatabaseConnector;
import Model.Appointments;
import Model.Customers;
import Model.Customers.*;
import Model.Users;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class modify customers initializes data into the table from the database and updates customer information in the database */
public class ModifyCustomers implements Initializable {

    /** Button that saves data. */
    @FXML
    private Button saveButton;

    /** Button that cancels all inserted data. */
    @FXML
    private Button cancelButton;

    /** Button that returns to the main screen. */
    @FXML
    private Button mainButton;

    /**Table view for customers*/
    @FXML
    private TableView<Customers> customersTableView;

    /**Cust ID column for customers*/
    @FXML
    private TableColumn<Customers, String> custIdCol;

    /**Cust Name column for customers*/
    @FXML
    private TableColumn<Customers, String> custNameCol;

    /**Address column for customers*/
    @FXML
    private TableColumn<Customers, String> custAddress1Col;

    /**Country column for customers*/
    @FXML
    private TableColumn<Customers, String> custCountryCol;

    /**Postal column for customers*/
    @FXML
    private TableColumn<Customers, String> custZipCol;

    /**Phone column for customers*/
    @FXML
    private TableColumn<Customers, String> custPhoneCol;

    /** Text that retrieves customer name. */
    @FXML
    private TextField nameField;

    /** Text that retrieves address. */
    @FXML
    private TextField address1Field;

    /** Text that retrieves zip code. */
    @FXML
    private TextField zipField;

    /** Text that retrieves phone. */
    @FXML
    private TextField phoneField;

    /** Combo box that sets country options */
    @FXML
    private ComboBox<String> countryComboBox;

    /** Combo box that sets division options */
    @FXML
    private ComboBox<String> divisionComboBox;

    /** Text that retrieves division id. */
    @FXML
    private TextField divisionID;

    /** Text that retrieves appt id. */
    @FXML
    private TextField idField;

    /** Text that retrieves search term. */
    @FXML
    private TextField searchField;

    /** Button for search. */
    @FXML
    private Button searchButton;

    /** Button for delete. */
    @FXML
    private Button deleteButton;

    /** Button for edit. */
    @FXML
    private Button editButton;



    /** Initialize retrieves data from database and places it into the tableview */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> apptLocation = FXCollections.observableArrayList("U.S","UK", "Canada");

        ResultSet rs = accessDB();
        ResultSet trs = accessDivisions();

        //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
        //needed to capitalize ID in appointments variable.

        custIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
        custAddress1Col.setCellValueFactory(new PropertyValueFactory<>("Address"));
        custZipCol.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));

        countryComboBox.setItems(apptLocation);

        ObservableList<Customers> custList = FXCollections.observableArrayList();

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                Integer customer_ID = rs.getInt("Customer_ID"); //parameter is the column name in the database
                String customer_name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String Country = rs.getString("Country");
                String zip = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");

                Customers tr = new Customers(customer_ID, customer_name, address, Country, zip, phone);

                custList.add(tr);
            }
            customersTableView.setItems(custList);
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    ObservableList<String> divisionList = FXCollections.observableArrayList();
    try{
        trs.beforeFirst();
        while (trs.next()) {
            String divisions = trs.getString("Division");

            divisionList.add(divisions);
        }

        divisionComboBox.setItems(divisionList);


    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    }

    /** Result set accessDB queries the data needed for the table view in customers */
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
                rs = stmt.executeQuery("SELECT * FROM customers, first_level_divisions, countries" +
                        " Where customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID");
                while (rs.next()) {

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

//        } catch (ClassNotFoundException | SQLException ex) {
//            ex.printStackTrace();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    /** Result set accessDivisionID queries the division id needed using the inputted division */
    public ResultSet accessDivisionID() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet drs = null;

        try {



            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String division = divisionComboBox.getValue();

                drs = stmt.executeQuery(String.format("SELECT Division_ID FROM first_level_divisions WHERE Division = '%s'", division));


                while (drs.next()) {
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return drs;
    }

    /** Result set accessDivisions queries the division for combo boxes using the input countries */
    public ResultSet accessDivisions() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet trs = null;

        try {



            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String country = countryComboBox.getValue();

                trs = stmt.executeQuery(String.format("SELECT * FROM first_level_divisions, countries WHERE first_level_divisions.COUNTRY_ID = countries.country_ID AND country = '%s'", country));
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

    /** Result set setDivisions queries the divisions again upon reselect of country combo option to prevent looping */
    public ResultSet setDivisions() {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";

        Connection conn;
        boolean res = false;
        ResultSet trs = null;

        try {

            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String customerID = idField.getText();

                trs = stmt.executeQuery(String.format("SELECT Division FROM first_level_divisions, customers WHERE first_level_divisions.Division_ID = customers.Division_ID AND customer_ID = '%s'", customerID));
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

    /** Main action sets to main GUI */
    @FXML
    void mainPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /** Edit action sets field text and combo to selected tableview data */
    @FXML
    void editPushed(ActionEvent event) throws IOException {

        if (customersTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nothing is selected.");
            alert.showAndWait();
            return;
        }

        ObservableList<Customers> selectedItems = customersTableView.getSelectionModel().getSelectedItems();

        String name = selectedItems.get(0).getCustomer_Name();
        int id = selectedItems.get(0).getCustomer_ID();
        String address = selectedItems.get(0).getAddress();
        String phone = selectedItems.get(0).getPhone();
        String postal = selectedItems.get(0).getPostal_Code();
        String country = selectedItems.get(0).getCountry();


        nameField.setText(name);
        idField.setText(String.valueOf(id));
        address1Field.setText(address);
        phoneField.setText(phone);
        zipField.setText(postal);
        if (country.endsWith("S")) {
            countryComboBox.getSelectionModel().select(0);
        } else if (country.endsWith("K")) {
            countryComboBox.getSelectionModel().select(1);
        } else if (country.endsWith("a")) {
            countryComboBox.getSelectionModel().select(2);
        }


        try {
            ResultSet rs = setDivisions();
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            rs.next();
                String division = rs.getString("Division");

                divisionComboBox.getSelectionModel().select(division);

        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);


        }


    }




    /** Delete customer queries from the database and runs an error if the customer has an associated appt */
    public static void deleteCustomer(Integer id) throws SQLException {
        boolean error = true;
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";
        ResultSet rs = null;
        Statement stmt;
        Connection conn ;
        conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);


            try {

                conn.createStatement().executeUpdate(String.format("DELETE FROM customers WHERE Customer_ID='%s'", id));

            } catch (Exception e) {
                System.out.println("Error deleting customer: " + e.getMessage());
                Alert f = new Alert(Alert.AlertType.ERROR);
                f.setTitle("Error");
                f.setContentText("Customer id " + id + " was not deleted due to a relation to an appointment. Please remove appointment first.");
                f.showAndWait();
                return;}

            if(error == true) {
                Alert e = new Alert(Alert.AlertType.INFORMATION);
                e.setTitle("Success");
                e.setContentText("Customer ID " + id + " was deleted successfully.");
                e.showAndWait();
            }
            }

    /** Delete action gets selected data from table and sends it to the query. It refreshes the GUI*/
    @FXML
    void deleteButtonPushed(ActionEvent event) throws IOException, SQLException {



            if (customersTableView.getSelectionModel().isEmpty()) {
                Alert e = new Alert(Alert.AlertType.ERROR);
                e.setTitle("No Selection");
                e.setContentText("There is no customer selected to delete.");
                e.showAndWait();
                return;
            }

            Customers customerToDelete = customersTableView.getSelectionModel().getSelectedItem();

        Integer deleteCustID = customerToDelete.getCustomer_ID();
        String deletedCustName = customerToDelete.getCustomer_Name();

            try {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Delete Customer?");
                alert.setHeaderText("Please Confirm...");
                alert.setContentText("Are you sure you want to delete customer #" + deleteCustID +  "?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    //User selected OK to delete
                    deleteCustomer(deleteCustID);

                    ResultSet rs = accessDB();

                    custIdCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
                    custCountryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
                    custAddress1Col.setCellValueFactory(new PropertyValueFactory<>("Address"));
                    custZipCol.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
                    custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
                    custNameCol.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));


                    ObservableList<Customers> custList = FXCollections.observableArrayList();

                    try {
                        rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
                        while (rs.next()) {
                            Integer customer_ID = rs.getInt("Customer_ID"); //parameter is the column name in the database
                            String customer_name = rs.getString("Customer_Name");
                            String address = rs.getString("Address");
                            String Country = ("Country");
                            String zip = rs.getString("Postal_Code");
                            String phone = rs.getString("Phone");

                            Customers tr = new Customers(customer_ID, customer_name, address, Country, zip, phone);

                            custList.add(tr);
                        }
                        customersTableView.setItems(custList);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    Parent parent = FXMLLoader.load(getClass().getResource("../ViewControllers/ModifyCustomers.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                }

            } catch (RuntimeException| IOException | SQLException e) {



            }
        }

    /** Search action queries the input search text to see if there is a match in the database */
    //Search button
    @FXML private void searchPushed (ActionEvent event) throws IOException, SQLException {
        ObservableList<Customers> custSched = FXCollections.observableArrayList();
        Connection con;
        searchField.getText();
        if (searchField.getText().isEmpty()) {
            try {
                custSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * FROM customers, first_level_divisions, countries" +
                        " Where customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID"));

                while (rs.next()) {
                    custSched.add(new Customers(rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("address"),
                            rs.getString("country"),
                            rs.getString("postal_code"),
                            rs.getString("phone")));


                }
                customersTableView.setItems(custSched);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Blank");
                alert.setContentText("Search field left blank. Returned to view all.");
                alert.showAndWait();
                return;

            } catch (SQLException e) {
                System.out.println("Something wrong with SQL: " + e.getMessage());
            }
        }
        else {
            try {
                custSched.clear();
                con = DatabaseConnector.getConnection();
                ResultSet rs = con.createStatement().executeQuery(String.format("SELECT * FROM customers, first_level_divisions, countries" +
                        " Where customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID AND customers.Customer_ID = " + searchField.getText()));

                while (rs.next()) {
                    custSched.add(new Customers(rs.getInt("Customer_ID"),
                            rs.getString("Customer_Name"),
                            rs.getString("address"),
                            rs.getString("country"),
                            rs.getString("postal_code"),
                            rs.getString("phone")));


                }
                customersTableView.setItems(custSched);

                if (custSched.isEmpty()) {


                    Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/ModifyCustomers.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    searchField.setText("");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Blank");
                    alert.setContentText("No customer found for search. Returned to view all.");
                    alert.showAndWait();

                }

            } catch (NullPointerException| NumberFormatException| SQLException e) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fields have incorrect values or are left blank. Please fix fields and try again.");
                alert.showAndWait();
                return;
            }
        }
    }

    /** Edit customer query that updates input into database for customers */
    public void editCustomer(Integer id, String custName, String address, Integer country, String zip, String phone ) throws SQLException {
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ08BQC";

        //  Database credentials
        final String DBUSER = "U08BQC";
        final String DBPASS = "53689240206";
        ResultSet rs = null;
        Statement stmt;
        Connection conn;
        conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

        customersTableView.getSelectionModel().getSelectedItems();



        try {

            id = Integer.parseInt(idField.getText());

            country = Integer.parseInt((divisionID.getText()));

            System.out.println(country);
            conn.createStatement().executeUpdate(String.format("UPDATE customers SET customer_Name = '%s', address = '%s', division_ID ='%s', postal_Code = '%s', phone = '%s', last_Update = '%s', last_Updated_By = '%s' WHERE customer_ID = '%s'", custName, address, country, zip, phone, LocalDateTime.now(), Users.getUserName(), id));


        } catch (Exception e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    /** Save action retrieves all input data in fields and sends it to the query. It refreshes the GUI. It checks for errors. */
    @FXML
    private void saveButtonPushed (ActionEvent event) throws IOException, SQLException {
        try {
            //Gather user-entered data from textfields
            Integer id = Integer.parseInt(idField.getText());
            String custName = nameField.getText();
            String address = address1Field.getText();
            Integer country = Integer.valueOf(divisionID.getText());
            System.out.println(country instanceof Integer);
            String zip = zipField.getText();
            String phone = phoneField.getText();


            if (idField.getText().isEmpty() || nameField.getText().isEmpty() || address1Field.getText().isEmpty() || divisionID.getText().isEmpty() || zipField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fields cannot be left blank before a save. Please fill in ALL fields.");
                alert.showAndWait();
                return;
            } else

                //Adds new appointment to the database
//                    if (checkYourself(editStartTime, editEndTime, editDate, custName, apptId) && insideBusinessHours(editStartTime, editEndTime, editDate)) {
                editCustomer(id, custName, address, country, zip, phone);

            //Refreshes screen, shows the new data in the table
            System.out.println("Edit successful! Refresh page.");
            Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/ModifyCustomers.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();


        } catch (NumberFormatException | IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Fields have incorrect values or are left blank. Please fix fields and try again.");
            alert.showAndWait();
            return;
        }
    }

    /** Combo country selection sets the division list when a country is selected. */
    @FXML
    private void comboCountrySelection (ActionEvent event) {



        ResultSet trs = accessDivisions();

        try {

            if (countryComboBox.getValue().toString() == "U.S") {
                divisionComboBox.setItems(null);
                divisionID.clear();
                ObservableList<String> divisionList = FXCollections.observableArrayList();
                try {
                    trs.beforeFirst();
                    while (trs.next()) {
                        String divisions = trs.getString("Division");

                        divisionList.add(divisions);
                    }

                    divisionComboBox.setItems(divisionList);


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }


            if (countryComboBox.getValue().toString() == "UK") {
                divisionComboBox.setItems(null);
                divisionID.clear();
                ObservableList<String> divisionList = FXCollections.observableArrayList();
                try {
                    trs.beforeFirst();
                    while (trs.next()) {
                        String divisions = trs.getString("Division");

                        divisionList.add(divisions);
                    }

                    divisionComboBox.setItems(divisionList);


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (countryComboBox.getValue().toString() == "Canada") {
                divisionComboBox.setItems(null);
                divisionID.clear();
                ObservableList<String> divisionList = FXCollections.observableArrayList();
                try {
                    trs.beforeFirst();
                    while (trs.next()) {
                        String divisions = trs.getString("Division");

                        divisionList.add(divisions);
                    }

                    divisionComboBox.setItems(divisionList);


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (NullPointerException e) {

            countryComboBox.getSelectionModel().select(-1);
            countryComboBox.setPromptText("Choose");
            divisionComboBox.getSelectionModel().select(-1);
            divisionID.clear();
        }
    }

    //java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0

    /** Combo division selection sets the division id when a division is selected. */
    @FXML
    private void divisionBoxSelected (ActionEvent event) throws SQLException {

        if (divisionComboBox.getValue() == null) {
            divisionID.clear();
        } else {
            ResultSet drs = accessDivisionID();


            ObservableList<String> divisionIdList = FXCollections.observableArrayList();


            try {
                drs.beforeFirst();
                while (drs.next()) {
                    String divisions = drs.getString("Division_ID");


                    divisionIdList.add(divisions);
                }


                if (divisionIdList.size() < 1) {
                    countryComboBox.getSelectionModel().select(-1);
                    countryComboBox.setPromptText("Choose");
                    divisionComboBox.getSelectionModel().select(-1);
                    divisionID.clear();
                } else {
                    String divId = divisionIdList.get(0);
                    divisionID.setText(String.valueOf(divId));
                }


            } catch (IndexOutOfBoundsException | NullPointerException | SQLException throwables) {
                throwables.printStackTrace();
                countryComboBox.getSelectionModel().select(-1);
                countryComboBox.setPromptText("Choose");
                divisionComboBox.getSelectionModel().select(-1);
                divisionID.clear();
            }

        }
    }



    /** Cancel action clears all input data in GUI. */
    @FXML
    private void cancelButton(ActionEvent event) throws IOException{
        nameField.clear();
        phoneField.clear();
        zipField.clear();
        address1Field.clear();
        idField.clear();
        divisionComboBox.getSelectionModel().select(-1);
        divisionID.clear();
        countryComboBox.getSelectionModel().select(-1);
        countryComboBox.setPromptText("Choose");


    }


}
