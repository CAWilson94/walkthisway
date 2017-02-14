package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Charlotte on 12/02/2017.
 * Class for working with Database basically
 */

public class DbManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3; //update version when database update
    private static final String DATABASE_NAME = "walkthisway.db";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_HISTORY = "history";
    // Names of your columns for Goals Table
    public static final String COLUMN_GOAL_ID = "_id";
    public static final String COLUMN_GOAL_NAME = "goal_name";
    public static final String COLUMN_ACTIVE = "is_active";
    public static final String COLUMN_CURRENT_STEPS = "current_steps";
    public static final String COLUMN_STEP_GOALS = "step_goal";
    public static final String COLUMN_GOAL_COMPLETE = "goal_complete";


    public DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * Whenyou create the table for the first time what do you want me to do?
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the goals table to store stuff in here
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_GOALS + "(" +
                COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GOAL_NAME + " TEXT, " +
                COLUMN_CURRENT_STEPS + " INTEGER, " +
                COLUMN_ACTIVE + " INTEGER DEFAULT 0, " +
                COLUMN_GOAL_COMPLETE + " INTEGER DEFAULT 0, " +
                COLUMN_STEP_GOALS + " INTEGER NOT NULL" +
                ")";
        db.execSQL(query);
    }

    /**
     * when you upgrade version it will call this
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop current table and create new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    /**
     * Add row to table
     *
     * @param goal
     */
    public void addGoal(Goals goal) {
        // Basically a list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, goal.getName());
        values.put(COLUMN_ACTIVE, (goal.getActive()) ? 1 : 0);
        values.put(COLUMN_STEP_GOALS, goal.getStepTarget());
        values.put(COLUMN_CURRENT_STEPS, goal.getNumSteps());
        values.put(COLUMN_GOAL_COMPLETE, (goal.getActive()) ? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_GOALS, null, values);
        db.close();
    }

    /**
     * Delete Goals from data base
     * Remembering you can only delete it if it is non active!
     *
     * @param goalName
     */
    public void deleteGoal(String goalName) {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GOALS + " WHERE " + COLUMN_GOAL_NAME + " =\" " + goalName + "\";");
    }

    /**
     * Checks the db for entries which have
     * Active Goal set to 1: if active goal, return.
     *
     * @return
     */
    public Boolean checkForActiveGoal() {
        String dbString = "";
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + " = 1";
        ; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("is_active")) != null) {
                dbString += c.getString(c.getColumnIndex("is_active"));
                dbString += "\n";
                return true;
            }
            c.moveToNext();
        }
        db.close();

        return false;
    }

    /**
     * For a given goal name/active goal?
     *
     * @return
     */
    public int getCurrentProgress() {
        int progress = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_STEP_GOALS + " , " + COLUMN_CURRENT_STEPS + " FROM "
                + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + " = 1 "; // TODO: fix this hack you horrid person
        Cursor c = db.rawQuery(query, null);
        int stepGoal = 0;
        int currentSteps = 0;
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("is_active")) != null) {
                stepGoal = c.getInt(c.getColumnIndex("step_goal"));
                currentSteps = c.getInt(c.getColumnIndex("current_steps"));
                progress = stepGoal - currentSteps; //TODO: make this get abs value?
            }
            c.moveToNext();
        }
        db.close();
        return progress;
    }

    public String getActiveGoalName() {
        /**
         * Could return an array containing goal name and also current progress?
         */
        String activeGoalName = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + " = 1";
        ; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("is_active")) != null) {
                activeGoalName = c.getString(c.getColumnIndex("goal_name"));
            }
            c.moveToNext();
        }
        db.close();
        return activeGoalName;
    }


    /**
     * testing db has things in it
     *
     * @return
     */
    public String dbToString() {
        String dbString = "";
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE 1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("goal_name")) != null) {
                dbString += c.getString(c.getColumnIndex("goal_name"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

    public Cursor getAllRows() {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " " + "WHERE 1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("goal_name")) != null) {

            }
            c.moveToNext();
        }
        db.close();

        return c;

    }
}
