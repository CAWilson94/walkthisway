package Models;

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
}
