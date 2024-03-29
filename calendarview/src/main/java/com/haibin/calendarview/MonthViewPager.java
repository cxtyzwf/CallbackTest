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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;


/**
 * 月份切换ViewPager，自定义适应高度
 */
public final class MonthViewPager extends ViewPager {

    private boolean isUpdateMonthView;

    private int mMonthCount;

    private CalendarViewDelegate mDelegate;

    private int mNextViewHeight, mPreViewHeight, mCurrentViewHeight;

    CalendarLayout mParentLayout;

    WeekViewPager mWeekPager;

    WeekBar mWeekBar;

    /**
     * 是否使用滚动到某一天
     */
    private boolean isUsingScrollToCalendar = false;

    public MonthViewPager(Context context) {
        this(context, null);
    }

    public MonthViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     *
     * @param delegate delegate
     */
    void setup(CalendarViewDelegate delegate) {
        this.mDelegate = delegate;

        updateMonthViewHeight(mDelegate.getCurrentDay().getYear(),
                mDelegate.getCurrentDay().getMonth());

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        setAdapter(new MonthViewPagerAdapter());
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
                    return;
                }
                int height;
                if (position < getCurrentItem()) {//右滑-1
                    height = (int) ((mPreViewHeight)
                            * (1 - positionOffset) +
                            mCurrentViewHeight
                                    * positionOffset);
                } else {//左滑+！
                    height = (int) ((mCurrentViewHeight)
                            * (1 - positionOffset) +
                            (mNextViewHeight)
                                    * positionOffset);
                }
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                CalendarviewCalendar calendarviewCalendar = CalendarUtil.getFirstCalendarFromMonthViewPager(position, mDelegate);
                mDelegate.mIndexCalendarviewCalendar = calendarviewCalendar;
                //月份改变事件
                if (mDelegate.mMonthChangeListener != null) {
                    mDelegate.mMonthChangeListener.onMonthChange(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth());
                }

                //周视图显示的时候就需要动态改变月视图高度
                if (mWeekPager.getVisibility() == VISIBLE) {
                    updateMonthViewHeight(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth());
                    return;
                }


