package com.tinytinybites.android.kopiaddict.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.common.ResourceUtil;
import com.tinytinybites.android.kopiaddict.model.Drink;
import com.tinytinybites.android.kopiaddict.viewholder.DrinkViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by bundee on 11/2/16.
 */

public class DrinkRecyclerViewAdapter extends RealmRecyclerViewAdapter<Drink, DrinkViewHolder> {
    //Tag
    protected static final String TAG = DrinkRecyclerViewAdapter.class.getSimpleName();

    //Variables

    /**
     * Constructor
     * @param context
     * @param data
     */
    public DrinkRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Drink> data) {
        super(context, data, true);
    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_drink, parent, false);
        DrinkViewHolder holder = new DrinkViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position) {
        //Get item
        Drink drink = getData().get(position);

        //Set card bg color here
        drink.setColorResId(ResourceUtil.GetColor(getCardBackgroundColorBasedOnPosition(position)));
        holder.getBinding().setVariable(com.tinytinybites.android.kopiaddict.BR.drink, drink);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    private int getCardBackgroundColorBasedOnPosition(int position){
        switch ((position+1)%7){
            case 0: return R.color.drinks_card_bg_1;
            case 1: return R.color.drinks_card_bg_2;
            case 2: return R.color.drinks_card_bg_3;
            case 3: return R.color.drinks_card_bg_4;
            case 4: return R.color.drinks_card_bg_5;
            case 5: return R.color.drinks_card_bg_6;
            default: return R.color.drinks_card_bg_7;
        }
    }

}
