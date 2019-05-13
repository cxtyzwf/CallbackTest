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
 * 范围选择月视图
 * Created by huanghaibin on 2018/9/11.
 */
@SuppressWarnings("unused")
public abstract class RangeWeekView extends BaseWeekView {

    public RangeWeekView(Context context) {
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
            boolean isSelected = isCalendarSelected(calendarviewCalendar);
            boolean isPreSelected = isSelectPreCalendar(calendarviewCalendar);
            boolean isNextSelected = isSelectNextCalendar(calendarviewCalendar);
            boolean hasScheme = calendarviewCalendar.hasScheme();
            if (hasScheme) {
                boolean isDrawSelected = false;//是否继续绘制选中的onDrawScheme
                if (isSelected) {
                    isDrawSelected = onDrawSelected(canvas, calendarviewCalendar, x, true, isPreSelected, isNextSelected);
                }
                if (isDrawSelected || !isSelected) {
                    //将画笔设置为标记颜色
                    mSchemePaint.setColor(calendarviewCalendar.getSchemeColor() != 0 ? calendarviewCalendar.getSchemeColor() : mDelegate.getSchemeThemeColor());
                    onDrawScheme(canvas, calendarviewCalendar, x, isSelected);
                }
            } else {
                if (isSelected) {
                    onDrawSelected(canvas, calendarviewCalendar, x, false, isPreSelected, isNextSelected);
                }
            }
            onDrawText(canvas, calendarviewCalendar, x, hasScheme, isSelected);
        }
    }


    /**
     * 日历是否被选中
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @return 日历是否被选中
     */
    private boolean isCalendarSelected(CalendarviewCalendar calendarviewCalendar) {
        if (mDelegate.mSelectedStartRangeCalendarviewCalendar == null) {
            return false;
        }
        if (onCalendarIntercept(calendarviewCalendar)) {
            return false;
        }
        if (mDelegate.mSelectedEndRangeCalendarviewCalendar == null) {
            return calendarviewCalendar.compareTo(mDelegate.mSelectedStartRangeCalendarviewCalendar) == 0;
        }
        return calendarviewCalendar.compareTo(mDelegate.mSelectedStartRangeCalendarviewCalendar) >= 0 &&
                calendarviewCalendar.compareTo(mDelegate.mSelectedEndRangeCalendarviewCalendar) <= 0;
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
            if (mDelegate.mCalendarRangeSelectListener != null) {
                mDelegate.mCalendarRangeSelectListener.onCalendarSelectOutOfRange(calendarviewCalendar);
            }
            return;
        }

        //优先判断各种直接return的情况，减少代码深度
        if (mDelegate.mSelectedStartRangeCalendarviewCalendar != null && mDelegate.mSelectedEndRangeCalendarviewCalendar == null) {
            int minDiffer = CalendarUtil.differ(calendarviewCalendar, mDelegate.mSelectedStartRangeCalendarviewCalendar);
            if (minDiffer >= 0 && mDelegate.getMinSelectRange() != -1 && mDelegate.getMinSelectRange() > minDiffer + 1) {
                if (mDelegate.mCalendarRangeSelectListener != null) {
                    mDelegate.mCalendarRangeSelectListener.onSelectOutOfRange(calendarviewCalendar, true);
                }
                return;
            } else if (mDelegate.getMaxSelectRange() != -1 && mDelegate.getMaxSelectRange() <
                    CalendarUtil.differ(calendarviewCalendar, mDelegate.mSelectedStartRangeCalendarviewCalendar) + 1) {
                if (mDelegate.mCalendarRangeSelectListener != null) {
                    mDelegate.mCalendarRangeSelectListener.onSelectOutOfRange(calendarviewCalendar, false);
                }
                return;
            }
        }

        if (mDelegate.mSelectedStartRangeCalendarviewCalendar == null || mDelegate.mSelectedEndRangeCalendarviewCalendar != null) {
            mDelegate.mSelectedStartRangeCalendarviewCalendar = calendarviewCalendar;
            mDelegate.mSelectedEndRangeCalendarviewCalendar = null;
        } else {
            int compare = calendarviewCalendar.compareTo(mDelegate.mSelectedStartRangeCalendarviewCalendar);
            if (mDelegate.getMinSelectRange() == -1 && compare <= 0) {
                mDelegate.mSelectedStartRangeCalendarviewCalendar = calendarviewCalendar;
                mDelegate.mSelectedEndRangeCalendarviewCalendar = null;
            } else if (compare < 0) {
                mDelegate.mSelectedStartRangeCalendarviewCalendar = calendarviewCalendar;
                mDelegate.mSelectedEndRangeCalendarviewCalendar = null;
            } else if (compare == 0 &&
                    mDelegate.getMinSelectRange() == 1) {
                mDelegate.mSelectedEndRangeCalendarviewCalendar = calendarviewCalendar;
            } else {
                mDelegate.mSelectedEndRangeCalendarviewCalendar = calendarviewCalendar;
            }

        }

        mCurrentItem = mItems.indexOf(calendarviewCalendar);

        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onWeekDateSelected(calendarviewCalendar, true);
        }
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }

        if (mDelegate.mCalendarRangeSelectListener != null) {
            mDelegate.mCalendarRangeSelectListener.onCalendarRangeSelect(calendarviewCalendar,
                    mDelegate.mSelectedEndRangeCalendarviewCalendar != null);
        }

        invalidate();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    /**
     * 上一个日期是否选中
     *
     * @param calendarviewCalendar 当前日期
     * @return 上一个日期是否选中
     */
    protected final boolean isSelectPreCalendar(CalendarviewCalendar calendarviewCalendar) {
        return mDelegate.mSelectedStartRangeCalendarviewCalendar != null &&
                !onCalendarIntercept(calendarviewCalendar) &&
                isCalendarSelected(CalendarUtil.getPreCalendar(calendarviewCalendar));
    }

    /**
     * 下一个日期是否选中
     *
     * @param calendarviewCalendar 当前日期
     * @return 下一个日期是否选中
     */
    protected final boolean isSelectNextCalendar(CalendarviewCalendar calendarviewCalendar) {
        return mDelegate.mSelectedStartRangeCalendarviewCalendar != null &&
                !onCalendarIntercept(calendarviewCalendar) &&
                isCalendarSelected(CalendarUtil.getNextCalendar(calendarviewCalendar));
    }

    /**
     * 绘制选中的日期
     *
     * @param canvas         canvas
     * @param calendarviewCalendar       日历日历calendar
     * @param x              日历Card x起点坐标
     * @param hasScheme      hasScheme 非标记的日期
     * @param isSelectedPre  上一个日期是否选中
     * @param isSelectedNext 下一个日期是否选中
     * @return 是否绘制 onDrawScheme
     */
    protected abstract boolean onDrawSelected(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean hasScheme,
                                              boolean isSelectedPre, boolean isSelectedNext);

    /**
     * 绘制标记的日期
     *
     * @param canvas     canvas
     * @param calendarviewCalendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param isSelected 是否选中
     */
    protected abstract void onDrawScheme(Canvas canvas, CalendarviewCalendar calendarviewCalendar, int x, boolean isSelected);


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
