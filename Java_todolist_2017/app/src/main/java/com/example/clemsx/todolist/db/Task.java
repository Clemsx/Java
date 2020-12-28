package com.example.clemsx.todolist.db;

import android.provider.BaseColumns;

/**
 * Created by Clemsx on 29/01/2018.
 */

public class Task {
    public static final String DB_NAME = "com.example.clemsx.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns
    {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESCRIPTION = "description";
        public static final String COL_TASK_DATE = "date";
        public static final String COL_TASK_STATUS = "status";

    }
}

