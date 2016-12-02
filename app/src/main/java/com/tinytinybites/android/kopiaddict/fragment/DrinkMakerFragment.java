package com.tinytinybites.android.kopiaddict.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.R;
import com.tinytinybites.android.kopiaddict.activity.DrinkControlNavigation;
import com.tinytinybites.android.kopiaddict.application.EApplication;
import com.tinytinybites.android.kopiaddict.common.ResourceUtil;
import com.tinytinybites.android.kopiaddict.databinding.FragmentDrinkMakerBinding;
import com.tinytinybites.android.kopiaddict.helper.WaveHelper;
import com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_1;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_2;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_3;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_4;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_5;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_6;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_7;
import static com.tinytinybites.android.kopiaddict.viewmodel.DrinkMakerViewModel.MAKE_STAGE_COMPLETE;

/**
 * Created by bundee on 11/9/16.
 */

public class DrinkMakerFragment extends Fragment implements View.OnClickListener {
    //Tag
    protected static final String TAG = DrinkMakerFragment.class.getSimpleName();

    //Variables
    private FragmentDrinkMakerBinding mBinding;
    private DrinkMakerViewModel mDrinkMakerViewModel;
    private WaveHelper mWaveHelper;
    private Realm mRealm;
    private RealmAsyncTask mAsyncTransaction;

    /**
     * Static constructor
     * @return
     */
    public static DrinkMakerFragment newInstance(){
        DrinkMakerFragment fragment = new DrinkMakerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        mWaveHelper.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onPause() {
        super.onPause();

        mWaveHelper.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Close realm and cancel async transaction
        cancelAsyncTransaction();
        mRealm.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drink_maker, container, false);
        mDrinkMakerViewModel = new DrinkMakerViewModel(this);
        mBinding.setDrinkMakerViewModel(mDrinkMakerViewModel);

        //Ui elements
        mWaveHelper = new WaveHelper(mBinding.wave);

        //Initial setup of answer buttons
        setupAnswerButtons(mDrinkMakerViewModel.getStage());

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.unbind();
    }

    /**
     * Setup all the answer buttons given a stage that the drink making is in
     * @param stage
     */
    public void setupAnswerButtons(int stage) {
        //Remove all in layout
        mBinding.drinkMakerChoiceContainer.removeAllViews();

        //Generate buttons for the specific stage
        List<? extends View> answers = GetAnswersButtons(stage);
        for (View view: answers){
            view.setOnClickListener(this);
            mBinding.drinkMakerChoiceContainer.addView(view);
        }
    }

    /**
     * Given a stage, generate the answer buttons
     * @param stage
     * @return
     */
    private static List<? extends View> GetAnswersButtons(int stage){
        ArrayList views = new ArrayList();

        switch (stage){
            case MAKE_STAGE_1:{
                for(int i = 1; i <= 3; i++){
                    Button button = CreateGenericAnswerButton();
                    switch (i){
                        case 1: button.setText(R.string.drink_q1_a1); break;
                        case 2: button.setText(R.string.drink_q1_a2); break;
                        case 3: button.setText(R.string.drink_q1_a3); break;
                    }
                    button.setTag(i);
                    views.add(button);
                }
                break;
            }
            case MAKE_STAGE_2:
            case MAKE_STAGE_3:
            case MAKE_STAGE_7:{
                for(int i = 1; i <= 2; i++){
                    Button button = CreateGenericAnswerButton();
                    switch (i){
                        case 1: button.setText(R.string.drink_q_yes); break;
                        case 2: button.setText(R.string.drink_q_no); break;
                    }
                    button.setTag(i);
                    views.add(button);
                }
                break;
            }
            case MAKE_STAGE_4:{
                for(int i = 1; i <= 5; i++){
                    Button button = CreateGenericAnswerButton();
                    switch (i){
                        case 1: button.setText(R.string.drink_q4_a1); break;
                        case 2: button.setText(R.string.drink_q4_a2); break;
                        case 3: button.setText(R.string.drink_q4_a3); break;
                        case 4: button.setText(R.string.drink_q4_a4); break;
                        case 5: button.setText(R.string.drink_q4_a5); break;
                    }
                    button.setTag(i);
                    views.add(button);
                }
                break;
            }
            case MAKE_STAGE_5:{
                for(int i = 1; i <= 3; i++){
                    Button button = CreateGenericAnswerButton();
                    switch (i){
                        case 1: button.setText(R.string.drink_q5_a1); break;
                        case 2: button.setText(R.string.drink_q5_a2); break;
                        case 3: button.setText(R.string.drink_q5_a3); break;
                    }
                    button.setTag(i);
                    views.add(button);
                }
                break;
            }
            case MAKE_STAGE_6:{
                for(int i = 1; i <= 2; i++){
                    Button button = CreateGenericAnswerButton();
                    switch (i){
                        case 1: button.setText(R.string.drink_q6_a1); break;
                        case 2: button.setText(R.string.drink_q6_a2); break;
                    }
                    button.setTag(i);
                    views.add(button);
                }
                break;
            }
        }

        return views;
    }

