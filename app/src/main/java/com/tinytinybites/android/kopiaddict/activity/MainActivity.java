package com.tinytinybites.android.kopiaddict.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tinytinybites.android.kopiaddict.R;

public class MainActivity extends AppCompatActivity {
    //Tag
    protected static final String TAG = MainActivity.class.getSimpleName();

    //Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
