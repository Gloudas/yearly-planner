package wreden.douglas.YearlyPlanner;

import java.util.ArrayList;

import android.content.Context;

public class DatabaseManager {

    private static DatabaseHelper sDbHelper;

    /**
     * Construct a database manager to access the database
     * @param context
     */
    public DatabaseManager(Context context) {
        if (sDbHelper == null) {
            sDbHelper = new DatabaseHelper(context);
        }
    }

    public Event getEvent(int eventId) {
        return sDbHelper.getEvent(eventId);
    }

    public ArrayList<Event> getEventsByMonth(int month) {
        return sDbHelper.getEventsByMonth(month);
    }

    public void insertNewEvent(Event newEvent) {
        sDbHelper.insertNewEvent(newEvent);
    }

    public void updateEvent(Event updatedEvent) {
        sDbHelper.updateEvent(updatedEvent);
    }
}
