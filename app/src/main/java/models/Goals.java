package models;

/**
 * Created by charl on 09/02/2017.
 */

public class Goals {

    private String goalName;
    private int stepsSoFar;
    private int stepGoal;
    private Boolean acitve;

    public int getStepsSoFar() {
        return stepsSoFar;
    }

    public void setStepsSoFar(int stepsSoFar) {
        this.stepsSoFar = stepsSoFar;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public Boolean getAcitve() {
        return acitve;
    }

    public void setAcitve(Boolean acitve) {
        this.acitve = acitve;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }


}
