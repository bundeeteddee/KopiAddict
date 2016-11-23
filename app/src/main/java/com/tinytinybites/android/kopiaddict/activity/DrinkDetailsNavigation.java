package com.tinytinybites.android.kopiaddict.activity;

import android.view.View;

/**
 * Created by bundee on 11/23/16.
 */

public interface DrinkDetailsNavigation {

    void OnShowDrinkDetails(String drinkId, View topPanel, View background);
    void OnBackPressed();

}
