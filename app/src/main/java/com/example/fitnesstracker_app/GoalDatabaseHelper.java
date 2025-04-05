package com.example.fitnesstracker_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_goals.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table name and column names
    public static final String TABLE_GOALS = "goals";
    public static final String COLUMN_ID = "_id"; // Convention for primary key
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEADLINE = "deadline";

    // SQL statement to create the goals table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_GOALS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DEADLINE + " TEXT);";

    public GoalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded.
        // For simplicity in this lab, we'll just drop the old table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }
}