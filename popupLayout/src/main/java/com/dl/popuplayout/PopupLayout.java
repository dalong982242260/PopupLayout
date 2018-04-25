package com.dl.popuplayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;


/**
 * 弹出框布局（从上往下  从下往上弹出）
 * Created by dalong on 18/4/20
 */
public class PopupLayout extends RelativeLayout {

    private boolean canceledOnTouchOutside;
    private int shadowBackgroundColor;
    private int animDuration;
    private boolean isAnim;
    private View contentContainer;
    private Animation inAnim, outAnim;
    private boolean isShowing;
    private boolean dismissing;
    private ANIM animType;
    private OnDismissListener onDismissListener;

    public PopupLayout(Context context) {
        this(context, null);
    }

    public PopupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PopupLayout, defStyleAttr, 0);
        isAnim = typedArray.getBoolean(R.styleable.PopupLayout_isAnim, true);
        canceledOnTouchOutside = typedArray.getBoolean(R.styleable.PopupLayout_canceledOnTouchOutside, true);
        animDuration = typedArray.getInteger(R.styleable.PopupLayout_animDuration, 300);
        shadowBackgroundColor = typedArray.getColor(R.styleable.PopupLayout_shadowBackgroundColor, Color.parseColor("#80000000"));
        int animTypeValue = typedArray.getInteger(R.styleable.PopupLayout_animType, ANIM.BOTTOM.getValue());
        animType = getAnimType(animTypeValue);
        typedArray.recycle();
        init(context);
    }

    public ANIM getAnimType(int animTypeValue) {
        if (animTypeValue == ANIM.LEFT.value) {
            return ANIM.LEFT;
        }
        if (animTypeValue == ANIM.RIGHT.value) {
            return ANIM.RIGHT;
        }
        if (animTypeValue == ANIM.TOP.value) {
            return ANIM.TOP;
        }
        if (animTypeValue == ANIM.BOTTOM.value) {
            return ANIM.BOTTOM;
        }
        return ANIM.BOTTOM;
    }

    private void init(Context context) {
        initAnim();
        setKeyBackCancelable(canceledOnTouchOutside);
        ViewGroup decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        decorView.setOnTouchListener(onTouchListener);
        setOnTouchListener(onTouchListener);

    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("PopupLayout can host only one direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("PopupLayout can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("PopupLayout can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("PopupLayout can host only one direct child");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >= 1) {
            contentContainer = getChildAt(0);
            contentContainer.setVisibility(GONE);
            contentContainer.setClickable(true);
        }
    }

    public void setAnimType(ANIM animType) {
        this.animType = animType;
        initAnim();
    }

    public ANIM getAnimType() {
        return animType;
    }

    /**
     * init anim（show and hide）
     */
    private void initAnim() {
        switch (animType) {
            case TOP:
                inAnim = getAnimation(R.anim.popup_slide_in_top);
                outAnim = getAnimation(R.anim.popup_slide_out_top);
                setGravity(Gravity.TOP);
                break;
            case RIGHT:
                inAnim = getAnimation(R.anim.popup_slide_in_right);
                outAnim = getAnimation(R.anim.popup_slide_out_right);
                setGravity(Gravity.RIGHT);
                break;
            case BOTTOM:
                inAnim = getAnimation(R.anim.popup_slide_in_bottom);
                outAnim = getAnimation(R.anim.popup_slide_out_bottom);
                setGravity(Gravity.BOTTOM);
                break;
            case LEFT:
                inAnim = getAnimation(R.anim.popup_slide_in_left);
                outAnim = getAnimation(R.anim.popup_slide_out_left);
                setGravity(Gravity.LEFT);
                break;
        }
        inAnim.setDuration(animDuration);
        outAnim.setDuration(animDuration);
    }

    /**
     * show view
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        if (getChildCount() == 0) {
            return;
        }
        if (contentContainer == null) {
            contentContainer = getChildAt(0);
        }
        isShowing = true;
        contentContainer.setVisibility(VISIBLE);
        if (isAnim) {
            inAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setBackgroundColor(shadowBackgroundColor);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            contentContainer.startAnimation(inAnim);
        }
        contentContainer.requestFocus();
    }

    /**
     * is showing
     *
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * dismiss view
     */
    public void dismiss() {
        if (dismissing) {
            return;
        }
        if (getChildCount() == 0) {
            return;
        }
        if (contentContainer == null) {
            contentContainer = getChildAt(0);
        }
        if (isAnim) {
            //消失动画
            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissImmediately();
                    setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            contentContainer.startAnimation(outAnim);
        } else {
            dismissImmediately();
        }
        dismissing = true;
    }

    /**
     * hide view after do some thing
     */
    public void dismissImmediately() {
        this.post(new Runnable() {
            @Override
            public void run() {
                isShowing = false;
                dismissing = false;
                contentContainer.setVisibility(GONE);
                if (onDismissListener != null) {
                    onDismissListener.onDismiss(PopupLayout.this);
                }
            }
        });

    }

    /**
     * create anim
     *
     * @return
     */
    private Animation getAnimation(int animRes) {
        return AnimationUtils.loadAnimation(getContext(), animRes);
    }

    /**
     * setting is can cancel on press keyback
     *
     * @param isCancelable
     */
    public void setKeyBackCancelable(boolean isCancelable) {
        this.setFocusable(isCancelable);
        this.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            this.setOnKeyListener(onKeyBackListener);
        } else {
            this.setOnKeyListener(null);
        }
    }

    /**
     * onKeyBackListener
     */
    private View.OnKeyListener onKeyBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN && isShowing()) {
                dismiss();
                return true;
            }
            return false;
        }
    };
    /**
     * onTouchListener
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (canceledOnTouchOutside && isShowing()) {
                    dismiss();
                }
            }
            return false;
        }
    };

    public PopupLayout setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public interface OnDismissListener {
        public void onDismiss(Object o);
    }

    public enum ANIM {
        TOP(0), RIGHT(1), BOTTOM(2), LEFT(3);

        private int value;

        ANIM(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
