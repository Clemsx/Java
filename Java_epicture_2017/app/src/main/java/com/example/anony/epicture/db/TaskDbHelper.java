package com.example.anony.epicture.db;

/**
 * Created by anony on 08/02/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 *  TaskDbHelper Class to create a TABLE in SQLite
 */
public class TaskDbHelper extends SQLiteOpenHelper {

    /**
     * TaskDbHelper constructor
     * @param context
     */
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    /**
     * onCreate create a TABLE on SQLite
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_ID_CLIENT + " TEXT" +
                ");";
        db.execSQL(createTable);
        Log.i("Database", "Create OK");
    }

    /**
     * onUpgrade do an upgrade of the database
     * @param db name of database
     * @param oldVersion of database
     * @param newVersion of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}