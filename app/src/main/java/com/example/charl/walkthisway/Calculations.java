package com.example.charl.walkthisway;

/**
 * Created by Charlotte on 19/03/2017.
 */

public class Calculations {

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
    public double stepsToKM(int steps) {
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
    public double stepsToMiles(int steps) {
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
    public double stepsToYards(int steps) {
        // steps * stride length / 1000
        double yards = 0;
        yards = (((double) steps * strideLength() * 1.09361));
        return yards;
    }

    /**
     * Users steps to metres calculation
     *
     * @return
     */
    public double stepsToMetres(int steps) {
        // steps * stride length / 1000
        double yards = 0;
        yards = (((double) steps * strideLength()));
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
            case "km":
                return KMToSteps(num);
            case "miles":
                return milesToSteps(num);
            case "yards":
                return yardsToSteps(num);
            case "metres":
                return metresToSteps(num);
            case "steps":
                return num;
            default:
                return 0.0;
        }

    }

    public double fromStepsToUnits(String units, double num) {

        switch (units) {
            case "km":
                return KMToSteps(num);
            case "miles":
                return milesToSteps(num);
            case "yards":
                return yardsToSteps(num);
            case "metres":
                return metresToSteps(num);
            case "steps":
                return num;
            default:
                return 0.0;
        }
    }
}
