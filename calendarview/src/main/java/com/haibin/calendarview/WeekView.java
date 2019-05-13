/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haibin.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * 周视图，因为日历UI采用热插拔实现，所以这里必须继承实现，达到UI一致即可
 * Created by huanghaibin on 2017/11/21.
 */
public abstract class WeekView extends BaseWeekView {

    public WeekView(Context context) {
        super(context);
    }

    /**
     * 绘制日历文本
     *
     * @param canvas canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems.size() == 0)
            return;
        mItemWidth = (getWidth() - 2 * mDelegate.getCalendarPadding()) / 7;
        onPreviewHook();

        for (int i = 0; i < 7; i++) {
            int x = i * mItemWidth + mDelegate.getCalendarPadding();
            onLoopStart(x);
            CalendarviewCalendar calendarviewCalendar = mItems.get(i);
            boolean isSelected = i == mCurrentItem;
            boolean hasScheme = calendarviewCalendar.hasScheme();
            if (hasScheme) {
                boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
                if (isSelected) {
                    isDrawSelected = onDrawSelected(canvas, calendarviewCalendar, x, true);
                }
                if (isDrawSelected || !isSelected) {
                    //将画笔设置为标记颜色
                    mSchemePaint.setColor(calendarviewCalendar.getSchemeColor() != 0 ? calendarviewCalendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                    onDrawScheme(canvas, calendarviewCalendar, x);
                }
            } else {
                if (isSelected) {
                    onDrawSelected(canvas, calendarviewCalendar, x, false);
                }
            }
            onDrawText(canvas, calendarviewCalendar, x, hasScheme, isSelected);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isClick) {
            return;
        }
        CalendarviewCalendar calendarviewCalendar = getIndex();
        if (calendarviewCalendar == null) {
            return;
        }
        if (onCalendarIntercept(calendarviewCalendar)) {
            mDelegate.mCalendarInterceptListener.onCalendarInterceptClick(calendarviewCalendar, true);
            return;
        }
        if (!isInRange(calendarviewCalendar)) {
            if (mDelegate.mCalendarSelectListener != null) {
                mDelegate.mCalendarSelectListener.onCalendarOutOfRange(calendarviewCalendar);
            }
            return;
        }

        mCurrentItem = mItems.indexOf(calendarviewCalendar);

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendarviewCalendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendarviewCalendar, true);
        }

        invalidate();
    }


    @Override
    public boolean onLongClick(View v) {
        if (mDelegate.mCalendarLongClickListener == null)
            return false;
        if (!isClick) {
            return false;
        }
        CalendarviewCalendar calendarviewCalendar = getIndex();
        if (calendarviewCalendar == null) {
            return false;
        }
        if (onCalendarIntercept(calendarviewCalendar)) {
            mDelegate.mCalendarInterceptListener.onCalendarInterceptClick(calendarviewCalendar, true);
            return true;
        }
        boolean isCalendarInRange = isInRange(calendarviewCalendar);

        if (!isCalendarInRange) {
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClickOutOfRange(calendarviewCalendar);
            }
            return true;
        }

        if (mDelegate.isPreventLongPressedSelected()) {//如果启用拦截长按事件不选择日期
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClick(calendarviewCalendar);
            }
            return true;
        }


        mCurrentItem = mItems.indexOf(calendarviewCalendar);

        mDelegate.mIndexCalendarviewCalendar = mDelegate.mSelectedCalendarviewCalendar;

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendarviewCalendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendarviewCalendar, true);
        }

        if (mDelegate.mCalendarLongClickListener != null) {
            mDelegate.mCalendarLongClickListener.onCalendarLongClick(calendarviewCalendar);
        }

        invalidate();
        return true;
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas    canvas
     * @param calendarviewCalendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 是否绘制 onDrawScheme
     */
    protected abstract boolean onDrawSelected(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean hasScheme);

    /**
     * 绘制标记的日期
     *
     * @param canvas   canvas
     * @param calendarviewCalendar 日历calendar
     * @param x        日历Card x起点坐标
     */
    protected abstract void onDrawScheme(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x);


    /**
     * 绘制日历文本
     *
     * @param canvas     canvas
     * @param calendarviewCalendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    protected abstract void onDrawText(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean hasScheme, boolean isSelected);
}
