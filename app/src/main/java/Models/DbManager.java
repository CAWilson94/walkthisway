package Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import SystemDateStrategy.SystemDateManager;

import static android.R.attr.id;
import static android.R.attr.value;
import static android.os.Build.ID;
import static javax.xml.datatype.DatatypeConstants.DATETIME;

/**
 * Created by Charlotte on 12/02/2017.
 * Class for working with Database basically
 */

public class DbManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 8; //update version when database update
    private static final String DATABASE_NAME = "Goal.db";
    public static final String TABLE_GOALS = "goals";
    // Names of your columns for Goal Table
    public static final String COLUMN_GOAL_ID = "_id";
    public static final String COLUMN_GOAL_NAME = "goal_name";
    public static final String COLUMN_ACTIVE = "is_active";
    public static final String COLUMN_CURRENT_STEPS = "current_steps";
    public static final String COLUMN_STEP_GOALS = "step_goal";
    public static final String COLUMN_GOAL_COMPLETE = "goal_complete";
    public static final String COLUMN_DATE_GOALS = "goal_date";
    public static final String COLUMN_DAY_PASSED = "day_passed";
    public static final String COLUMN_GOAL_UNITS = "units";


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
                COLUMN_DAY_PASSED + " INTEGER DEFAULT 0, " +
                COLUMN_GOAL_UNITS + " TEXT " +
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
        values.put(COLUMN_GOAL_UNITS, goal.getUnits());
        values.put(COLUMN_ACTIVE, (goal.getActive()) ? 1 : 0);
        values.put(COLUMN_STEP_GOALS, goal.getStepTarget());
        values.put(COLUMN_CURRENT_STEPS, goal.getNumSteps());
        values.put(COLUMN_GOAL_COMPLETE, (goal.getComplete()) ? 1 : 0);
        values.put(COLUMN_DATE_GOALS, String.valueOf(goal.getDateGoal()));
        values.put(COLUMN_DAY_PASSED, (goal.getDayPassed()) ? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_GOALS, null, values);
        db.close();
    }

    /**
     * Delete Goal from data base
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
     * <p>
     * Will need to look for active goals on todays date..
     *
     * @return
     */
    public Boolean checkForActiveGoal(Context context, String date) {
        Boolean bool = false;
        String dbString = "";
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + " = 1"; // + " AND " + "date(" + COLUMN_DATE_GOALS + ")" + " = " + "'" + date + "'";


        // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("is_active")) != null) {
                Log.e("TAG", DatabaseUtils.dumpCurrentRowToString(c));
                dbString += c.getString(c.getColumnIndex("is_active"));
                dbString += "\n";
                bool = true;
            }
            c.moveToNext();
        }
        c.close();
        db.close();
        Log.e("TAG", "VALUE IS: " + bool);
        Log.e("TAG", "DATE IS: " + date);
        return bool;
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

    public int getDailyActivity(Context context) {

        SystemDateManager date = new SystemDateManager();
        String systemorUserDate = date.systemDateDecider(context);
        int activity = 0;

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_CURRENT_STEPS + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + "=1";
        int activeSteps = 0;
        Cursor c = db.rawQuery(query, null);
        // Move to first row in your results
        c.moveToFirst();

        if (c.getString(c.getColumnIndex("current_steps")) != null) {
            activity = c.getInt(c.getColumnIndex("current_steps"));
        } else if (c.getString(c.getColumnIndex("current_steps")) == null) {
            activity = c.getInt(0);
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


    /**
     * Return only rows from todays date i.e. either system date or user specified date
     *
     * @return
     */
    public Cursor getAllRows(Context context) {
        SystemDateManager date = new SystemDateManager();
        String systemorUserDate = date.systemDateDecider(context);
        // Reference to db
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " " + "WHERE " + COLUMN_ACTIVE + " =0" + " AND " + "date(" + COLUMN_DATE_GOALS + ")" + " = " + "'" + systemorUserDate + "'";
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
                + COLUMN_CURRENT_STEPS + " = " + COLUMN_CURRENT_STEPS + " + " + steps + " WHERE " + COLUMN_ACTIVE + "=1");
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
        ;
        //db.close();
    }

    public void createGoalInitalizer(Boolean sw, Goals goal, String currentDate, Context context) {
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
                goal.setNumSteps(getDailyActivity(context));
            }
            goal.setActive(false);
        }
    }

    /**
     * Make only the goals where day has passed i.e. in the past, available.
     * Day past should insinuate that it was the last goal active at that time.
     *
     * @return
     */
    public Cursor simpleHistory() {
        // Reference to db

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " " + "WHERE " + COLUMN_DAY_PASSED + " =1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() != 0) {

            // Move to first row in your results
            c.moveToFirst();
            while (!c.isAfterLast()) {
                //if (c.getString(c.getColumnIndex("goal_name")) != null) {
                //}
                c.moveToNext();
            }
        }
        //db.close();
        //c.close();
        return c;
    }

    /**
     * Delete all in history
     */
    public void clearHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("goals", "day_passed =?", new String[]{"1"});
    }

    public int averageStat() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("SELECt AVG(" + COLUMN_CURRENT_STEPS + ") FROM " + TABLE_GOALS, null);
        if (cur.moveToFirst()) {
            Log.d("TAG", String.valueOf(cur.getInt(0)) + ": AVERAGE VALUE");
            return cur.getInt(0);
        }
        return 0;
    }

    public int maxStat() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("SELECt MAX(" + COLUMN_CURRENT_STEPS + ") FROM " + TABLE_GOALS, null);
        if (cur.moveToFirst()) {
            Log.d("TAG", String.valueOf(cur.getInt(0)) + ": MAX VALUE");
            return cur.getInt(0);
        }
        return 0;
    }

    public int minStat() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("SELECt MIN(" + COLUMN_CURRENT_STEPS + ") FROM " + TABLE_GOALS, null);
        if (cur.moveToFirst()) {
            Log.d("TAG", String.valueOf(cur.getInt(0)) + ": MIN VALUE + -------------------------------------");
            return cur.getInt(0);
        }
        return 0;
    }

    /**
     * Show history where complete was a thing
     */
    public Cursor CursorHistoryCompleteGoals() {

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GOALS + " " + "WHERE " + COLUMN_DAY_PASSED + " =1" + COLUMN_GOAL_COMPLETE + "=1"; // one means select every row ( every condition is met)
        // Cursor will point to location in your results
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() != 0) {

            // Move to first row in your results
            c.moveToFirst();
            while (!c.isAfterLast()) {
                //if (c.getString(c.getColumnIndex("goal_name")) != null) {
                //}
                c.moveToNext();
            }
        }
        //db.close();
        //c.close();
        return c;
    }

    /**
     * In this glorious method, we turn our goals carriage back into a pumpkin
     * The active goal is no longer.. current daily steps are nil.
     */
    public void carriageToPumpkin() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACTIVE, String.valueOf(0));
        int boop = db.update(TABLE_GOALS, cv, COLUMN_ACTIVE + " =?", new String[]{"1"});
    }

    /**
     * When alarm manager called change the active goal column to day passed
     */
    public void dayPassed() {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DAY_PASSED, String.valueOf(1));
        int boop = db.update(TABLE_GOALS, cv, COLUMN_ACTIVE + " =?", new String[]{"1"});
    }

    public String displayUnitsFromActiveStatus() {

        String units = "steps";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_GOAL_UNITS + " FROM " + TABLE_GOALS + " WHERE " + COLUMN_ACTIVE + "=1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            units = cursor.getString(cursor.getColumnIndex("units"));
        }
        cursor.close();
        return units;
    }

}
