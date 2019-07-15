package com.loftschool.fomin.moneyloft;

public class Item {
    private String name;
    private int price;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }
}