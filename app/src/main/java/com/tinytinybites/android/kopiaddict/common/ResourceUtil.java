package com.tinytinybites.android.kopiaddict.common;

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

}
