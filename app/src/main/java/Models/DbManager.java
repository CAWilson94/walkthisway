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

    private static final int DATABASE_VERSION = 2; //update version when database update
    private static final String DATABASE_NAME = "walkthisway.db";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_HISTORY = "history";
    // Names of your columns for Goals Table
    public static final String COLUMN_GOAL_ID = "goal_id";
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
                //COLUMN_ACTIVE + " INTEGER " +
                //COLUMN_GOAL_COMPLETE + " INTEGER " +
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
       // values.put(COLUMN_ACTIVE, goal.getActive());
        values.put(COLUMN_STEP_GOALS, goal.getStepTarget());
        values.put(COLUMN_CURRENT_STEPS, goal.getNumSteps());
        //values.put(COLUMN_GOAL_COMPLETE, goal.getComplete());
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
}
