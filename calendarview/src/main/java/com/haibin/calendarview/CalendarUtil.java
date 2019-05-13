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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一些日期辅助计算工具
 */
final class CalendarUtil {

    private static final long ONE_DAY = 1000 * 3600 * 24;

    @SuppressLint("SimpleDateFormat")
    static int getDate(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return Integer.parseInt(format.format(date));
    }

    /**
     * 判断一个日期是否是周末，即周六日
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @return 判断一个日期是否是周末，即周六日
     */
    static boolean isWeekend(CalendarviewCalendar calendarviewCalendar) {
        int week = getWeekFormCalendar(calendarviewCalendar);
        return week == 0 || week == 6;
    }

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    static int getMonthDaysCount(int year, int month) {
        int count = 0;
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31;
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30;
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29;
            } else {
                count = 28;
            }
        }
        return count;
    }


    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
    static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }


    /**
     * 获取月视图的确切高度
     * Test pass
     *
     * @param year       年
     * @param month      月
     * @param itemHeight 每项的高度
     * @return 不需要多余行的高度
     */
    static int getMonthViewHeight(int year, int month, int itemHeight, int weekStartWith) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int preDiff = getMonthViewStartDiff(year, month, weekStartWith);
        int monthDaysCount = getMonthDaysCount(year, month);
        int nextDiff = getMonthEndDiff(year, month, monthDaysCount, weekStartWith);
        return (preDiff + monthDaysCount + nextDiff) / 7 * itemHeight;
    }


    /**
     * 获取某天在该月的第几周,换言之就是获取这一天在该月视图的第几行,第几周，根据周起始动态获取
     * Test pass，单元测试通过
     *
     * @param calendarviewCalendar  calendarviewCalendar
     * @param weekStart 其实星期是哪一天？
     * @return 获取某天在该月的第几周 the week line in MonthView
     */
    static int getWeekFromDayInMonth(CalendarviewCalendar calendarviewCalendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, 1);
        //该月第一天为星期几,星期天 == 0
        int diff = getMonthViewStartDiff(calendarviewCalendar, weekStart);
        return (calendarviewCalendar.getDay() + diff - 1) / 7 + 1;
    }

    /**
     * 获取上一个日子
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @return 获取上一个日子
     */
    static CalendarviewCalendar getPreCalendar(CalendarviewCalendar calendarviewCalendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills - ONE_DAY);

        CalendarviewCalendar preCalendarviewCalendar = new CalendarviewCalendar();
        preCalendarviewCalendar.setYear(date.get(java.util.Calendar.YEAR));
        preCalendarviewCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        preCalendarviewCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return preCalendarviewCalendar;
    }

    static CalendarviewCalendar getNextCalendar(CalendarviewCalendar calendarviewCalendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills + ONE_DAY);

        CalendarviewCalendar nextCalendarviewCalendar = new CalendarviewCalendar();
        nextCalendarviewCalendar.setYear(date.get(java.util.Calendar.YEAR));
        nextCalendarviewCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        nextCalendarviewCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return nextCalendarviewCalendar;
    }

    /**
     * DAY_OF_WEEK return_back2  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param calendarviewCalendar  calendarviewCalendar
     * @param weekStart weekStart 星期的起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(CalendarviewCalendar calendarviewCalendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, 1);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return_back2  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月份的结束偏移量，用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param calendarviewCalendar  calendarviewCalendar
     * @param weekStart weekStart 星期的起始
     * @return 获取日期所在月份的结束偏移量 the end diff in Month not MonthView
     */
    @SuppressWarnings("unused")
    static int getMonthEndDiff(CalendarviewCalendar calendarviewCalendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, getMonthDaysCount(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth()));
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return 7 - week;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 6 : 7 - week - 1;
    }

    /**
     * DAY_OF_WEEK return_back2  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(int year, int month, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return_back2  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    static int getMonthEndDiff(int year, int month, int weekStart) {
        return getMonthEndDiff(year, month, getMonthDaysCount(year, month), weekStart);
    }


    /**
     * DAY_OF_WEEK return_back2  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    private static int getMonthEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return 7 - week;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }

    /**
     * 获取某个日期是星期几
     * 测试通过
     *
     * @param calendarviewCalendar 某个日期
     * @return 返回某个日期是星期几
     */
    static int getWeekFormCalendar(CalendarviewCalendar calendarviewCalendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());
        return date.get(java.util.Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     * 获取周视图的切换默认选项位置 WeekView index
     * 测试通过 test pass
     *
     * @param calendarviewCalendar  calendarviewCalendar
     * @param weekStart weekStart
     * @return 获取周视图的切换默认选项位置
     */
    static int getWeekViewIndexFromCalendar(CalendarviewCalendar calendarviewCalendar, int weekStart) {
        return getWeekViewStartDiff(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth(), calendarviewCalendar.getDay(), weekStart);
    }

    /**
     * 是否在日期范围內
     * 测试通过 test pass
     *
     * @param calendarviewCalendar     calendarviewCalendar
     * @param minYear      minYear
     * @param minYearDay   最小年份天
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   最大年份天
     * @return 是否在日期范围內
     */
    static boolean isCalendarInRange(CalendarviewCalendar calendarviewCalendar,
                                     int minYear, int minYearMonth, int minYearDay,
                                     int maxYear, int maxYearMonth, int maxYearDay) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(minYear, minYearMonth - 1, minYearDay);
        long minTime = c.getTimeInMillis();
        c.set(maxYear, maxYearMonth - 1, maxYearDay);
        long maxTime = c.getTimeInMillis();
        c.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime >= minTime && curTime <= maxTime;
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     * 测试通过 test pass
     *
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param maxYear      maxYear 最大年份
     * @param maxYearMonth maxYear 最大年份月份
     * @param maxYearDay   最大年份天
     * @param weekStart    周起始
     * @return 周数用于WeekViewPager itemCount
     */
    static int getWeekCountBetweenBothCalendar(int minYear, int minYearMonth, int minYearDay,
                                               int maxYear, int maxYearMonth, int maxYearDay,
                                               int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);
        long minTimeMills = date.getTimeInMillis();//给定时间戳
        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);

        date.set(maxYear, maxYearMonth - 1, maxYearDay);

        long maxTimeMills = date.getTimeInMillis();//给定时间戳

        int nextDiff = getWeekViewEndDiff(maxYear, maxYearMonth, maxYearDay, weekStart);

        int count = preDiff + nextDiff;

        int c = (int) ((maxTimeMills - minTimeMills) / ONE_DAY) + 1;
        count += c;
        return count / 7;
    }


    /**
     * 根据日期获取距离最小日期在第几周
     * 用来设置 WeekView currentItem
     * 测试通过 test pass
     *
     * @param calendarviewCalendar     calendarviewCalendar
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param weekStart    周起始
     * @return 返回两个年份中第几周 the WeekView currentItem
     */
    static int getWeekFromCalendarStartWithMinCalendar(CalendarviewCalendar calendarviewCalendar,
                                                       int minYear, int minYearMonth, int minYearDay,
                                                       int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);//起始日期
        long firstTimeMill = date.getTimeInMillis();//获得范围起始时间戳

        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);//范围起始的周偏移量

        int weekStartDiff = getWeekViewStartDiff(calendarviewCalendar.getYear(),
                calendarviewCalendar.getMonth(),
                calendarviewCalendar.getDay(),
                weekStart);//获取点击的日子在周视图的起始，为了兼容全球时区，最大日差为一天，如果周起始偏差weekStartDiff=0，则日期加1

        date.set(calendarviewCalendar.getYear(),
                calendarviewCalendar.getMonth() - 1,
                weekStartDiff == 0 ? calendarviewCalendar.getDay() + 1 : calendarviewCalendar.getDay());

        long curTimeMills = date.getTimeInMillis();//给定时间戳

        int c = (int) ((curTimeMills - firstTimeMill) / ONE_DAY);

        int count = preDiff + c;

        return count / 7 + 1;
    }

    /**
     * 根据星期数和最小日期推算出该星期的第一天
     * //测试通过 Test pass
     *
     * @param minYear      最小年份如2017
     * @param minYearMonth maxYear 最小年份月份，like : 2017-07
     * @param minYearDay   最小年份天
     * @param week         从最小年份minYear月minYearMonth 日1 开始的第几周 week > 0
     * @return 该星期的第一天日期
     */
    static CalendarviewCalendar getFirstCalendarStartWithMinCalendar(int minYear, int minYearMonth, int minYearDay, int week, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(minYear, minYearMonth - 1, minYearDay);//

        long firstTimeMills = date.getTimeInMillis();//获得起始时间戳


        long weekTimeMills = (week - 1) * 7 * ONE_DAY;

        long timeCountMills = weekTimeMills + firstTimeMills;

        date.setTimeInMillis(timeCountMills);

        int startDiff = getWeekViewStartDiff(date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH) + 1,
                date.get(java.util.Calendar.DAY_OF_MONTH), weekStart);

        timeCountMills -= startDiff * ONE_DAY;
        date.setTimeInMillis(timeCountMills);

        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(date.get(java.util.Calendar.YEAR));
        calendarviewCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        calendarviewCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return calendarviewCalendar;
    }


    /**
     * 是否在日期范围内
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @param delegate delegate
     * @return 是否在日期范围内
     */
    static boolean isCalendarInRange(CalendarviewCalendar calendarviewCalendar, CalendarViewDelegate delegate) {
        return isCalendarInRange(calendarviewCalendar,
                delegate.getMinYear(), delegate.getMinYearMonth(), delegate.getMinYearDay(),
                delegate.getMaxYear(), delegate.getMaxYearMonth(), delegate.getMaxYearDay());
    }

    /**
     * 是否在日期范围內
     *
     * @param year         year
     * @param month        month
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @return 是否在日期范围內
     */
    static boolean isMonthInRange(int year, int month, int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        return !(year < minYear || year > maxYear) &&
                !(year == minYear && month < minYearMonth) &&
                !(year == maxYear && month > maxYearMonth);
    }

    /**
     * 运算 calendarviewCalendar1 - calendarviewCalendar2
     * test Pass
     * @param calendarviewCalendar1 calendarviewCalendar1
     * @param calendarviewCalendar2 calendarviewCalendar2
     * @return calendarviewCalendar1 - calendarviewCalendar2
     */
    static int differ(CalendarviewCalendar calendarviewCalendar1, CalendarviewCalendar calendarviewCalendar2) {
        if (calendarviewCalendar1 == null) {
            return Integer.MIN_VALUE;
        }
        if (calendarviewCalendar2 == null) {
            return Integer.MAX_VALUE;
        }
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendarviewCalendar1.getYear(), calendarviewCalendar1.getMonth() - 1, calendarviewCalendar1.getDay());//

        long startTimeMills = date.getTimeInMillis();//获得起始时间戳

        date.set(calendarviewCalendar2.getYear(), calendarviewCalendar2.getMonth() - 1, calendarviewCalendar2.getDay());//

        long endTimeMills = date.getTimeInMillis();//获得结束时间戳

        return (int) ((startTimeMills - endTimeMills) / ONE_DAY);
    }

    /**
     * 比较日期大小
     *
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param minYearDay   minYearDay
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   maxYearDay
     * @return -1 0 1
     */
    static int compareTo(int minYear, int minYearMonth, int minYearDay,
                         int maxYear, int maxYearMonth, int maxYearDay) {
        CalendarviewCalendar first = new CalendarviewCalendar();
        first.setYear(minYear);
        first.setMonth(minYearMonth);
        first.setDay(minYearDay);

        CalendarviewCalendar second = new CalendarviewCalendar();
        second.setYear(maxYear);
        second.setMonth(maxYearMonth);
        second.setDay(maxYearDay);
        return first.compareTo(second);
    }

    /**
     * 为月视图初始化日历
     *
     * @param year        year
     * @param month       month
     * @param currentDate currentDate
     * @param weekStar    weekStar
     * @return 为月视图初始化日历项
     */
    static List<CalendarviewCalendar> initCalendarForMonthView(int year, int month, CalendarviewCalendar currentDate, int weekStar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(year, month - 1, 1);

        int mPreDiff = getMonthViewStartDiff(year, month, weekStar);//获取月视图其实偏移量

        int monthDayCount = getMonthDaysCount(year, month);//获取月份真实天数

        int preYear, preMonth;
        int nextYear, nextMonth;

        int size = 42;

        List<CalendarviewCalendar> mItems = new ArrayList<>();

        int preMonthDaysCount;
        if (month == 1) {//如果是1月
            preYear = year - 1;
            preMonth = 12;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else if (month == 12) {//如果是12月
            preYear = year;
            preMonth = month - 1;
            nextYear = year + 1;
            nextMonth = 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = year;
            preMonth = month - 1;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        for (int i = 0; i < size; i++) {
            CalendarviewCalendar calendarviewCalendarDate = new CalendarviewCalendar();
            if (i < mPreDiff) {
                calendarviewCalendarDate.setYear(preYear);
                calendarviewCalendarDate.setMonth(preMonth);
                calendarviewCalendarDate.setDay(preMonthDaysCount - mPreDiff + i + 1);
            } else if (i >= monthDayCount + mPreDiff) {
                calendarviewCalendarDate.setYear(nextYear);
                calendarviewCalendarDate.setMonth(nextMonth);
                calendarviewCalendarDate.setDay(nextDay);
                ++nextDay;
            } else {
                calendarviewCalendarDate.setYear(year);
                calendarviewCalendarDate.setMonth(month);
                calendarviewCalendarDate.setCurrentMonth(true);
                calendarviewCalendarDate.setDay(i - mPreDiff + 1);
            }
            if (calendarviewCalendarDate.equals(currentDate)) {
                calendarviewCalendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarviewCalendarDate);
            mItems.add(calendarviewCalendarDate);
        }
        return mItems;
    }

    static List<CalendarviewCalendar> getWeekCalendars(CalendarviewCalendar calendarviewCalendar, CalendarViewDelegate mDelegate) {
        long curTime = calendarviewCalendar.getTimeInMillis();

        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendarviewCalendar.getYear(),
                calendarviewCalendar.getMonth() - 1,
                calendarviewCalendar.getDay());//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        int startDiff;
        if (mDelegate.getWeekStart() == 1) {
            startDiff = week - 1;
        } else if (mDelegate.getWeekStart() == 2) {
            startDiff = week == 1 ? 6 : week - mDelegate.getWeekStart();
        } else {
            startDiff = week == 7 ? 0 : week;
        }

        curTime -= startDiff * ONE_DAY;
        java.util.Calendar minCalendar = java.util.Calendar.getInstance();
        minCalendar.setTimeInMillis(curTime);
        CalendarviewCalendar startCalendarviewCalendar = new CalendarviewCalendar();
        startCalendarviewCalendar.setYear(minCalendar.get(java.util.Calendar.YEAR));
        startCalendarviewCalendar.setMonth(minCalendar.get(java.util.Calendar.MONTH) + 1);
        startCalendarviewCalendar.setDay(minCalendar.get(java.util.Calendar.DAY_OF_MONTH));
        return initCalendarForWeekView(startCalendarviewCalendar, mDelegate, mDelegate.getWeekStart());
    }

    /**
     * 生成周视图的7个item
     *
     * @param calendarviewCalendar  calendarviewCalendar
     * @param mDelegate mDelegate
     * @param weekStart weekStart
     * @return 生成周视图的7个item
     */
    static List<CalendarviewCalendar> initCalendarForWeekView(CalendarviewCalendar calendarviewCalendar, CalendarViewDelegate mDelegate, int weekStart) {

        java.util.Calendar date = java.util.Calendar.getInstance();//当天时间
        date.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());
        long curDateMills = date.getTimeInMillis();//生成选择的日期时间戳

        int weekEndDiff = getWeekViewEndDiff(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth(), calendarviewCalendar.getDay(), weekStart);
        List<CalendarviewCalendar> mItems = new ArrayList<>();

        date.setTimeInMillis(curDateMills);
        CalendarviewCalendar selectCalendarviewCalendar = new CalendarviewCalendar();
        selectCalendarviewCalendar.setYear(date.get(java.util.Calendar.YEAR));
        selectCalendarviewCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        selectCalendarviewCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
        if (selectCalendarviewCalendar.equals(mDelegate.getCurrentDay())) {
            selectCalendarviewCalendar.setCurrentDay(true);
        }
        LunarCalendar.setupLunarCalendar(selectCalendarviewCalendar);
        selectCalendarviewCalendar.setCurrentMonth(true);
        mItems.add(selectCalendarviewCalendar);


        for (int i = 1; i <= weekEndDiff; i++) {
            date.setTimeInMillis(curDateMills + i * ONE_DAY);
            CalendarviewCalendar calendarviewCalendarDate = new CalendarviewCalendar();
            calendarviewCalendarDate.setYear(date.get(java.util.Calendar.YEAR));
            calendarviewCalendarDate.setMonth(date.get(java.util.Calendar.MONTH) + 1);
            calendarviewCalendarDate.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
            if (calendarviewCalendarDate.equals(mDelegate.getCurrentDay())) {
                calendarviewCalendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarviewCalendarDate);
            calendarviewCalendarDate.setCurrentMonth(true);
            mItems.add(calendarviewCalendarDate);
        }
        return mItems;
    }

    /**
     * 单元测试通过
     * 从选定的日期，获取周视图起始偏移量，用来生成周视图布局
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图起始偏移量，用来生成周视图布局
     */
    private static int getWeekViewStartDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return week - 1;
        }
        if (weekStart == 2) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == 7 ? 0 : week;
    }


    /**
     * 单元测试通过
     * 从选定的日期，获取周视图结束偏移量，用来生成周视图布局
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图结束偏移量，用来生成周视图布局
     */
    private static int getWeekViewEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return 7 - week;
        }
        if (weekStart == 2) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }


    /**
     * 从月视图切换获得第一天的日期
     *
     * @param position position
     * @param delegate position
     * @return 从月视图切换获得第一天的日期
     */
    static CalendarviewCalendar getFirstCalendarFromMonthViewPager(int position, CalendarViewDelegate delegate) {
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear((position + delegate.getMinYearMonth() - 1) / 12 + delegate.getMinYear());
        calendarviewCalendar.setMonth((position + delegate.getMinYearMonth() - 1) % 12 + 1);
        calendarviewCalendar.setDay(1);
        if (!isCalendarInRange(calendarviewCalendar, delegate)) {
            if (isMinRangeEdge(calendarviewCalendar, delegate)) {
                calendarviewCalendar = delegate.getMinRangeCalendar();
            } else {
                calendarviewCalendar = delegate.getMaxRangeCalendar();
            }
        }
        calendarviewCalendar.setCurrentMonth(calendarviewCalendar.getYear() == delegate.getCurrentDay().getYear() &&
                calendarviewCalendar.getMonth() == delegate.getCurrentDay().getMonth());
        calendarviewCalendar.setCurrentDay(calendarviewCalendar.equals(delegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendarviewCalendar);
        return calendarviewCalendar;
    }


    /**
     * 根据传入的日期获取边界访问日期，要么最大，要么最小
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @param delegate delegate
     * @return 获取边界访问日期
     */
    static CalendarviewCalendar getRangeEdgeCalendar(CalendarviewCalendar calendarviewCalendar, CalendarViewDelegate delegate) {
        if (CalendarUtil.isCalendarInRange(delegate.getCurrentDay(), delegate)) {
            return delegate.createCurrentDate();
        }
        if (isCalendarInRange(calendarviewCalendar, delegate)) {
            return calendarviewCalendar;
        }
        CalendarviewCalendar minRangeCalendarviewCalendar = delegate.getMinRangeCalendar();
        if (minRangeCalendarviewCalendar.isSameMonth(calendarviewCalendar)) {
            return delegate.getMinRangeCalendar();
        }
        return delegate.getMaxRangeCalendar();
    }

    /**
     * 是否是最小访问边界了
     *
     * @param calendarviewCalendar calendarviewCalendar
     * @return 是否是最小访问边界了
     */
    private static boolean isMinRangeEdge(CalendarviewCalendar calendarviewCalendar, CalendarViewDelegate delegate) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(delegate.getMinYear(), delegate.getMinYearMonth() - 1, delegate.getMinYearDay());
        long minTime = c.getTimeInMillis();
        c.set(calendarviewCalendar.getYear(), calendarviewCalendar.getMonth() - 1, calendarviewCalendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
