package ru.akirakozov.sd.refactoring.servlet;

public class ProductItem {
    private String name;
    private int price;

    ProductItem(String n, int p) {
        name = n;
        price = p;
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
