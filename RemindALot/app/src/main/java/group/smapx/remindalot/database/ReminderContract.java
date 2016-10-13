package group.smapx.remindalot.database;

import android.provider.BaseColumns;

public class ReminderContract {

    private ReminderContract() {
    }

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FeedEntry.COLUMN_TITLE + " TEXT, " +
            FeedEntry.COLUMN_DESCRIPTION + " TEXT, " +
            FeedEntry.COLUMN_DATE + " INTEGER, " +
            FeedEntry.COLUMN_LOCATION_ADDRESS + " TEXT, " +
            FeedEntry.COLUMN_LOCATION_LAT + " INTEGER, " +
            FeedEntry.COLUMN_LOCATION_LON + " INTEGER," +
            FeedEntry.COLUMN_SMS_SENT + " INTEGER)";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "REMINDER";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION_ADDRESS = "address";
        public static final String COLUMN_LOCATION_LAT = "latitude";
        public static final String COLUMN_LOCATION_LON = "longitude";
        public static final String COLUMN_SMS_SENT = "Sms";
    }

    public static final String[] PROJECTION = {
            FeedEntry._ID,
            FeedEntry.COLUMN_TITLE,
            FeedEntry.COLUMN_DESCRIPTION,
            FeedEntry.COLUMN_DATE,
            FeedEntry.COLUMN_LOCATION_ADDRESS,
            FeedEntry.COLUMN_LOCATION_LAT,
            FeedEntry.COLUMN_LOCATION_LON
    };
}
