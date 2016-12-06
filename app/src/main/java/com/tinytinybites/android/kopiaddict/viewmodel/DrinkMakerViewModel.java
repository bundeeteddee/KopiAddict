package com.tinytinybites.android.kopiaddict.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import com.tinytinybites.android.kopiaddict.model.ConcentrationLevel;
import com.tinytinybites.android.kopiaddict.model.Drink;
import com.tinytinybites.android.kopiaddict.model.Flavor;
import com.tinytinybites.android.kopiaddict.model.Sweetener;
import com.tinytinybites.android.kopiaddict.model.SweetenerLevel;
import io.realm.RealmList;

/**
 * Created by bundee on 11/25/16.
 */

public class DrinkMakerViewModel extends BaseObservable implements ViewModel{
    //Tag
    protected static final String TAG = DrinkMakerViewModel.class.getSimpleName();

    //Static
    public static final int MAKE_INITIAL = 0; //Initial
    public static final int MAKE_STAGE_1 = MAKE_INITIAL + 1; //TEA, COFFEE OR BOTH
    public static final int MAKE_STAGE_2 = MAKE_STAGE_1 + 1; //EVAPORATED MILK
    public static final int MAKE_STAGE_3 = MAKE_STAGE_2 + 1; //CONDENSED MILK
    public static final int MAKE_STAGE_4 = MAKE_STAGE_3 + 1; //SWEETNESS LEVEL
    public static final int MAKE_STAGE_5 = MAKE_STAGE_4 + 1; //CONCENTRATION LEVEL
    public static final int MAKE_STAGE_6 = MAKE_STAGE_5 + 1; //ICED / HOT
    public static final int MAKE_STAGE_7 = MAKE_STAGE_6 + 1; //TAKE AWAY LEVEL
    public static final int MAKE_STAGE_COMPLETE = MAKE_STAGE_7 + 1; //COMPLETE

    //Variables
    private int mStage;
    private Drink mDrink;
    private View.OnClickListener mListener;


    /**
     * Constructor
     */
    public DrinkMakerViewModel(View.OnClickListener listener){
        mStage = MAKE_STAGE_1; //TODO: Straight to stage 1?
        mDrink = new Drink();
        mDrink.setKeyPrepopulated(false);

        this.mListener = listener;
    }

    public int getStage(){
        return mStage;
    }

    public Drink getDrink(){    return mDrink;}

    /**
     * Depending on stage, return the question
     * @return
     */
    @Bindable
    public String getQuestion(){
        switch (mStage){
            case MAKE_STAGE_1:{ return EApplication.getInstance().getString(R.string.drink_q1);}
            case MAKE_STAGE_2:{ return EApplication.getInstance().getString(R.string.drink_q2);}
            case MAKE_STAGE_3:{ return EApplication.getInstance().getString(R.string.drink_q3);}
            case MAKE_STAGE_4:{ return EApplication.getInstance().getString(R.string.drink_q4);}
            case MAKE_STAGE_5:{ return EApplication.getInstance().getString(R.string.drink_q5);}
            case MAKE_STAGE_6:{ return EApplication.getInstance().getString(R.string.drink_q6);}
            case MAKE_STAGE_7:{ return EApplication.getInstance().getString(R.string.drink_q7);}
            case MAKE_STAGE_COMPLETE:{  return EApplication.getInstance().getString(R.string.boiling_water);}
            default:
                return null;
        }
    }

    @Bindable
    public Drawable getQuestionImage(){
        int drawable = R.drawable.img_composition_condensed_milk;

        switch (mStage){
            case MAKE_STAGE_1:{
                drawable = R.drawable.img_composition_gula_melaka;
                break;
            }
        }

        //Use ContextCompat for backward compatibility reason as getDrawable from context itself is only from lollipop onwards
        return ContextCompat.getDrawable(EApplication.getInstance(), drawable);
    }

    @Bindable
    public int getProgressVisibility(){
        return getStage() == MAKE_STAGE_COMPLETE ? View.VISIBLE : View.GONE;
    }

    /**
     * Build up a drink composition based on answers given at each step
     * @param viewTapped
     */
    public void onAnswerSelected(View viewTapped){
        int tag = (int) viewTapped.getTag();
        switch (getStage()){
            case MAKE_STAGE_1:{//Tea, coffee, both
                if(tag == 1){
                    mDrink.setFlavor(new Flavor(Flavor.TYPE_TEH)); //Tea
                }else if(tag == 2){
                    mDrink.setFlavor(new Flavor(Flavor.TYPE_KOPI)); //Coffee
                }else if(tag == 3){
                    mDrink.setFlavor(new Flavor(Flavor.TYPE_YUAN_YANG)); //Both
                }
                break;
            }
            case MAKE_STAGE_2:{//Evaporated milk
                if(tag == 1){
                    if(mDrink.getSweeteners() == null){
                        mDrink.setSweeteners(new RealmList<>(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK)));
                    }else{
                        mDrink.getSweeteners().add(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK));
                    }
                }
                break;
            }
            case MAKE_STAGE_3:{//Condensed milk
                if(tag == 1){
                    if(mDrink.getSweeteners() == null){
                        mDrink.setSweeteners(new RealmList<>(new Sweetener(Sweetener.TYPE_CONDENSED_MILK)));
                    }else{
                        mDrink.getSweeteners().add(new Sweetener(Sweetener.TYPE_CONDENSED_MILK));
                    }
                }
                break;
            }
            case MAKE_STAGE_4:{//Sweetness level
                if(tag == 1){//0%
                    mDrink.setSweetenerLevel(new SweetenerLevel(SweetenerLevel.TYPE_0));
                }else if(tag == 2){//25%
                    mDrink.setSweetenerLevel(new SweetenerLevel(SweetenerLevel.TYPE_25));
                }else if(tag == 3){//50%
                    mDrink.setSweetenerLevel(new SweetenerLevel(SweetenerLevel.TYPE_50));
                }else if(tag == 4){//100%
                    mDrink.setSweetenerLevel(new SweetenerLevel(SweetenerLevel.TYPE_100));
                }else if(tag == 5){//150%
                    mDrink.setSweetenerLevel(new SweetenerLevel(SweetenerLevel.TYPE_150));
                }
                break;
            }
            case MAKE_STAGE_5:{//Concentration level
                if(tag == 1){
                    mDrink.setConcentrationLevel(new ConcentrationLevel(ConcentrationLevel.TYPE_WEAKER)); //Weaker
                }else if(tag == 2){
                    mDrink.setConcentrationLevel(new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL)); //Normal
                }else if(tag == 3){
                    mDrink.setConcentrationLevel(new ConcentrationLevel(ConcentrationLevel.TYPE_STRONGER)); //Stronger
                }
                break;
            }
            case MAKE_STAGE_6:{//Iced
                if(tag == 1){
                    mDrink.setIced(true);
                }else{
                    mDrink.setIced(false);
                }
                break;
            }
            case MAKE_STAGE_7:{//Takeaway
                if(tag == 1){
                    mDrink.setTakeAway(true);
                }else{
                    mDrink.setTakeAway(false);
                }
                break;
            }
        }
    }

    /**
     * Move the process along and notify observable
     */
    public void nextStage(){
        if(mStage < MAKE_STAGE_COMPLETE){
            mStage++;
            notifyPropertyChanged(BR.question);
            notifyPropertyChanged(BR.questionImage);
            notifyPropertyChanged(BR.progressVisibility);
        }
    }

    @Override
    public void destroy() {
        if(mListener != null){
            mListener = null;
        }
    }

}
