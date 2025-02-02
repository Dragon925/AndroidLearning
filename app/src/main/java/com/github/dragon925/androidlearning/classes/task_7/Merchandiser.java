package com.github.dragon925.androidlearning.classes.task_7;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Merchandiser {
    private final MerchandiserActions shop;

    public Merchandiser(MerchandiserActions shop) {
        this.shop = shop;
    }

    public void addProduct(@NonNull Product product) {
        shop.addProduct(Objects.requireNonNull(product, "product is null"));
    }

    public Sale registerateSale(@NonNull Order order, long time) {
        if (!Objects.requireNonNull(order, "order is null").isPaid()) {
            addToBlackList(order.getClient());
            throw new IllegalArgumentException("order is not paid");
        }
        return shop.registerateSale(order, time);
    }

    public void addToBlackList(@NonNull Client client) {
        shop.addToBlackList(client);
    }
}
