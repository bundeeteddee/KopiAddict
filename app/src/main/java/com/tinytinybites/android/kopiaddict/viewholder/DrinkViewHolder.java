package com.tinytinybites.android.kopiaddict.viewholder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bundee on 11/2/16.
 */
public class DrinkViewHolder extends RecyclerView.ViewHolder {
    //Tag
    protected static final String TAG = DrinkViewHolder.class.getSimpleName();

    //Variables
    private ViewDataBinding mBinding;

    public DrinkViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBinding(){
        return mBinding;
    }

}
