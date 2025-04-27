package fyp.com.riona;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rai on 8/9/2016.
 */
public class FriendlyDB extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "Postquam";

    private static final String TABLE_NOTIFICATION = "notification";

    //////Location Table

    private static final String NOTIFICATION_ID = "id";
    private static final String NOTIFICATION_content = "content";




    public FriendlyDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    ///////Creating table Person
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "(" + NOTIFICATION_ID + " INTEGER PRIMARY KEY," +   NOTIFICATION_content + " TEXT)";

        db.execSQL(CREATE_ORDER_TABLE);

    }
    /////Upgrade the table

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older Folder

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NOTIFICATION);
        db.close();
        return count;
    }
    public boolean addOrder(String nam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTIFICATION_content, nam);

        /////now insert in database
        long isInserted = db.insert(TABLE_NOTIFICATION, null, contentValues);
        db.close();
        return true;
    }

    public void deleteTableF() {

        String selectQuery = "DELETE FROM " + TABLE_NOTIFICATION;

        SQLiteDatabase db= this.getWritableDatabase();

        db.execSQL(selectQuery);
    }


}