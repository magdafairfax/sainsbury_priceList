package entity;

import java.util.List;

public class Results {
    private List<Product> products;
    private  Total total;

    public Results(List<Product> products, Total total) {
        this.products = products;
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

}
