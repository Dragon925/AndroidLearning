package com.github.dragon925.androidlearning.classes.task7;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Задача на взаимодействие между классами. Разработать систему «Интернет-магазин».
 * <p>
 * Товаровед добавляет информацию о Товаре.
 * <p>
 * Клиент делает и оплачивает Заказ на Товары.
 * <p>
 * Товаровед регистрирует Продажу и может занести неплательщика в «черный список».
 */
public class InternetShop implements MerchandiserActions, ClientActions {

    private final List<Product> products = new ArrayList<>();
    private final List<Client> clients = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Sale> sales = new ArrayList<>();
    private final List<Client> blackList = new ArrayList<>();
    private final Merchandiser merchandiser;

    public InternetShop() {
        merchandiser = new Merchandiser(this);
    }

    public Client registerClient(int id, @NonNull String name) {
        Client client = new Client(id, Objects.requireNonNull(name, "name is null"), this);
        clients.add(client);
        return client;
    }

    @Override
    public void addProduct(@NonNull Product product) {
        products.add(Objects.requireNonNull(product, "product is null"));
    }

    @Override
    public Sale registerateSale(@NonNull Order order, long time) {
        var sale = new Sale(time, order);
        sales.add(sale);
        return sale;
    }

    @Override
    public void addToBlackList(@NonNull Client client) {
        if (Objects.nonNull(client) && !blackList.contains(client)) {
            blackList.add(client);
        }
    }

    @Override
    public boolean isInBlacklist(@NonNull Client client) {
        return blackList.contains(client);
    }

    @Override
    public Order createOrder(int orderId, @NonNull Client client) {
        if (isInBlacklist(client)) {
            throw new IllegalStateException("client is in blacklist");
        }

        var order = new Order(orderId, client);
        orders.add(order);
        return order;
    }

    @Override
    public void payOrder(@NonNull Order order) {
        Objects.requireNonNull(order, "order is null").setPaid(true);
    }

    public Merchandiser getMerchandiser() {
        return merchandiser;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public List<Client> getBlackList() {
        return blackList;
    }
}
