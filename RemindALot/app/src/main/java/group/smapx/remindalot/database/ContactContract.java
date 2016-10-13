package group.smapx.remindalot.database;

import android.provider.BaseColumns;

public class ContactContract {

    private ContactContract() {
    }

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FeedEntry.COLUMN_NAME + " TEXT, " +
            FeedEntry.COLUMN_PHONE_NUM + " TEXT, " +
            FeedEntry.COLUMN_REMINDER_FK + " INTEGER, " +
            "FOREIGN KEY(" + FeedEntry.COLUMN_REMINDER_FK + ") REFERENCES " +
            ReminderContract.FeedEntry.TABLE_NAME + "(" + ReminderContract.FeedEntry._ID + "))";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CONTACT";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE_NUM = "phonenumber";
        public static final String COLUMN_REMINDER_FK = "reminder_fk";
    }

    public static final String[] PROJECTION = {
            FeedEntry._ID,
            FeedEntry.COLUMN_NAME,
            FeedEntry.COLUMN_PHONE_NUM,
            FeedEntry.COLUMN_REMINDER_FK
    };
}
