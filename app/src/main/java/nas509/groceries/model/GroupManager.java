package nas509.groceries.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 10/23/2017.
 */

public class GroupManager {

    private static final GroupManager _instance = new GroupManager();
    public static GroupManager getInstance() { return _instance; }
    private HashMap<String, Group> groupNames;

    private DatabaseReference databaseGroups;

    private GroupManager() {
        groupNames = new HashMap<>();
        databaseGroups = FirebaseDatabase.getInstance().getReference("groups"); // lol delete this
//        databaseGroups.setValue(null);
//        databaseGroups = FirebaseDatabase.getInstance().getReference("groups");
    }


    public void createGroupsFromUsers(Map<String, User> users) {
        for (Map.Entry<String, User> entry : users.entrySet())
        {
            User u = entry.getValue();
            if (groupNames.containsKey(u.getGroupName())) {
                groupNames.get(u.getGroupName()).addUser(u);
                databaseGroups.child(u.getGroupName()).setValue(groupNames.get(u.getGroupName()));
            } else {
                if (!u.getGroupName().equals("")) {
                    groupNames.put(u.getGroupName(), new Group(u.getGroupName()));
                    groupNames.get(u.getGroupName()).addUser(u);
                    databaseGroups.child(u.getGroupName()).setValue(groupNames.get(u.getGroupName()));
                }

            }
        }
    }

    public void retrieveGroups() {
        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                groupNames.clear();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    groupNames.put(group.getGroupName(), group);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GroupManager", "cancelled");
            }
        });
    }

    public void addUserToGroup(String groupName, User u) {
        addGroup(groupName);
        if (groupNames.containsKey(u.getGroupName())) {
            groupNames.get(u.getGroupName()).removeUser(u);
            if (groupNames.get(u.getGroupName()).isEmpty()) {
                databaseGroups.child(u.getGroupName()).setValue(null);
            }
        }
        groupNames.get(groupName).addUser(u);
        Log.d("group manager", Integer.toString(groupNames.get(groupName).getSize()));
        databaseGroups.child(groupName).setValue(groupNames.get(groupName));
    }

    public Group getGroup(String groupName) {
        return groupNames.get(groupName);
    }

    public boolean addGroup(String groupName) {
        if (!groupNames.containsKey(groupName)) {
            groupNames.put(groupName, new Group(groupName));
            databaseGroups.child(groupName).setValue(groupNames.get(groupName));
            Log.d("Add Group", "Added " + groupName);
            return true;
        }
        return false;
    }
}
