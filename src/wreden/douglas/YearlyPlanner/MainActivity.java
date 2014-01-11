package wreden.douglas.YearlyPlanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity
{
    public static final int NUM_MONTHS = 12;
    public static final String EVENT_ID_KEY = "event_id";
    public static final String DEFAULT_MONTH_KEY = "default_month";
    public static final String[] MONTHS_LIST = new String[] {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

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

        mRelativeLayoutNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEventClicked();
            }
        });

        Time now = new Time();
        now.setToNow();
        mCurrentMonth = now.month;
        mCurrentDay = now.monthDay - 1;     // Convert now.monthDay to 0 index value for comparison against database values
        mCurrentPosition = mCurrentMonth;
	}

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
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

    private void newEventClicked() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra(MainActivity.DEFAULT_MONTH_KEY, mCurrentPosition);
        startActivity(intent);
    }

    // Called when user swipes or presses either of the left/right buttons
    public void updatePosition() {
        mViewPager.setCurrentItem(mCurrentPosition, true);
    }

    public void leftArrowClick() {
        mCurrentPosition--;
        if (mCurrentPosition < 0) {
            mCurrentPosition = mAdapter.getCount() - 1;
        }
        updatePosition();
    }

    public void rightArrowClick() {
        mCurrentPosition++;
        mCurrentPosition %= mAdapter.getCount();
        updatePosition();
    }

    public static String getMonthString(int month) {
        return MONTHS_LIST[month];
    }

    public static String getDateSuffix(int day) {
        // Handle exceptions cases first
        switch (day) {
            case 11:
                return "11th";
            case 12:
                return "12th";
            case 13:
                return "13th";
        }
        int lastDigit = day % 10;
        switch (lastDigit) {
            case 1:
                return ""+day+"st";
            case 2:
                return ""+day+"nd";
            case 3:
                return ""+day+"rd";
            default:
                return ""+day+"th";
        }

    }
}
