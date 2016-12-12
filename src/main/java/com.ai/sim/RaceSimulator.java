package com.ai.sim;

import com.ai.Policy;
import com.ai.Racetrack;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;

/**
 * An object for simulating a full race with a particular policy and starting position.
 */
public class RaceSimulator {
    private final ActionSimulator actionSimulator;
    private final Racetrack racetrack;
    private final int iterationLimit;

    /**
     * Makes a race simulator for running races on the given racetrack and collision model.
     *
     * @param racetrack the racetrack to run on
     * @param collisionModel the model for handling collisions
     */
    public RaceSimulator(Racetrack racetrack, CollisionModel collisionModel) {
	this.racetrack = racetrack;
        this.actionSimulator = new MDPActionSimulator(new RacetrackMDP(racetrack, collisionModel));
        this.iterationLimit = racetrack.getWidth() * racetrack.getHeight() * 121;
    }

    /**
     * Runs the given policy from the given start position until it either reaches the finish line
     * or hits an iteration limit.
     *
     * @param start the position to start at
     * @param policy the policy to run
     * @return the total cost (number of moves) to reach the end state
     */
    public Integer runPolicy(Position start, Policy policy) {
        int cost = 0;

        State currentState = new State(start, new Velocity(0, 0));

        while (currentState != null && cost < iterationLimit) {
            currentState = actionSimulator.getNextState(currentState, policy.getAction(currentState));
            cost++;
        }

        if (currentState == null)
            return cost;
        return iterationLimit;
    }

    public int[][] policyMap(Position start, Policy policy) {
	int cost = 0;
	int[][] numVisited = new int[racetrack.getWidth()][racetrack.getHeight()];

        State currentState = new State(start, new Velocity(0, 0));

        while (currentState != null && cost < iterationLimit) {
	    numVisited[currentState.getPosition().getX()][currentState.getPosition().getY()]++;

            currentState = actionSimulator.getNextState(currentState, policy.getAction(currentState));
            cost++;
        }

	return numVisited;
    }

    public String printPolicyMap(int[][] map) {
	StringBuffer mapOutput = new StringBuffer();

	for(int y = 0; y < racetrack.getHeight(); y++) {
	    for (int x = 0; x < racetrack.getWidth(); x++) {
		if (racetrack.isSafe(new Position(x, y))) {
		    mapOutput.append(padNumber(map[x][y]));
		} else {
		    mapOutput.append(" ###");
		}
	    }

	    mapOutput.append("\n");
	}

	return mapOutput.toString();
    }

    private String padNumber(int num) {
	String numStr = "" + num;
	if (numStr.length() > 3) {
	    return " ***";
	}

	return "    ".substring(numStr.length()) + numStr;
    }

    /**
     * Tests whether the given number of iterations would have gone over the iteration limit.
     *
     * @param iterationCount a number of iterations for a run
     * @returns whether that run terminated from the iteration limit
     */
    public boolean atIterationLimit(int iterationCount) {
        return iterationCount >= iterationLimit;
    }
}