    /**
     * Convenience function to create a button view
     * @return
     */
    private static Button CreateGenericAnswerButton(){
        Button button = (Button) LayoutInflater.from(EApplication.getInstance()).inflate(R.layout.button_answer_option, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int sideMargins = ResourceUtil.ConvertDpToPixels((int) EApplication.getInstance().getResources().getDimension(R.dimen.drink_maker_answer_button_margin_side), EApplication.getInstance());
        int topBottomMargins = ResourceUtil.ConvertDpToPixels((int) EApplication.getInstance().getResources().getDimension(R.dimen.drink_maker_answer_button_margin_top_bottom), EApplication.getInstance());
        lp.setMargins(sideMargins, topBottomMargins, sideMargins, topBottomMargins);
        button.setLayoutParams(lp);
        return button;
    }

    @Override
    public void onClick(View v) {
        //Apply transitions to ui elements
        TransitionSet transitionSet = new TransitionSet();

        Transition questionTransition = new Fade();
        Transition answersContainerTransition = new ChangeBounds();

        questionTransition.addTarget(mBinding.question);
        answersContainerTransition.addTarget(mBinding.drinkMakerChoiceContainer);

        transitionSet
                .addTransition(questionTransition)
                .addTransition(answersContainerTransition);

        TransitionManager.beginDelayedTransition(mBinding.mainBackground, transitionSet);

        //Setup drink
        mDrinkMakerViewModel.onAnswerSelected(v);

        //Move to next stage
        mDrinkMakerViewModel.nextStage();

        //Increase wave level
        mWaveHelper.increaseWaveLevel();

        //Setup new answers
        setupAnswerButtons(mDrinkMakerViewModel.getStage());

        //If we reached the end of the questionaire, do a last check on the drinks object(?), and save it
        if(mDrinkMakerViewModel.getStage() == MAKE_STAGE_COMPLETE){
            mAsyncTransaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(mDrinkMakerViewModel.getDrink());

                    //TODO: Add some delay in a better way
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "onSuccess >>> "+ mDrinkMakerViewModel.getDrink().getId());
                    //Move to details ui
                    ((DrinkControlNavigation)getActivity()).OnShowDrinkDetails(DrinkMakerFragment.this, mDrinkMakerViewModel.getDrink().getId(), null);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    //TODO: What do we do here?
                    Log.e(TAG, "Saving drink into Realm failed: " + error.getMessage());
                }
            });
        }
    }

    /**
     * Cancel realm async transaction if need to
     */
    private void cancelAsyncTransaction() {
        if (mAsyncTransaction != null && !mAsyncTransaction.isCancelled()) {
            mAsyncTransaction.cancel();
            mAsyncTransaction = null;
        }
    }
}
