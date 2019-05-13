package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.callbacktest.R;
import com.haibin.calendarview.CalendarviewCalendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityE extends AppCompatActivity implements CalendarView.OnCalendarSelectListener {

    @BindView(R.id.calendar)
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e);
        ButterKnife.bind(this);
        int year = calendar.getCurYear();
        int month = calendar.getCurMonth();

        Map<String, CalendarviewCalendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, "1").toString(),
                getSchemeCalendar(year, month, 3, "1"));
        map.put(getSchemeCalendar(year, month, 6, "1").toString(),
                getSchemeCalendar(year, month, 6, "1"));
        map.put(getSchemeCalendar(year, month, 9, "1").toString(),
                getSchemeCalendar(year, month, 9, "1"));
        map.put(getSchemeCalendar(year, month, 13,  "2").toString(),
                getSchemeCalendar(year, month, 13,  "2"));
        map.put(getSchemeCalendar(year, month, 14, "2").toString(),
                getSchemeCalendar(year, month, 14,  "2"));
        map.put(getSchemeCalendar(year, month, 15, "3").toString(),
                getSchemeCalendar(year, month, 15, "3"));
        map.put(getSchemeCalendar(year, month, 18, "3").toString(),
                getSchemeCalendar(year, month, 18,  "3"));
        map.put(getSchemeCalendar(year, month, 25, "2").toString(),
                getSchemeCalendar(year, month, 25, "2"));
        map.put(getSchemeCalendar(year, month, 27, "1").toString(),
                getSchemeCalendar(year, month, 27,  "1"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendar.setSchemeDate(map);
        calendar.setOnCalendarSelectListener(this);

    }
    private CalendarviewCalendar getSchemeCalendar(int year, int month, int day, String text) {
        CalendarviewCalendar calendarviewCalendar = new CalendarviewCalendar();
        calendarviewCalendar.setYear(year);
        calendarviewCalendar.setMonth(month);
        calendarviewCalendar.setDay(day);
        calendarviewCalendar.setScheme(text);
        return calendarviewCalendar;
    }


    @Override
    public void onCalendarOutOfRange(CalendarviewCalendar calendarviewCalendar) {

    }

    @Override
    public void onCalendarSelect(CalendarviewCalendar calendarviewCalendar, boolean isClick) {
//        mTextLunar.setVisibility(View.VISIBLE);
//        mTextYear.setVisibility(View.VISIBLE);
//        mTextMonthDay.setText(calendar1.getMonth() + "月" + calendar1.getDay() + "日");
//        mTextYear.setText(String.valueOf(calendar1.getYear()));
//        mTextLunar.setText(calendar1.getLunar());
//        mYear = calendar1.getYear();
    }
}
