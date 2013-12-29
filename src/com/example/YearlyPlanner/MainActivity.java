package com.example.YearlyPlanner;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
    public static final int NUM_MONTHS = 12;
    public static final String EVENT_ID_KEY = "event_id";

    RelativeLayout mRelativeLayoutNewEvent;
    ViewPager mViewPager;

    MonthsAdapter mAdapter;
    public static int mCurrentMonth = 0;
    public static int mCurrentDay = 0;
    private int mCurrentPosition; // The current position in the pager. We need to keep track of this manually, because ViewPager.getCurrentItem() does not work properly.

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        mRelativeLayoutNewEvent = (RelativeLayout) findViewById(R.id.relativeLayout_new_event);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_months);
        mAdapter = new MonthsAdapter(this);
	}

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {

        Time now = new Time();
        now.setToNow();
        mCurrentMonth = now.month;
        mCurrentDay = now.monthDay;
        mCurrentPosition = mCurrentMonth;

        initUi();
    }

    public void initUi() {
        mAdapter.notifyDataSetChanged();
        if (mViewPager.getAdapter() == null) {
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentPosition = position;
                    updatePosition();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }

            });
        }
        updatePosition();
    }

    // Called when user swipes or presses either of the left/right buttons
    public void updatePosition() {
        mViewPager.setCurrentItem(mCurrentPosition, true);
    }

    public void leftArrowClick() {
        mCurrentPosition--;
        if (mCurrentPosition < 0) {
            mCurrentPosition = 0;
        }
        updatePosition();
    }

    public void rightArrowClick() {
        mCurrentPosition++;
        if (mCurrentPosition >= mAdapter.getCount()) {
            mCurrentPosition = mAdapter.getCount() - 1;
        }
        updatePosition();
    }
}
