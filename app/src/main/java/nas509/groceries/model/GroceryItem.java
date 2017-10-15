package nas509.groceries.model;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by jason on 9/16/2017.
 */

public class GroceryItem {
    private String name;
    private BigDecimal price;
    private int id;

    public GroceryItem() {
        this("No name", BigDecimal.ZERO, -1);
    }

    public GroceryItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.id = GroceryItemManager.getInstance().getNewId();
    }

    public GroceryItem(String name, String price) {
        this(name, new BigDecimal(price));
    }

    public GroceryItem(String name, String price, String id) {
        this.name = name;
        this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.id = Integer.valueOf(id);
    }

    public GroceryItem(String name, BigDecimal price, int id) {
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.id = id;
    }

    public void saveAsText(PrintWriter writer) {
        System.out.println("GroceryItem saving grocery item: " + name);
        writer.println(name + "\t" + price + "\t" + id);
    }

    public static GroceryItem parseEntry(String line) {
        assert line != null;
        String[] tokens = line.split("\t");
        assert tokens.length == 3;
        GroceryItem g = new GroceryItem(tokens[0], tokens[1], tokens[2]);

        return g;
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

    public BigDecimal getPrice() {
        return price;
    }

    public String getPriceString() {
        return price.toPlainString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toString() {
        return this.name + price.toPlainString();
    }
}
