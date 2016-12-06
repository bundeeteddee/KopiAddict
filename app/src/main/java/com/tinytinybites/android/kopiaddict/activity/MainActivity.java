package com.tinytinybites.android.kopiaddict.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.fragment.DashboardFragment;
import com.tinytinybites.android.kopiaddict.fragment.DrinkDetailFragment;
import com.tinytinybites.android.kopiaddict.fragment.DrinkMakerFragment;

public class MainActivity extends AppCompatActivity implements DrinkControlNavigation {

    //Tag
    protected static final String TAG = MainActivity.class.getSimpleName();

    //Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Show dashboard. Don't need backstack as this will be the the prime fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        ft.add(R.id.fragment, DashboardFragment.newInstance(), DashboardFragment.class.getName());
        ft.commit();
    }

    @Override
    public void OnShowDrinkDetails(Fragment originFragment, String drinkId, final View topPanel) {
        //drink detail fragment instance
        DrinkDetailFragment fragment = DrinkDetailFragment.newInstance(drinkId);

        //Basic fragment transaction with animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //If supported, apply shared element (top panel and background)
        //Also, add some transition for all other elements in the fragment (the new fragment)
        //Only for lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Inflate transitions to apply, for both original fragment and to-fragment
            originFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            originFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            originFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_bottom));

            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top));

            if(topPanel != null) {
                ViewCompat.setTransitionName(topPanel, getString(R.string.transition_top_panel));
                ft.addSharedElement(topPanel, getString(R.string.transition_top_panel));
            }
        }

        //Add to fragment manager and add to back stack, retain backstack state
        ft.replace(R.id.fragment, fragment, DrinkDetailFragment.class.getName());
        ft.addToBackStack(DrinkDetailFragment.class.getName());

        ft.commit();
    }

    @Override
    public void OnStartDrinkMaking(Fragment originFragment, View topPanel){
        //drink maker fragment instance
        DrinkMakerFragment fragment = DrinkMakerFragment.newInstance();

        //Basic fragment transaction with animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //If supported, apply shared element (top panel and background)
        //Also, add some transition for all other elements in the fragment (the new fragment)
        //Only for lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Inflate transitions to apply, for both original fragment and to-fragment
            originFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            originFragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            originFragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_bottom));

            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top));

            if(topPanel != null) {
                ViewCompat.setTransitionName(topPanel, getString(R.string.transition_top_panel));
                ft.addSharedElement(topPanel, getString(R.string.transition_top_panel));
            }
        }

        //Add to fragment manager and add to back stack, retain backstack state
        ft.replace(R.id.fragment, fragment, DrinkMakerFragment.class.getName());
        ft.addToBackStack(DrinkMakerFragment.class.getName());

        ft.commit();
    }

    @Override
    public void OnBackPressed() {
        //This is only called by drink details for now, therefore it will always pop everything to the first item
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
