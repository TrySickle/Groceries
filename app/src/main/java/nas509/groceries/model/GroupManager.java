package nas509.groceries.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rahul on 10/15/17.
 */

/**
 * Manages all registered groups, keeps track of logged in group, provides some
 * services
 */

public class GroupManager {

    /** singleton design */
    private static final GroupManager _instance = new GroupManager();
    public static GroupManager getInstance() { return _instance; }

    /** Hashmap containing the groupnames as keys and passwords as values
     *  Used for checking groupnames to passwords */
    private Map<String, String> _groupsPasswords;

    /** Hashmap containing the groupnames as keys and Group objects as values
     *  Used for checking if a Group/Groupname is already registered */
    private Map<String, Group> _groups;

    /** The currently logged in group, changes the appActivity text */
    private Group loggedInGroup;

    /** Private constructor for singleton */
    private GroupManager() {
        _groupsPasswords = new HashMap<>();
        _groups = new HashMap<>();
        loggedInGroup = null;
    }

    /** Getter and setter */
    public void setloggedInGroup(Group group) {
        loggedInGroup = group;
    }

    public Group getloggedInGroup() {
        return loggedInGroup;
    }

    /**
     * Adds a new group to the system, checks if the groupname is already
     * registered and does not add if they are
     * @param group      The group to add to the system
     * @return          True if the group is successfully added, false if the
     *                  group is a duplicate
     */
    public boolean addGroup(Group group) {
        if (_groups.containsValue(group)) {
            return false;
        }
        _groups.put(group.getGroupname().toLowerCase(), group);
        _groupsPasswords.put(group.getGroupname().toLowerCase(), group.getPassword());
        return true;
    }

    /**
     * Returns a group object based on their groupname
     * @param groupname      The groupname of the desired Group
     * @return              The desired Group matched with their groupname
     */
    public Group getGroup(String groupname) {
        return _groups.get(groupname.toLowerCase());
    }

    /**
     * Check if this group is already registered with a Group object
     * @param group      The group object to check
     * @return          True if the group is already registered, false if not
     */
    public boolean containsGroup(Group group) {
        return _groups.containsValue(group);
    }

    /**
     * Check if this group is already registered using their groupname as a String
     * @param groupname      The String representation of their groupname which is
     *                      checked ignoring case
     * @return              True if the group is already registered, false if not
     */
    public boolean containsGroup(String groupname) {
        return _groups.containsKey(groupname.toLowerCase());
    }

    /**
     * Checks if a groupname, password combination is valid
     * @param groupname      The groupname to search for
     * @param password      The password to match the groupname with
     * @return              True if the combination is valid, false if group is
     *                      not registered or invalid password
     */
    public boolean checkGroupCredentials(String groupname, String password) {
        if (!containsGroup(groupname)) {
            return false;
        }
        return password.equals(_groupsPasswords.get(groupname.toLowerCase()));
    }

    /**
     * Just for UI, helps set up spinner values in RegisterActivity
     * @return      An List containing the group types: Group and Admin
     */
    public List<String> getGroupTypes() {
        List<String> groupTypes = new ArrayList<>();
        groupTypes.add("Group");
        groupTypes.add("Admin");
        return groupTypes;
    }
}

