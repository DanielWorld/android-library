package net.danielpark.library.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import net.danielpark.library.R;


/**
 * Loading Progress bar
 * <br><br>
 * Created by Daniel Park on 2017. 11. 15..
 */

public class LoadingProgressBar extends View {

    public LoadingProgressBar(Context context) {
        this(context, null);
    }

    public LoadingProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Daniel (2017-02-17 18:00:20): Manage attributes
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingProgressBar, defStyleAttr, 0);

        if (typedArray.hasValue(R.styleable.LoadingProgressBar_backgroundColor)) {
            mBackgroundColor = typedArray.getColor(R.styleable.LoadingProgressBar_backgroundColor, mBackgroundColor);      // Background color
        }

        if (typedArray.hasValue(R.styleable.LoadingProgressBar_progressColor)){
            mProgressColor = typedArray.getColor(R.styleable.LoadingProgressBar_progressColor, mProgressColor);		// Progress color
        }

        if (typedArray.hasValue(R.styleable.LoadingProgressBar_borderRound)) {
            mRound = typedArray.getDimensionPixelSize(R.styleable.LoadingProgressBar_borderRound, 0);
        }

        if (typedArray.hasValue(R.styleable.LoadingProgressBar_progressRate)) {
            float tempRate = typedArray.getFloat(R.styleable.LoadingProgressBar_progressRate, 0.005f) / 100;

            if (tempRate > 0)
                mProgressRate = tempRate;
            else
                mProgressRate = 0.005f;
        }

        initView();

        typedArray.recycle();
    }

    protected @ColorInt
    int mBackgroundColor = 0x22ffffff;              // 배경색
    protected @ColorInt int mProgressColor = 0xFFffffff;				// Progress 색
    protected float mProgressRate = 0.005f;								// Progress 증가율

    private Paint pBG;
    private Paint pProgress;
    private Path pPath;

    private float mRound = -1;
    private RectF mRect = new RectF();
    private RectF mRectProgress = new RectF();

    private long mInitTime = 0;

    private void initView() {
        mInitTime = System.currentTimeMillis();

        pBG = new Paint(Paint.ANTI_ALIAS_FLAG);
        pProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        pPath = new Path();

        pBG.setColor(mBackgroundColor);
        pProgress.setColor(mProgressColor);

        mHandler = new Handler(
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {

                        if (System.currentTimeMillis() - mInitTime > 500) {
                            if (getVisibility() != VISIBLE) {
                                setVisibility(VISIBLE);
                            }
                        }

                        mRate += mProgressRate; // Daniel (2017-05-24 22:31:54): 0.5 % 씩 증가
                        if (mRate < mGoRate) {
                            mRate = mGoRate;
                        }

                        if (mRate > 1)
                            mRate = 1;

                        if (mListener != null)
                            mListener.currentProgress(mRate * 100);

                        invalidate();
                        if (mRate < 1) {
                            mHandler.sendEmptyMessageDelayed(0, 55);
                        } else {
                            // 100 % 완료시 listener 해제
                            mListener = null;
                        }
                        return true;
                    }
                }
        );
//		mHandler.sendEmptyMessage(0);

        this.setVisibility(INVISIBLE);
    }

    private Handler mHandler;

    private float mRate = 0;
    private float mGoRate = 0;

    public void startProgress() {
        this.mGoRate = 0;			// Daniel (2017-05-26 10:58:55): 수치 초기화
        this.mRate = 0;

        // Remove pending post message with what = 0
        mHandler.removeMessages(0);

        if (mHandler != null)
            mHandler.sendEmptyMessage(0);
    }

    public void setProgress(float percentage) {
        this.mGoRate = percentage / 100;
    }

    public void complete() {
        mGoRate = 1;
        mRate = 1;
        // Remove pending post message with what = 0
        mHandler.removeMessages(0);
        invalidate();
    }

    private void customDraw(Canvas c) {
        if (mRound >= 0) {
            c.drawRoundRect(mRect, mRound, mRound, pBG);
//			mRectProgress.right = mRectProgress.left + mRect.width() * mRate;
//			c.drawRoundRect(mRectProgress, mRound, mRound, pProgress);

            // ----------------------------------------------
            if (mRect.width() * mRate <= mRound) {
                mRate = mRound / mRect.width();
                mRectProgress.right = mRectProgress.left + mRound;
            } else {
                mRectProgress.right = mRectProgress.left + mRect.width() * mRate;
            }

            // TODO: Progress 진행되는 동안에는 round 제거
            // mRect.width() - mRect.width() * mRate < mRound 일 경우에 round 부여
            if (mRect.width() - mRect.width() * mRate <= mRound) {
                c.drawRoundRect(mRectProgress, mRound, mRound, pProgress);
            } else {
                // top-left, bottom-left round rect progress 바 처리
                pPath.reset();
                RoundedRect(pPath, 0, 0, mRectProgress.width() , mRectProgress.height()
                        , mRound, mRound,
                        true, false, false, true);
                c.drawPath(pPath, pProgress);
            }
            // --------------------------------------------------

        } else {
            c.drawRoundRect(mRect, 0, 0, pBG);
            mRectProgress.right = mRectProgress.left + mRect.width() * mRate;
            c.drawRoundRect(mRectProgress, 0, 0, pProgress);
        }
    }

    private void sizeChanged(int w, int h, int oldw, int oldh) {
        mRect.set(1, 1, w - 1, h - 1);
        if (mRound < 0) {
            mRound = mRect.height() / 2f;
        }

        mRectProgress.set(mRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        customDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        sizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = 0, h = 0;
        // ceil not round - avoid thin vertical gaps along the left/right edges
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth() + getMeasuredWidth();
        int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight() + getMeasuredHeight();

        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case View.MeasureSpec.AT_MOST://wrap_content
                w = minw;
                break;
            case View.MeasureSpec.UNSPECIFIED://unspecified
                w = widthMeasureSpec;
                break;
            case View.MeasureSpec.EXACTLY://match_parent
                w = View.MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case View.MeasureSpec.AT_MOST:
                h = minh;
                break;
            case View.MeasureSpec.UNSPECIFIED:
                h = heightMeasureSpec;
                break;
            case View.MeasureSpec.EXACTLY:
                h = View.MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(w, h);
    }

    private static Path RoundedRect(Path path,
                                   float left, float top, float right, float bottom, float rx, float ry,
                                   boolean tl, boolean tr, boolean br, boolean bl
    ){
//		Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    private OnLoadingProgressBarListener mListener;

    public interface OnLoadingProgressBarListener {
        void currentProgress(float percentage);
    }

    public void setOnLoadingProgressBarListener(OnLoadingProgressBarListener listener) {
        this.mListener = listener;
    }
}
