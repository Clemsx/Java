package com.example.clemsx.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.clemsx.todolist.db.Task;
import com.example.clemsx.todolist.db.TaskHelperTest;
import com.example.clemsx.todolist.db.TaskTest;

/**
 * Created by Clemsx on 30/01/2018.
 */

public class DBtesting extends AndroidTestCase {

    private static String taskTitle = "TESTTITLE";
    private static String taskContent = "testBlabla";
    private static String taskDate = "10/10/2018";
    private static long res;

    public void testDropDB() {
        assertTrue(mContext.deleteDatabase(TaskTest.DB_NAME));
    }

    public void testCreateDB() {
        TaskHelperTest dbHelper = new TaskHelperTest(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testInsertData() {
        TaskHelperTest dbHelper = new TaskHelperTest(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        taskTitle = "TESTTITLE";
        taskContent = "testBlabla";
        taskDate = "10/10/2018";

        ContentValues values = new ContentValues();
        values.put(TaskTest.TaskEntryTest.COL_TASK_TITLE, taskTitle);
        values.put(TaskTest.TaskEntryTest.COL_TASK_DESCRIPTION, taskContent);
        values.put(TaskTest.TaskEntryTest.COL_TASK_DATE, taskDate);
        values.put(TaskTest.TaskEntryTest.COL_TASK_STATUS, "false");

        res = db.insert(TaskTest.TaskEntryTest.TABLE, null, values);
        assertTrue(res != -1);
    }

    public void testIsDataCorrectInDB() {
        TaskHelperTest dbHelper = new TaskHelperTest(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TaskTest.TaskEntryTest.TABLE, null, null, null, null, null, null, null);
        assertTrue(cursor.moveToFirst());

        int idTaskTitle = cursor.getColumnIndex(TaskTest.TaskEntryTest.COL_TASK_TITLE);
        String taskTitleIn = cursor.getString(idTaskTitle);

        int idTaskContent = cursor.getColumnIndex(TaskTest.TaskEntryTest.COL_TASK_DESCRIPTION);
        String taskContentIn = cursor.getString(idTaskContent);

        int idTaskDate = cursor.getColumnIndex(TaskTest.TaskEntryTest.COL_TASK_DATE);
        String taskDateIn = cursor.getString(idTaskDate);

        assertEquals(taskTitle, taskTitleIn);
        assertEquals(taskContent, taskContentIn);
        assertEquals(taskDate, taskDateIn);
    }

    public void testEditTask() throws Exception {

        ContentValues values = new ContentValues();
        values.put(TaskTest.TaskEntryTest.COL_TASK_TITLE, taskTitle);
        values.put(TaskTest.TaskEntryTest.COL_TASK_DESCRIPTION, "blablabla");
        values.put(TaskTest.TaskEntryTest.COL_TASK_DATE, taskDate);

        TaskHelperTest taskHelper = new TaskHelperTest(mContext);
        SQLiteDatabase db = taskHelper.getWritableDatabase();
        res = db.update(TaskTest.TaskEntryTest.TABLE, values, TaskTest.TaskEntryTest.COL_TASK_TITLE + "='" + taskTitle + "';", null);
        assertTrue(res != -1);
    }

    public void testDeleteTask() throws Exception {
        TaskHelperTest taskHelper = new TaskHelperTest(mContext);
        SQLiteDatabase db = taskHelper.getWritableDatabase();

        res = db.delete(TaskTest.TaskEntryTest.TABLE, TaskTest.TaskEntryTest.COL_TASK_TITLE + " = ?", new String[]{taskTitle});
        db.close();
        assertTrue(res != -1);
    }
}