                if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                    if (!calendarviewCalendar.isCurrentMonth()) {
                        mDelegate.mSelectedCalendarviewCalendar = calendarviewCalendar;
                    } else {
                        mDelegate.mSelectedCalendarviewCalendar = CalendarUtil.getRangeEdgeCalendar(calendarviewCalendar, mDelegate);
                    }
                    mDelegate.mIndexCalendarviewCalendar = mDelegate.mSelectedCalendarviewCalendar;
                } else {
                    if (mDelegate.mSelectedStartRangeCalendarviewCalendar != null &&
                            mDelegate.mSelectedStartRangeCalendarviewCalendar.isSameMonth(mDelegate.mIndexCalendarviewCalendar)) {
                        mDelegate.mIndexCalendarviewCalendar = mDelegate.mSelectedStartRangeCalendarviewCalendar;
                    } else {
                        if (calendarviewCalendar.isSameMonth(mDelegate.mSelectedCalendarviewCalendar)) {
                            mDelegate.mIndexCalendarviewCalendar = mDelegate.mSelectedCalendarviewCalendar;
                        }
                    }
                }

                mDelegate.updateSelectCalendarScheme();
                if (!isUsingScrollToCalendar && mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                    mWeekBar.onDateSelected(mDelegate.mSelectedCalendarviewCalendar, mDelegate.getWeekStart(), false);
                    if (mDelegate.mCalendarSelectListener != null) {
                        mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendarviewCalendar, false);
                    }
                }

                BaseMonthView view = (BaseMonthView) findViewWithTag(position);
                if (view != null) {
                    int index = view.getSelectedIndex(mDelegate.mIndexCalendarviewCalendar);
                    if (mDelegate.getSelectMode() == CalendarViewDelegate.SELECT_MODE_DEFAULT) {
                        view.mCurrentItem = index;
                    }
                    if (index >= 0 && mParentLayout != null) {
                        mParentLayout.updateSelectPosition(index);
                    }
                    view.invalidate();
                }
                mWeekPager.updateSelected(mDelegate.mIndexCalendarviewCalendar, false);
                updateMonthViewHeight(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth());
                isUsingScrollToCalendar = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 更新月视图的高度
     *
     * @param year  year
     * @param month month
     */
    private void updateMonthViewHeight(int year, int month) {
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {//非动态高度就不需要了
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            return;
        }

        if (mParentLayout != null) {
            if (getVisibility() != VISIBLE) {//如果已经显示周视图，则需要动态改变月视图高度，否则显示就有bug
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = CalendarUtil.getMonthViewHeight(year, month, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
                setLayoutParams(params);
            }
            mParentLayout.updateContentViewTranslateY();
        }
        mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
        if (month == 1) {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year - 1, 12, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            mNextViewHeight = CalendarUtil.getMonthViewHeight(year, 2, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
        } else {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year, month - 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            if (month == 12) {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year + 1, 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            } else {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year, month + 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            }
        }
    }

    /**
     * 刷新
     */
    void notifyDataSetChanged() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 +
                mDelegate.getMaxYearMonth();
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 更新月视图Class
     */
    void updateMonthViewClass() {
        isUpdateMonthView = true;
        getAdapter().notifyDataSetChanged();
        isUpdateMonthView = false;
    }

    /**
     * 更新日期范围
     */
    final void updateRange() {
        isUpdateMonthView = true;
        notifyDataSetChanged();
        isUpdateMonthView = false;
        if (getVisibility() != VISIBLE) {
            return;
        }
        isUsingScrollToCalendar = true;
        CalendarviewCalendar calendarviewCalendar = mDelegate.mSelectedCalendarviewCalendar;
        int y = calendarviewCalendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendarviewCalendar.getMonth() - mDelegate.getMinYearMonth();
        setCurrentItem(position, false);
        BaseMonthView view = (BaseMonthView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendarviewCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendarviewCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }


        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendarviewCalendar, false);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendarviewCalendar, false);
        }
        updateSelected();
    }

    /**
     * 滚动到指定日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    void scrollToCalendar(int year, int month, int day, boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(year);
        calendarviewCalendar.setMonth(month);
        calendarviewCalendar.setDay(day);
        calendarviewCalendar.setCurrentDay(calendarviewCalendar.equals(mDelegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendarviewCalendar);
        mDelegate.mIndexCalendarviewCalendar = calendarviewCalendar;
        mDelegate.mSelectedCalendarviewCalendar = calendarviewCalendar;
        mDelegate.updateSelectCalendarScheme();
        int y = calendarviewCalendar.getYear() - mDelegate.getMinYear();
        int position = 12 * y + calendarviewCalendar.getMonth() - mDelegate.getMinYearMonth();
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }
        setCurrentItem(position, smoothScroll);

        BaseMonthView view = (BaseMonthView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.mIndexCalendarviewCalendar);
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.mIndexCalendarviewCalendar));
            }
        }
        if (mParentLayout != null) {
            int week = CalendarUtil.getWeekFromDayInMonth(calendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(week);
        }

        if (mDelegate.mCalendarSelectListener != null) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(calendarviewCalendar, false);
        }
        if (mDelegate.mInnerListener != null) {
            mDelegate.mInnerListener.onMonthDateSelected(calendarviewCalendar, false);
        }

        updateSelected();
    }

    /**
     * 滚动到当前日期
     */
    void scrollToCurrent(boolean smoothScroll) {
        isUsingScrollToCalendar = true;
        int position = 12 * (mDelegate.getCurrentDay().getYear() - mDelegate.getMinYear()) +
                mDelegate.getCurrentDay().getMonth() - mDelegate.getMinYearMonth();
        int curItem = getCurrentItem();
        if (curItem == position) {
            isUsingScrollToCalendar = false;
        }

        setCurrentItem(position, smoothScroll);

        BaseMonthView view = (BaseMonthView) findViewWithTag(position);
        if (view != null) {
            view.setSelectedCalendar(mDelegate.getCurrentDay());
            view.invalidate();
            if (mParentLayout != null) {
                mParentLayout.updateSelectPosition(view.getSelectedIndex(mDelegate.getCurrentDay()));
            }
        }

        if (mDelegate.mCalendarSelectListener != null && getVisibility() == VISIBLE) {
            mDelegate.mCalendarSelectListener.onCalendarSelect(mDelegate.mSelectedCalendarviewCalendar, false);
        }
    }

    /**
     * 更新为默认选择模式
     */
    void updateDefaultSelect() {
        BaseMonthView view = (BaseMonthView) findViewWithTag(getCurrentItem());
        if (view != null) {
            int index = view.getSelectedIndex(mDelegate.mSelectedCalendarviewCalendar);
            view.mCurrentItem = index;
            if (index >= 0 && mParentLayout != null) {
                mParentLayout.updateSelectPosition(index);
            }
            view.invalidate();
        }
    }


    /**
     * 更新选择效果
     */
    void updateSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.setSelectedCalendar(mDelegate.mSelectedCalendarviewCalendar);
            view.invalidate();
        }
    }

    /**
     * 更新标记日期
     */
    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.update();
        }
    }

    /**
     * 更新当前日期，夜间过度的时候调用这个函数，一般不需要调用
     */
    void updateCurrentDate() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateCurrentDate();
        }
    }


    /**
     * 更新显示模式
     */
    void updateShowMode() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateShowMode();
            view.requestLayout();
        }
        if (mDelegate.getMonthViewShowMode() == CalendarViewDelegate.MODE_ALL_MONTH) {
            mCurrentViewHeight = 6 * mDelegate.getCalendarItemHeight();
            mNextViewHeight = mCurrentViewHeight;
            mPreViewHeight = mCurrentViewHeight;
        } else {
            updateMonthViewHeight(mDelegate.mSelectedCalendarviewCalendar.getYear(), mDelegate.mSelectedCalendarviewCalendar.getMonth());
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        if (mParentLayout != null) {
            mParentLayout.updateContentViewTranslateY();
        }
    }

    /**
     * 更新周起始
     */
    void updateWeekStart() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateWeekStart();
            view.requestLayout();
        }

        updateMonthViewHeight(mDelegate.mSelectedCalendarviewCalendar.getYear(), mDelegate.mSelectedCalendarviewCalendar.getMonth());
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
        if (mParentLayout != null) {
            int i = CalendarUtil.getWeekFromDayInMonth(mDelegate.mSelectedCalendarviewCalendar, mDelegate.getWeekStart());
            mParentLayout.updateSelectWeek(i);
        }
        updateSelected();
    }

    /**
     * 更新高度
     */
    final void updateItemHeight() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.updateItemHeight();
            view.requestLayout();
        }

        int year = mDelegate.mIndexCalendarviewCalendar.getYear();
        int month = mDelegate.mIndexCalendarviewCalendar.getMonth();
        mCurrentViewHeight = CalendarUtil.getMonthViewHeight(year, month,
                mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
        if (month == 1) {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year - 1, 12, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            mNextViewHeight = CalendarUtil.getMonthViewHeight(year, 2, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
        } else {
            mPreViewHeight = CalendarUtil.getMonthViewHeight(year, month - 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            if (month == 12) {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year + 1, 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            } else {
                mNextViewHeight = CalendarUtil.getMonthViewHeight(year, month + 1, mDelegate.getCalendarItemHeight(), mDelegate.getWeekStart());
            }
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = mCurrentViewHeight;
        setLayoutParams(params);
    }

    /**
     * 清除选择范围
     */
    final void clearSelectRange() {
        for (int i = 0; i < getChildCount(); i++) {
            BaseMonthView view = (BaseMonthView) getChildAt(i);
            view.invalidate();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDelegate.isMonthViewScrollable() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (Math.abs(getCurrentItem() - item) > 1) {
            super.setCurrentItem(item, false);
        } else {
            super.setCurrentItem(item, smoothScroll);
        }
    }


    /**
     * 日历卡月份Adapter
     */
    private final class MonthViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mMonthCount;
        }

        @Override
        public int getItemPosition(Object object) {
            return isUpdateMonthView ? POSITION_NONE : super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int year = (position + mDelegate.getMinYearMonth() - 1) / 12 + mDelegate.getMinYear();
            int month = (position + mDelegate.getMinYearMonth() - 1) % 12 + 1;
            BaseMonthView view;
            try {
                Constructor constructor = mDelegate.getMonthViewClass().getConstructor(Context.class);
                view = (BaseMonthView) constructor.newInstance(getContext());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            view.mMonthViewPager = MonthViewPager.this;
            view.mParentLayout = mParentLayout;
            view.setup(mDelegate);
            view.setTag(position);
            view.initMonthWithDate(year, month);
            view.setSelectedCalendar(mDelegate.mSelectedCalendarviewCalendar);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            BaseView view = (BaseView) object;
            if (view == null) {
                return;
            }
            view.onDestroy();
            container.removeView(view);
        }
    }


}
