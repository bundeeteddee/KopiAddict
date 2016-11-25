package com.tinytinybites.android.kopiaddict.helper;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.view.WaveView;

/**
 * Created by bundee on 11/23/16.
 *
 * This is sourced from: https://github.com/gelitenight/WaveView/blob/master/sample/src/main/java/com/gelitenight/waveview/sample/WaveHelper.java
 */

public class WaveHelper {
    //Tag
    protected static final String TAG = WaveHelper.class.getSimpleName();

    //Variables
    private WaveView mWaveView;
    private AnimatorSet mAnimatorSet;
    private float mWaveLevel = 0.0f; //0 ~ 1 ratio
    private float mWaveLevelOld = 0.0f; //0 ~ 1 ratio
    private ObjectAnimator mWaterLevelAnim;
    private ObjectAnimator mAmplitudeAnim;

    public WaveHelper(WaveView waveView) {
        mWaveView = waveView;
        initAnimation();
    }

    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level starts from the very bottom
        mWaterLevelAnim = ObjectAnimator.ofFloat(mWaveView, "waterLevelRatio", 0, mWaveLevel);
        mWaterLevelAnim.setAutoCancel(true);
        mWaterLevelAnim.setDuration(5000);
        mWaterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(mWaterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        mAmplitudeAnim = ObjectAnimator.ofFloat(mWaveView, "amplitudeRatio", 0.0001f, 0.03f);
        mAmplitudeAnim.setRepeatCount(1);
        mAmplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        mAmplitudeAnim.setDuration(5000);
        mAmplitudeAnim.setInterpolator(new LinearInterpolator());

        animators.add(mAmplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    /**
     * Increase the water level by steps.
     * Currently we have 7 steps.
     */
    public void increaseWaveLevel(){
        if(mWaveLevel < 1.0f){
            //Get old value by checking on the latest animated value
            mWaveLevelOld = (float)mWaterLevelAnim.getAnimatedValue();
            mWaveLevel += 1.0f / 7; //Increase target max level by 1/7 as we have 7 steps

            //Deal with water level animation by setting new current value to target value
            //Reset current play time and start the animation again if its not already
            //Water level animation is straight forward as we don't need to worry about different phases of the wave crescent
            mWaterLevelAnim.setFloatValues(mWaveLevelOld, mWaveLevel);
            mWaterLevelAnim.setCurrentPlayTime(0);
            if(!mWaterLevelAnim.isRunning()) {
                mWaterLevelAnim.start();
            }

            //Deal with amplitude animation
            //If animation has completed, just restart the animation
            //If animation is already running, determine what section it is in out of the duration
            //If the current play time is more than half way through when the change of water level is called, then we flip the current playtime from the half way duration time
            //This is so the curve is smooth.
            if (!mAmplitudeAnim.isRunning()) {
                //Reset to original duration
                mAmplitudeAnim.start();
            }else{
                //Get the elapsed animation time
                long elapsedTime = mAmplitudeAnim.getCurrentPlayTime();

                //Compensate duration, so animation is smoother
                if(elapsedTime > 2500){
                    //Flip the curve
                    mAmplitudeAnim.setCurrentPlayTime(elapsedTime - (elapsedTime-2500));
                }else{
                    //Do nothing, let it keep going
                }
            }
        }
    }

    public void cancel() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }
}