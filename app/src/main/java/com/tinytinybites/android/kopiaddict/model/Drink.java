package com.tinytinybites.android.kopiaddict.model;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.UUID;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.application.EApplication;
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

    /**
     * Data Bind method
     * @return
     */
    public String getFriendlyDrinkName(){
        StringBuilder builder = new StringBuilder();
        builder.append(getFlavor().getSelectionName());

        //Check on sweetener combos
        if(getSweeteners().isEmpty()){
            //"O"
            builder.append(" ");
            builder.append(EApplication.getInstance().getString(R.string.drink_combo_o));
        }else{
            boolean hasEvapMilk = false;
            boolean hasCondensedMilk = false;
            boolean hasPalmSugar = false;
            boolean completed = false;
            for(Sweetener sweetener: getSweeteners()){
                if(sweetener.isEvaporatedMilk()){
                    hasEvapMilk = true;
                }else if(sweetener.isCondensedMilk()){
                    hasCondensedMilk = true;
                }else if(sweetener.isPalmSugar()){
                    hasPalmSugar = true;
                }
            }

            if(hasEvapMilk){
                if(hasPalmSugar){
                    builder.append(" ");
                    builder.append(EApplication.getInstance().getString(R.string.sweetener_palm_sugar_local));
                    completed = true;
                }else if(hasCondensedMilk){
                    if(!getFlavor().isYuanYang()){
                        builder.append(" + ");
                        builder.append(EApplication.getInstance().getString(R.string.drink_combo_c));
                        completed = true;
                    }
                }
            }

            if(!completed){
                if(!hasCondensedMilk){
                    builder.append(" ");
                    builder.append(EApplication.getInstance().getString(R.string.drink_combo_c));
                }
            }
        }

        //Check sweetness level
        if(getSweetenerLevel().getSweetenessFriendlyString() != null){
            builder.append(" ");
            builder.append(getSweetenerLevel().getSweetenessFriendlyString());
        }

        //Check concentration level
        if(getConcentrationLevel().getConcentrationFriendlyString() != null){
            builder.append(" ");
            builder.append(getConcentrationLevel().getConcentrationFriendlyString());
        }

        //Check for ice
        if(isIced()){
            builder.append(" ");
            builder.append(EApplication.getInstance().getString(R.string.drink_combo_iced));
        }

        return builder.toString();
    }

    public String getDrinkDescription(){
        //TODO:
        return "Drink description here!";
    }

    /**
     *
     * @return
     */
    @BindingAdapter("layoutWeight")
    public static void weight(final View view, int weight) {
        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)view.getLayoutParams());
        lp.weight = weight;
        view.setLayoutParams(lp);

        if(weight > 0){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @return
     */
    @BindingAdapter("llLayoutWeight")
    public static void llweight(final LinearLayout view, int weight) {
        //LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)view.getLayoutParams());
        Log.e(TAG, "Come on wtf: !!! >>>>>> " + weight);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float)weight);

        //lp.weight = weight;
        view.setLayoutParams(lp);


        if(weight > 0){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Calculate evaporated milk weight
     * @return
     */
    public int getEvaporatedMilkWeight(){
        return getSweetenerCompositionWeight(Sweetener.TYPE_EVAPORATED_MILK);
    }

    /**
     * Calculate condensed milk weight
     * @return
     */
    public int getCondensedMilkWeight(){
        return getSweetenerCompositionWeight(Sweetener.TYPE_CONDENSED_MILK);
    }

    /**
     * Calculate palm sugar weight
     * @return
     */
    public int getPalmSugarWeight(){
        return getSweetenerCompositionWeight(Sweetener.TYPE_PALM_SUGAR);
    }

    /**
     * Calculate tea weight
     * @return
     */
    public int getTeaWeight(){
        if(getFlavor().getId() == Flavor.TYPE_YUAN_YANG || getFlavor().getId() == Flavor.TYPE_TEH) {
            return 2;
        }
        return 0;
    }

    /**
     * Calculate coffee weight
     * @return
     */
    public int getCoffeeWeight(){
        if(getFlavor().getId() == Flavor.TYPE_YUAN_YANG || getFlavor().getId() == Flavor.TYPE_KOPI) {
            return 2;
        }
        return 0;
    }

    /**
     * Base function to calculate weight of sweeteners
     * @param type
     * @return
     */
    public int getSweetenerCompositionWeight(@Sweetener.SweetenerType int type){
        int weight = 0;
        if (!getSweeteners().isEmpty()) {
            for (Sweetener sweetener : getSweeteners()) {
                if (type == Sweetener.TYPE_CONDENSED_MILK && sweetener.isCondensedMilk()) {
                    weight = 1;
                    break;
                }else if (type == Sweetener.TYPE_EVAPORATED_MILK && sweetener.isEvaporatedMilk()) {
                    weight = 1;
                    break;
                }else if (type == Sweetener.TYPE_PALM_SUGAR && sweetener.isPalmSugar()) {
                    weight = 1;
                    break;
                }
            }
        }
        return weight;
    }


    public int getWaterCompositionWeight(){
        //The rule is: Is user ask for stronger drink OR drink already contains both evaporated and condensed milk, there will be no water
        if(getConcentrationLevel().isStronger()){
            return 0;
        }

        if(!getSweeteners().isEmpty()){
            boolean hasEvap = false;
            boolean hasCondensedMilk = false;
            for(Sweetener sweetener: getSweeteners()){
                if(sweetener.isEvaporatedMilk()){
                    hasEvap = true;
                }else if(sweetener.isCondensedMilk()){
                    hasCondensedMilk = true;
                }
            }

            if(hasCondensedMilk && hasEvap){
                return 0;
            }
        }

        return 1;
    }

}
