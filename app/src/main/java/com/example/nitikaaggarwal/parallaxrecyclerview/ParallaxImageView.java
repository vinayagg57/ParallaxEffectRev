package com.example.nitikaaggarwal.parallaxrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.nitikaaggarwal.parallaxrecyclerview.utils.AppConstants;
import com.example.nitikaaggarwal.parallaxrecyclerview.utils.InterpolatorSelector;

/**
 * Created by nitikaaggarwal on 26/06/16.
 */
public class ParallaxImageView extends ImageView {

    public boolean reverseX = false;
    public boolean reverseY = false;
    public boolean updateOnDraw = false;
    public boolean blockParallaxX = false;
    public boolean blockParallaxY = false;

    private int screenWidth;
    private int screenHeight;
    private float scrollSpaceX = 0;
    private float scrollSpaceY = 0;
    private float heightImageView;
    private float widthImageView;

    private Interpolator interpolator = new LinearInterpolator();

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
    private ViewTreeObserver.OnGlobalLayoutListener  mOnGlobalLayoutListener = null;
    private ViewTreeObserver.OnDrawListener onDrawListener = null;

    public ParallaxImageView(Context context) {
        super(context);
        checkScale();
    }

    public ParallaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }
        checkScale();
    }

    public ParallaxImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }
        checkScale();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax();
            }
        };

        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                heightImageView = (float) getHeight();
                widthImageView = (float) getWidth();

                applyParallax();
            }
        };

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        parallaxAnimation();
    }

    private boolean checkScale() {
        switch (getScaleType()) {
            case CENTER:
            case CENTER_CROP:
            case CENTER_INSIDE:
                return true;
            case FIT_CENTER:
                Log.d("ParallaxEverywhere", "Scale type firCenter unsupported");
                break;
            case FIT_END:
                Log.d("ParallaxEverywhere", "Scale type fitEnd unsupported");
                break;
            case FIT_START:
                Log.d("ParallaxEverywhere", "Scale type fitStart unsupported");
                break;
            case FIT_XY:
                Log.d("ParallaxEverywhere", "Scale type fitXY unsupported");
                break;
            case MATRIX:
                Log.d("ParallaxEverywhere", "Scale type matrix unsupported");
                break;
        }
        return false;
    }

    private void checkAttributes(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWAttrs);
        int reverse = arr.getInt(R.styleable.PEWAttrs_reverse, 4);

        updateOnDraw = arr.getBoolean(R.styleable.PEWAttrs_update_onDraw, false);

        blockParallaxX = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_x, false);
        blockParallaxY = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_y, false);

        reverseX = false;
        reverseY = false;
        switch (reverse) {
            case AppConstants.REVERSE_NONE:
                break;
            case AppConstants.REVERSE_X:
                reverseX = true;
                break;
            case AppConstants.REVERSE_Y:
                reverseY = true;
                break;
            case AppConstants.REVERSE_BOTH:
                reverseX = true;
                reverseY = true;
                break;
        }

        checkScale();

        int interpolationId = arr.getInt(R.styleable.PEWAttrs_interpolation, 4);

        interpolator = InterpolatorSelector.interpolatorId(interpolationId);

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() != null) {

            int dheight = getDrawable().getIntrinsicHeight();
            int dwidth = getDrawable().getIntrinsicWidth();
            int vheight = getMeasuredHeight();
            int vwidth = getMeasuredWidth();

            float scale;

            float dnewHeight = 0;
            float dnewWidth = 0;

            switch (getScaleType()) {
                case CENTER_CROP:
                case CENTER:
                case CENTER_INSIDE:
                    if (dwidth * vheight > vwidth * dheight) {
                        scale = (float) vheight / (float) dheight;
                        dnewWidth = dwidth * scale;
                        dnewHeight = vheight;
                    } else {
                        scale = (float) vwidth / (float) dwidth;
                        dnewWidth = vwidth;
                        dnewHeight = dheight * scale;
                    }
                    break;
                case FIT_CENTER:
                case FIT_END:
                case FIT_START:
                case FIT_XY:
                case MATRIX:
                    break;
            }

            scrollSpaceY = (dnewHeight > vheight) ? (dnewHeight - vheight) : 0;
            scrollSpaceX = (dnewWidth > vwidth) ? (dnewWidth - vwidth) : 0;
        }
        applyParallax();
    }

    private void parallaxAnimation() {
        initSizeScreen();

        applyParallax();

    }

    private void initSizeScreen() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

            screenHeight = display.getHeight();
            screenWidth = display.getWidth();

    }

    private void applyParallax() {
        int[] location = new int[2];
        getLocationOnScreen(location);

        if (scrollSpaceY != 0) {
            float locationY = (float) location[1];
            float locationUsableY = locationY + heightImageView / 2;
            float scrollDeltaY = locationUsableY / screenHeight;

            float interpolatedScrollDeltaY = interpolator.getInterpolation(scrollDeltaY);

            if (reverseY)
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * -scrollSpaceY));
            else
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * scrollSpaceY));
        }

        if (scrollSpaceX != 0) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + widthImageView / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            float interpolatedScrollDeltaX = interpolator.getInterpolation(scrollDeltaX);

            if (reverseX) {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }
    }

    private void setMyScrollX(int value) {

            scrollTo(value, getScrollY());

    }

    private void setMyScrollY(int value) {

            scrollTo(getScrollX(),value);
        }


    public void setInterpolator(Interpolator interpol) {
        interpolator = interpol;
    }

    public boolean isReverseX() {
        return reverseX;
    }

    public void setReverseX(boolean reverseX) {
        this.reverseX = reverseX;
    }

    public boolean isReverseY() {
        return reverseY;
    }

    public void setReverseY(boolean reverseY) {
        this.reverseY = reverseY;
    }

    public boolean isBlockParallaxX() {
        return blockParallaxX;
    }

    public void setBlockParallaxX(boolean blockParallaxX) {
        this.blockParallaxX = blockParallaxX;
    }

    public boolean isBlockParallaxY() {
        return blockParallaxY;
    }

    public void setBlockParallaxY(boolean blockParallaxY) {
        this.blockParallaxY = blockParallaxY;
    }
}