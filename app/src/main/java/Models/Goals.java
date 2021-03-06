package Models;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Charlotte on 12/02/2017.
 */

public class Goals {
    // extend dataset observable

    String name;
    int numSteps;
    int stepTarget;
    Boolean active;
    Boolean complete;
    String dateGoal;
    Boolean dayPassed;
   String units;

    /**
     * int myInt = (myBoolean) ? 1 : 0
     * <p>
     * change the boolean to int for the DB
     */

    public Goals() {

    }

    public Goals(String name, int numSteps, int stepTarget, String date) {

        //this.active = active;
        //this.complete = complete;
        this.numSteps = numSteps;
        this.stepTarget = stepTarget;
        this.name = name;
        this.dateGoal = date;
    }

    public void setDayPassed(Boolean dayPassed) {
        this.dayPassed = dayPassed;
    }

    public Boolean getDayPassed() {
        return dayPassed;
    }


    public String getDateGoal() {
        return dateGoal;
    }

    public void setDateGoal(String someDate) {
        //haw = new Date(System.currentTimeMillis());
        //System.currentTimeMillis();
        this.dateGoal = someDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public int getStepTarget() {
        return stepTarget;
    }

    public void setStepTarget(int stepTarget) {
        this.stepTarget = stepTarget;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }


    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
