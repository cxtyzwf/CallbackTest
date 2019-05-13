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
 * 月视图基础控件,可自由继承实现
 * Created by huanghaibin on 2017/11/15.
 */
public abstract class MonthView extends BaseMonthView {

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLineCount == 0)
            return;
        mItemWidth = (getWidth() - 2 * mDelegate.getCalendarPadding()) / 7;
        onPreviewHook();
        int count = mLineCount * 7;
        int d = 0;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarviewCalendar calendarviewCalendar = mItems.get(d);
                if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ONLY_CURRENT_MONTH) {
                    if (d > mItems.size() - mNextDiff) {
                        return;
                    }
                    if (!calendarviewCalendar.isCurrentMonth()) {
                        ++d;
                        continue;
                    }
                } else if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_FIT_MONTH) {
                    if (d >= count) {
                        return;
                    }
                }
                draw(canvas, calendarviewCalendar, i, j, d);
                ++d;
            }
        }
    }


    /**
     * 开始绘制
     *
     * @param canvas   canvas
     * @param calendarviewCalendar 对应日历
     * @param i        i
     * @param j        j
     * @param d        d
     */
    private void draw(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int i, int j, int d) {
        int x = j * mItemWidth + mDelegate.getCalendarPadding();
        int y = i * mItemHeight;
        onLoopStart(x, y);
        boolean isSelected = d == mCurrentItem;
        boolean hasScheme = calendarviewCalendar.hasScheme();

        if (hasScheme) {
            //标记的日子
            boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
            if (isSelected) {
                isDrawSelected = onDrawSelected(canvas, calendarviewCalendar, x, y, true);
            }
            if (isDrawSelected || !isSelected) {
                //将画笔设置为标记颜色
                mSchemePaint.setColor(calendarviewCalendar.getSchemeColor() != 0 ? calendarviewCalendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                onDrawScheme(canvas, calendarviewCalendar, x, y);
            }
        } else {
            if (isSelected) {
                onDrawSelected(canvas, calendarviewCalendar, x, y, false);
            }
        }
        onDrawText(canvas, calendarviewCalendar, x, y, hasScheme, isSelected);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (!isClick) {
            return;
        }
        CalendarviewCalendar calendarviewCalendar = getIndex();

        if (calendarviewCalendar == null) {
            return;
        }

        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ONLY_CURRENT_MONTH &&
                !calendarviewCalendar.isCurrentMonth()) {
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

        /**
         * 其余月份的日子，点击会自动跳转
         */
        if (!calendarviewCalendar.isCurrentMonth() && mMonthViewPager != null) {
            int cur = mMonthViewPager.getCurrentItem();
            int position = mCurrentItem < 7 ? cur - 1 : cur + 1;
            mMonthViewPager.setCurrentItem(position);
        }

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendarviewCalendar, true);
        }

        if (mParentLayout != null) {
            if (calendarviewCalendar.isCurrentMonth()) {
                mParentLayout.updateSelectPosition(mItems.indexOf(calendarviewCalendar));
            } else {
                mParentLayout.updateSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart()));
            }

        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendarviewCalendar, true);
        }

    }

    @SuppressWarnings("deprecation")
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

        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ONLY_CURRENT_MONTH &&
                !calendarviewCalendar.isCurrentMonth()) {
            return false;
        }


        if (onCalendarIntercept(calendarviewCalendar)) {
            mDelegate.mCalendarInterceptListener.onCalendarInterceptClick(calendarviewCalendar, true);
            return false;
        }

        boolean isCalendarInRange = isInRange(calendarviewCalendar);

        if (!isCalendarInRange) {
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClickOutOfRange(calendarviewCalendar);
            }
            return true;
        }

        if (mDelegate.isPreventLongPressedSelected()) {
            if (mDelegate.mCalendarLongClickListener != null) {
                mDelegate.mCalendarLongClickListener.onCalendarLongClick(calendarviewCalendar);
            }
            return true;
        }


        mCurrentItem = mItems.indexOf(calendarviewCalendar);

        if (!calendarviewCalendar.isCurrentMonth() && mMonthViewPager != null) {
            int cur = mMonthViewPager.getCurrentItem();
            int position = mCurrentItem < 7 ? cur - 1 : cur + 1;
            mMonthViewPager.setCurrentItem(position);
        }

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendarviewCalendar, true);
        }

        if (mParentLayout != null) {
            if (calendarviewCalendar.isCurrentMonth()) {
                mParentLayout.updateSelectPosition(mItems.indexOf(calendarviewCalendar));
            } else {
                mParentLayout.updateSelectWeek(CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart()));
            }

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
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 是否绘制onDrawScheme，true or false
     */
    protected abstract boolean onDrawSelected(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y, boolean hasScheme);

    /**
     * 绘制标记的日期,这里可以是背景色，标记色什么的
     *
     * @param canvas   canvas
     * @param calendarviewCalendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    protected abstract void onDrawScheme(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y);


    /**
     * 绘制日历文本
     *
     * @param canvas     canvas
     * @param calendarviewCalendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    protected abstract void onDrawText(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, int y, boolean hasScheme, boolean isSelected);
}
