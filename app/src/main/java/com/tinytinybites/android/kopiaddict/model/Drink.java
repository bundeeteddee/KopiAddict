package com.tinytinybites.android.kopiaddict.model;

import android.support.annotation.ColorRes;

import java.util.UUID;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bundee on 11/1/16.
 *
 */
public class Drink extends RealmObject{
    //Tag
    protected static final String TAG = Drink.class.getSimpleName();

    //Static column key for searching
    public static final String KEY_PREPOPULATED = "prepopulated";
    public static final String KEY_ID = "id";

    //Variables
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private Flavor flavor;
    private RealmList<Sweetener> sweeteners;
    private SweetenerLevel sweetenerLevel;
    private ConcentrationLevel concentrationLevel;
    @Index private boolean iced;
    @Index private boolean prepopulated;
    private String note;

    @Ignore
    private int colorResId;

    /**
     * Default constructor for Realm
     */
    public Drink() {
    }

    /**
     * Constructor
     * @param flavor
     * @param sweeteners
     * @param sweetenerLevel
     * @param concentrationLevel
     * @param iced
     * @param prepopulated
     */
    public Drink(Flavor flavor,
                 RealmList<Sweetener> sweeteners,
                 SweetenerLevel sweetenerLevel,
                 ConcentrationLevel concentrationLevel,
                 boolean iced,
                 boolean prepopulated){
        this.flavor = flavor;
        this.sweeteners = sweeteners;
        this.sweetenerLevel = sweetenerLevel;
        this.concentrationLevel = concentrationLevel;
        this.iced = iced;
        this.prepopulated = prepopulated;
    }

    public ConcentrationLevel getConcentrationLevel() {
        return concentrationLevel;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public String getId() {
        return id;
    }

    public boolean isPrepopulated() {
        return prepopulated;
    }

    public SweetenerLevel getSweetenerLevel() {
        return sweetenerLevel;
    }

    public RealmList<Sweetener> getSweeteners() {
        return sweeteners;
    }

    public boolean isIced() {
        return iced;
    }

    public String getNote(){
        return note;
    }

    public int getColorResId(){
        return colorResId;
    }

    public void setColorResId(@ColorRes int colorResId){
        this.colorResId = colorResId;
    }

    public String getFriendlyDrinkName(){
        //TODO:
        StringBuilder builder = new StringBuilder();
        builder.append(flavor.getSelectionName());
        builder.append("");
        builder.append(getConcentrationLevel().getSelectionName());
        return builder.toString();
    }
}
