package wreden.douglas.YearlyPlanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import wreden.douglas.YearlyPlanner.WheelView.OnWheelChangedListener;
import wreden.douglas.YearlyPlanner.WheelView.WheelView;
import wreden.douglas.YearlyPlanner.WheelView.adapters.ArrayWheelAdapter;
import wreden.douglas.YearlyPlanner.WheelView.adapters.NumericWheelAdapter;

import java.util.Calendar;

public class EditEventActivity extends Activity {

    WheelView mDayWheel;
    WheelView mMonthWheel;
    EditText mEditName;
    EditText mEditNotes;
    ImageView mDeleteButton;

    Event mEvent;
    int mStartingMonthValue;    // used only when creating a new event

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        mEditName = (EditText) findViewById(R.id.editText_event_name);
        mEditNotes = (EditText) findViewById(R.id.editText_event_notes);
        mDayWheel = (WheelView) findViewById(R.id.wheelView_day);
        mMonthWheel = (WheelView) findViewById(R.id.wheelView_month);
        mDeleteButton = (ImageView) findViewById(R.id.imageView_delete);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        int eventId = intent.getIntExtra(MainActivity.EVENT_ID_KEY, -1);
        mEvent = null;
        if (eventId != -1) {
            DatabaseManager dbManager = new DatabaseManager(this);
            mEvent = dbManager.getEvent(eventId);
        } else {
            // User is creating a new event, start the month wheel at the last viewed month
            mStartingMonthValue = intent.getIntExtra(MainActivity.DEFAULT_MONTH_KEY, 0);
        }
        initUi();
    }

    private void initUi() {

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays();
            }
        };

        // Set up the month chooser wheel
        mMonthWheel.setViewAdapter(new DateArrayAdapter(this, MainActivity.MONTHS_LIST, 0));
        mMonthWheel.addChangingListener(listener);

        if (mEvent != null) {
            mEditName.setText(mEvent.getName());
            mEditNotes.setText(mEvent.getNotes());
            mMonthWheel.setCurrentItem(mEvent.getMonth());
            updateDays();
            mDayWheel.setCurrentItem(mEvent.getDay());

            mDeleteButton.setVisibility(View.VISIBLE);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteClicked();
                }
            });
        } else {
            mMonthWheel.setCurrentItem(mStartingMonthValue);
            updateDays();
            mDeleteButton.setVisibility(View.GONE);
        }
    }

    public void saveButtonClicked(View v) {

        String newName = mEditName.getText().toString();
        if (newName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter a valid event name!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseManager dbManager = new DatabaseManager(this);
        if (mEvent != null) {
            mEvent.setName(newName);
            mEvent.setMonth(mMonthWheel.getCurrentItem());
            mEvent.setDay(mDayWheel.getCurrentItem());
            mEvent.setNotes(mEditNotes.getText().toString());
            dbManager.updateEvent(mEvent);
        } else {
            mEvent = new Event();
            mEvent.setName(newName);
            mEvent.setMonth(mMonthWheel.getCurrentItem());
            mEvent.setDay(mDayWheel.getCurrentItem());
            mEvent.setNotes(mEditNotes.getText().toString());
            dbManager.insertNewEvent(mEvent);
        }

        finish();
    }

    public void cancelButtonClicked(View v) {
        finish();
    }

    public void deleteClicked() {
        if (mEvent != null) {
            //Ask the user if they want to delete the event
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.delete_event)
                    .setMessage(R.string.delete_event_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteConfirmClicked();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }

    public void deleteConfirmClicked() {
        if (mEvent != null) {
            DatabaseManager dbManager = new DatabaseManager(this);
            dbManager.deleteEvent(mEvent);
            Toast.makeText(getApplicationContext(), "Event deleted!",
                    Toast.LENGTH_LONG).show();
        }
        finish();
    }


    /**
     * Updates day wheel. Sets max days according to selected month
     */
    void updateDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonthWheel.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayWheel.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, mDayWheel.getCurrentItem() + 1);
        mDayWheel.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     *
     * Taken from: http://www.youtube.com/watch?v=cUGyvYjGkKA
     *
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            //view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            //view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

}
