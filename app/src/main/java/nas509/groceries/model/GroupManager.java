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
    private HashMap<String, Group> groups; // groupId to group

    private DatabaseReference databaseGroups;
    String nullGroupId;

    private GroupManager() {
        groups = new HashMap<>();
        databaseGroups = FirebaseDatabase.getInstance().getReference("groups"); // lol delete this
//        databaseGroups.setValue(null);
//        databaseGroups = FirebaseDatabase.getInstance().getReference("groups");
    }


    public void createGroupsFromUsers(Map<String, User> users) {
        for (Map.Entry<String, User> entry : users.entrySet())
        {
            User u = entry.getValue();
            if (groups.containsKey(u.getGroupId())) {
                groups.get(u.getGroupId()).addUser(u);
                databaseGroups.child(u.getGroupId()).setValue(groups.get(u.getGroupId()));
            } else {
                if (!u.getGroupId().equals("")) {
                    groups.put(u.getGroupId(), new Group(u.getGroupId()));
                    groups.get(u.getGroupId()).addUser(u);
                    databaseGroups.child(u.getGroupId()).setValue(groups.get(u.getGroupId()));
                }

            }
        }
    }

    public void retrieveGroups() {
        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                groups.clear();
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    groups.put(group.getGroupId(), group);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("GroupManager", "cancelled");
            }
        });
    }
    
    public String getNewId() {
        return databaseGroups.push().getKey();
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public void addGroup(Group group) {
        groups.put(group.getGroupId(), group);
    }
}
