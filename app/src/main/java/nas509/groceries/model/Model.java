package nas509.groceries.model;

import java.util.ArrayList;
import java.util.List;


import nas509.groceries.controller.MyListFragment;
import nas509.groceries.controller.PaymentsFragment;
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
        boolean returnThis = groceryItemManager.addGroceryItem(groceryItem);
        PaymentsFragment.refreshPayments();
        return returnThis;
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
        PaymentsFragment.refreshPayments();
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
        PaymentsFragment.refreshPayments();
    }

    public boolean editGroceryItem(int id, String name, String price) {
        boolean returnThis = groceryItemManager.editGroceryItem(id, name, price);
        PaymentsFragment.refreshPayments();
        return returnThis;
    }

    public void removeGroceryItem(int id) {
        groceryItemManager.removeGroceryItem(id);
        PaymentsFragment.refreshPayments();
    }

    public void updateItemsGroupChange(String groupName, User user) {
        groceryItemManager.updateItemsGroupChange(groupName, user);
        PaymentsFragment.refreshPayments();
    }

    public void setUserGroupName(String groupName) {
        userManager.setLoggedInUserGroup(groupName);
        PaymentsFragment.refreshPayments();
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
        PaymentsFragment.refreshPayments();
    }

    public ArrayList<String> getPaymentsList() {
        PaymentsManager paymentsManager = new PaymentsManager();
        return paymentsManager.getPaymentsList();
    }

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
