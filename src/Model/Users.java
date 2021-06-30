package Model;

/**
 * Users class sets up variables for the users login id and name.
 */
public class Users {

        private static String userName;
        private static String userID;
        private static boolean userActive;

    /**
     * Constructor for Users.
     */
    public Users(String userName, String userID, boolean userActive) {
        this.userName = userName;
       this.userActive = userActive;
        this.userID = userID;
    }

    /**
     * @return the userName
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * @param userName the name to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the id
     */
    public static String getUserID() {
        return userID;
    }

    /**
     * @param userID the id to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return the activeStance of user
     */
    public static boolean isUserActive() {
        return userActive;
    }

    /**
     * @param userActive activity to set
     */
    public static void setUserActive(boolean userActive) {
        Users.userActive = userActive;
    }
}
