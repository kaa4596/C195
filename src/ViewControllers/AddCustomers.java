package ViewControllers;

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
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Class add customers initializes the GUI to insert new customers into the database. It loads all data related into a tableview.*/
public class AddCustomers implements Initializable {

    /** Button that saves data. */
    @FXML
    private Button addButton;

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
    private TableColumn<Customers, ?> custIDCol;

    /**Cust Name column for customers*/
    @FXML
    private TableColumn<Customers, ?> custNameCol;

    /**Address column for customers*/
    @FXML
    private TableColumn<Customers, ?> custAddress1Col;

    /**Country column for customers*/
    @FXML
    private TableColumn<Customers, ?> countryCol;

    /**Postal column for customers*/
    @FXML
    private TableColumn<Customers, ?> custZipCol;

    /**Phone column for customers*/
    @FXML
    private TableColumn<Customers, ?> custPhoneCol;

    /** Text that retrieves customer name. */
    @FXML
    private TextField nameField;

    /** Text that retrieves address. */
    @FXML
    private TextField address1Field;

    /** Text that retrieves zip code. */
    @FXML
    private TextField zipField;

    /** Text that retrieves customer id. */
    @FXML
    private TextField custIdField;

    /** Text that retrieves phone. */
    @FXML
    private TextField phoneField;

    /** Text that retrieves division id. */
    @FXML
    private TextField divisionId;

    /** Combo box that retrieves country. */
    @FXML
    private ComboBox<String> countryComboBox;

    /** Combo box that retrieves divisions. */
    @FXML
    private ComboBox<String> divisionComboBox;

    /** List of countries to populate combo box. */
    ObservableList<String> apptLocation = FXCollections.observableArrayList("U.S","UK", "Canada");

    /** Initializes the tableview to populate with appropriate data. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResultSet rs = accessDB();

        //'setAppointment_id(javafx.beans.value.ObservableValue<java.lang.Integer>)' in 'Model.Appointments' cannot be applied to '(int)'
        //needed to capitalize ID in appointments variable.

        custIDCol.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("Country"));
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

            custIdField.setText(String.valueOf(custList.size() + 1));
        } catch (SQLException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /** Result Set accessDB queries the data needed for the tableview */
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

            System.out.println("Connecting to database accessDB...");
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
//
//        } catch (ClassNotFoundException | SQLException ex) {
//            ex.printStackTrace();
        }catch (SQLException ex) {
            ex.printStackTrace();
         ;
        }

        return rs;
    }

    /** Result set accessDivisions queries divisions from a selected country to populate combo box */
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


            System.out.println("Connecting to database accessDivisions...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);

            Statement stmt;

            try {

                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                System.out.println(countryComboBox.getValue());
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

    /** Result set accessDivisionsID queries division ids from a selected division to populate combo box */
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


            System.out.println("Connecting to database DivID...");
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

        }catch (IndexOutOfBoundsException| SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        return drs;
    }

    /** Action country combo sends inputted selections of combo box to accessDivisions to be queried and set division combo options. */
    @FXML
    private void comboCountrySelection (ActionEvent event) {



        ResultSet trs = accessDivisions();
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        try {

            if (countryComboBox.getValue().toString() == "U.S") {
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
        } catch (IndexOutOfBoundsException| NullPointerException e) {
            System.out.println(e);
            countryComboBox.getSelectionModel().select(-1);
            countryComboBox.setPromptText("Choose");
            divisionComboBox.getSelectionModel().select(-1);
            divisionId.clear();
        }
    }

    /** Action division combo sends inputted selections of combo box to accessDivisionIDs to be queried and set division id field. */
    @FXML
    private void divisionBoxSelected (ActionEvent event) throws SQLException {

        if (divisionComboBox.getValue() == null){
            divisionId.clear();

        }else {
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
                    divisionId.clear();
                } else {
                    String divId = divisionIdList.get(0);
                    divisionId.setText(String.valueOf(divId));
                }


            } catch (IndexOutOfBoundsException | NullPointerException | SQLException throwables) {
                throwables.printStackTrace();
                countryComboBox.getSelectionModel().select(-1);
                countryComboBox.setPromptText("Choose");
                divisionComboBox.getSelectionModel().select(-1);
                divisionId.clear();
            }
        }
    }

    /** Action main returns user to main GUI */
    @FXML
    void mainPushed(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/ViewControllers/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    /** Edit Customer is a query that inputs data into the customer database from the GUI */
    @FXML
    public void editCustomer(Integer custID, String custName, String address, String zip, String phone, Integer country) throws SQLException {
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

            custID = Integer.parseInt(custIdField.getText());

            conn.createStatement().executeUpdate(String.format("INSERT INTO customers (customer_ID, customer_Name, address, postal_Code, phone, division_ID)" +
                    "VALUES('%s',  '%s',  '%s',  '%s',  '%s',  '%s')", custID, custName, address, zip, phone, country ));

            conn.createStatement().executeUpdate(String.format("INSERT INTO customers "
                            + "( create_Date, created_By, last_Update, last_Updated_By) " +
                            "VALUES ('%s', '%s', '%s', '%s')",
                    LocalDateTime.now(), Users.getUserName(), LocalDateTime.now(), Users.getUserName()));

        } catch (Exception e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }


    /** Action save selects data inserted into fields in the GUI and sends it to be queried in editCustomers. */
    @FXML
    private void saveButtonPushed (ActionEvent event) throws IOException, SQLException {


        boolean error = true;

        try {
            Integer customerId = Integer.parseInt(custIdField.getText());
            String custName = nameField.getText();
            String address = address1Field.getText();
            String zip = zipField.getText();
            String phone = phoneField.getText();
            Integer country = Integer.parseInt(divisionId.getText());


            if (custIdField.getText().isEmpty() || custName.isEmpty() || address.isEmpty() || zip.isEmpty() || phone.isEmpty() || divisionId.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Fields cannot be left blank before a save. Please fill in ALL fields.");
                alert.showAndWait();
                return;
            } else
                editCustomer(customerId, custName, address, zip, phone, country);


            Parent parent = FXMLLoader.load(getClass().getResource("/ViewControllers/AddCustomers.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException| IOException | SQLException e) {
            error = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Fields have incorrect values or are left blank. Please fix fields and try again.");
            alert.showAndWait();
            return;
        }

        if (error = true){
            Integer customerId = Integer.parseInt(custIdField.getText());


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Customer " + customerId + " saved.");
            alert.showAndWait();
            return;
        }
    }

    /** Action cancel clears all data entered into GUI */
    @FXML
    private void cancelButton(ActionEvent event) throws IOException{
        nameField.clear();
        phoneField.clear();
        zipField.clear();
        address1Field.clear();
        divisionComboBox.getSelectionModel().select(-1);
        countryComboBox.getSelectionModel().select(-1);
        divisionId.clear();



    }
}
