package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinytinybites.android.kopiaddict.R;
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
        mBinding.setDrink(mDrink);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();
    }

}
