package nas509.groceries.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nas509.groceries.controller.MyListFragment;
import nas509.groceries.controller.SharedListFragment;


/**
 * Created by robertwaters on 1/5/17.
 *
 * This is our facade to the GroceryItemManager.  We are using a Singleton design pattern to allow
 * access to the model from each controller.
 *
 *
 */

public class GroceryItemManager {
    /** Singleton instance */
    private static final GroceryItemManager _instance = new GroceryItemManager();
    public static GroceryItemManager getInstance() { return _instance; }

    /** holds the list of all groceryItems */
    private List<GroceryItem> _groceryItems;

    private List<GroceryItem> _myList;

    /** the currently selected groceryItem, defaults to first groceryItem */
    private GroceryItem _currentGroceryItem;

    /** Null Object pattern, returned when no groceryItem is found */
    private final GroceryItem theNullGroceryItem = new GroceryItem();

    private int idSeed;

    DatabaseReference databaseGroceries;

    /**
     * make a new model
     */
    private GroceryItemManager() {
        _groceryItems = new ArrayList<>();
        _myList = new ArrayList<>();
        idSeed = 0;
        databaseGroceries = getDatabase();
        //comment this out after full app developed -- for homework leave in
    }

    public DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("groceries");
    }

    public void retrieveData(final SharedListFragment.GroceryItemRecyclerViewAdapter adapter, final boolean detach) {
        databaseGroceries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _groceryItems.clear();
                _myList.clear();

                int highestId = 0;
                for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                    //GroceryItem grocery = grocerySnapshot.getValue(GroceryItem.class);
                    String name = (String) (grocerySnapshot.child("name").getValue());
                    String value = (String) grocerySnapshot.child("price").getValue();
                    BigDecimal money = new BigDecimal(value.replaceAll(",", ""));
                    String createdUserId = (String) grocerySnapshot.child("createdUserId").getValue();
                    String groupName = (String) grocerySnapshot.child("groupName").getValue();
                    ArrayList<String> wantedBy = (ArrayList<String>) grocerySnapshot.child("wantedBy").getValue();
                    if (wantedBy == null) {
                        wantedBy = new ArrayList<>();
                    }
                    ArrayList<String> purchasedBy = (ArrayList<String>) grocerySnapshot.child("purchasedBy").getValue();
                    if (purchasedBy == null) {
                        purchasedBy = new ArrayList<>();
                    }
                    long l = (long) grocerySnapshot.child("id").getValue();
                    int id = (int) l;
                    if (id > highestId) {
                        highestId = id;
                    }
                    String loggedInUserGroupName = Model.getInstance().getLoggedInUser().getGroupName();
                    if (loggedInUserGroupName.equals("")) {
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, "", wantedBy, purchasedBy);
                            _groceryItems.add(newItem);
                            _myList.add(newItem);
                        }
                    } else if (loggedInUserGroupName.equals(groupName)){
                        GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, groupName, wantedBy, purchasedBy);
                        _groceryItems.add(newItem);
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            _myList.add(newItem);
                        }
                    }

                }
                idSeed = highestId + 1;
                adapter.notifyDataSetChanged();
                if (detach) {
                    databaseGroceries.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void retrieveData(final MyListFragment.GroceryItemRecyclerViewAdapter adapter, final boolean detach) {
        databaseGroceries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _groceryItems.clear();
                _myList.clear();

                int highestId = 0;
                for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                    //GroceryItem grocery = grocerySnapshot.getValue(GroceryItem.class);
                    String name = (String) (grocerySnapshot.child("name").getValue());
                    String value = (String) grocerySnapshot.child("price").getValue();
                    BigDecimal money = new BigDecimal(value.replaceAll(",", ""));
                    String createdUserId = (String) grocerySnapshot.child("createdUserId").getValue();
                    String groupName = (String) grocerySnapshot.child("groupName").getValue();
                    ArrayList<String> wantedBy = (ArrayList<String>) grocerySnapshot.child("wantedBy").getValue();
                    if (wantedBy == null) {
                        wantedBy = new ArrayList<>();
                    }
                    ArrayList<String> purchasedBy = (ArrayList<String>) grocerySnapshot.child("purchasedBy").getValue();
                    if (purchasedBy == null) {
                        purchasedBy = new ArrayList<>();
                    }
                    long l = (long) grocerySnapshot.child("id").getValue();
                    int id = (int) l;
                    if (id > highestId) {
                        highestId = id;
                    }
                    String loggedInUserGroupName = Model.getInstance().getLoggedInUser().getGroupName();
                    if (loggedInUserGroupName.equals("")) {
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, "", wantedBy, purchasedBy);
                            _groceryItems.add(newItem);
                            _myList.add(newItem);
                        }
                    } else if (loggedInUserGroupName.equals(groupName)){
                        GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, groupName, wantedBy, purchasedBy);
                        _groceryItems.add(newItem);
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            _myList.add(newItem);
                        }
                    }

                }
                idSeed = highestId + 1;
                adapter.notifyDataSetChanged();
                if (detach) {
                    databaseGroceries.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void retrieveData() {
        databaseGroceries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _groceryItems.clear();
                _myList.clear();

                int highestId = 0;
                for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                    //GroceryItem grocery = grocerySnapshot.getValue(GroceryItem.class);
                    String name = (String) (grocerySnapshot.child("name").getValue());
                    String value = (String) grocerySnapshot.child("price").getValue();
                    BigDecimal money = new BigDecimal(value.replaceAll(",", ""));
                    String createdUserId = (String) grocerySnapshot.child("createdUserId").getValue();
                    String groupName = (String) grocerySnapshot.child("groupName").getValue();
                    ArrayList<String> wantedBy = (ArrayList<String>) grocerySnapshot.child("wantedBy").getValue();
                    if (wantedBy == null) {
                        wantedBy = new ArrayList<>();
                    }
                    ArrayList<String> purchasedBy = (ArrayList<String>) grocerySnapshot.child("purchasedBy").getValue();
                    if (purchasedBy == null) {
                        purchasedBy = new ArrayList<>();
                    }
                    long l = (long) grocerySnapshot.child("id").getValue();
                    int id = (int) l;
                    if (id > highestId) {
                        highestId = id;
                    }
                    String loggedInUserGroupName = Model.getInstance().getLoggedInUser().getGroupName();
                    if (loggedInUserGroupName.equals("")) {
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, "", wantedBy, purchasedBy);
                            _groceryItems.add(newItem);
                            _myList.add(newItem);
                        }
                    } else if (loggedInUserGroupName.equals(groupName)){
                        GroceryItem newItem = new GroceryItem(name, money, id, createdUserId, groupName, wantedBy, purchasedBy);
                        _groceryItems.add(newItem);
                        if (createdUserId.equals(Model.getInstance().getLoggedInUser().getId())) {
                            _myList.add(newItem);
                        }
                    }

                }
                idSeed = highestId + 1;
                databaseGroceries.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getNewId() {
        return idSeed++;
    }

    /**
     * get the groceryItems
     * @return a list of the groceryItems in the app
     */
    public List<GroceryItem> getGroceryItems() { return _groceryItems; }

    public List<GroceryItem> getMyList() { return _myList; }

    public void updateItemsGroupChange(String groupName, User user) {
        HashMap<String, Object> updated = new HashMap<>();
        for (GroceryItem item : _groceryItems) {
            if (item.getCreatedUserId().equals(user.getId())) {
                item.setGroupName(groupName);
            }
            updated.put(Integer.toString(item.getId()), item);
        }
        for (GroceryItem item : _myList) {
            if (item.getCreatedUserId().equals(user.getId())) {
                item.setGroupName(groupName);
            }
        }
        databaseGroceries.updateChildren(updated);
    }

    /**
     * add a groceryItem to the app.  checks if the groceryItem is already entered
     *
     * uses O(n) linear search for groceryItem
     *
     * @param groceryItem  the groceryItem to be added
     * @return true if added, false if a duplicate
     */
    public boolean addGroceryItem(GroceryItem groceryItem) {
        for (GroceryItem g : _groceryItems ) {
            if (g.equals(groceryItem)) return false;
        }
        databaseGroceries.child(Integer.toString(groceryItem.getId())).setValue(groceryItem);
        return true;
    }

    public void updateItem(int id) {
        int index = 0;
        while (_groceryItems.get(index).getId() != id) {
            index++;
        }
        databaseGroceries.child(Integer.toString(id)).setValue(_groceryItems.get(index));
    }

    public boolean editGroceryItem(int id, String name, String price) {
        int index = 0;
        while (index < _groceryItems.size() && _groceryItems.get(index).getId() != id) {
            index++;
        }
        if (_groceryItems.size() > 0 && _groceryItems.get(index).getId() == id) {
            GroceryItem old = _groceryItems.get(index);
            GroceryItem updated = new GroceryItem(name, new BigDecimal(price).setScale(2, RoundingMode.HALF_UP), id, old.getCreatedUserId(), old.getGroupName(), old.getWantedBy(), old.getPurchasedBy());
            databaseGroceries.child(Integer.toString(updated.getId())).setValue(updated);
            return true;
        }

        return false;
    }

    public void removeGroceryItem(int id) {
        int index = 0;
        while (index < _groceryItems.size() && _groceryItems.get(index).getId() != id) {
            index++;
        }
        int indexForMyList = 0;
        while (indexForMyList < _myList.size() && _myList.get(indexForMyList).getId() != id) {
            indexForMyList++;
        }
        if (_groceryItems.get(index).getId() == id) {
            databaseGroceries.child(Integer.toString(_groceryItems.get(index).getId())).removeValue();
        }
    }

    /**
     *
     * @return  the currently selected groceryItem
     */
    public GroceryItem getCurrentGroceryItem() { return _currentGroceryItem;}

    public void setCurrentGroceryItem(GroceryItem groceryItem) { _currentGroceryItem = groceryItem; }

    /**
     * Return a groceryItem that has the matching id
     * This uses a linear O(n) search
     *
     * @param id the id number of the groceryItem
     * @return the groceryItem with this id or theNullGroceryItem if no such id exists.
     */
    public GroceryItem getGroceryItemById(int id) {
        for (GroceryItem c : _groceryItems ) {
            if (c.getId() == id) {
                return c;
            }
        }
        return theNullGroceryItem;
    }

    /**
     *
     * @param writer
     */
