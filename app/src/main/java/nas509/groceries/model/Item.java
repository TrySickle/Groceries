package nas509.groceries.model;

import java.math.BigDecimal;

/**
 * Created by jason on 9/16/2017.
 */

public class Item {
    private String name;
    private BigDecimal price;

    public Item() {
        this("No name", BigDecimal.ZERO);
    }

    public Item(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String toString() {
        return this.name + price.toPlainString();
    }
}
