package nas509.groceries.model;

import java.util.HashMap;

/**
 * Created by jason on 10/23/2017.
 */

public class Group {
    private HashMap<String, User> users;

    private String groupName;

    private int size;

    public Group() {
        users = new HashMap<>();
        groupName = "";
        size = 0;
    }

    public Group(String groupName) {
        users = new HashMap<>();
        this.groupName = groupName;
    }

    public void addUser(User u) {
        users.put(u.getId(), u);
        size = users.size();
//        u.setGroupName(groupName);
    }

    public void removeUser(User u) {
        users.remove(u.getId());
        size = users.size();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public int getSize() {
        return size;
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public String getGroupName() {
        return groupName;
    }
}
