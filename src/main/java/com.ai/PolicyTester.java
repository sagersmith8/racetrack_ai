package com.ai;

import com.ai.sim.CollisionModel;
import com.ai.sim.RaceSimulator;

import java.util.ArrayList;
import java.util.List;

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

    public Result testPolicy(Policy policy) {
        return testPolicy(policy, numTests);
    }

    public Result testPolicy(Policy policy, int numTests) {
        List<Integer> runData = new ArrayList<>();
	boolean terminated = false;

        for (int i = 0; i < numTests; i++) {
	    if (i == EARLY_STOP_TESTS && !terminated)
		break;

	    int runLength = raceSimulator.runPolicy(racetrack.randomStartingPosition(), policy);
	    if (!terminated) {
		terminated = !raceSimulator.atIterationLimit(runLength);
	    }
            runData.add(runLength);
        }
        return new Result(runData);
    }
}

class Result {
    private final List<Integer> data;

    private Double mean, variance, confidence;

    public Result(List<Integer> data) {
        this.data = data;
    }

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

    public double getConfidence() {
        if (confidence == null) {
            double variance = getVariance();

            confidence = Math.sqrt(variance / data.size()) * 1.96;
        }
        return confidence;
    }
}
