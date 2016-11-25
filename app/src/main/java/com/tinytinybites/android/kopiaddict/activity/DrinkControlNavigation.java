package com.tinytinybites.android.kopiaddict.activity;

import android.view.View;

/**
 * Created by bundee on 11/23/16.
 */

public interface DrinkControlNavigation {

    void OnShowDrinkDetails(String drinkId, View topPanel, View background);
    void OnStartDrinkMaking(View background);
    void OnBackPressed();

}
