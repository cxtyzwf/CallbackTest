package com.example.administrator.callbacktest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.callbacktest.R;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {


    @BindView(R.id.fill_status_bar)
    View fillStatusBar;
    @BindView(R.id.home_page_title)
    TextView homePageTitle;
    @BindView(R.id.poa_switch)
    TextView poaSwitch;
    @BindView(R.id.title_rl)
    RelativeLayout titleRl;
    @BindView(R.id.title)
    RelativeLayout title;
    //    @BindView(R.id.my_backlog_calendar2)
//    CalendarView myBacklogCalendar2;
//    @BindView(R.id.my_backlog_calendar)
//    CalendarView myBacklogCalendar;
    @BindView(R.id.current_date)
    TextView currentDate;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.if_none)
    TextView ifNone;
    @BindView(R.id.if_none_text)
    TextView ifNoneText;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private boolean isSingleChoose = true;//默认是单选,false 为多选

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        CalendarView singleCalendarView = new CalendarView(this);
        singleCalendarView = (CalendarView) LayoutInflater.from(this).inflate(R.layout.single_calendar_view, calendarLayout, false);
        calendarLayout.addView(singleCalendarView, 0);
        calendarLayout.invalidate();

        CalendarView rangeCalendarView = new CalendarView(this);
        rangeCalendarView = (CalendarView) LayoutInflater.from(this).inflate(R.layout.range_calendar_view, calendarLayout, false);


        final CalendarView finalSingleCalendarView = singleCalendarView;
        final CalendarView finalRangeCalendarView = rangeCalendarView;
//        calendarLayout.addView(singleCalendarView);


        poaSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSingleChoose) {//如果是单选，点击之后的效果,此时是多选
                    isSingleChoose = false;
                    poaSwitch.setText("多选");
//                    myBacklogCalendar2.setVisibility(View.VISIBLE);
//                    myBacklogCalendar.setVisibility(View.GONE);
//                    myBacklogCalendar2.clearSelectRange();
                    calendarLayout.removeView(finalSingleCalendarView);
                    calendarLayout.addView(finalRangeCalendarView,0);
                    calendarLayout.invalidate();
                } else {
                    isSingleChoose = true;
                    poaSwitch.setText("单选");
//                    myBacklogCalendar2.setVisibility(View.GONE);
//                    myBacklogCalendar.setVisibility(View.VISIBLE);
                    calendarLayout.removeView(finalRangeCalendarView);
                    calendarLayout.addView(finalSingleCalendarView,0);
                    calendarLayout.invalidate();
                }
            }
        });
    }

}
