package com.ai;

import com.ai.sim.CollisionModel;
import com.ai.sim.RaceSimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for testing how well a given policy performs on a given track and collision model.
 */
public class PolicyTester {
    private final Racetrack racetrack;
    private final RaceSimulator raceSimulator;
    private final CollisionModel collisionModel;

    private final int numTests;

    private static final int DEFAULT_NUM_TESTS = 20;
    private static final int EARLY_STOP_TESTS = 10;

    public PolicyTester(Racetrack racetrack, CollisionModel collisionModel) {
        this(racetrack, collisionModel, DEFAULT_NUM_TESTS);
    }

    public PolicyTester(Racetrack racetrack, CollisionModel collisionModel, int numTests) {
        this.racetrack = racetrack;
        this.raceSimulator = new RaceSimulator(racetrack, collisionModel);
        this.numTests = numTests;
        this.collisionModel = collisionModel;
    }

    public CollisionModel collisionModel() {
        return collisionModel;
    }

    /**
     * Test the specified policy with the default number of tests.
     *
     * @param policy the policy to test
     * @return the result of testing the policy
     */
    public Result testPolicy(Policy policy) {
        return testPolicy(policy, numTests);
    }

    /**
     * Test the specified policy with the specified number of tests.
     *
     * If the first `EARLY_STOP_TESTS` all don't terminate, stop the testing early.
     *
     * @param policy the policy to test
     * @return the result of testing the policy
     */
    public Result testPolicy(Policy policy, int numTests) {
        List<Integer> runData = new ArrayList<>();
        boolean terminated = false;

        for (int i = 0; i < numTests; i++) {
            if (i == EARLY_STOP_TESTS && !terminated) {
                break;
            }

            int runLength = raceSimulator.runPolicy(racetrack.randomStartingPosition(), policy);
            if (!terminated) {
                terminated = !raceSimulator.atIterationLimit(runLength);
            }
            runData.add(runLength);
        }
        return new Result(runData);
    }
}

/**
 * Class for a sample of run lengths for a policy and calculating statistics on that sample.
 * It can calculate the mean, variance, and confidence interval for each sample.
 */
class Result {
    private final List<Integer> data;

    private Double mean, variance, confidence;

    /**
     * Make a new result from a list of run lengths.
     *
     * @param data the list of run lengths to generate statistics from
     */
    public Result(List<Integer> data) {
        this.data = data;
    }

    /**
     * Generates or retrieves the mean for the given sample data.
     *
     * @return the mean for the data
     */
    public double getMean() {
        if (mean == null) {
            double sum = 0.0;

            for (Integer datum : data) {
                sum += datum;
            }
            mean = sum / data.size();
        }
        return mean;
    }

    /**
     * Generates or retrieves the variance for the given sample data.
     *
     * @return the variance for the data
     */
    public double getVariance() {
        if (variance == null) {
            double mean = getMean();

            variance = 0.0;
            for (Integer datum : data) {
                variance += Math.pow(datum - mean, 2);
            }
            variance /= data.size() - 1;
        }
        return variance;
    }

    /**
     * Generates or retrieves the 95% confidence interval for the given sample data.
     *
     * @return the confidence interval for the data
     */
    public double getConfidence() {
        if (confidence == null) {
            double variance = getVariance();

            confidence = Math.sqrt(variance / data.size()) * 1.96;
        }
        return confidence;
    }
}
