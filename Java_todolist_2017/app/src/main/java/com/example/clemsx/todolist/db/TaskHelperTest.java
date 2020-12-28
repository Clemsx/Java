package com.example.clemsx.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskHelperTest extends SQLiteOpenHelper {

    public TaskHelperTest(Context context)
    {
        super(context, TaskTest.DB_NAME, null, TaskTest.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskTest.TaskEntryTest.TABLE + " ( " +
                TaskTest.TaskEntryTest._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskTest.TaskEntryTest.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskTest.TaskEntryTest.COL_TASK_DESCRIPTION + " TEXT NOT NULL, " +
                TaskTest.TaskEntryTest.COL_TASK_DATE + " TEXT NOT NULL, " +
                TaskTest.TaskEntryTest.COL_TASK_STATUS + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TaskTest.TaskEntryTest.TABLE);
        onCreate(db);
    }
}
