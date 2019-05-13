package com.haibin.calendarview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 新版月视图算法测试
 * Created by huanghaibin on 2018/2/8.
 */
@SuppressWarnings("all")
public class CalendarviewCalendarUtilTest {
    @Test
    public void getMonthViewHeight() throws Exception {
        //周一起始
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 4, 1, 1));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 5, 1, 1));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 6, 1, 1));
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 9, 1, 1));

        //周一起始
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 4, 1, 2));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 5, 1, 2));
        assertEquals(5, CalendarUtil.getMonthViewHeight(2018, 6, 1, 2));
        assertEquals(6, CalendarUtil.getMonthViewHeight(2018, 7, 1, 2));
    }

    /**
     * 根据星期数和最小日期推算出该星期的第一天
     */
    @Test
    public void getFirstCalendarStartWithMinCalendar() throws Exception {
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(8);
        calendarviewCalendar.setDay(13);

        CalendarviewCalendar firstCalendarviewCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                2);
        assertEquals(calendarviewCalendar, firstCalendarviewCalendar);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(8);
        calendarviewCalendar.setDay(20);

        firstCalendarviewCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                2,
                2);
        assertEquals(calendarviewCalendar, firstCalendarviewCalendar);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(8);
        calendarviewCalendar.setDay(12);

        firstCalendarviewCalendar = CalendarUtil.getFirstCalendarStartWithMinCalendar(2018, 8, 13,
                1,
                1);
        assertEquals(calendarviewCalendar, firstCalendarviewCalendar);
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     *
     * @return 周数用于WeekViewPager itemCount
     */
    @Test
    public void getWeekCountBetweenBothCalendar() throws Exception {
        int count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 5,
                2018, 8, 12, 1);
        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 1,
                2018, 8, 12, 2);

        assertEquals(2, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 7, 29,
                2018, 8, 12, 2);

        assertEquals(3, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2017, 12, 1,
                2017, 12, 3, 2);

        assertEquals(1, count);

        count = CalendarUtil.getWeekCountBetweenBothCalendar(2018, 8, 13,
                2018, 12, 12, 2);

        assertEquals(18, count);
    }

    /**
     * 是否在日期范围內
     */
    @Test
    public void isCalendarInRange() throws Exception {
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2017);
        calendarviewCalendar.setMonth(12);
        calendarviewCalendar.setDay(30);

        boolean isInRange = CalendarUtil.isCalendarInRange(calendarviewCalendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(true, isInRange);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2017);
        calendarviewCalendar.setMonth(3);
        calendarviewCalendar.setDay(11);

        isInRange = CalendarUtil.isCalendarInRange(calendarviewCalendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(false, isInRange);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(12);
        calendarviewCalendar.setDay(31);

        isInRange = CalendarUtil.isCalendarInRange(calendarviewCalendar,
                2017, 3, 12,
                2018, 12, 31);

        assertEquals(true, isInRange);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(12);
        calendarviewCalendar.setDay(31);

        isInRange = CalendarUtil.isCalendarInRange(calendarviewCalendar,
                2019, 1, 31,
                2020, 11, 30);

        assertEquals(false, isInRange);

    }

    /**
     * 根据日期获取距离最小年份是第几周
     */
    @Test
    public void getWeekFromCalendarStartWithMinCalendar() throws Exception {
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(8);
        calendarviewCalendar.setDay(1);

        int week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendarviewCalendar,
                2018, 8, 1, 1);

        assertEquals(1, week);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(9);
        calendarviewCalendar.setDay(1);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendarviewCalendar,
                2018, 8, 13, 2);

        assertEquals(3, week);


        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(8);
        calendarviewCalendar.setDay(7);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendarviewCalendar,
                2018, 7, 29, 1);

        assertEquals(2, week);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(9);
        calendarviewCalendar.setDay(12);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendarviewCalendar,
                2018, 7, 29, 1);

        assertEquals(7, week);

        calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2017);
        calendarviewCalendar.setMonth(11);
        calendarviewCalendar.setDay(12);

        week = CalendarUtil.getWeekFromCalendarStartWithMinCalendar(calendarviewCalendar,
                2017, 10, 1, 2);

        assertEquals(7, week);
    }

    @Test
    public void getWeekCountDiff() throws Exception {

    }


    @Test
    public void getWeekViewStartDiff() throws Exception {

    }


    @Test
    public void getWeekViewEndDiff() throws Exception {

    }

    @Test
    public void getWeekFromDayInMonth() throws Exception {

        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(4);
        calendarviewCalendar.setDay(24);

        CalendarUtil.initCalendarForMonthView(2018, 4, calendarviewCalendar, 1);

    }

    @Test
    public void getMonthEndDiff() throws Exception {
        assertEquals(5, CalendarUtil.getMonthEndDiff(2018, 4, 1));
        assertEquals(2, CalendarUtil.getMonthEndDiff(2018, 5, 1));
        assertEquals(0, CalendarUtil.getMonthEndDiff(2018, 6, 1));
        assertEquals(4, CalendarUtil.getMonthEndDiff(2018, 7, 1));

        assertEquals(4, CalendarUtil.getMonthEndDiff(2018, 4, 7));
        assertEquals(1, CalendarUtil.getMonthEndDiff(2018, 5, 7));
        assertEquals(6, CalendarUtil.getMonthEndDiff(2018, 6, 7));
        assertEquals(3, CalendarUtil.getMonthEndDiff(2018, 7, 7));

        assertEquals(6, CalendarUtil.getMonthEndDiff(2018, 4, 2));
        assertEquals(3, CalendarUtil.getMonthEndDiff(2018, 5, 2));
        assertEquals(1, CalendarUtil.getMonthEndDiff(2018, 6, 2));
        assertEquals(5, CalendarUtil.getMonthEndDiff(2018, 7, 2));
    }

    @Test
    public void getMonthViewStartDiff() throws Exception {

        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(2018);
        calendarviewCalendar.setMonth(4);
        calendarviewCalendar.setDay(1);

        assertEquals(1, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 7));

        assertEquals(0, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 1));

        assertEquals(6, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 2));

        calendarviewCalendar.setMonth(5);


        assertEquals(3, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 7));

        assertEquals(2, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 1));

        assertEquals(1, CalendarUtil.getMonthViewStartDiff(calendarviewCalendar, 2));
    }

    /**
     * 根据日期获取两个年份中第几周,用来设置 WeekView currentItem
     *
     * @throws Exception Exception
     */
    @Test
    public void differ() throws Exception {
        CalendarviewCalendar calendarviewCalendar1 = new CalendarviewCalendar();
        calendarviewCalendar1.setYear(2018);
        calendarviewCalendar1.setMonth(4);
        calendarviewCalendar1.setDay(1);

        CalendarviewCalendar calendarviewCalendar2 = new CalendarviewCalendar();
        calendarviewCalendar2.setYear(2018);
        calendarviewCalendar2.setMonth(4);
        calendarviewCalendar2.setDay(3);

        assertEquals(-2,CalendarUtil.differ(calendarviewCalendar1, calendarviewCalendar2));

        calendarviewCalendar1.setYear(2018);
        calendarviewCalendar1.setMonth(9);
        calendarviewCalendar1.setDay(30);

        calendarviewCalendar2.setYear(2018);
        calendarviewCalendar2.setMonth(9);
        calendarviewCalendar2.setDay(1);

        assertEquals(29,CalendarUtil.differ(calendarviewCalendar1, calendarviewCalendar2));

        calendarviewCalendar1.setYear(2018);
        calendarviewCalendar1.setMonth(9);
        calendarviewCalendar1.setDay(12);

        calendarviewCalendar2.setYear(2018);
        calendarviewCalendar2.setMonth(9);
        calendarviewCalendar2.setDay(5);

        assertEquals(7,CalendarUtil.differ(calendarviewCalendar1, calendarviewCalendar2));
    }
}