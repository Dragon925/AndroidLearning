package com.github.dragon925.androidlearning.classes.task_7;

import androidx.annotation.NonNull;

public class Client {

    private final int id;
    private final String name;
    private ClientActions shop;

    public Client(int id, String name, ClientActions shop) {
        this.id = id;
        this.name = name;
        this.shop = shop;
    }

    public Order createOrder(int orderId) {
        return shop.createOrder(orderId, this);
    }

    public void payOrder(@NonNull Order order) {
        shop.payOrder(order);
    }

    public boolean isInBlacklist() {
        return shop.isInBlacklist(this);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
