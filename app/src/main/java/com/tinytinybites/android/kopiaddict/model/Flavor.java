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
 * 3 possible types only: Kopi, Teh, Yuan Yang
 */
public class Flavor extends RealmObject{
    //Tag
    protected static final String TAG = Flavor.class.getSimpleName();

    //Type Options
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_KOPI, TYPE_TEH, TYPE_YUAN_YANG})
    public @interface FlavorType {}
    public static final int TYPE_KOPI = 0;
    public static final int TYPE_TEH = 1;
    public static final int TYPE_YUAN_YANG = 2;

    //Variables
    @PrimaryKey
    private @FlavorType int id;

    @Index
    private String name; //i.e. 'Coffee', 'Tea', 'Coffee + Tea'

    private String selectionName; //i.e. 'Kopi', 'Teh', 'Yuan Yang'

    /**
     * Convenient constructor based on type
     * @param type
     */
    public Flavor(@NonNull @FlavorType int type){
        id = type;
        name = getNameForId(type);
        selectionName = getSelectionNameForId(type);
    }

    /**
     * Given the type, return the name (the name of the type seen by user when choosing)
     * i.e. 'Coffee', 'Tea', 'Coffee + Tea'
     * @param type
     * @return
     */
    private String getNameForId(@NonNull @FlavorType int type){
        switch (type){
            case TYPE_KOPI: return EApplication.getInstance().getString(R.string.flavor_coffee);
            case TYPE_TEH: return EApplication.getInstance().getString(R.string.flavor_tea);
            case TYPE_YUAN_YANG: return EApplication.getInstance().getString(R.string.flavor_both);
        }
        return null;
    }

    /**
     * Given the type, return the selection name (the local name of the type)
     * i.e. 'Kopi', 'Teh', 'Yuan Yang'
     * @param type
     * @return
     */
    private String getSelectionNameForId(@NonNull @FlavorType int type){
        switch (type){
            case TYPE_KOPI: return EApplication.getInstance().getString(R.string.flavor_coffee_local);
            case TYPE_TEH: return EApplication.getInstance().getString(R.string.flavor_tea_local);
            case TYPE_YUAN_YANG: return EApplication.getInstance().getString(R.string.flavor_both_local);
        }
        return null;
    }

}
