package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.util.Date;
/**
 * Appointments class sets up variables that dictate the input of appointment data.
 */
public class Appointments {
//    private static ObservableList<Appointments> allAppointments;
    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
    private Integer appointment_ID;
    private String title;
    private String description;
    private String location;
    private Integer contact_ID;
    private String type;
    private String start;
    private String end;
    private Integer customer_ID;
    private String customer_Name;
    private String country;
    private Integer user_ID;
    private Date startDate;

    /**
     * Constructor class for appointments.
     */
    public Appointments(Integer appointment_ID, String title, String description, String location, Integer contact_ID, String type, String start, String end, Date startDate, Integer customer_ID, String customer_Name, String country, Integer user_ID) {
        this.appointment_ID = appointment_ID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact_ID = contact_ID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.startDate = startDate;
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.country = country;
        this.user_ID = user_ID;

    }

    /**
     * @return the start
     */
    public String getStart() {
        return start;
    }

    /**
     * @param start the time to set
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end the time to set
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @return the stardate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the userID
     */
    public Integer getUser_ID() {
        return user_ID;
    }

    /**
     * @param user_ID the userid to set
     */
    public void setUser_ID(Integer user_ID) {
        this.user_ID = user_ID;
    }

    /**
     * @return the appointmentID
     */
    public Integer getAppointment_ID() {
        return appointment_ID;
    }

    /**
     * @param appointment_Id the apptid to set
     */
    public void setAppointment_ID(Integer appointment_Id) {
        this.appointment_ID = appointment_Id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the title
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contactId
     */
    public Integer getContact_ID() {
        return contact_ID;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact_ID(Integer contact) {
        this.contact_ID = contact_ID;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the customerID
     */
    public Integer getCustomer_ID() {
        return customer_ID;
    }

    /**
     * @param customer_Id the id to set
     */
    public void setCustomer_ID(Integer customer_Id) {
        this.customer_ID = customer_Id;
    }

    /**
     * @return the customerName
     */
    public String getCustomer_Name() {
        return customer_Name;
    }

    /**
     * @param customer_Name the customername to set
     */
    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
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
     * Observable list used to call to function a list of all appointments in database.
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }


}


