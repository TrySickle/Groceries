package nas509.groceries.model;

import java.util.List;


import nas509.groceries.controller.MyListFragment;
import nas509.groceries.controller.SharedListFragment;


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
    private UserManager userManager;
    private GroupManager groupManager;
//    private PersistenceManager persistenceManager;
    /**
     * make a new model
     */
    private Model() {
        groceryItemManager = GroceryItemManager.getInstance();
        userManager = UserManager.getInstance();
        groupManager = GroupManager.getInstance();
//        persistenceManager = PersistenceManager.getInstance();
    }

    public void getDatabase() {
        groceryItemManager.getDatabase();
    }

    public void retrieveData(MyListFragment.GroceryItemRecyclerViewAdapter adapter, final boolean detach) {
        groceryItemManager.retrieveData(adapter, detach);
    }

    public void retrieveData(SharedListFragment.GroceryItemRecyclerViewAdapter adapter, final boolean detach) {
        groceryItemManager.retrieveData(adapter, detach);
    }

    public void retrieveUsers() {
        userManager.retrieveUsers();
        //groupManager.createGroupsFromUsers(userManager.getUsers());
    }

    public void retrieveGroups() {
        groupManager.retrieveGroups();
    }

    /**
     * get the courses
     * @return a list of the courses in the app
     */
    public List<GroceryItem> getGroceryItems() { return groceryItemManager.getGroceryItems(); }

    public List<GroceryItem> getMyList() { return groceryItemManager.getMyList(); }

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

    public void dollarItem(int id, boolean favorite) {
        for (GroceryItem item : getGroceryItems()) {
            if (item.getId() == id) {
                if (favorite) {
                    item.addPurchasedBy(getLoggedInUser());
                } else {
                    item.removePurchasedBy(getLoggedInUser());
                }
            }
        }
        for (GroceryItem item : getMyList()) {
            if (item.getId() == id) {
                if (favorite) {
                    item.addPurchasedBy(getLoggedInUser());
                } else {
                    item.removePurchasedBy(getLoggedInUser());
                }
            }
        }
        groceryItemManager.updateItem(id);
    }

    public void starItem(int id, boolean favorite) {
        for (GroceryItem item : getGroceryItems()) {
            System.out.println("Before:" + item.getWantedBy().size());
            if (item.getId() == id) {
                if (favorite) {
                    item.addWantedBy(getLoggedInUser());
                } else {
                    item.removeWantedBy(getLoggedInUser());
                }
            }
            System.out.println("After:" + item.getWantedBy().size());
        }
        for (GroceryItem item : getMyList()) {
            System.out.println("Before:" + item.getWantedBy().size());
            if (item.getId() == id) {
                if (favorite) {
                    item.addWantedBy(getLoggedInUser());
                } else {
                    item.removeWantedBy(getLoggedInUser());
                }
            }
            System.out.println("After:" + item.getWantedBy().size());
        }
        groceryItemManager.updateItem(id);
    }

    public boolean editGroceryItem(int id, String name, String price) {
        return groceryItemManager.editGroceryItem(id, name, price);
    }

    public void removeGroceryItem(int id) {
        groceryItemManager.removeGroceryItem(id);
    }

    public void updateItemsGroupChange(String groupName, User user) {
        groceryItemManager.updateItemsGroupChange(groupName, user);
    }

    public void setUserGroupName(String groupName) {
        userManager.setLoggedInUserGroup(groupName);
    }

    public void setLoggedInUser(User user) {
        userManager.setLoggedInUser(user);
    }

    public User getUser(String username) {
        return userManager.getUser(username);
    }

    public void addUser(User user) {
        userManager.addUser(user);
    }

    public User getLoggedInUser() {
        return UserManager.getInstance().getLoggedInUser();
    }

    public void addUserToGroup(String groupName, User u) {
        groupManager.addUserToGroup(groupName, u);
    }

//    public boolean loadText(File file) {
//        return persistenceManager.loadText(file);
//    }

//    public boolean saveText(File file) {
//        return persistenceManager.saveText(file);
//    }

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
