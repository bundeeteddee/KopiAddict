package com.tinytinybites.android.kopiaddict.activity;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by bundee on 11/23/16.
 */

public interface DrinkControlNavigation {

    void OnShowDrinkDetails(Fragment originFragment, String drinkId, View topPanel);
    void OnStartDrinkMaking(Fragment originFragment, View topPanel);
    void OnBackPressed();

}
