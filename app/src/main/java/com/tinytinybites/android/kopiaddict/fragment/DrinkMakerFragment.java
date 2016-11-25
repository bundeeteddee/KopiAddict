package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDrinkMakerBinding;
import com.tinytinybites.android.kopiaddict.helper.WaveHelper;
import com.tinytinybites.android.kopiaddict.view.WaveView;
import rx.Subscription;

/**
 * Created by bundee on 11/9/16.
 */

public class DrinkMakerFragment extends Fragment implements View.OnClickListener {
    //Tag
    protected static final String TAG = DrinkMakerFragment.class.getSimpleName();

    //Variables
    private FragmentDrinkMakerBinding mBinding;
    private Subscription mSubscription;
    private WaveView mWaveView;
    private WaveHelper mWaveHelper;
    private LinearLayout mLayout;

    /**
     * Static constructor
     * @return
     */
    public static DrinkMakerFragment newInstance(){
        DrinkMakerFragment fragment = new DrinkMakerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        mWaveHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        mWaveHelper.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drink_maker, container, false);

        //Ui elements
        mWaveView = (WaveView) mBinding.getRoot().findViewById(R.id.wave);
        mWaveHelper = new WaveHelper(mWaveView);
        mLayout = (LinearLayout) mBinding.getRoot().findViewById(R.id.drink_maker_choice_container);
        mLayout.setOnClickListener(this);



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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drink_maker_choice_container:{
                mWaveHelper.increaseWaveLevel();
                break;
            }
        }
    }
}
