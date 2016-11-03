package com.tinytinybites.android.kopiaddict.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import org.parceler.Parcel;
import com.tinytinybites.android.kopiaddict.common.ResourceUtil;
import com.tinytinybites.android.kopiaddict.model.Drink;

/**
 * Created by bundee on 11/2/16.
 */
@Parcel
public class DashboardViewModel extends BaseObservable implements ViewModel {
    //Tag
    protected static final String TAG = DashboardViewModel.class.getSimpleName();

    //Variables

    /**
     * On click handler for starting new drink
     * @param view
     */
    public void onStartNewConction(View view){

    }

    /**
     * Get the background color of the card for the drink, based on position
     * @param drink
     * @return
     */
    public int getBackgroundColor(Drink drink){
        return ResourceUtil.GetColor(drink.getColorResId());
    }



    /**
     * Constructor
     */
    public DashboardViewModel() {
    }

    @Override
    public void destroy() {

    }


}
