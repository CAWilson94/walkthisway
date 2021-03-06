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
        double steps = 0;

        switch (units) {
            case KM:
                return KMToSteps(num);
            case MILES:
                return milesToSteps(num);
            case YARDS:
                return yardsToSteps(num);
            case METRES:
                return metresToSteps(num);
            case STEPS:
                return num;
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
        return stepConversion;
    }

    public double fromStepsToUnits(String units, double num) {

        switch (units) {
            case KM:
                return stepsToKM(num);
            case MILES:
                return stepsToMiles(num);
            case YARDS:
                return stepsToYards(num);
            case METRES:
                return stepsToMetres(num);
            case STEPS:
                return num;
            default:
                return 0.0;
        }
    }
}