//    void saveAsText(PrintWriter writer) {
//        System.out.println("Manager saving: " + _groceryItems.size() + " grocery items" );
//        writer.println(_groceryItems.size());
//        for(GroceryItem g : _groceryItems) {
//            g.saveAsText(writer);
//        }
//    }

    /**
     * load the model from a custom text file
     *
     * @param reader  the file to read from
     */
//    void loadFromText(BufferedReader reader) {
//        System.out.println("Loading Text File");
//        //studentMap.clear();
//        _groceryItems.clear();
//        try {
//            String countStr = reader.readLine();
//            assert countStr != null;
//            int count = Integer.parseInt(countStr);
//            int highestId = 0;
//            //then read in each user to model
//            for (int i = 0; i < count; ++i) {
//                String line = reader.readLine();
//                GroceryItem g = GroceryItem.parseEntry(line);
//                _groceryItems.add(g);
//                String[] tokens = line.split("\t");
//                if (Integer.valueOf(tokens[2]) > highestId) {
//                    highestId = Integer.valueOf(tokens[2]);
//                }
//                //studentMap.put(g.getName(), g);
//            }
//            //be sure and close the file
//            reader.close();
//            idSeed = highestId + 1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Done loading text file with " + _groceryItems.size() + " grocery items");
//
//    }
}
