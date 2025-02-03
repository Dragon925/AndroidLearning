package com.github.dragon925.androidlearning.classes.task7;

import androidx.annotation.NonNull;

public interface MerchandiserActions {

    void addProduct(@NonNull Product product);

    Sale registerateSale(@NonNull Order order, long time);

    void addToBlackList(@NonNull Client client);
}
