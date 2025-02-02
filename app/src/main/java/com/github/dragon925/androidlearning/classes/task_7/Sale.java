package com.github.dragon925.androidlearning.classes.task_7;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Sale {

    private final long timestamp;
    private final Order order;

    public Sale(long timestamp, @NonNull Order order) {
        this.timestamp = timestamp;
        if (!Objects.requireNonNull(order, "order is null").isPaid()) {
            throw new IllegalArgumentException("order is not paid");
        }
        this.order = order;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Order getOrder() {
        return order;
    }
}
