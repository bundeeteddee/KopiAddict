package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;
import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.activity.DrinkDetailsNavigation;
import com.tinytinybites.android.kopiaddict.adapter.DrinkRecyclerViewAdapter;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import com.tinytinybites.android.kopiaddict.common.BundleUtil;
import com.tinytinybites.android.kopiaddict.dao.DrinkDao;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDashboardBinding;
import com.tinytinybites.android.kopiaddict.viewmodel.DashboardViewModel;

/**
 * Created by bundee on 11/2/16.
 */

public class DashboardFragment extends Fragment implements DrinkRecyclerViewAdapter.OnDrinkSelectedListener {
    //Tag
    protected static final String TAG = DashboardFragment.class.getSimpleName();

    //Variables
    private FragmentDashboardBinding mBinding;
    private FanLayoutManager mFanLayoutManager;
    private DrinkRecyclerViewAdapter mDrinksAdapter;
    private DashboardViewModel mDashboardViewModel;
    private RecyclerView mRecyclerView;
    private TextView mHeaderTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if(savedInstanceState != null &&
                savedInstanceState.containsKey(BundleUtil.VM_DASHBOARD)){
            mDashboardViewModel = Parcels.unwrap(savedInstanceState.getParcelable(BundleUtil.VM_DASHBOARD));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BundleUtil.VM_DASHBOARD, Parcels.wrap(mDashboardViewModel));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        mDashboardViewModel = new DashboardViewModel();
        mBinding.setDashboardViewModel(mDashboardViewModel);

        //Apply quick dirty way for custom font for header
        //Ref: https://futurestud.io/tutorials/custom-fonts-on-android-quick-and-dirty
        mHeaderTextView = (TextView) mBinding.getRoot().findViewById(R.id.header);
        Typeface typeface = Typeface.createFromAsset(EApplication.getInstance().getAssets(),"angelina.TTF");
        mHeaderTextView.setTypeface(typeface);

        //Customize fan layout and setup recycler view
        mRecyclerView = (RecyclerView) mBinding.getRoot().findViewById(R.id.recycler_view);
        FanLayoutManagerSettings fanLayoutManagerSettings = FanLayoutManagerSettings
                                                            .newBuilder(getContext())
                                                            .withFanRadius(true)
                                                            .withAngleItemBounce(8)
                                                            .withViewWidthDp(140)
                                                            .withViewHeightDp(180)
                                                            .build();
        mFanLayoutManager = new FanLayoutManager(getContext(), fanLayoutManagerSettings);
        mRecyclerView.setLayoutManager(mFanLayoutManager);
        mDrinksAdapter = new DrinkRecyclerViewAdapter(getActivity(), DrinkDao.getInstance().loadAllAsync());
        mDrinksAdapter.setDrinkSelectedListener(this);

        mRecyclerView.setAdapter(mDrinksAdapter);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setChildDrawingOrderCallback(new FanChildDrawingOrderCallback(mFanLayoutManager));

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();
    }

    @Override
    public void OnDrinkClicked(int pos, final View view) {
        if (mFanLayoutManager.getSelectedItemPosition() != pos) {
            mFanLayoutManager.switchItem(mRecyclerView, pos);
        } else {
            //Use fragment manager, instead of a new activity
            /*Intent drinkDetailsIntent = new Intent(getActivity(), DrinkDetailActivity.class);
            drinkDetailsIntent.putExtra(Drink.KEY_ID, ((DrinkRecyclerViewAdapter)mRecyclerView.getAdapter()).getItem(mFanLayoutManager.getSelectedItemPosition()).getId());
            getActivity().startActivity(drinkDetailsIntent);*/
            ((DrinkDetailsNavigation)getActivity()).OnShowDrinkDetails(((DrinkRecyclerViewAdapter)mRecyclerView.getAdapter()).getItem(mFanLayoutManager.getSelectedItemPosition()).getId(),
                    mBinding.topPanel,
                    mBinding.mainBackground);
        }
    }

    public boolean deselectIfSelected() {
        if (mFanLayoutManager.isItemSelected()) {
            mFanLayoutManager.deselectItem();
            return true;
        } else {
            return false;
        }
    }
}
