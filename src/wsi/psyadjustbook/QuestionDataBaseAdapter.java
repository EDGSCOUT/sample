package wsi.psyadjustbook;

import java.util.Calendar;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.PowerManager;
import android.util.Log;

public class QuestionDataBaseAdapter {



//    private static final String IMEI =MobileSens.IMEI;
	public static final String IMEI="1234";
    public static final String KEY_DATARECORD = "datarecord";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIME = "recordtime";
    public static final String KEY_RECORD="record";

    private static final String TAG = "DataBaseAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /** Database creation sql statement */
    private static final String DATABASE_CREATE =
            "create table qanda (_id integer primary key "  //id  recordtime data
           + "autoincrement, recordtime text not null, " 
           + "record integer not null ,"+"datarecord text not null);";

    private static final String DATABASE_NAME = "qandalist";
    private static final String DATABASE_TABLE = "qanda";
    private static final int DATABASE_VERSION = 1;



    private HashSet<ContentValues> mBuffer;
    private HashSet<ContentValues> tempBuffer;

    private boolean mOpenLock = false;
    private boolean mFlushLock = false;


    private final Context mCtx;
    private final PowerManager.WakeLock mWL;

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {

        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            
            db.execSQL("DROP TABLE IF EXISTS qanda");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx       the Context within which to work
     */
    public QuestionDataBaseAdapter (Context ctx) 
    {
        this.mCtx = ctx;
        mBuffer = new HashSet<ContentValues>(); 

        PowerManager pm = (PowerManager)
            ctx.getSystemService(Context.POWER_SERVICE);

        mWL = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                TAG);
        mWL.setReferenceCounted(false);

    }

    /**
     * Open the database.
     * If it cannot be opened, try to create a new instance of the
     * database. If it cannot be created, throw an exception to signal
     * the failure.
     * 
     * @return this         (self reference, allowing this to be
     *                      chained in an initialization call)
     * @throws SQLException if the database could be neither opened or
     *                      created
     */
    public synchronized QuestionDataBaseAdapter  open() throws SQLException 
    {
        if (!mFlushLock)
        {
            mDbHelper = new DatabaseHelper(mCtx);
            mDb = mDbHelper.getWritableDatabase();
        }
        mOpenLock = true;
        return this;
    }
    
    /**
      * Closes the database.
      */
    public synchronized void close() 
    {
        if (!mFlushLock)
        {
            mDb.close();
            mDbHelper.close();
        }
        mOpenLock = false;
    }


    /**
     * Create a new entry using the datarecord provided. 
     * If the entry is successfully created returns the new rowId for
     * that entry, otherwise returns a -1 to indicate failure.
     * @param record 
     * 
     * @param datarecord        datarecord for the entry
     */
    public synchronized void createEntry(JSONObject data, int record) 
    {

        JSONObject dataRecord = new JSONObject();

        // First thing, get the current time
        Calendar cal = Calendar.getInstance();
        String timeStr = "" +
            cal.get(Calendar.YEAR) + "." +
            (cal.get(Calendar.MONTH) + 1) + "." +
            cal.get(Calendar.DAY_OF_MONTH) + "." +
            cal.get(Calendar.HOUR_OF_DAY) + "." +
            cal.get(Calendar.MINUTE)  + "." +
            cal.get(Calendar.SECOND);



		try {
			dataRecord.put("date", timeStr);
			dataRecord.put("imei", "0");
			dataRecord.put("msg", data);
		} catch (JSONException e) {
			Log.e(TAG, "Exception", e);
		}



        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, timeStr);
        initialValues.put(KEY_RECORD, record);
        initialValues.put(KEY_DATARECORD, dataRecord.toString());

        mBuffer.add(initialValues);

        Log.i(TAG, "Creating data record" + dataRecord.toString());

        //return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    public void flushDb()
    {
        synchronized(this)
        {
            tempBuffer = mBuffer;
            mBuffer = new HashSet<ContentValues>(); 
        }

//        Thread flushThread = new Thread()
//        {
//            public void run()
//            {
//                mWL.acquire();
//
                if (!mOpenLock)
                {
                    try
                    {
                        mDbHelper = new DatabaseHelper(mCtx);
                        mDb = mDbHelper.getWritableDatabase();
                    }
                    catch (SQLException se)
                    {
                        Log.e(TAG, "Could not open DB helper", se);

                    }
                }
                mFlushLock = true;


                Log.i(TAG, "Flushing " 
                        + tempBuffer.size() + " records.");

                for (ContentValues value : tempBuffer)
                {
                    mDb.insert(DATABASE_TABLE, null, value);
                }


                if (!mOpenLock)
                {
                    mDb.close();
                    mDbHelper.close();
                }

                mFlushLock = false;
//                mWL.release();
//            }
//        };
//
//        flushThread.start();

    }

    /**
     * Deletes the entry with the given rowId
     * 
     * @param rowId         id of datarecord to delete
     * @return              true if deleted, false otherwise
     */
    public synchronized boolean deleteEntry(long rowId) 
    {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID 
                + "=" + rowId, null) > 0;
    }


    /**
     * Deletes the entries in a range.
     * 
     * @param fromId         id of first datarecord to delete
     * @param toId           id of last datarecord to delete
     * @return              true if deleted, false otherwise
     */
    public synchronized boolean deleteRange(long fromId, long toId) 
    {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID 
                + " BETWEEN " 
                + fromId
                + " AND " 
                + toId, null) > 0;
    }


    /**
     * Returns a Cursor over the list of all datarecords in the database
     * 
     * @return              Cursor over all notes
     */
    public Cursor fetchAllEntries() 
    {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TIME,KEY_RECORD,KEY_DATARECORD}, 
                null, null, null, null, null);
    }

    /**
     * Returns a Cursor positioned at the record that matches the
     * given rowId.
     * 
     * @param  rowId        id of note to retrieve
     * @return              Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchEntry(long rowId) throws SQLException 
    {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[]
                {KEY_ROWID, KEY_TIME, KEY_RECORD, KEY_DATARECORD}, 
                KEY_ROWID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



}
