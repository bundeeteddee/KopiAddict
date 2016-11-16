package com.tinytinybites.android.kopiaddict.common;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.tinytinybites.android.kopiaddict.application.EApplication;

/**
 * Created by bundee on 11/3/16.
 */

public class ResourceUtil {

    /**
     * Get the color given color res id
     * @param colorResId
     * @return
     */
    public static final int GetColor(@ColorRes int colorResId){
        return ContextCompat.getColor(EApplication.getInstance(), colorResId);
    }

    /**
     * Convert DP to Pixels
     * @param dpSize
     * @param context
     * @return
     */
    public static final int ConvertDpToPixels(int dpSize, Context context){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpSize * scale + 0.5f);
    }

}
