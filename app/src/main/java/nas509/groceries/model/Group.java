package nas509.groceries.model;

import java.util.HashMap;

/**
 * Created by jason on 10/23/2017.
 */

public class Group {
    private HashMap<String, User> users;

    private String groupName;

    public Group() {
        users = new HashMap<>();
        groupName = "";
    }

    public Group(String groupName) {
        users = new HashMap<>();
        this.groupName = groupName;
    }

    public void addUser(User u) {
        users.put(u.getId(), u);
//        u.setGroupName(groupName);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public String getGroupName() {
        return groupName;
    }
}
