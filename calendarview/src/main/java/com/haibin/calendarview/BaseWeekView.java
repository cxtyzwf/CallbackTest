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

/**
 * 最基础周视图，因为日历UI采用热插拔实现，所以这里必须继承实现，达到UI一致即可
 * 可通过此扩展各种视图如：WeekView、RangeWeekView
 */

public abstract class BaseWeekView extends BaseView {

    public BaseWeekView(Context context) {
        super(context);
    }

    /**
     * 初始化周视图控件
     *
     * @param calendarviewCalendar calendarviewCalendar
     */
    final void setup(CalendarviewCalendar calendarviewCalendar) {
        mItems = CalendarUtil.initCalendarForWeekView(calendarviewCalendar, mDelegate, mDelegate.getWeekStart());
        addSchemesFromMap();
        invalidate();
    }


    /**
     * 记录已经选择的日期
     *
     * @param calendarviewCalendar calendarviewCalendar
     */
    final void setSelectedCalendar(CalendarviewCalendar calendarviewCalendar) {
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_SINGLE &&
                !calendarviewCalendar.equals(mDelegate.mSelectedCalendarviewCalendar)) {
            return;
        }
        mCurrentItem = mItems.indexOf(calendarviewCalendar);
    }


    /**
     * 周视图切换点击默认位置
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @param isNotice isNotice
     */
    final void performClickCalendar(CalendarviewCalendar calendarviewCalendar, boolean isNotice) {
        if (mParentLayout == null || mDelegate.mInnerListener == null || mItems == null || mItems.size() == 0) {
            return;
        }

        int week = CalendarUtil.getWeekViewIndexFromCalendar(calendarviewCalendar, mDelegate.getWeekStart());
        if (mItems.contains(mDelegate.getCurrentDay())) {
            week = CalendarUtil.getWeekViewIndexFromCalendar(mDelegate.getCurrentDay(), mDelegate.getWeekStart());
        }

        int curIndex = week;

        CalendarviewCalendar currentCalendarviewCalendar = mItems.get(week);
        if (mDelegate.getSelectMode() != CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            if (mItems.contains(mDelegate.mSelectedCalendarviewCalendar)) {
                currentCalendarviewCalendar = mDelegate.mSelectedCalendarviewCalendar;
            } else {
                mCurrentItem = -1;
            }
        }

        if (!isInRange(currentCalendarviewCalendar)) {
            curIndex = getEdgeIndex(isMinRangeEdge(currentCalendarviewCalendar));
            currentCalendarviewCalendar = mItems.get(curIndex);
        }


        currentCalendarviewCalendar.setCurrentDay(currentCalendarviewCalendar.equals(mDelegate.getCurrentDay()));
        mDelegate.mInnerListener.onWeekDateSelected(currentCalendarviewCalendar, false);
        int i = CalendarUtil.getWeekFromDayInMonth(currentCalendarviewCalendar, mDelegate.getWeekStart());
        mParentLayout.updateSelectWeek(i);


        if (mDelegate.mCalendarSelectListener != null
                && isNotice
                && mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(currentCalendarviewCalendar, false);
        }

        mParentLayout.updateContentViewTranslateY();
        if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
            mCurrentItem = curIndex;
        }
        mDelegate.mIndexCalendarviewCalendar = currentCalendarviewCalendar;
        invalidate();
    }

    /**
     * 是否是最小访问边界了
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @return 是否是最小访问边界了
     */
    final boolean isMinRangeEdge(CalendarviewCalendar calendarviewCalendar) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(mDelegate.getMinYear(), mDelegate.getMinYearMonth() - 1, mDelegate.getMinYearDay());
        long minTime = c.getTimeInMillis();
        c.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
    }

    /**
     * 获得边界范围内下标
     *
     * @param isMinEdge isMinEdge
     * @return 获得边界范围内下标
     */
    final int getEdgeIndex(boolean isMinEdge) {
        for (int i = 0; i < mItems.size(); i++) {
            CalendarviewCalendar item = mItems.get(i);
            boolean isInRange = isInRange(item);
            if (isMinEdge && isInRange) {
                return i;
            } else if (!isMinEdge && !isInRange) {
                return i - 1;
            }
        }
        return isMinEdge ? 6 : 0;
    }


    /**
     * 获取点击的日历
     *
     * @return 获取点击的日历
     */
    protected CalendarviewCalendar getIndex() {

        int indexX = (int) mX / mItemWidth;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        int position = indexY * 7 + indexX;// 选择项
        if (position >= 0 && position < mItems.size())
            return mItems.get(position);
        return null;
    }


    /**
     * 更新显示模式
     */
    final void updateShowMode() {
        invalidate();
    }

    /**
     * 更新周起始
     */
    final void updateWeekStart() {

        int position = (int) getTag();
        CalendarviewCalendar calendarviewCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(mDelegate.getMinYear(),
                mDelegate.getMinYearMonth(),
                mDelegate.getMinYearDay(),
                position + 1,
                mDelegate.getWeekStart());
        setSelectedCalendar(mDelegate.mSelectedCalendarviewCalendar);
        setup(calendarviewCalendar);
    }

    /**
     * 更新当选模式
     */
    final void updateSingleSelect() {
        if (!mItems.contains(mDelegate.mSelectedCalendarviewCalendar)) {
            mCurrentItem = -1;
            invalidate();
        }
    }

    @Override
    void updateCurrentDate() {
        if (mItems == null)
            return;
        if (mItems.contains(mDelegate.getCurrentDay())) {
            for (CalendarviewCalendar a : mItems) {//添加操作
                a.setCurrentDay(false);
            }
            int index = mItems.indexOf(mDelegate.getCurrentDay());
            mItems.get(index).setCurrentDay(true);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 开始绘制前的钩子，这里做一些初始化的操作，每次绘制只调用一次，性能高效
     * 没有需要可忽略不实现
     * 例如：
     * 1、需要绘制圆形标记事件背景，可以在这里计算半径
     * 2、绘制矩形选中效果，也可以在这里计算矩形宽和高
     */
    protected void onPreviewHook() {
        // TODO: 2017/11/16
    }


    /**
     * 循环绘制开始的回调，不需要可忽略
     * 绘制每个日历项的循环，用来计算baseLine、圆心坐标等都可以在这里实现
     *
     * @param x 日历Card x起点坐标
     */
    @SuppressWarnings("unused")
    protected void onLoopStart(int x) {
        // TODO: 2017/11/16
    }

    @Override
    protected void onDestroy() {

    }
}
