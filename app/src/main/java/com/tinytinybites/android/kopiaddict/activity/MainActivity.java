package com.tinytinybites.android.kopiaddict.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;

import com.tinytinybites.android.kopiaddict.R;
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
    }

    @Override
    public void OnShowDrinkDetails(String drinkId, View topPanel, View background) {
        //drink detail fragment instance
        DrinkDetailFragment fragment = DrinkDetailFragment.newInstance(drinkId);

        //Basic fragment transaction with animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        //If supported, apply shared element (top panel and background)
        //Also, add some transition for all other elements in the fragment (the new fragment)
        //Only for lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Inflate transitions to apply
            Transition noTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition);
            Transition slideRightTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right);

            //Setup enter transition on fragment
            fragment.setSharedElementEnterTransition(noTransform); //Transition for shared elements
            fragment.setEnterTransition(slideRightTransform); //Transition for fragment entering

            //Check transition ui elements, add shared elements
            if(background != null && topPanel != null) {
                ViewCompat.setTransitionName(topPanel, getString(R.string.transition_top_panel));
                ViewCompat.setTransitionName(background, getString(R.string.transition_background));
                ft.addSharedElement(background, getString(R.string.transition_background));
                ft.addSharedElement(topPanel, getString(R.string.transition_top_panel));
            }
        }

        //Add to fragment manager and add to back stack, retain backstack state
        ft.add(R.id.fragment, fragment, DrinkDetailFragment.class.getName());
        ft.addToBackStack(DrinkDetailFragment.class.getName());
        ft.commit();
    }

    @Override
    public void OnStartDrinkMaking(View background){
        //drink maker fragment instance
        DrinkMakerFragment fragment = DrinkMakerFragment.newInstance();

        //Basic fragment transaction with animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        //If supported, apply shared element (top panel and background)
        //Also, add some transition for all other elements in the fragment (the new fragment)
        //Only for lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Inflate transitions to apply
            Transition noTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition);
            Transition slideRightTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right);

            //Setup enter transition on fragment
            fragment.setSharedElementEnterTransition(noTransform); //Transition for shared elements
            fragment.setEnterTransition(slideRightTransform); //Transition for fragment entering

            //Check transition ui elements, add shared elements
            if(background != null) {
                ViewCompat.setTransitionName(background, getString(R.string.transition_background));
                ft.addSharedElement(background, getString(R.string.transition_background));
            }
        }

        //Add to fragment manager and add to back stack, retain backstack state
        ft.add(R.id.fragment, fragment, DrinkMakerFragment.class.getName());
        ft.addToBackStack(DrinkMakerFragment.class.getName());
        ft.commit();
    }

    @Override
    public void OnBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();
    }

}
