package nas509.groceries.model;

import java.util.HashMap;

/**
 * Created by jason on 10/23/2017.
 */

public class Group {
    private HashMap<String, User> users;

    private String groupId;
    private String groupName;

    public Group() {
        users = new HashMap<>();
        groupId = GroupManager.getInstance().getNewId();
        groupName = "";
    }

    public Group(String groupId) {
        users = new HashMap<>();
        this.groupId = groupId;
        groupName = "";
    }

    public void addUser(User u) {
        users.put(u.getId(), u);
        u.setGroupId(groupId);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }
}
