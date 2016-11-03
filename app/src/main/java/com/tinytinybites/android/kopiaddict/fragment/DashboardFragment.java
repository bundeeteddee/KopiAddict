package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;
import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.adapter.DrinkRecyclerViewAdapter;
import com.tinytinybites.android.kopiaddict.common.BundleUtil;
import com.tinytinybites.android.kopiaddict.dao.DrinkDao;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDashboardBinding;
import com.tinytinybites.android.kopiaddict.view.DividerItemDecoration;
import com.tinytinybites.android.kopiaddict.viewmodel.DashboardViewModel;

/**
 * Created by bundee on 11/2/16.
 */

public class DashboardFragment extends Fragment {
    //Tag
    protected static final String TAG = DashboardFragment.class.getSimpleName();

    //Variables
    private FragmentDashboardBinding mBinding;
    private DashboardViewModel mDashboardViewModel;
    private RecyclerView mRecyclerView;

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

        //Customize fan layout and setup recycler view
        mRecyclerView = (RecyclerView) mBinding.getRoot().findViewById(R.id.recycler_view);
        FanLayoutManagerSettings fanLayoutManagerSettings = FanLayoutManagerSettings
                                                            .newBuilder(getContext())
                                                            .withFanRadius(true)
                                                            .withAngleItemBounce(8)
                                                            .withViewWidthDp(170)
                                                            .withViewHeightDp(200)
                                                            .build();
        FanLayoutManager fanLayoutManager = new FanLayoutManager(getContext(), fanLayoutManagerSettings);
        mRecyclerView.setLayoutManager(fanLayoutManager);

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new DrinkRecyclerViewAdapter(getActivity(), DrinkDao.getInstance().loadAllAsync()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();
    }


}
