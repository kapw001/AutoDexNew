package volleywebrequest.com.mysqlitelibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yasar on 6/10/17.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myappcontacts.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_ContactListENTRIES);
        db.execSQL(FeedReaderContract.SQL_CREATE_ContactAttributeENTRIES);
        db.execSQL(FeedReaderContract.SQL_CREATE_ContactCategoryENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FeedReaderContract.SQL_DELETE_ContactListENTRIES);
        db.execSQL(FeedReaderContract.SQL_DELETE_ContactAttributeENTRIES);
        db.execSQL(FeedReaderContract.SQL_DELETE_ContactCategoryENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}