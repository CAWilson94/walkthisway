package Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Charlotte on 12/02/2017.
 * Class for working with Database basically
 */

public class DbManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; //update version when database update
    private static final String DATABASE_NAME = "walkthisway.db";
    public static final String TABLE_GOALS = "goals";
    public static final String TABLE_HISTORY = "history";
    // Names of your columns for Goals Table
    public static final String COLUMN_GOAL_NAME = "goal_name";
    public static final String COLUMN_ACTIVE = "is_active";
    public static final String COLUMN_CURRENT_STEPS = "current_steps";
    public static final String COLUMN_STEP_GOALS = "step_goal";
    public static final String COLUMN_GOAL_COMPLETE = "goal_complete";


    public DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
