package nas509.groceries.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 10/26/2017.
 */

public class PaymentsManager {
    private HashMap<User, Integer> userOrdering;
    private HashMap<Integer, User> userOrderingReverse;
    private ArrayList<BigDecimal> youOwe;
    private ArrayList<BigDecimal> owesYou;
    private GroceryItemManager groceryItemManager = GroceryItemManager.getInstance();
    private UserManager userManager = UserManager.getInstance();
    private HashMap<String, User> users;
    private ArrayList<GroceryItem> items;
    User loggedInUser = userManager.getLoggedInUser();

    public PaymentsManager() {
        youOwe = new ArrayList<>();
        owesYou = new ArrayList<>();
        userOrdering = new HashMap<>();
        userOrderingReverse = new HashMap<>();
        users = (HashMap<String, User>) userManager.getUsers();
        items = (ArrayList<GroceryItem>) groceryItemManager.getGroceryItems();
        int order = 0;
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (!entry.getValue().getId().equals(loggedInUser.getId()) && entry.getValue().getGroupName().equals(loggedInUser.getGroupName())) {
                userOrdering.put(entry.getValue(), order);
                userOrderingReverse.put(order++, entry.getValue());
            }
        }
    }

    // need methods to link userOrdering to prices in youOwe and owesYou, getters
    public void calculatePayments() {
        for (GroceryItem g : items) {
            // item is in your group and has been purchased by someone
            if (g.getGroupName().equals(loggedInUser.getGroupName()) && !g.getPurchasedBy().isEmpty() && !g.getWantedBy().isEmpty()) {
                // total price = price of item * size of purchasedBy
                BigDecimal total = new BigDecimal(g.getPrice()).multiply(new BigDecimal(g.getPurchasedBy().size()));
                total = total.setScale(2, RoundingMode.HALF_UP);
                BigDecimal individualPrice = total.divide(new BigDecimal(g.getWantedBy().size()), 2, RoundingMode.HALF_UP);
                if (g.containsPurchasedBy(loggedInUser)) {
                    for (String s : g.getWantedBy()) {
                        if (!s.equals(loggedInUser.getId())) {
                            BigDecimal old = null;
                            if (userOrdering.get(userManager.getUserById(s)) < owesYou.size()) {
                                old = owesYou.get(userOrdering.get(userManager.getUserById(s)));
                            }
                            BigDecimal newPrice = individualPrice;
                            if (old != null) {
                                newPrice = old.add(newPrice);
                            }
                            if (userOrdering.get(userManager.getUserById(s)) < owesYou.size()) {
                                owesYou.set(userOrdering.get(userManager.getUserById(s)), newPrice);
                            } else {
                                owesYou.add(userOrdering.get(userManager.getUserById(s)), newPrice);
                            }

                        }
                    }
                } else if (g.containsWantedBy(loggedInUser)) {
                    for (String s : g.getPurchasedBy()) {
                        BigDecimal old = null;
                        if (userOrdering.get(userManager.getUserById(s)) < youOwe.size()) {
                            old = youOwe.get(userOrdering.get(userManager.getUserById(s)));
                        }
                        BigDecimal newPrice = individualPrice;
                        if (old != null) {
                            newPrice = old.add(newPrice);
                        }
                        if (userOrdering.get(userManager.getUserById(s)) < youOwe.size()) {
                            youOwe.set(userOrdering.get(userManager.getUserById(s)), newPrice);
                        } else {
                            youOwe.add(userOrdering.get(userManager.getUserById(s)), newPrice);
                        }

                    }
                }
            }
        }
    }

    public ArrayList<String> getYouOweList() {
        ArrayList<String> youOweList = new ArrayList<>();
        for (int i = 0; i < youOwe.size(); i++) {
            if (youOwe.get(i) != null) {
                youOweList.add("You owe " + userOrderingReverse.get(i).getUsername() + " $" + youOwe.get(i).toPlainString());
            }
        }
        return youOweList;
    }

    public ArrayList<String> getOwesYouList() {
        ArrayList<String> owesYouList = new ArrayList<>();
        for (int i = 0; i < owesYou.size(); i++) {
            if (owesYou.get(i) != null) {
                owesYouList.add(userOrderingReverse.get(i).getUsername() + " owes you $" + owesYou.get(i).toPlainString());
            }
        }
        return owesYouList;
    }

    public ArrayList<String> getPaymentsList() {
        calculatePayments();
        ArrayList<String> displayList = getOwesYouList();
        displayList.addAll(getYouOweList());
        return displayList;
    }
}
