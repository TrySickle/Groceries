package nas509.groceries.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by jason on 9/16/2017.
 */

public class GroceryItem {
    private String name;
    private BigDecimal price;

    public GroceryItem() {
        this("No name", BigDecimal.ZERO);
    }

    public GroceryItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public GroceryItem(String name, String price) {
        this(name, new BigDecimal(price));
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
