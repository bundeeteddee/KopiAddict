package com.tinytinybites.android.kopiaddict.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.tinytinybites.android.kopiaddict.R;

/**
 * Created by bundee on 11/30/16.
 */

public class SlantedView extends View {
    //Tag
    private static final String TAG = SlantedView.class.getSimpleName();

    //MODES Available
    private static final int MODE_SLANT_DOWN_FILL_TOP = 1;
    private static final int MODE_SLANT_UP_FILL_TOP = 2;
    private static final int MODE_SLANT_DOWN_FILL_BOTTOM = 3;
    private static final int MODE_SLANT_UP_FILL_BOTTOM = 4;

    //Defaults
    //private static final int DEFAULT_BACKGROUND =

    //Variables
    private Paint mPaint;
    private Path mPath;
    private int mMode;
    private @ColorInt int mSlantedBackgroundColor;
    private float mShadowRadius;
    private @ColorInt int mShadowColor;
    private float mShadowDy;
    private float mShadowOffset;
    private float mSlantRatio;

    public SlantedView(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public SlantedView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SlantedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlantedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * Initialize required objects
     * @param attrs
     */
    public void init(AttributeSet attrs) {
        //Get attributes first
        TypedArray array = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SlantedView,
                0, 0);

        //Retrieve whatever we can from layout attributes
        mSlantedBackgroundColor = array.getColor(R.styleable.SlantedView_backgroundColor, Color.TRANSPARENT);
        mSlantRatio = array.getFraction(R.styleable.SlantedView_slantRatio, 1, 1, 0.5f);
        if(array.getBoolean(R.styleable.SlantedView_hasShadow, false)){
            mShadowRadius = array.getFloat(R.styleable.SlantedView_shadowRadius, 0f);
            mShadowColor = array.getColor(R.styleable.SlantedView_shadowColor, 0x66000000);
            mShadowDy = array.getFloat(R.styleable.SlantedView_shadowDy, 0f);
        }
        mMode = array.getInt(R.styleable.SlantedView_slantMode, MODE_SLANT_DOWN_FILL_TOP);
        array.recycle();

        //Setup paint object
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mSlantedBackgroundColor);

        //Setup shadow if there is
        if(mShadowRadius > 0f) {
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            mShadowOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mShadowRadius, dm);
            if(mMode == MODE_SLANT_DOWN_FILL_BOTTOM || mMode == MODE_SLANT_UP_FILL_BOTTOM){
                mPaint.setShadowLayer(mShadowRadius, 0, -Math.abs(mShadowDy), mShadowColor);
            }else {
                mPaint.setShadowLayer(mShadowRadius, 0, mShadowDy, mShadowColor);
            }
        }

        //Ref: http://stackoverflow.com/a/3723654/377844
        setLayerType(LAYER_TYPE_SOFTWARE, mPaint);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth(), h = getHeight();

        mPath = new Path();
        mPath.setFillType(Path.FillType.EVEN_ODD);

        //Depending on mode, path is different
        switch (mMode){
            case MODE_SLANT_DOWN_FILL_BOTTOM:{
                mPath.moveTo(0, mShadowOffset);
                mPath.lineTo(0,h);
                mPath.lineTo(w,h);
                mPath.lineTo(w,h*(1- mSlantRatio));
                mPath.close();
                break;
            }
            case MODE_SLANT_DOWN_FILL_TOP:{
                mPath.moveTo(0,0);
                mPath.lineTo(0,h * mSlantRatio);
                mPath.lineTo(w,h - mShadowOffset);
                mPath.lineTo(w,0);
                mPath.close();
                break;
            }
            case MODE_SLANT_UP_FILL_BOTTOM:{
                mPath.moveTo(0,h*(1- mSlantRatio));
                mPath.lineTo(0,h);
                mPath.lineTo(w,h);
                mPath.lineTo(w, mShadowOffset);
                mPath.close();
                break;
            }
            case MODE_SLANT_UP_FILL_TOP:{
                mPath.moveTo(0,0);
                mPath.lineTo(0,h - mShadowOffset);
                mPath.lineTo(w,h* mSlantRatio);
                mPath.lineTo(w,0);
                mPath.close();
                break;
            }
        }
        canvas.drawPath(mPath, mPaint);
    }

}
