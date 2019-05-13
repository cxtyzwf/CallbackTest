package com.example.administrator.callbacktest.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.haibin.calendarview.CalendarviewCalendar;
import com.haibin.calendarview.WeekView;

/**
 * 简单周视图
 * Created by huanghaibin on 2017/11/29.
 */

public class SimpleWeekView extends WeekView {

    private int mRadius;
    private Paint bluePaint = new Paint();
    private Paint redPaint = new Paint();
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;


    public SimpleWeekView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE,mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
        redPaint.setAntiAlias(true);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(0xFFFF4848);
        bluePaint.setAntiAlias(true);
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(0xFF3F52E7);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

        mPadding = dipToPx(getContext(), 2);
        mPointRadius = dipToPx(context, 2);
        mCircleRadius = dipToPx(getContext(), 7);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        int angle = getAngle(100);
        if (calendarviewCalendar.isCurrentDay()){
            RectF progressRectF1 = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
            canvas.drawArc(progressRectF1, -180, angle, false, bluePaint);
        }else {
            RectF progressRectF1 = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
            canvas.drawArc(progressRectF1, -180, angle, false, bluePaint);
        }

        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
        int angle = getAngle(100);
        if (calendarviewCalendar.isCurrentDay()){
            RectF progressRectF1 = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
            canvas.drawArc(progressRectF1, -180, angle, false, bluePaint);
        }
            boolean isSelected = isSelected(calendarviewCalendar);
            if (isSelected) {
                mPointPaint.setColor(0xFF3F52E7);
            } else {
                mPointPaint.setColor(0xFF3F52E7);
            }

        if (calendarviewCalendar.hasScheme()){
            canvas.drawCircle(x + mItemWidth / 2, mItemHeight -  mPadding, mPointRadius, bluePaint);
        }

    }

    @Override
    protected void onDrawText(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;
//        if (isSelected) {
////            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
////                    cx,
////                    baselineY,
////                    mSelectTextPaint);
////            canvas.drawText(calendarviewCalendar.isCurrentDay() ? "今":String.valueOf(calendarviewCalendar.getDay()),
//            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
//
//                    cx,
//                    baselineY,
//                    mSelectTextPaint);
////        } else if (hasScheme) {
//        } else if (calendarviewCalendar.isCurrentDay()) {
////            canvas.drawText(calendarviewCalendar.isCurrentDay() ? "今":String.valueOf(calendarviewCalendar.getDay()),
//            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
//
//                    cx,
//                    baselineY,
//                    calendarviewCalendar.isCurrentDay() ? mSelectTextPaint :
//                            calendarviewCalendar.isCurrentMonth() ? mSchemeTextPaint : mSchemeTextPaint);
//
//        } else {
//            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
//                    cx,
//                    baselineY,
//                    calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
//                            calendarviewCalendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
//        }
        if (isSelected) {
            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);

        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
                    cx,
                    baselineY,
                    calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
                            calendarviewCalendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);


        } else {
            canvas.drawText(String.valueOf(calendarviewCalendar.getDay()),
                    cx,
                    baselineY,
                    calendarviewCalendar.isCurrentDay() ? mCurDayTextPaint :
                            calendarviewCalendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

        }
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
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
