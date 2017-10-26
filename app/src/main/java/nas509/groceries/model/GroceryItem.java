package nas509.groceries.model;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by jason on 9/16/2017.
 */

public class GroceryItem {
    private String name;

    private BigDecimal price;
    private int id;
    private String createdUserId;
    private String groupName;
    private ArrayList<String> wantedBy;
    private ArrayList<String> purchasedBy;

    public GroceryItem() {
        this("No name", BigDecimal.ZERO, -1, "", "", null);
    }

    public GroceryItem(String name, String price, String createdUserId, String groupName) {
        this.name = name;
        this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        this.id = GroceryItemManager.getInstance().getNewId();
        this.createdUserId = createdUserId;
        this.groupName = groupName;
        wantedBy = new ArrayList<>();
        wantedBy.add(createdUserId);
        purchasedBy = new ArrayList<>();
    }

    public GroceryItem(String name, String price, String id, String createdUserId, String groupName) {
        this.name = name;
        this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.id = Integer.valueOf(id);
        this.createdUserId = createdUserId;
        this.groupName = groupName;

        purchasedBy = new ArrayList<>();
    }

    public GroceryItem(String name, BigDecimal price, int id, String createdUserId, String groupName, ArrayList<String> wantedBy) {
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.id = id;
        this.createdUserId = createdUserId;
        this.groupName = groupName;
        this.wantedBy = wantedBy;
        purchasedBy = new ArrayList<>();
    }

    public void removeWantedBy(User user) {
        wantedBy.remove(user.getId());
    }

    public void addWantedBy(User user) {
        if (!wantedBy.contains(user.getId())) {
            wantedBy.add(user.getId());
        }
    }

    public boolean containsWantedBy(User user) {
        return wantedBy.contains(user.getId());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price.toPlainString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String toString() {
        return this.name + price.toPlainString();
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public ArrayList<String> getPurchasedBy() { return purchasedBy; }

    public ArrayList<String> getWantedBy() {
        return wantedBy;
    }
}
