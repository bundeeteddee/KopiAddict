package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import com.tinytinybites.android.kopiaddict.common.ResourceUtil;
import com.tinytinybites.android.kopiaddict.dao.DrinkDao;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDrinkDetailBinding;
import com.tinytinybites.android.kopiaddict.model.Drink;

/**
 * Created by bundee on 11/9/16.
 */

public class DrinkDetailFragment extends Fragment{
    //Tag
    protected static final String TAG = DrinkDetailFragment.class.getSimpleName();

    //Variables
    private FragmentDrinkDetailBinding mBinding;
    private Drink mDrink;

    /**
     * Static constructor
     * @param id
     * @return
     */
    public static DrinkDetailFragment newInstance(String id){
        DrinkDetailFragment fragment = new DrinkDetailFragment();

        //Supply arguments
        Bundle args = new Bundle();
        args.putString(Drink.KEY_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        Bundle arguments = getArguments();
        /*if(savedInstanceState != null &&
                savedInstanceState.containsKey(Drink.KEY_ID)){

            //mDashboardViewModel = Parcels.unwrap(savedInstanceState.getParcelable(BundleUtil.VM_DASHBOARD));
        }else if(arguments != null){

        }*/
        Log.e(TAG, "onCreate: >>> " + arguments);
        mDrink = DrinkDao.getInstance().loadDrink(arguments.getString(Drink.KEY_ID));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelable(BundleUtil.VM_DASHBOARD, Parcels.wrap(mDashboardViewModel));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drink_detail, container, false);
        //mBinding.setDrink(mDrink);

        //Dynamically add drink composition into the layout
        addDrinkComposition();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();
    }

    private void addDrinkComposition(){
        //Get master linear layout container
        LinearLayout ingredientLinearLayout = (LinearLayout) mBinding.getRoot().findViewById(R.id.ingredients_container);

        //TODO: Ensure there is nothing. If not empty, clear it?

        //Get all required components
        int waterLevel = mDrink.getWaterCompositionWeight();
        int evapLevel = mDrink.getEvaporatedMilkWeight();
        int condensedLevel = mDrink.getCondensedMilkWeight();
        int palmSugarLevel = mDrink.getPalmSugarWeight();
        int teaLevel = mDrink.getTeaWeight();
        int coffeeLevel = mDrink.getCoffeeWeight();

        //Get total weight level for weight sum
        float totalWeightLevel = waterLevel + evapLevel + condensedLevel + palmSugarLevel + teaLevel + coffeeLevel;
        //ingredientLinearLayout.setWeightSum(totalWeightLevel);
        ingredientLinearLayout.setWeightSum(1.0f);

        Log.e(TAG, "addDrinkComposition >>>>> " + ingredientLinearLayout.getChildCount() + " >> " + totalWeightLevel);

        //Go through all components and add those that are required
        LayoutInflater layoutInflater = LayoutInflater.from(EApplication.getInstance());
        if(waterLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_water_glass,
                            R.string.drink_composition_water,
                            R.color.drinks_com_water,
                            waterLevel/totalWeightLevel));
        }
        if(evapLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_evaporated_milk,
                            R.string.sweetener_evaporated_milk,
                            R.color.drinks_com_evaporated_milk,
                            evapLevel/totalWeightLevel));
        }
        if(condensedLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_condensed_milk,
                            R.string.sweetener_condensed_milk,
                            R.color.drinks_com_condensed_milk,
                            condensedLevel/totalWeightLevel));
        }
        if(palmSugarLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_gula_melaka,
                            R.string.sweetener_palm_sugar,
                            R.color.drinks_com_palm_sugar,
                            palmSugarLevel/totalWeightLevel));
        }
        if(teaLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_tea_leaves,
                            R.string.flavor_tea,
                            R.color.drinks_com_tea,
                            teaLevel/totalWeightLevel));
        }
        if(coffeeLevel > 0){
            ingredientLinearLayout.addView(
                    initDrinkComponent(layoutInflater,
                            ingredientLinearLayout,
                            R.drawable.img_composition_coffee,
                            R.string.flavor_coffee,
                            R.color.drinks_com_coffee,
                            coffeeLevel/totalWeightLevel));
        }

    }

    /**
     * Inflate and setup a view that corresponds to a drink component, with proper weight for layout and content
     * @param layoutInflater
     * @param root
     * @param imageResId
     * @param stringResId
     * @param bgColorResId
     * @param weightF
     * @return
     */
    private View initDrinkComponent(LayoutInflater layoutInflater,
                                    ViewGroup root,
                                    @DrawableRes int imageResId,
                                    @StringRes int stringResId,
                                    @ColorRes int bgColorResId,
                                    float weightF){
        //Inflate and set background color res
        LinearLayout layoutContainer = (LinearLayout) layoutInflater.inflate(R.layout.linearlayout_item_drink_composition, root, false);
        layoutContainer.setBackgroundResource(bgColorResId);

        //Create layout param and set margin
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        int marginTop = ResourceUtil.ConvertDpToPixels((int)EApplication.getInstance().getResources().getDimension(R.dimen.ingredient_margin_top), EApplication.getInstance());
        lp.topMargin = marginTop;

        //Set image res
        ((ImageView)layoutContainer.findViewById(R.id.ingredient_image)).setImageResource(imageResId);

        //Set string
        ((TextView)layoutContainer.findViewById(R.id.drink_detail_composition_text)).setText(stringResId);

        //Set weight
        lp.weight = weightF;
        layoutContainer.setLayoutParams(lp);

        return layoutContainer;
    }

}
