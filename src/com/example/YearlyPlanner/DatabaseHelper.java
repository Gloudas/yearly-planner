package com.example.YearlyPlanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Defines database schemas and provides implementation of all database methods
 *
 * Written with help from these sources:
 * http://developer.android.com/training/basics/data-storage/databases.html
 * http://www.codeproject.com/Articles/119293/Using-SQLite-Database-with-Android
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName = "yearlyPlannerDb";
    static final int dbVersion = 1;     // update this when changes are made to the database schema

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventsEntry.SQL_CREATE_EVENTS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventsEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        this.getWritableDatabase();
    }

    public Event getEvent(int eventId) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(EventsEntry.TABLE_NAME,
                null,
                EventsEntry._ID+"=" + eventId,
                new String[]{},
                null,
                null,
                null);
        if (cursor.getCount() <= 0) {
            throw new RuntimeException("Couldn't find the event in the database");
        }
        cursor.moveToFirst();
        Event event = new Event(cursor);
        cursor.close();
        return event;
    }

    public ArrayList<Event> getEventsByMonth(int month) {
        ArrayList<Event> events = new ArrayList<Event>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(EventsEntry.TABLE_NAME,
                null,
                EventsEntry.COLUMN_NAME_MONTH + "=" + month,
                new String[]{},
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0, n = cursor.getCount(); i < n; i++) {
                Event race = new Event(cursor);
                events.add(race);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return events;
    }

    public void insertNewEvent(Event newEvent) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = newEvent.getValues();

        // Insert the new row, returning the primary key value of the new row
        long newEventId;
        newEventId = db.insert(
                EventsEntry.TABLE_NAME,
                null,
                values);
        newEvent.setId((int) newEventId);
    }

    public void updateEvent(Event updatedEvent) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = updatedEvent.getValues();
        // Update the user score with the provided score update
        db.update(EventsEntry.TABLE_NAME, values,
                EventsEntry._ID +"=" + updatedEvent.getId(),
                new String []{});

    }

    /* Defines the contents of the Races table */
    public static abstract class EventsEntry implements BaseColumns {

        private static final String SQL_CREATE_EVENTS_TABLE =
                "CREATE TABLE " + EventsEntry.TABLE_NAME + " (" +
                        EventsEntry._ID + " INTEGER PRIMARY KEY," +
                        EventsEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        EventsEntry.COLUMN_NAME_MONTH + INTEGER_TYPE + COMMA_SEP +
                        EventsEntry.COLUMN_NAME_DAY + INTEGER_TYPE + COMMA_SEP +
                        EventsEntry.COLUMN_NAME_NOTES + TEXT_TYPE +
                        " )";

        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_NOTES = "notes";
    }
}
