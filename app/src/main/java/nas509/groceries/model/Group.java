package nas509.groceries.model;

/**
 * Created by rahul on 10/15/17.
 */

/**
 * Information holder - represents a regular group in model
 */

public class Group {

    /** The group's groupname, will be stored case sensitive, but checked case
     * insensitive */
    private final String groupname;

    /** The group's password, case sensitive */
    private final String password;

    /** Make a new group, default "group", "pass" */
    public Group() {
        this("group", "pass");
    }

    /** Make a new group
     *
     * @param groupname      The group's groupname
     * @param password      The group's password
     */
    public Group(String groupname, String password) {
        this.groupname = groupname;
        this.password = password;
    }

    /** Getters */
    public String getGroupname() {
        return groupname;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Checks that the group's groupname matches a provided one, case insensitive
     * @param groupname      Groupname to check
     * @return              True if the groupnames match, false if not
     */
    public boolean checkGroupname(String groupname) {
        return this.groupname.equalsIgnoreCase(groupname);
    }

    /**
     * Checks that the group's password matches a provided one, case sensitive
     * @param password      The password to check
     * @return              True if the passwords match, false if not
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Equals method, checks that groupnames match, case insensitive
     * @param o     The object to check this object with
     * @return      True if the objects are the same or their groupnames are the
     *              same
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Group)) {
            return false;
        }

        Group u = (Group) o;
        return u.checkGroupname(groupname);
    }
}

