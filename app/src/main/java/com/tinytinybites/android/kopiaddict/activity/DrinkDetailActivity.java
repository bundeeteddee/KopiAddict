package com.tinytinybites.android.kopiaddict.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.fragment.DrinkDetailFragment;
import com.tinytinybites.android.kopiaddict.model.Drink;

public class DrinkDetailActivity extends AppCompatActivity {
    //Tag
    protected static final String TAG = DrinkDetailActivity.class.getSimpleName();

    //Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        String id = getIntent().getStringExtra(Drink.KEY_ID);

        showDrinkDetailFragment(id);
    }

    private void showDrinkDetailFragment(String id){
        //Quiz fragment instance
        DrinkDetailFragment fragment = DrinkDetailFragment.newInstance(id);

        //Basic fragment transaction with animation
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        ft.replace(R.id.fragment, fragment, DrinkDetailFragment.class.getName());
        ft.commit();
    }
}
