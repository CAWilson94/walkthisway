package Models;

import java.sql.Date;
import java.util.List;

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
        // TODO: set dates cos I am lazy
        for (int i = 0; i < 5; i++) {
            Goals random = new Goals();
            int year = 2017 - 1900;
            random.setActive(false);
            random.setNumSteps(90);
            random.setComplete(true);
            random.setName("FEAR ME: " + i);
            random.setStepTarget(100 + (i * 10));
            db.addGoal(random);
        }

    }
}
