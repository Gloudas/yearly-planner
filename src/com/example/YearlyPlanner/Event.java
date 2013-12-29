package com.example.YearlyPlanner;

import android.content.ContentValues;
import android.database.Cursor;

public class Event {

    private int mId = -1;
    private String mName;
    private int mMonth;
    private int mDay;
    private String mNotes;

    public Event() {
    }

    public Event(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EventsEntry._ID));
        mName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EventsEntry.COLUMN_NAME_NAME));
        mMonth = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EventsEntry.COLUMN_NAME_MONTH));
        mDay = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EventsEntry.COLUMN_NAME_DAY));
        mNotes = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EventsEntry.COLUMN_NAME_NOTES));
    }

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public int getMonth() {
        return mMonth;
    }
    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDay() {
        return mDay;
    }
    public void setDay(int day) {
        mDay = day;
    }

    public String getNotes() {
        return mNotes;
    }
    public void setNotes(String notes) {
        mNotes = notes;
    }

    /**
     * Creates a ContentValues object to insert into the database
     * @return ContentValues that represents all the fields of this Race
     */
    public ContentValues getValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EventsEntry.COLUMN_NAME_NAME, getName());
        values.put(DatabaseHelper.EventsEntry.COLUMN_NAME_MONTH, getMonth());
        values.put(DatabaseHelper.EventsEntry.COLUMN_NAME_DAY, getDay());
        values.put(DatabaseHelper.EventsEntry.COLUMN_NAME_NOTES, getNotes());
        return values;
    }

}
