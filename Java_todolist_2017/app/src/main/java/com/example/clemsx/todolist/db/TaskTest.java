package com.example.clemsx.todolist.db;

import android.provider.BaseColumns;

public class TaskTest {
    public static final String DB_NAME = "com.example.clemsx.todolist.dbTest";
    public static final int DB_VERSION = 1;

    public class TaskEntryTest implements BaseColumns
    {
        public static final String TABLE = "tasksTest";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESCRIPTION = "description";
        public static final String COL_TASK_DATE = "date";
        public static final String COL_TASK_STATUS = "status";

    }
}
