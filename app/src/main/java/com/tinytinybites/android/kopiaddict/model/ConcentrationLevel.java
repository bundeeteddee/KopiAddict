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
public class ConcentrationLevel extends RealmObject {
    //Tag
    protected static final String TAG = ConcentrationLevel.class.getSimpleName();

    //Type Options
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_WEAKER, TYPE_NORMAL, TYPE_STRONGER})
    public @interface ConcentrationLevelType {}
    public static final int TYPE_WEAKER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_STRONGER = 2;

    //Variables
    @PrimaryKey
    private @ConcentrationLevelType int id;

    @Index
    private String name; //i.e. 'Weaker', 'Normal', 'Stronger'

    private String selectionName; //i.e. 'PO', 'Gao' etc

    /**
     * Convenient constructor based on type
     * @param type
     */
    public ConcentrationLevel(@NonNull @ConcentrationLevelType int type){
        id = type;
        name = getNameForId(type);
        selectionName = getSelectionNameForId(type);
    }

    /**
     * Given the type, return the name (the name of the type seen by user when choosing)
     * i.e. '0%', '50%', '150%'
     * @param type
     * @return
     */
    private String getNameForId(@NonNull @ConcentrationLevelType int type){
        switch (type){
            case TYPE_WEAKER: return EApplication.getInstance().getString(R.string.concentration_level_weaker);
            case TYPE_NORMAL: return EApplication.getInstance().getString(R.string.concentration_level_normal);
            case TYPE_STRONGER: return EApplication.getInstance().getString(R.string.concentration_level_stronger);
        }
        return null;
    }

    /**
     * Given the type, return the selection name (the local name of the type)
     * i.e. 'Kopi', 'Teh', 'Yuan Yang'
     * @param type
     * @return
     */
    private String getSelectionNameForId(@NonNull @ConcentrationLevelType int type){
        switch (type){
            case TYPE_WEAKER: return EApplication.getInstance().getString(R.string.concentration_level_weaker_local);
            case TYPE_NORMAL: return "";
            case TYPE_STRONGER: return EApplication.getInstance().getString(R.string.concentration_level_stronger_local);
        }
        return null;
    }
}
