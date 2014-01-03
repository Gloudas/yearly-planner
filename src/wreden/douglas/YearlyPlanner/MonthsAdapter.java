package wreden.douglas.YearlyPlanner;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MonthsAdapter extends PagerAdapter {

    Activity mActivity;
    DatabaseManager mDbManager;

    public MonthsAdapter(Activity activity) {
        mActivity = activity;
        mDbManager = new DatabaseManager(mActivity);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ArrayList<Event> events = mDbManager.getEventsByMonth(position);
        MonthListView view = new MonthListView(mActivity);
        view.bind(position, events);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }

    @Override
    public int getItemPosition(Object object) {

        /*
        if (object instanceof MonthListView) {
            return ((MonthListView) object).getMonth();
        }
        throw new RuntimeException("YO DOUG THERE IS A PROBLEM IN MONTHSADAPTER");
        */
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return MainActivity.NUM_MONTHS;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
