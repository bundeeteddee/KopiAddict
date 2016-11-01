package com.tinytinybites.android.kopiaddict.model;

import java.util.UUID;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bundee on 11/1/16.
 *
 */
public class Drink extends RealmObject{
    //Tag
    protected static final String TAG = Drink.class.getSimpleName();

    //Variables
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @Index
    private Flavor flavor;
    private RealmList<Sweetener> sweeteners;




}
