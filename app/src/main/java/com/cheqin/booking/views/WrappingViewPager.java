package com.cheqin.booking.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.viewpager.widget.ViewPager;

public class WrappingViewPager extends ViewPager  implements Animation.AnimationListener{
    private View mCurrentView;
    private Boolean mAnimStarted = false;
    private PagerAnimation mAnimation = new PagerAnimation();
    private long mAnimDuration = 100;
    private int measuredHeight;

    public WrappingViewPager(Context context) {
        super(context);
        mAnimation.setAnimationListener(this);

    }

    public WrappingViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        mAnimation.setAnimationListener(this);

    }

    public int getActualHeight() {
     return measuredHeight;
    }



/*
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
*/

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mAnimStarted && mCurrentView != null) {
            int height;
            mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = mCurrentView.getMeasuredHeight();

            if (height < getMinimumHeight()) {
                height = getMinimumHeight();
            }

            int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED);
            if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
                mAnimation.setDimensions(height, getLayoutParams().height);
                mAnimation.setDuration(mAnimDuration);
                startAnimation(mAnimation);
                mAnimStarted = true;
            } else {
                heightMeasureSpec = newHeight;
            }
        }

        measuredHeight = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }



    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mAnimStarted = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mAnimStarted = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class PagerAnimation extends Animation {
        private int targetHeight;
        private int currentHeight;
        private int heightChange;

        /**
         * Set the dimensions for the animation.
         *
         * @param targetHeight  View's target height
         * @param currentHeight View's current height
         */
        void setDimensions(int targetHeight, int currentHeight) {
            this.targetHeight = targetHeight;
            this.currentHeight = currentHeight;
            this.heightChange = targetHeight - currentHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime >= 1) {
                getLayoutParams().height = targetHeight;
            } else {
                int stepHeight = (int) (heightChange * interpolatedTime);
                getLayoutParams().height = currentHeight + stepHeight;
            }
            requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}