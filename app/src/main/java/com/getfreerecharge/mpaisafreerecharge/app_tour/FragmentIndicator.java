package com.getfreerecharge.mpaisafreerecharge.app_tour;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;


import com.getfreerecharge.mpaisafreerecharge.R;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

public class FragmentIndicator extends LinearLayout implements OnPageChangeListener {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewpager;
    private OnPageChangeListener mViewPagerOnPageChangeListener;
    private int mIndicatorMargin;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mAnimatorResId = R.animator.scale_with_alpha;
    private int mAnimatorReverseResId = -1;
    private int mIndicatorBackground = R.drawable.indicator_radius;
    private int mIndicatorUnselectedBackground = R.drawable.indicator_radius;
    private int mCurrentPosition = 0;
    private Animator mAnimationOut;
    private Animator mAnimationIn;

    public FragmentIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public FragmentIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        handleTypedArray(context, attrs);
        mAnimationOut = AnimatorInflater.loadAnimator(context, mAnimatorResId);
        if (mAnimatorReverseResId == -1) {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            mAnimationIn.setInterpolator(new ReverseInterpolator());
        } else {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.FragmentIndicator);
            mIndicatorWidth =
                    typedArray.getDimensionPixelSize(R.styleable.FragmentIndicator_fi_width, -1);
            mIndicatorHeight =
                    typedArray.getDimensionPixelSize(R.styleable.FragmentIndicator_fi_height, -1);
            mIndicatorMargin =
                    typedArray.getDimensionPixelSize(R.styleable.FragmentIndicator_fi_margin, -1);

            mAnimatorResId = typedArray.getResourceId(R.styleable.FragmentIndicator_fi_animator,
                    R.animator.scale_with_alpha);
            mAnimatorReverseResId =
                    typedArray.getResourceId(R.styleable.FragmentIndicator_fi_animator_reverse, -1);
            mIndicatorBackground = typedArray.getResourceId(R.styleable.FragmentIndicator_fi_drawable,
                    R.drawable.indicator_radius);
            mIndicatorUnselectedBackground =
                    typedArray.getResourceId(R.styleable.FragmentIndicator_fi_drawable_unselected,
                            mIndicatorBackground);
            typedArray.recycle();
        }
        mIndicatorWidth =
                (mIndicatorWidth == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewpager = viewPager;
        mCurrentPosition = mViewpager.getCurrentItem();
        createIndicators(viewPager);
        mViewpager.setOnPageChangeListener(this);
        onPageSelected(mCurrentPosition);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        mViewPagerOnPageChangeListener = onPageChangeListener;
        mViewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageScrolled(position, positionOffset,
                    positionOffsetPixels);
        }
    }

    @Override public void onPageSelected(int position) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageSelected(position);
        }

        if (mAnimationIn.isRunning()) mAnimationIn.end();
        if (mAnimationOut.isRunning()) mAnimationOut.end();

        View currentIndicator = getChildAt(mCurrentPosition);
        currentIndicator.setBackgroundResource(mIndicatorUnselectedBackground);
        mAnimationIn.setTarget(currentIndicator);
        mAnimationIn.start();

        View selectedIndicator = getChildAt(position);
        selectedIndicator.setBackgroundResource(mIndicatorBackground);
        mAnimationOut.setTarget(selectedIndicator);
        mAnimationOut.start();

        mCurrentPosition = position;
    }

    @Override public void onPageScrollStateChanged(int state) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void createIndicators(ViewPager viewPager) {
        removeAllViews();
        int count = viewPager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        addIndicator(mIndicatorBackground, mAnimationOut);
        for (int i = 1; i < count; i++) {
            addIndicator(mIndicatorUnselectedBackground, mAnimationIn);
        }
    }

    private void addIndicator(@DrawableRes int backgroundDrawableId, Animator animator) {
        if (animator.isRunning()) animator.end();

        View Indicator = new View(getContext());
        Indicator.setBackgroundResource(backgroundDrawableId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);

        animator.setTarget(Indicator);
        animator.start();
    }

    private class ReverseInterpolator implements Interpolator {
        @Override public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}