package models;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Charlotte on 09/02/2017.
 * <p>
 * Each member of the class has its own attribute for column.
 */

@Table(name = "Goals")
public class GoalModel extends Model {
    @Column(name = "goal_name")
    public String goalName;

    @Column(name = "steps_so_far")
    public int stepsSoFar;

    @Column(name = "step_goal")
    public int stepGoal;

    @Column(name = "active")
    public Boolean active;

    public static List<GoalModel> getAllGoals() {
        return new Select().from(GoalModel.class).execute();
    }
}
