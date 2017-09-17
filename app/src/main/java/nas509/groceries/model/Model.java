package nas509.groceries.model;

import android.support.compat.BuildConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by robertwaters on 1/5/17.
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

    /** holds the list of all courses */
    private List<GroceryItem> _groceryItems;

    /** the currently selected course, defaults to first course */
    private GroceryItem _currentGroceryItem;

    /** Null Object pattern, returned when no course is found */
    private final GroceryItem theNullGroceryItem = new GroceryItem();


    /**
     * make a new model
     */
    private Model() {
        _groceryItems = new ArrayList<>();

        //comment this out after full app developed -- for homework leave in
        //loadDummyData();

    }

    /**
     * populate the model with some dummy data.  The full app would not require this.
     * comment out when adding new courses functionality is present.
     */
//    private void loadDummyData() {
//        _groceryItems.add(new GroceryItem("Objects and Design", "2340", SchoolCode.CS));
//        _groceryItems.add(new GroceryItem( "TQM", "4321", SchoolCode.IE));
//        _groceryItems.add(new Course("Concrete Ideas", "5432", SchoolCode.AR));
//        _groceryItems.add(new Course("Calc I", "2213", SchoolCode.MATH));
//        _groceryItems.get(0).getStudents().add(new Student("Bob", "CS"));
//        _groceryItems.get(0).getStudents().add(new Student("Sally", "ISYE"));
//        _groceryItems.get(1).getStudents().add(new Student("Fred", "Math"));
//        _groceryItems.get(1).getStudents().add(new Student("Edith", "CM"));
//        _currentCourse = _groceryItems.get(0);
//    }

    /**
     * get the courses
     * @return a list of the courses in the app
     */
    public List<GroceryItem> getCourses() { return _groceryItems; }

    /**
     * add a course to the app.  checks if the course is already entered
     *
     * uses O(n) linear search for course
     *
     * @param course  the course to be added
     * @return true if added, false if a duplicate
     */
    public boolean addCourse(GroceryItem groceryItem) {
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
    public Course getCurrentGroceryItem() { return _currentGroceryItem;}

    public void setCurrentGroceryItem(GroceryItem groceryItem) { _currentGroceryItem = groceryItem; }

    /**
     * Return a course that has matching number.
     * This uses an O(n) linear search.
     *
     * @param number the number of the course to find
     * @return  the course with that number or the NullCourse if no such number exists.
     *
     */
    public Course getCourseByNumber (String number) {
        for (GroceryItem c : _groceryItems ) {
            if (c.getNumber().equals(number)) return c;
        }
        return theNullGroceryItem;
    }

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
     * add a student to the current course
     *
     * @param student the student to add
     * @return true if student added, false if not added
     */
    public boolean addStudent(Student student) {
        return _currentGroceryItem != null && _currentGroceryItem.addStudent(student);
    }

    /**
     * Replace an existing students data with new data
     *
     * @param student the student being edited
     */
    public void replaceStudentData(Student student) {
        Student existing = _currentGroceryItem.findStudentById(student.getId());

        //if existing comes back null, something is seriously wrong
        if (BuildConfig.DEBUG && (existing == null)) { throw new AssertionError(); }

        //update the name
        existing.setName(student.getName());

        //update the major
        existing.setMajor(student.getMajor());

        //update the class standing
        existing.setClassStanding(student.getStanding());
    }
}
