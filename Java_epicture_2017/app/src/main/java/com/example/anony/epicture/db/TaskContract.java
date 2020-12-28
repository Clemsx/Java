package com.example.anony.epicture.db;

/**
 * Created by anony on 08/02/2018.
 */

import android.provider.BaseColumns;

/**
 * TaskContract class contains the information of the database
 * such as DB_NAME, DB_VERSION
 */
public class TaskContract {
    public static final String DB_NAME = "epicture.db";
    public static final int DB_VERSION = 1;

    /**
     * TaskEntry class manages the table identity and id of client
     */
    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "Identity";
        public static final String COL_ID_CLIENT = "idClient";
    }
}