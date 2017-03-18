package Models;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import static android.R.attr.max;

/**
 * Created by Charlotte on 12/02/2017.
 */

public class GoalHandler {
    /**
     * Checks for active goal: if there is an active goal
     * CardView frag won't change
     *
     * @param goalList
     * @return
     */
    public Boolean isThereActiveGoal(List<Goals> goalList) {
        // Replace with db shit
        for (Goals goal : goalList) {
            if (goal.active == true) {
                return true;
            }
        }
        return false;
    }

    public void createRandomGoals(DbManager db) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 5; i++) {
            Goals random = new Goals();
            int year = 2017 - 1900;
            String currentDate = sdf.format(new Date(year, 03, i));
            random.dateGoal = currentDate;
            random.setActive(false);
            Boolean randDayPass = Math.random() < 0.5;
            random.setDayPassed(false);
            random.setNumSteps(90);
            random.setComplete(true);
            random.setName("FEAR ME: " + i);
            random.setStepTarget(100 + (i * 10));
            db.addGoal(random);
        }

    }
}
