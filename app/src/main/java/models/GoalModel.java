package models;

import com.activeandroid.Model;
import com.activeandroid.annotation.*;

/**
 * Created by Charlotte on 09/02/2017.
 *
 * Each member of the class has its own attribute for column.
 */

@Table(name = "Goals")
public class GoalModel extends Model{
    @Column(name = "goal_name")
    String goalName;

    @Column(name = "steps_so_far")
    int stepsSoFar;

    @Column(name = "step_goal")
    int stepGoal;

    @Column(name = "active")
    Boolean acitve;



}
