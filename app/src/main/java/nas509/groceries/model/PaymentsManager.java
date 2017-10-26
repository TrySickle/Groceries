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
    private HashMap<String, Integer> userOrdering;
    private ArrayList<BigDecimal> youOwe;
    private ArrayList<BigDecimal> owesYou;
    private int order;
    private GroceryItemManager groceryItemManager = GroceryItemManager.getInstance();
    private UserManager userManager = UserManager.getInstance();
    private HashMap<String, User> users = (HashMap<String, User>) userManager.getUsers();
    private ArrayList<GroceryItem> items = (ArrayList<GroceryItem>) groceryItemManager.getGroceryItems();
    User loggedInUser = userManager.getLoggedInUser();

    public PaymentsManager() {
        youOwe = new ArrayList<>();
        owesYou = new ArrayList<>();
        userOrdering = new HashMap<>();
        order = 0;
        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (!entry.getValue().getId().equals(loggedInUser.getId())) {
                userOrdering.put(entry.getValue().getId(), order++);
            }
        }
    }

    // need methods to link userOrdering to prices in youOwe and owesYou, getters
    public String calculatePayments() {
        for (GroceryItem g : items) {
            // item is in your group and has been purchased by someone
            if (g.getGroupName().equals(loggedInUser.getGroupName()) && !g.getPurchasedBy().isEmpty()) {
                // total price = price of item * size of purchasedBy
                BigDecimal total = new BigDecimal(g.getPrice()).multiply(new BigDecimal(g.getPurchasedBy().size()));
                total = total.setScale(2, RoundingMode.HALF_UP);
                BigDecimal individualPrice = total.divide(new BigDecimal(g.getWantedBy().size()), 2, RoundingMode.HALF_UP);
                if (g.containsPurchasedBy(loggedInUser)) {
                    for (String s : g.getWantedBy()) {
                        if (!s.equals(loggedInUser.getId())) {
                            BigDecimal old = owesYou.get(userOrdering.get(s));
                            BigDecimal newPrice = individualPrice;
                            if (old != null) {
                                newPrice = old.add(newPrice);
                            }
                            owesYou.set(userOrdering.get(s), newPrice);
                        }
                    }
                } else if (g.containsWantedBy(loggedInUser)) {
                    for (String s : g.getPurchasedBy()) {
                        BigDecimal old = youOwe.get(userOrdering.get(s));
                        BigDecimal newPrice = individualPrice;
                        if (old != null) {
                            newPrice = old.add(newPrice);
                        }
                        youOwe.set(userOrdering.get(s), newPrice);
                    }
                }
            }
        }
    }
}
