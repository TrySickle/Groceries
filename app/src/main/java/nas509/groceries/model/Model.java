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
 * Created by *** on 1/5/17.
 *
 * This is our facade to the Model.  We are using a Singleton design pattern to allow
 * access to the model from each controller.
 *
 *
 */

public class Model {
    /** Singleton instance */
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }
    private GroceryItemManager groceryItemManager;

    /**
     * make a new model
     */
    private Model() {
        groceryItemManager = GroceryItemManager.getInstance();
        groceryItemManager.loadDummyData();
    }


    /**
     * get the courses
     * @return a list of the courses in the app
     */
    public List<GroceryItem> getGroceryItems() { return groceryItemManager.getGroceryItems(); }

    /**
     * add a course to the app.  checks if the course is already entered
     *
     * uses O(n) linear search for course
     *
     * @param groceryItem  the course to be added
     * @return true if added, false if a duplicate
     */
    public boolean addGroceryItem(GroceryItem groceryItem) {
        return groceryItemManager.addGroceryItem(groceryItem);
    }

    public boolean editGroceryItem(int id, String name, String price) {
        return groceryItemManager.editGroceryItem(id, name, price);
    }

    public int removeGroceryItem(int id) {
        return groceryItemManager.removeGroceryItem(id);
    }

    // TODO: separate model into itemManager and itemManagementFacade
//    public boolean saveText(File file) {
//        System.out.println("Saving as a text file");
//        try {
//            PrintWriter pw = new PrintWriter(file);
//            sm.saveAsText(pw);
//            pw.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d("UserManagerFacade", "Error opening the text file for save!");
//            return false;
//        }
//
//        return true;
//    }
//
//    public boolean loadText(File file) {
//        try {
//            //make an input object for reading
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            sm.loadFromText(reader);
//
//        } catch (FileNotFoundException e) {
//            Log.e("ModelSingleton", "Failed to open text file for loading!");
//            return false;
//        }
//
//        return true;
//    }

    /**
     *
     * @return  the currently selected course
     */
    public GroceryItem getCurrentGroceryItem() { return groceryItemManager.getCurrentGroceryItem();}

    public void setCurrentGroceryItem(GroceryItem groceryItem) { groceryItemManager.setCurrentGroceryItem(groceryItem); }

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
        return groceryItemManager.getGroceryItemById(id);
    }
}
