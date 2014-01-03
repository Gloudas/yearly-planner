package wreden.douglas.YearlyPlanner;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MonthListView extends LinearLayout {

    TextView mTextViewMonth;
    ListView mListViewEvents;

    private Context mContext;
    private EventsListAdapter mAdapter;
    private int mMonth = -1;
    private ArrayList<Event> mEvents;


    public MonthListView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_month_list, this, true);
    }

    // Called by the adapter when this month view is being instantiated
    public void bind(int month, ArrayList<Event> events) {

        mMonth = month;
        mEvents = events;

        mListViewEvents = (ListView) findViewById(R.id.listView_events);

        mTextViewMonth = (TextView) findViewById(R.id.textView_month);
        mTextViewMonth.setText(MainActivity.getMonthString(month));
        //mTextViewMonth.setPaintFlags(mTextViewMonth.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        mAdapter = new EventsListAdapter(mContext, mEvents);
        mListViewEvents.setAdapter(mAdapter);
        mListViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventClicked(mAdapter.getItem(position));
            }
        });
    }

    private void eventClicked(Event event) {
        Intent intent = new Intent(mContext, EditEventActivity.class);
        intent.putExtra(MainActivity.EVENT_ID_KEY, event.getId());
        mContext.startActivity(intent);
    }

    public int getMonth() {
        return mMonth;
    }

    class EventsListAdapter extends ArrayAdapter<Event> {
        private Context mAdapterContext;
        private ArrayList<Event> mValues;

        public EventsListAdapter(Context context, ArrayList<Event> values) {
            super(context, R.layout.view_event, values);
            this.mAdapterContext = context;
            this.mValues = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View eventView = inflater.inflate(R.layout.view_event, parent, false);

            Event event = mValues.get(position);

            TextView nameText = (TextView) eventView.findViewById(R.id.textView_name);
            nameText.setText(event.getName());
            TextView dayText = (TextView) eventView.findViewById(R.id.textView_day);
            dayText.setText(MainActivity.getDateSuffix(event.getDay()+1));

            TextView notesText = (TextView) eventView.findViewById(R.id.textView_notes);
            notesText.setText(event.getNotes());
            if (event.getNotes().isEmpty()) {
                // This event does not have attached notes
                notesText.setVisibility(View.GONE);
            }

            // For any events that have already passed, color them blue
            if (event.getMonth() < MainActivity.mCurrentMonth) {
                eventView.setBackground(getResources().getDrawable(R.drawable.shaded_event_background));
            } else if (event.getMonth() == MainActivity.mCurrentMonth && event.getDay() < MainActivity.mCurrentDay) {
                eventView.setBackground(getResources().getDrawable(R.drawable.shaded_event_background));
            }

            // set to underline
            mTextViewMonth.setPaintFlags(mTextViewMonth.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            return eventView;
        }

        @Override
        public int getCount() {
            return mValues.size();
        }

        @Override
        public Event getItem(int position) {
            return mValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
