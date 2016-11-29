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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.activity.DrinkControlNavigation;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import com.tinytinybites.android.kopiaddict.common.ResourceUtil;
import com.tinytinybites.android.kopiaddict.dao.DrinkDao;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDrinkDetailBinding;
import com.tinytinybites.android.kopiaddict.model.Drink;
import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by bundee on 11/9/16.
 */

public class DrinkDetailFragment extends Fragment implements View.OnClickListener{
    //Tag
    protected static final String TAG = DrinkDetailFragment.class.getSimpleName();

    //Variables
    private FragmentDrinkDetailBinding mBinding;
    private String mDrinkId;
    private Drink mDrink;
    private LinearLayout mIngredientLinearLayout;
    private ImageView mCloseButton;
    private Subscription mSubscription;
    private Realm mRealm;

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
        mDrinkId = arguments.getString(Drink.KEY_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();

        mRealm = Realm.getDefaultInstance();

        //From a list of observable Views, break it down to individual observable and subscribe to that
        mSubscription = mRealm.where(Drink.class).equalTo(Drink.KEY_ID, mDrinkId).findFirstAsync().asObservable()
                .filter(new Func1<RealmObject, Boolean>() {
                    @Override
                    public Boolean call(RealmObject realmObject) {
                        return realmObject.isLoaded();
                    }
                })
                .map(new Func1<RealmObject, List<View>>() {
                    @Override
                    public List<View> call(RealmObject realmObject) {
                        return getDrinkCompositionViews((Drink) realmObject);
                    }
                })
                .flatMap(new Func1<List<View>, Observable<View>>() {
                    @Override
                    public Observable<View> call(List<View> views) {
                        return Observable.from(views);
                    }
                })
                .subscribe(new Subscriber<View>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "On Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error : " + e.toString());
                    }

                    @Override
                    public void onNext(View view) {
                        mIngredientLinearLayout.addView(view);

                        Animation animation = AnimationUtils.loadAnimation(EApplication.getInstance(), R.anim.enter_from_bottom);
                        animation.setStartOffset(0);
                        view.startAnimation(animation);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();

        mRealm.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drink_detail, container, false);
        mDrink = DrinkDao.getInstance().loadDrink(mDrinkId);
        mBinding.setDrink(mDrink);

        //Get reference to ingredient linear layout
        mIngredientLinearLayout = (LinearLayout) mBinding.getRoot().findViewById(R.id.ingredients_container);
        mCloseButton = (ImageView) mBinding.getRoot().findViewById(R.id.close);

        //Set listeners
        mCloseButton.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();

        //Clean up subscription if need to
        if(mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    /**
     * Get a list of composition views based on a given drink
     * @return
     */
    private List<View> getDrinkCompositionViews(Drink drink){
        ArrayList<View> compositionViews = new ArrayList<>();

        //Get all required components
        int waterLevel = drink.getWaterCompositionWeight();
        int evapLevel = drink.getEvaporatedMilkWeight();
        int condensedLevel = drink.getCondensedMilkWeight();
        int palmSugarLevel = drink.getPalmSugarWeight();
        int teaLevel = drink.getTeaWeight();
        int coffeeLevel = drink.getCoffeeWeight();

        //Get total weight level for weight sum
        float totalWeightLevel = waterLevel + evapLevel + condensedLevel + palmSugarLevel + teaLevel + coffeeLevel;

        //Go through all components and add those that are required
        LayoutInflater layoutInflater = LayoutInflater.from(EApplication.getInstance());
        boolean notFirst = false;
        if(waterLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_water_glass,
                            R.string.drink_composition_water,
                            R.color.drinks_com_water,
                            waterLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }
        if(evapLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_evaporated_milk,
                            R.string.sweetener_evaporated_milk,
                            R.color.drinks_com_evaporated_milk,
                            evapLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }
        if(condensedLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_condensed_milk,
                            R.string.sweetener_condensed_milk,
                            R.color.drinks_com_condensed_milk,
                            condensedLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }
        if(palmSugarLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_gula_melaka,
                            R.string.sweetener_palm_sugar,
                            R.color.drinks_com_palm_sugar,
                            palmSugarLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }
        if(teaLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_tea_leaves,
                            R.string.flavor_tea,
                            R.color.drinks_com_tea,
                            teaLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }
        if(coffeeLevel > 0){
            compositionViews.add(
                    initDrinkComponent(layoutInflater,
                            R.drawable.img_composition_coffee,
                            R.string.flavor_coffee,
                            R.color.drinks_com_coffee,
                            coffeeLevel/totalWeightLevel,
                            notFirst));
            notFirst = true;
        }

        return compositionViews;
    }

    /**
     * Inflate and setup a view that corresponds to a drink component, with proper weight for layout and content
     * @param layoutInflater
     * @param imageResId
     * @param stringResId
     * @param bgColorResId
     * @param weightF
     * @return
     */
    private View initDrinkComponent(LayoutInflater layoutInflater,
                                    @DrawableRes int imageResId,
                                    @StringRes int stringResId,
                                    @ColorRes int bgColorResId,
                                    float weightF,
                                    boolean addMargin){
        //Inflate and set background color res
        LinearLayout layoutContainer = (LinearLayout) layoutInflater.inflate(R.layout.linearlayout_item_drink_composition, null, false);
        layoutContainer.setBackgroundResource(bgColorResId);

        //Create layout param and set margin
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        if(addMargin) {
            int marginTop = ResourceUtil.ConvertDpToPixels((int) EApplication.getInstance().getResources().getDimension(R.dimen.ingredient_margin_top), EApplication.getInstance());
            lp.topMargin = marginTop;
        }

        //Set image res
        ((ImageView)layoutContainer.findViewById(R.id.ingredient_image)).setImageResource(imageResId);

        //Set string
        ((TextView)layoutContainer.findViewById(R.id.drink_detail_composition_text)).setText(stringResId);

        //Set weight
        lp.weight = weightF;
        layoutContainer.setLayoutParams(lp);

        return layoutContainer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:{
                ((DrinkControlNavigation)getActivity()).OnBackPressed();
                break;
            }
        }
    }
}
