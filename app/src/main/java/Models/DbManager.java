package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.id;
import static android.R.attr.value;
import static javax.xml.datatype.DatatypeConstants.DATETIME;

/**
 * Created by Charlotte on 12/02/2017.
 * Class for working with Database basically
 */

public class DbManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7; //update version when database update
    private static final String DATABASE_NAME = "walkthisway.db";
    public static final String TABLE_GOALS = "goals";
    // Names of your columns for Goals Table
    public static final String COLUMN_GOAL_ID = "_id";
    public static final String COLUMN_GOAL_NAME = "goal_name";
    public static final String COLUMN_ACTIVE = "is_active";
    public static final String COLUMN_CURRENT_STEPS = "current_steps";
    public static final String COLUMN_STEP_GOALS = "step_goal";
    public static final String COLUMN_GOAL_COMPLETE = "goal_complete";
    public static final String COLUMN_DATE_GOALS = "goal_date";
    public static final String COLUMN_DAY_PASSED = "day_passed";


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
                COLUMN_CURRENT_STEPS + " INTEGER DEFAULT 0, " +
                COLUMN_ACTIVE + " INTEGER DEFAULT 0, " +
                COLUMN_GOAL_COMPLETE + " INTEGER DEFAULT 0, " +
                COLUMN_STEP_GOALS + " INTEGER NOT NULL, " +
                COLUMN_DATE_GOALS + " DATE DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_DAY_PASSED + " INTEGER DEFAULT 0 " +
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
        values.put(COLUMN_GOAL_COMPLETE, (goal.getComplete()) ? 1 : 0);
        values.put(COLUMN_DATE_GOALS, String.valueOf(goal.getDateGoal()));
        values.put(COLUMN_DAY_PASSED, (goal.getComplete()) ? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_GOALS, null, values);
        db.close();
    }

    /**
     * Delete Goals from data base
     * Remembering you can only delete it if it is non active!
     *
     * @param goalID
     */
    public void deleteGoal(String goalID) {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GOALS + " WHERE " + COLUMN_GOAL_ID + "='" + goalID + "'");
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
        // one means select every row ( every condition is met)
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
        c.close();
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
        c.close();
        db.close();
        return progress;
    }

    public int activeGoalInit() {
        int initSteps = 0;
        SQLiteDatabase db = getWritableDatabase();
        // Go to current active goal; get steps from that
        String query = "SELECT " + COLUMN_CURRENT_STEPS + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + "=1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            initSteps = cursor.getInt(cursor.getColumnIndex("current_steps"));
        }
        cursor.close();
        return initSteps;
    }

    public Boolean checkFieldExists(String field) {

        SQLiteDatabase db = getWritableDatabase();
        String Query = "Select * from " + TABLE_GOALS + " where " + field + " IS NOT NULL ";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getDailyActivity(String currentDate) {
        int activity = 0;

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_CURRENT_STEPS + " FROM " + TABLE_GOALS + " WHERE " + "date(" + COLUMN_DATE_GOALS + ")" + " = " + "'" + currentDate + "'";
        int activeSteps = 0;
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        if (c.getString(c.getColumnIndex("current_steps")) != null) {
            activity = c.getInt(c.getColumnIndex("current_steps"));
        }

        c.close();
        return activity;
    }

    public Boolean checkActiveGoal() {
        Boolean check = false;
        SQLiteDatabase db = getWritableDatabase();
        // Go to current active goal; get steps from that
        String query = "SELECT * " + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + "=1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            check = true;
        }
        cursor.close();
        return check;
    }


    public void updateFieldFromID(String field, String update, int ID) {
        // Reference to db
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(field, String.valueOf(update));
        int boop = db.update(TABLE_GOALS, cv, COLUMN_GOAL_ID + " = " + ID, null);
        //db.close();
    }

    public String getGoalName(int goalID) {
        String goalName = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_GOAL_NAME + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_GOAL_ID + "='" + goalID + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            goalName = cursor.getString(cursor.getColumnIndex("goal_name"));
        }

        cursor.close();

        return goalName;
    }

    public String getActiveGoalName() {
        /**
         * Could return an array containing goal name and also current progress?
         */
        String activeGoalName = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + " = 1";
        // one means select every row ( every condition is met)
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
        //db.close();
        c.close();
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
        //db.close();
        c.close();
        return dbString;
    }


    public Cursor getAllRows() {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " " + "WHERE " + COLUMN_ACTIVE + " =0"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("goal_name")) != null) {

            }
            c.moveToNext();
        }
        //db.close();
        //c.close();
        return c;

    }

    /**
     * @return
     */
    public String displayActiveName() {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_GOAL_NAME + " FROM " + TABLE_GOALS + " " + " WHERE " + COLUMN_ACTIVE + " =1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        String activeName = "";
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("goal_name")) != null) {
                activeName = c.getString(c.getColumnIndex("goal_name"));
            }
            c.moveToNext();
        }
        //db.close();
        c.close();
        return activeName;
    }

    /**
     * User inputs active goal steps, this updates the steps for the current goal.
     */
    public void addStepsCurrentGoal(int steps, String currentDate) {
        // Reference to db
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        int activeStep = displayActiveSteps();
        int newStepNewMe = activeStep + steps;
        cv.put(COLUMN_CURRENT_STEPS, newStepNewMe);
        String boop = "date(" + COLUMN_DATE_GOALS + ")" + " = " + "'" + currentDate + "'";
        db.update(TABLE_GOALS, cv, boop, null);
        //db.close();
    }

    public void incrementSteps(int steps, String currentDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_GOALS + " SET "
                + COLUMN_CURRENT_STEPS + " = " + COLUMN_CURRENT_STEPS + " + " + steps + " WHERE " + "date(" + COLUMN_DATE_GOALS + ")" + " = " + "'" + currentDate + "'");
        //db.close();
    }

    /**
     * @return
     */
    public int displayActiveSteps() {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_CURRENT_STEPS + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + "=1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        int activeSteps = 0;
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("current_steps")) != null) {
                activeSteps = c.getInt(c.getColumnIndex("current_steps"));
            }
            c.moveToNext();
        }
        //db.close();
        c.close();
        return activeSteps;
    }

    /**
     * @return
     */
    public int displayGoalSteps() {
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_STEP_GOALS + " FROM " + TABLE_GOALS + " " + " WHERE " + COLUMN_ACTIVE + " =1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        int goalSteps = 0;
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("step_goal")) != null) {
                goalSteps = c.getInt(c.getColumnIndex("step_goal"));
            }
            c.moveToNext();
        }
        //db.close();
        c.close();
        return goalSteps;
    }

    /**
     * Returns a string value of the current field based on ID
     *
     * @param field
     * @param ID
     * @return
     */
    public String displayFieldFromID(String field, int ID) {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + field + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_GOAL_ID + "='" + ID + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            field = cursor.getString(cursor.getColumnIndex(field));
        }
        cursor.close();
        return field;
    }


    public void sketchySetAllOthersInactive() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_GOALS + " SET "
                + COLUMN_ACTIVE + " = " + 0 + " WHERE "
                + COLUMN_ACTIVE + " =1");
        //db.close();
    }

    public void createGoalInitalizer(Boolean sw, Goals goal, String currentDate) {
        if (sw) {
            if (checkActiveGoal())
                // Check there is an active goal first...
                goal.setNumSteps(activeGoalInit());
            // else should set to default..
            // Sets new goals steps to those of last active goal
            sketchySetAllOthersInactive();
            goal.setActive(sw);
        } else {
            if (!checkFieldExists(COLUMN_DATE_GOALS)) {
                goal.setNumSteps(0);
            }
            if (checkFieldExists(COLUMN_DATE_GOALS)) {
                goal.setNumSteps(getDailyActivity(currentDate));
            }
            goal.setActive(false);
        }
    }
}
