package nas509.groceries.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all registered users, keeps track of logged in user, provides some
 * services
 */

public class UserManager {

    /** singleton design */
    private static final UserManager _instance = new UserManager();
    public static UserManager getInstance() { return _instance; }

    /** Hashmap containing the usernames as keys and passwords as values
     *  Used for checking usernames to passwords */
    private Map<String, String> _usersPasswords;

    /** Hashmap containing the usernames as keys and User objects as values
     *  Used for checking if a User/Username is already registered */
    private Map<String, User> _users;

    /** The currently logged in user, changes the appActivity text */
    private User loggedInUser;
    private String loggedInUsername;


    DatabaseReference databaseUsers;

    /** Private constructor for singleton */
    private UserManager() {
        _usersPasswords = new HashMap<>();
        _users = new HashMap<>();
        loggedInUser = null;
        loggedInUsername = "";
        databaseUsers = getDatabase();
    }

    public DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("users");
    }

    public void retrieveUsers() {
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _usersPasswords.clear();
                _users.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    _users.put(user.getUsername(), user);
                    _usersPasswords.put(user.getUsername(), user.getPassword());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Map<String, User> getUsers() {
        return _users;
    }

    public String getNewId() {
        return databaseUsers.push().getKey();
    }

    /** Getter and setter */
    public void setLoggedInUser(User user) {
        loggedInUsername = user.getUsername();
        loggedInUser = _users.get(loggedInUsername);
    }

    public void setLoggedInUserGroup(String groupName) {
        _users.get(loggedInUsername).setGroupName(groupName);
        databaseUsers.child(_users.get(loggedInUsername).getId()).setValue(_users.get(loggedInUsername));
    }

    public User getLoggedInUser() {
        return _users.get(loggedInUsername);
    }

    /**
     * Adds a new user to the system, checks if the username is already
     * registered and does not add if they are
     * @param user      The user to add to the system
     * @return          True if the user is successfully added, false if the
     *                  user is a duplicate
     */
    public boolean addUser(User user) {
        if (_users.containsValue(user)) {
            return false;
        }
        _users.put(user.getUsername().toLowerCase(), user);
        _usersPasswords.put(user.getUsername().toLowerCase(), user.getPassword());
        databaseUsers.child(user.getId()).setValue(user);

        return true;
    }

    /**
     * Returns a user object based on their username
     * @param username      The username of the desired User
     * @return              The desired User matched with their username
     */
    public User getUser(String username) {
        return _users.get(username.toLowerCase());
    }

    /**
     * Check if this user is already registered with a User object
     * @param user      The user object to check
     * @return          True if the user is already registered, false if not
     */
    public boolean containsUser(User user) {
        return _users.containsValue(user);
    }

    /**
     * Check if this user is already registered using their username as a String
     * @param username      The String representation of their username which is
     *                      checked ignoring case
     * @return              True if the user is already registered, false if not
     */
    public boolean containsUser(String username) {
        return _users.containsKey(username.toLowerCase());
    }

    /**
     * Checks if a username, password combination is valid
     * @param username      The username to search for
     * @param password      The password to match the username with
     * @return              True if the combination is valid, false if user is
     *                      not registered or invalid password
     */
    public boolean checkUserCredentials(String username, String password) {
        if (!containsUser(username)) {
            return false;
        }
        return password.equals(_usersPasswords.get(username.toLowerCase()));
    }

    public boolean logout() {
        setLoggedInUser(null);
        return true;
    }

    /**
     * Just for UI, helps set up spinner values in RegisterActivity
     * @return      An List containing the user types: User and Admin
     */
    public List<String> getUserTypes() {
        List<String> userTypes = new ArrayList<>();
        userTypes.add("User");
        userTypes.add("Admin");
        return userTypes;
    }
}
