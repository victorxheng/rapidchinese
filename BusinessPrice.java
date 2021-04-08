package com.victorcheng;

public class BusinessPrice {
    private String name;
    private long price;

    public BusinessPrice(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }


    @Override
    public String toString() {
        return name + "\nCOST: $" + price;
    }
}
