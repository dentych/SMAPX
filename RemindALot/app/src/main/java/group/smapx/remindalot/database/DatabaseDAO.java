package group.smapx.remindalot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import group.smapx.remindalot.model.Contact;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class DatabaseDAO {
    private static final String LOG_TAG = "DatabaseDAO";
    private DatabaseHelper databaseHelper;

    public DatabaseDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertReminder(Reminder reminder) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ReminderContract.FeedEntry.COLUMN_TITLE, reminder.getTitle());
        cv.put(ReminderContract.FeedEntry.COLUMN_DESCRIPTION, reminder.getDescription());
        cv.put(ReminderContract.FeedEntry.COLUMN_DATE, reminder.getDate());
        LocationData l = reminder.getLocationData();
        cv.put(ReminderContract.FeedEntry.COLUMN_LOCATION_ADDRESS, l.getFormattedAddress());
        cv.put(ReminderContract.FeedEntry.COLUMN_LOCATION_LAT, l.getLat());
        cv.put(ReminderContract.FeedEntry.COLUMN_LOCATION_LON, l.getLon());

        long reminderId = db.insert(ReminderContract.FeedEntry.TABLE_NAME,
                null, cv);

        for (Contact contact : reminder.getContacts()) {
            cv = new ContentValues();
            cv.put(ContactContract.FeedEntry.COLUMN_NAME, contact.getName());
            cv.put(ContactContract.FeedEntry.COLUMN_PHONE_NUM, contact.getPhoneNumber());
            cv.put(ContactContract.FeedEntry.COLUMN_REMINDER_FK, reminderId);

            long contactId = db.insert(ContactContract.FeedEntry.TABLE_NAME,
                    null, cv);
            Log.d(LOG_TAG, "Inserted contact with ID " + contactId);
        }

        return reminderId;
    }

    public boolean deleteReminder(long reminderId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = ContactContract.FeedEntry.COLUMN_REMINDER_FK + " = ?";
        String[] selectionArgs = {String.valueOf(reminderId)};
        int contactsDeleted = db.delete(ContactContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

        selection = ReminderContract.FeedEntry._ID + " = ?";
        int remindersDeleted = db.delete(ReminderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);

        return (contactsDeleted > 0) && (remindersDeleted > 0);
    }

    public Reminder getReminder(long reminderId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = ReminderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(reminderId)};

        String sortOrder = ReminderContract.FeedEntry.COLUMN_DATE;

        Cursor c = db.query(
                ReminderContract.FeedEntry.TABLE_NAME,
                ReminderContract.PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Reminder reminder = null;
        if (c.moveToFirst()) {
            reminder = new Reminder();

            reminder.setTitle(c.getString(1));
            reminder.setDescription(c.getString(2));
            reminder.setDate(c.getLong(3));
            LocationData l = new LocationData(c.getString(5), c.getString(6), c.getString(4));
            reminder.setLocationData(l);

            ArrayList<Contact> contacts = getContactsForReminder(reminderId);
            reminder.setContacts(contacts);
        }

        return reminder;
    }

    public List<Reminder> getAllReminders() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String sortOrder = ReminderContract.FeedEntry.COLUMN_DATE;

        Cursor c = null;
        List<Reminder> reminders = new ArrayList<>();
        try {
            c = db.query(
                    ReminderContract.FeedEntry.TABLE_NAME,
                    ReminderContract.PROJECTION,
                    null, null, null, null,
                    sortOrder
            );

            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    Reminder reminder = new Reminder();
                    reminder.setTitle(c.getString(1));
                    reminder.setDescription(c.getString(2));
                    reminder.setDate(c.getLong(3));
                    LocationData l = new LocationData(c.getString(5), c.getString(6), c.getString(4));
                    reminder.setLocationData(l);
                    ArrayList<Contact> contacts = getContactsForReminder(id);
                    reminder.setContacts(contacts);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return reminders;
    }

    private ArrayList<Contact> getContactsForReminder(long reminderId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = ContactContract.FeedEntry.COLUMN_REMINDER_FK + " = ?";
        String[] selectionArgs = {String.valueOf(reminderId)};
        String sortOrder = ContactContract.FeedEntry.COLUMN_NAME;

        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor c = null;
        try {
            c = db.query(
                    ContactContract.FeedEntry.TABLE_NAME,
                    ContactContract.PROJECTION,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            if (c.moveToFirst()) {
                do {
                    contacts.add(new Contact(c.getString(1), c.getString(2)));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return contacts;
    }
}
