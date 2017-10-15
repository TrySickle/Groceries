package nas509.groceries.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


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

    /** holds the list of all courses */
    private List<GroceryItem> _groceryItems;

    /** the currently selected course, defaults to first course */
    private GroceryItem _currentGroceryItem;

    /** Null Object pattern, returned when no course is found */
    private final GroceryItem theNullGroceryItem = new GroceryItem();

    private int idSeed;

    DatabaseReference databaseGroceries;

    /**
     * make a new model
     */
    private GroceryItemManager() {
        _groceryItems = new ArrayList<>();
        idSeed = 0;
        databaseGroceries = getDatabase();
        //comment this out after full app developed -- for homework leave in
    }

    public DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("groceries");
    }

    public void retrieveData() {
        databaseGroceries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                _groceryItems.clear();

                for (DataSnapshot grocerySnapshot : dataSnapshot.getChildren()) {
                    //GroceryItem grocery = grocerySnapshot.getValue(GroceryItem.class);
                    String name = (String) (grocerySnapshot.child("name").getValue());
                    String value = (String) grocerySnapshot.child("price").getValue();
                    BigDecimal money = new BigDecimal(value.replaceAll(",", ""));
                    long l = (long) grocerySnapshot.child("id").getValue();
                    int id = (int) l;
                    _groceryItems.add(new GroceryItem(name, money, id));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * populate the model with some dummy data.  The full app would not require this.
     * comment out when adding new courses functionality is present.
     */
    public void loadDummyData() {
        _groceryItems.add(new GroceryItem("Milk", BigDecimal.valueOf(3.50)));
        _groceryItems.add(new GroceryItem("Eggs", BigDecimal.valueOf(2.27)));
        _groceryItems.add(new GroceryItem("Hot Cheetos", BigDecimal.valueOf(3.99)));
        _currentGroceryItem = _groceryItems.get(0);
    }

    public int getNewId() {
        return idSeed++;
    }

    /**
     * get the courses
     * @return a list of the courses in the app
     */
    public List<GroceryItem> getGroceryItems() { return _groceryItems; }

    /**
     * add a course to the app.  checks if the course is already entered
     *
     * uses O(n) linear search for course
     *
     * @param groceryItem  the course to be added
     * @return true if added, false if a duplicate
     */
    public boolean addGroceryItem(GroceryItem groceryItem) {
        for (GroceryItem g : _groceryItems ) {
            if (g.equals(groceryItem)) return false;
        }
        _groceryItems.add(groceryItem);
        databaseGroceries.child(Integer.toString(groceryItem.getId())).setValue(groceryItem);
        return true;
    }

    public boolean editGroceryItem(int id, String name, String price) {
        int index = 0;
        while (index < _groceryItems.size() && _groceryItems.get(index).getId() != id) {
            index++;
        }
        if (_groceryItems.get(index).getId() == id) {
            GroceryItem updated = new GroceryItem(name, new BigDecimal(price).setScale(2, RoundingMode.HALF_UP), id);
            _groceryItems.set(index, updated);
            databaseGroceries.child(Integer.toString(updated.getId())).setValue(updated);
            return true;
        }
        return false;
    }

    public int removeGroceryItem(int id) {
        int index = 0;
        while (index < _groceryItems.size() && _groceryItems.get(index).getId() != id) {
            index++;
        }
        if (_groceryItems.get(index).getId() == id) {
            databaseGroceries.child(Integer.toString(_groceryItems.get(index).getId())).removeValue();
            _groceryItems.remove(index);
            return index;
        }
        return -1;
    }

    /**
     *
     * @return  the currently selected course
     */
    public GroceryItem getCurrentGroceryItem() { return _currentGroceryItem;}

    public void setCurrentGroceryItem(GroceryItem groceryItem) { _currentGroceryItem = groceryItem; }

    /**
     * Return a course that has the matching id
     * This uses a linear O(n) search
     *
     * @param id the id number of the course
     * @return the course with this id or theNullGroceryItem if no such id exists.
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
