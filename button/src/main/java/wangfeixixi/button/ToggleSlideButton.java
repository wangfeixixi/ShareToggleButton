package wangfeixixi.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class ToggleSlideButton extends View {

    private boolean currentState = false;
    private Bitmap mSwitchTrueBackground;
    private Bitmap mSwitchFalseBackground;
    private Bitmap mSlideButtonBackground;
    private int currentX;
    private boolean isTouching = false;

    private int slideTop;

    public ToggleSlideButton(Context context) {
        super(context);
    }

    public ToggleSlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleSlideButton);

        currentState = ta.getBoolean(R.styleable.ToggleSlideButton_currentState, false);

        Drawable falseDrawable = getResources().getDrawable(R.drawable.toggle_slide_btn_false_bg);
        Drawable trueDrawable = getResources().getDrawable(R.drawable.toggle_slide_btn_toggle_true_bg);
        Drawable slideDrawable = getResources().getDrawable(R.drawable.toggle_slide_btn_slide);

        setSwitchBackgroundResource(trueDrawable, falseDrawable);
        setSlideButtonBackgroundResource(slideDrawable);


        slideTop = (mSwitchFalseBackground.getHeight() - mSlideButtonBackground.getHeight()) / 2;
        ta.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentState = !currentState;
                invalidate();
                Log.d("TTT", "onClick: " + onToggleStateChangedListener);
                if (onToggleStateChangedListener != null) {

                    onToggleStateChangedListener.onToggleStateChanged(currentState);
                }
            }
        });
    }

    public void setSwitchBackgroundResource(Drawable switchTrueDrawble, Drawable switchFalseDrawble) {
        mSwitchTrueBackground = drawableToBitmap(switchTrueDrawble);
        mSwitchFalseBackground = drawableToBitmap(switchFalseDrawble);
    }


    public void setSlideButtonBackgroundResource(Drawable slideButtonDrawable) {
        mSlideButtonBackground = drawableToBitmap(slideButtonDrawable);
    }

    public void setCurrentState(boolean b) {
        this.currentState = b;
        invalidate();
    }

    public boolean getCurrentState() {
        return this.currentState;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(mSwitchFalseBackground.getWidth(), mSwitchFalseBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentState) {
            canvas.drawBitmap(mSwitchTrueBackground, 0, 0, null);
        } else {
            canvas.drawBitmap(mSwitchFalseBackground, 0, 0, null);
        }

        int dp2px = dp2px(getContext(), 1);
        if (isTouching) {
            int left = currentX - mSlideButtonBackground.getWidth() / 2;
            if (left < 0) {
                left = 0;
            } else if (left > mSwitchFalseBackground.getWidth() - mSlideButtonBackground.getWidth()) {
                left = mSwitchFalseBackground.getWidth() - mSlideButtonBackground.getWidth();
            }
            canvas.drawBitmap(mSlideButtonBackground, left, slideTop, null);
        } else {
            if (currentState) {
                int left = mSwitchFalseBackground.getWidth() - mSlideButtonBackground.getWidth();
                canvas.drawBitmap(mSlideButtonBackground, left - dp2px, slideTop, null);
            } else {
                canvas.drawBitmap(mSlideButtonBackground, dp2px, slideTop, null);
            }
        }
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    //	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			isTouching = true;
//			currentX = (int) event.getX();
//			break;
//		case MotionEvent.ACTION_MOVE:
//			currentX = (int) event.getX();
//			break;
//		case MotionEvent.ACTION_UP:
//			isTouching = false;
//			currentX = (int) event.getX();
//
//
//			int center = mSwitchFalseBackground.getWidth() / 2;
//
//			boolean state = currentX > center;
//
//			if(state != currentState && mOnToggleStateChangedListener != null) {
//				mOnToggleStateChangedListener.onToggleStateChanged(state);
//			}
//
//			currentState = state;
//			break;
//		default:
//			break;
//		}
//
//		invalidate();
//		return true;
//	}


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private OnToggleStateChangedListener onToggleStateChangedListener;

    public void setOnToggleStateChangedListener(OnToggleStateChangedListener listener) {
        this.onToggleStateChangedListener = listener;
    }

    public interface OnToggleStateChangedListener {

        public void onToggleStateChanged(boolean currentState);

    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
