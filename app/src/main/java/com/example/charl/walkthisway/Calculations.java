package com.example.charl.walkthisway;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class Calculations {


    public final String KM = "km";
    public final String STEPS = "steps";
    public final String MILES = "miles";
    public final String YARDS = "yards";
    public final String METRES = "metres";

    /**
     * Users stride length calculation
     *
     * @return
     */
    public double strideLength() {
        double strideLength = 0.5; // metres
        return strideLength;
    }


    /**
     * users steps to KM calculation
     *
     * @return
     */
    public double stepsToKM(double steps) {
        // steps * stride length / 1000
        double km = 0;
        km = ((steps * strideLength() / 1000));
        return km;
    }

    /**
     * users steps to Miles calculation
     *
     * @return
     */
    public double stepsToMiles(double steps) {
        // steps * stride length / 1000
        double miles = 0;
        miles = ((steps * strideLength() * 0.000621371));
        return miles;
    }


    /**
     * Users steps to Yards calculation
     *
     * @return
     */
    public double stepsToYards(double steps) {
        // steps * stride length / 1000
        double yards = 0;
        yards = ((steps * strideLength() * 1.09361));
        return yards;
    }

    /**
     * Users steps to metres calculation
     *
     * @return
     */
    public double stepsToMetres(double steps) {
        // steps * stride length / 1000
        double yards = 0;
        yards = ((steps * strideLength()));
        return yards;
    }

    /**
     * Users miles to steps calculation
     *
     * @return
     */
    public double milesToSteps(double miles) {
        // steps * stride length / 1000
        double steps = 0;
        steps = ((miles * 0.000621371 / strideLength()));
        return steps;
    }


    /**
     * users KM to steps calculations
     *
     * @return
     */
    public double KMToSteps(double km) {
        // steps * stride length / 1000
        double steps = 0;
        steps = ((km * 1000 / strideLength()));
        return steps;
    }

    /**
     * users KM to steps calculations
     *
     * @return
     */
    public double yardsToSteps(double yards) {
        // steps * stride length / 1000
        double steps = 0;
        steps = ((yards * 1.09361 / strideLength()));
        return steps;
    }

    /**
     * users KM to steps calculations
     *
     * @return
     */
    public double metresToSteps(double metres) {
        // steps * stride length / 1000
        double steps = 0;
        steps = ((metres / strideLength()));
        return steps;
    }

    public double fromUnitsToSteps(String units, double num) {
        double roundNum = Math.round(num*100.0)/100.0;
        switch (units) {
            case KM:
                return KMToSteps(roundNum);
            case MILES:
                return milesToSteps(roundNum);
            case YARDS:
                return yardsToSteps(roundNum);
            case METRES:
                return metresToSteps(roundNum);
            case STEPS:
                return roundNum;
            default:
                return 0.0;
        }

    }

    public double doConversion(String unitOrSteps, double num) {
        double stepConversion = 0;
        if (unitOrSteps == STEPS) {
            stepConversion = fromUnitsToSteps(unitOrSteps, num);
        } else {
            stepConversion = fromStepsToUnits(unitOrSteps, num);
        }

        double roundNum = Math.round(stepConversion*100.0)/100.0;
        return roundNum;
    }

    public double fromStepsToUnits(String units, double num) {
        double roundNum = Math.round(num*100.0)/100.0;
        switch (units) {
            case KM:
                return stepsToKM(roundNum);
            case MILES:
                return stepsToMiles(roundNum);
            case YARDS:
                return stepsToYards(roundNum);
            case METRES:
                return stepsToMetres(roundNum);
            case STEPS:
                return roundNum;
            default:
                return 0.0;
        }
    }
}
