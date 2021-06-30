package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * Customers class sets up private variable for customer information received.
 */

public class Customers {
    private ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
    private int customer_ID;
    private String customer_Name;
    private String address;
    private String postal_Code;
    private String phone;
    private String country;

    /**
     * Constructor for customers.
     */
    public Customers(int customer_ID, String customer_Name, String address, String country, String postal_Code, String phone) {
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.address = address;
        this.postal_Code = postal_Code;
        this.phone = phone;
        this.country = country;
    }

    /**
     * @return the customer id
     */
    public int getCustomer_ID() {
        return customer_ID;
    }

    /**
     * @param customer_ID the customer id to set
     */
    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    /**
     * @return the customer name
     */
    public String getCustomer_Name() {
        return customer_Name;
    }

    /**
     * @param customer_Name the name to set
     */
    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    /**
     * @return the adress
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the postal
     */
    public String getPostal_Code() {
        return postal_Code;
    }

    /**
     * @param postal_Code the zip to set
     */
    public void setPostal_Code(String postal_Code) {
        this.postal_Code = postal_Code;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Observable list used to call to function a list of all customers in database.
     */
    public ObservableList<Customers> getAllCustomers() {
        return allCustomers;
    }
}
