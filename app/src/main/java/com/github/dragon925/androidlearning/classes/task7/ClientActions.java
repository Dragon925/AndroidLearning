package com.github.dragon925.androidlearning.classes.task7;

import androidx.annotation.NonNull;

public interface ClientActions {

    Order createOrder(int orderId, @NonNull Client client);

    void payOrder(@NonNull Order order);

    boolean isInBlacklist(@NonNull Client client);
}
