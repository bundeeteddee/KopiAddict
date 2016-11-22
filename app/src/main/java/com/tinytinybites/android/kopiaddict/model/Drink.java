package com.tinytinybites.android.kopiaddict.model;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.view.View;
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

    /**
     * Get a longer description of the make up of this drink
     * @return
     */
    public String getDrinkDescription(){
        StringBuilder builder = new StringBuilder();

        //Flavor / temperature
        if(isIced()){
            builder.append("This is an iced ");
        }else{
            builder.append("This is a hot ");
        }
        builder.append(getFlavor().getFriendlyDescription());
        builder.append(" drink ");

        //Sweeteners
        if(getSweeteners().isEmpty()){
            builder.append(" with no additional sweeteners. ");
        }else{
            builder.append(" with additional sweeteners:");

            boolean hasEvapMilk = false;
            boolean hasCondensedMilk = false;
            boolean hasPalmSugar = false;
            boolean hasAccumulated = false;
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
                builder.append(" ");
                builder.append(EApplication.getInstance().getString(R.string.sweetener_evaporated_milk));
                hasAccumulated = true;
            }

            if(hasCondensedMilk){
                if(hasAccumulated){
                    builder.append(", ");
                }
                builder.append(EApplication.getInstance().getString(R.string.sweetener_condensed_milk));
                hasAccumulated = true;
            }

            if(hasPalmSugar){
                if(hasAccumulated){
                    builder.append(", ");
                }
                builder.append(EApplication.getInstance().getString(R.string.sweetener_palm_sugar));
                hasAccumulated = true;
            }

            builder.append(". ");
        }

        //Sweetener level
        if(getSweetenerLevel().isNormalSweetness()){
            builder.append(" This drink has normal sweetness level (100%).");
        }else if(getSweetenerLevel().isExtraSweetness()) {
            builder.append(" This drink has extra sweetness added (150%).");
        }else if(getSweetenerLevel().isHalfSweetness()) {
            builder.append(" This drink has half sweetness level (50%).");
        }else if(getSweetenerLevel().isQuarterSweetness()) {
            builder.append(" This drink has quarter sweetness level (25%).");
        }else if(getSweetenerLevel().isNoSweetness()) {
            builder.append(" This drink has zero sweetness to it (0%).");
        }

        //Concentration level
        if(getConcentrationLevel().isNormal()){
            builder.append(" The concentration level is normal.");
        }else if(getConcentrationLevel().isStronger()) {
            builder.append(" The drink is more concentrated than normal.");
        }else if(getConcentrationLevel().isWeaker()){
            builder.append(" The drink is less concentrated than normal.");
        }

        return builder.toString();
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
            if(getConcentrationLevel().isStronger()){
                return 4;
            }
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
            if(getConcentrationLevel().isStronger()){
                return 4;
            }
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

    /**
     * Get water composition weight. Takes into account concentration level
     * @return
     */
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

        if(getConcentrationLevel().isWeaker()) {
            return 2;
        }
        return 1;
    }

}
