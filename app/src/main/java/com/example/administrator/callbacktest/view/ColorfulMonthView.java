package com.example.administrator.callbacktest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.haibin.calendarview.CalendarviewCalendar;
import com.haibin.calendarview.MonthView;


public class ColorfulMonthView extends MonthView {

    private int mRadius,mRadius2;
   private Drawable drawable ;
    private Paint mProgressPaint = new Paint();
   private RectF progressRectF1;
   private RectF progressRectF2;


    private Paint redPaint = new Paint();
    private Paint yellowPaint = new Paint();
    private Paint bluePaint = new Paint();
    private Paint lightbluePaint = new Paint();
    private Paint alphaPaint = new Paint();
    private Paint onPaint = new Paint();
    private Paint offPaint = new Paint();
    private Paint mSchemeBasicINGPaint = new Paint();
    private Paint mTextPaint = new Paint();

   private Rect mTopSrcRect;
    private Rect  mTopDestRect ;
    private  Bitmap bitmap;

    private int mPadding;
    private float mCircleRadius;
    private float mSchemeBaseLine;
    private String on,off;
    private String leave_status,trip_status;



    public ColorfulMonthView(Context context) {
        super(context);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint);
        mSchemePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mProgressPaint.setStrokeWidth(dipToPx(context, 5.0f));
        mProgressPaint.setColor(0xFFAAD3FA);
        redPaint.setAntiAlias(true);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(0xFFFF4848);
        yellowPaint.setAntiAlias(true);
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(0xFFFFD200);
        bluePaint.setAntiAlias(true);
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(0xFF7FC3FF);
        lightbluePaint.setAntiAlias(true);
        lightbluePaint.setStyle(Paint.Style.FILL);
        lightbluePaint.setColor(0xFFA9D7FF);
        alphaPaint.setAntiAlias(true);
        alphaPaint.setStyle(Paint.Style.FILL);
        alphaPaint.setColor(0xFFCFCFCF);

        //右上角标记的一些画笔和数据
        mPadding = dipToPx(getContext(), 6);
        mCircleRadius = dipToPx(getContext(), 6);

        Paint.FontMetrics metrics = mSchemeBasicINGPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        mSchemeBasicINGPaint.setAntiAlias(true);
        mSchemeBasicINGPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicINGPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicINGPaint.setFakeBoldText(true);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(25);
        mTextPaint.setFakeBoldText(true);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;

    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendarviewCalendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;


        int angle = getAngle(100);
        mRadius2 = Math.min(mItemWidth, mItemHeight) / 11 * 5;

        RectF progressRectF = new RectF(cx - mRadius2, cy - mRadius2, cx + mRadius2, cy + mRadius2);
        canvas.drawArc(progressRectF, -90, angle, false, mProgressPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
                    int angle = getAngle(50);
                    on= calendarviewCalendar.getSchemes().get(0).getScheme();
                     off= calendarviewCalendar.getSchemes().get(1).getScheme();
        leave_status= calendarviewCalendar.getSchemes().get(2).getScheme();
        trip_status= calendarviewCalendar.getSchemes().get(3).getScheme();
                    if (on !=null||off!=null){
                        switch (on){
                            case "Normal":
                                onPaint = bluePaint;
                                break;
                            case "NotSigned":
                                onPaint = redPaint;
                                break;
                            case "Late":
                                onPaint = yellowPaint;
                                break;
                            case "NotInSign":
                                onPaint = lightbluePaint;
                                break;
//                            default:
//                                onPaint =alphaPaint;
//                                break;
                        }
                        RectF progressRectF1 = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
                        canvas.drawArc(progressRectF1, -180, angle, false, onPaint);
                        switch (off){
                            case "Normal":
                                offPaint = bluePaint;
                                break;
                            case "NotSigned":
                                offPaint = redPaint;
                                break;
                            case "Early":
                                offPaint = yellowPaint;
                                break;
                            case "NotInSign":
                                offPaint = lightbluePaint;
                                break;
//                            default:
//                                offPaint =alphaPaint;
//                                break;

                        }
                        RectF progressRectF2 = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
                        canvas.drawArc(progressRectF2, 0, angle, false, offPaint);
                    }else {
                        if(on ==null){
                           return;
                        }
                        if(off==null){
                           return;
                        }
                    }




    }

    @Override
    protected void onDrawText(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        // 右上角的标识





if (hasScheme){
 //  测试右上角标识的背景色和字

    if (leave_status!=null){
        if (trip_status.equals("0")||trip_status.equals("1")){
            mSchemeBasicINGPaint.setColor(Color.rgb(79,75,255));
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicINGPaint);
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius , y + mPadding+ mCircleRadius/3, mCircleRadius, mSchemeBasicINGPaint);

//            canvas.drawText("T", x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
            canvas.drawText("T", x + mItemWidth - mPadding - mCircleRadius-8, y + mPadding+ mSchemeBaseLine/2+3 , mTextPaint);
        }else if (trip_status.equals("2")){
            mSchemeBasicINGPaint.setColor(Color.rgb(43,165,236));
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicINGPaint);
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius , y + mPadding+ mCircleRadius/3 , mCircleRadius, mSchemeBasicINGPaint);

//            canvas.drawText("T", x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
            canvas.drawText("T", x + mItemWidth - mPadding - mCircleRadius-8, y + mPadding + mSchemeBaseLine/2+3, mTextPaint);
        }
    }

    if (trip_status!=null){
        if (leave_status.equals("0")||leave_status.equals("1")){
            mSchemeBasicINGPaint.setColor(Color.rgb(79,75,255));
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicINGPaint);
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius , y + mPadding+ mCircleRadius/3 , mCircleRadius, mSchemeBasicINGPaint);

//            canvas.drawText("L", x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
            canvas.drawText("L", x + mItemWidth - mPadding - mCircleRadius-8, y + mPadding + mSchemeBaseLine/2+3, mTextPaint);
        }else if (leave_status.equals("2")){
            mSchemeBasicINGPaint.setColor(Color.rgb(43,165,236));
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, y + mPadding + mCircleRadius, mCircleRadius, mSchemeBasicINGPaint);
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius , y + mPadding+ mCircleRadius/3 , mCircleRadius, mSchemeBasicINGPaint);

//            canvas.drawText("L", x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
            canvas.drawText("L", x + mItemWidth - mPadding - mCircleRadius-8, y + mPadding + mSchemeBaseLine/2+3, mTextPaint);
        }
    }
}

        if (isSelected) {
            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
    if (on ==null||off ==null){
        canvas.drawText(String.valueOf(calendarviewCalendar.getDay()), cx, baselineY,
                calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
                        calendarviewCalendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
    }else {
        canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
                cx,
                baselineY,
                calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
                        calendarviewCalendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
    }

        } else {
            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()), cx, baselineY,
                    calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
                            calendarviewCalendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }

    }
    /**
     * 获取角度
     *
     * @param progress 进度
     * @return 获取角度
     */
    private static int getAngle(int progress) {
        return (int) (progress * 3.6);
    }
    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
