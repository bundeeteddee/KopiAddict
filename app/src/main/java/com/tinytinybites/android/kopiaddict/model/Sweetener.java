package com.tinytinybites.android.kopiaddict.model;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bundee on 11/1/16.
 */
public class Sweetener extends RealmObject {
    //Tag
    protected static final String TAG = Sweetener.class.getSimpleName();

    //Variables
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_CONDENSED_MILK, TYPE_EVAPORATED_MILK, TYPE_PALM_SUGAR})
    public @interface SweetenerType {}
    public static final int TYPE_CONDENSED_MILK = 0;
    public static final int TYPE_EVAPORATED_MILK = 1;
    public static final int TYPE_PALM_SUGAR = 2;

    //Variables
    @PrimaryKey
    private @SweetenerType int id;

    @Index
    private String name; //i.e. 'Condensed Milk', 'Evaporated Milk', 'Palm Sugar'
    private String selectionName; //i.e. '' for condensed milk, 'C' for evaporated milk, 'Gula Melaka' for palm sugar

    /**
     * Convenient constructor based on type
     * @param type
     */
    public Sweetener(@NonNull @SweetenerType int type){
        id = type;
        name = getNameForId(type);
        selectionName = getSelectionNameForId(type);
    }

    /**
     * Given the type, return the name (the name of the type seen by user when choosing)
     * i.e. 'Condensed Milk', 'Evaporated Milk', 'Palm Sugar'
     * @param type
     * @return
     */
    private String getNameForId(@NonNull @SweetenerType int type){
        switch (type){
            case TYPE_CONDENSED_MILK: return EApplication.getInstance().getString(R.string.sweetener_condensed_milk);
            case TYPE_EVAPORATED_MILK: return EApplication.getInstance().getString(R.string.sweetener_evaporated_milk);
            case TYPE_PALM_SUGAR: return EApplication.getInstance().getString(R.string.sweetener_palm_sugar);
        }
        return null;
    }

    /**
     * Given the type, return the selection name (the local name of the type)
     * i.e. '' for condensed milk, 'C' for evaporated milk, 'Gula Melaka' for palm sugar
     * @param type
     * @return
     */
    private String getSelectionNameForId(@NonNull @SweetenerType int type){
        switch (type){
            case TYPE_CONDENSED_MILK: return "";
            case TYPE_EVAPORATED_MILK: return EApplication.getInstance().getString(R.string.sweetener_evaporated_milk_local);
            case TYPE_PALM_SUGAR: return EApplication.getInstance().getString(R.string.sweetener_palm_sugar_local);
        }
        return null;
    }


}
