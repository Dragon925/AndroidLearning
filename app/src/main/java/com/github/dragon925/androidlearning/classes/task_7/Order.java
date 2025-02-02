package com.github.dragon925.androidlearning.classes.task_7;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private final int id;
    private final Client client;
    private final List<Product> products;

    private boolean isPaid = false;

    public Order(int id, @NonNull Client client) {
        this.id = id;
        this.client = Objects.requireNonNull(client, "client is null");
        this.products = new ArrayList<>();
    }

    public Order(int id, Client client, List<Product> products) {
        this.id = id;
        this.client = client;
        this.products = products;
    }

    public Client getClient() {
        return client;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(@NonNull Product product) {
        products.add(Objects.requireNonNull(product));
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
