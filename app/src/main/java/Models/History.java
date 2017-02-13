package Models;

/**
 * Created by Charlotte on 13/02/2017.
 */

public class History {

    int numSteps;

    public int getHowMuchDailyGoal() {
        return howMuchDailyGoal;
    }

    public void setHowMuchDailyGoal(int howMuchDailyGoal) {
        this.howMuchDailyGoal = howMuchDailyGoal;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public String getDailyGoalName() {
        return dailyGoalName;
    }

    public void setDailyGoalName(String dailyGoalName) {
        this.dailyGoalName = dailyGoalName;
    }

    int howMuchDailyGoal;
    String dailyGoalName;

    public History() {

    }

    public History(int numSteps, int howMuchDailyGoal, String dailyGoalName) {
        this.numSteps = numSteps;
        this.howMuchDailyGoal = howMuchDailyGoal;
        this.dailyGoalName = dailyGoalName;
    }
}
