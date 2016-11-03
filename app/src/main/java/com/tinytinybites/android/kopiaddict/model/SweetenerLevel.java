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
public class SweetenerLevel extends RealmObject {
    //Tag
    protected static final String TAG = SweetenerLevel.class.getSimpleName();

    //Database key for search/load
    public static final String KEY_ID = "id";

    //Type Options
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_0, TYPE_25, TYPE_50, TYPE_100, TYPE_150})
    public @interface SweetenerLevelType {}
    public static final int TYPE_0 = 0;
    public static final int TYPE_25 = 1;
    public static final int TYPE_50 = 2;
    public static final int TYPE_100 = 3;
    public static final int TYPE_150 = 4;

    //Variables
    @PrimaryKey
    private @SweetenerLevelType int id;

    @Index
    private String name; //i.e. '0%', '50%', '150%'

    private String selectionName; //i.e. 'Kosong', 'Siew Dai', 'Ga Dai' etc


    /**
     * Default constructor for Realm
     */
    public SweetenerLevel() {
    }

    /**
     * Convenient constructor based on type
     * @param type
     */
    public SweetenerLevel(@NonNull @SweetenerLevelType int type){
        id = type;
        name = getNameForId(type);
        selectionName = getSelectionNameForId(type);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSelectionName() {
        return selectionName;
    }

    /**
     * Given the type, return the name (the name of the type seen by user when choosing)
     * i.e. '0%', '50%', '150%'
     * @param type
     * @return
     */
    private String getNameForId(@NonNull @SweetenerLevelType int type){
        switch (type){
            case TYPE_0: return "0%";
            case TYPE_25: return "25%";
            case TYPE_50: return "50%";
            case TYPE_100: return "100%";
            case TYPE_150: return "150%";
        }
        return null;
    }

    /**
     * Given the type, return the selection name (the local name of the type)
     * i.e. 'Kopi', 'Teh', 'Yuan Yang'
     * @param type
     * @return
     */
    private String getSelectionNameForId(@NonNull @SweetenerLevelType int type){
        switch (type){
            case TYPE_0: return EApplication.getInstance().getString(R.string.sweetener_level_0p);
            case TYPE_25: return EApplication.getInstance().getString(R.string.sweetener_level_25p);
            case TYPE_50: return EApplication.getInstance().getString(R.string.sweetener_level_50p);
            case TYPE_100: return EApplication.getInstance().getString(R.string.sweetener_level_100p);
            case TYPE_150: return EApplication.getInstance().getString(R.string.sweetener_level_150p);
        }
        return null;
    }
}
