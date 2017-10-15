package nas509.groceries.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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

    /**
     * make a new model
     */
    private GroceryItemManager() {
        _groceryItems = new ArrayList<>();
        idSeed = 0;
        //comment this out after full app developed -- for homework leave in
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
        for (GroceryItem c : _groceryItems ) {
            if (c.equals(groceryItem)) return false;
        }
        _groceryItems.add(groceryItem);
        return true;
    }

    /**
     *
     * @return  the currently selected course
     */
    public GroceryItem getCurrentGroceryItem() { return _currentGroceryItem;}

    public void setCurrentGroceryItem(GroceryItem groceryItem) { _currentGroceryItem = groceryItem; }

    /**
     * Return a course that has matching number.
     * This uses an O(n) linear search.
     *
     * @param number the number of the course to find
     * @return  the course with that number or the NullCourse if no such number exists.
     *
     */
//    public Course getCourseByNumber (String number) {
//        for (GroceryItem c : _groceryItems ) {
//            if (c.getNumber().equals(number)) return c;
//        }
//        return theNullGroceryItem;
//    }

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
}