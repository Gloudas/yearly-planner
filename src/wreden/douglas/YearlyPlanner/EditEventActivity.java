package wreden.douglas.YearlyPlanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.YearlyPlanner.R;

public class EditEventActivity extends Activity {

    Event mEvent;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
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
        }

        initUi();
    }

    private void initUi() {

        if (mEvent != null) {


            // TEST CHANGES!!!!

            TODO
            // populate all the text fields n' shit with mEvent

            // Use the link on thsi video for date wheel!
            // http://www.youtube.com/watch?v=cUGyvYjGkKA

        }
    }

    TODO
    // handle the click events for the SAVE and CANCEL buttons

}
