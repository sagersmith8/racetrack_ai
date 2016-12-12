package com.ai.alg;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import com.ai.Racetrack;
import com.ai.Policy;
import com.ai.model.Action;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;
import com.ai.sim.CollisionModel;
import com.ai.sim.MDP;
import com.ai.sim.PotentialState;
import com.ai.sim.RacetrackMDP;

public class ValueIteration extends RacetrackLearner {
    private static final Logger logger = Logger.getLogger(ValueIteration.class);

    private MDP mdp;

    private double[][][][] utility;
    private Action[][][][] bestActions;

    private boolean finished = false;
    private int iterationCount = 0;
    private Policy policy = new ValueIterationPolicy();

    private static final double GAMMA = 0.7;
    private static final double EPSILON = 0.01;
    
    public ValueIteration(Racetrack racetrack, CollisionModel collisionModel) {
	super(racetrack, collisionModel);

	mdp = new RacetrackMDP(racetrack, collisionModel);
	utility = new double[racetrack.getWidth()][racetrack.getHeight()][11][11];

	initializeUtilities();
    }

    private void initializeUtilities() {
	//set random values for the initial utilities
	for (int x = 0; x < racetrack.getWidth(); x++) {
	    for (int y = 0; y < racetrack.getHeight(); y++) {
		for (int vx = -5; vx <= 5; vx++) {
		    for (int vy = -5; vy <= 5; vy++) {
			utility[x][y][vx + 5][vy + 5] = Math.random();
		    }
		}
	    }
	}
    }

    @Override
    public void next() {
	if (finished) {
	    return;
	}
	
	double maxDelta = 0;
	double[][][][] nextUtility = new double[racetrack.getWidth()][racetrack.getHeight()][11][11];

	for (int x = 0; x < racetrack.getWidth(); x++) {
	    for (int y = 0; y < racetrack.getHeight(); y++) {
		Position position = new Position(x, y);

		if (!racetrack.isSafe(position) ||racetrack.finishLine().contains(position)) {
		    continue;
		}
		iterationCount += 121 * 9;
		for (int vx = -5; vx <= 5; vx++) {
		    for (int vy = -5; vy <= 5; vy++) {
			State state = new State(position, new Velocity(vx, vy));


			double bestExpectedUtility = Double.NEGATIVE_INFINITY;
			Action bestAction = null;
			for (int ax = -1; ax <= 1; ax++) {
			    for (int ay = -1; ay <= 1; ay++) {
				Action testAction = new Action(ax, ay);
				double expectedUtility = expectedUtility(state, testAction);

				if (expectedUtility > bestExpectedUtility) {
				    bestExpectedUtility = expectedUtility;
				    bestAction = testAction;
				}
				
			    }
			}
			bestActions[x][y][vx + 5][vy + 5] = bestAction;

			nextUtility[x][y][vx + 5][vy + 5] = -1 + GAMMA * bestExpectedUtility;
			maxDelta = Math.max(maxDelta, Math.abs(nextUtility[x][y][vx + 5][vy + 5] - utility[x][y][vx + 5][vy + 5]));
		    }
		}
	    }
	}
	utility = nextUtility;
	finished = maxDelta < EPSILON * (1 - GAMMA) / GAMMA;
    }

    private double getUtility(State state) {
	//terminal states have utility 0
	if (state == null) {
	    return 0.0;
	}
	
	Position position = state.getPosition();
	Velocity velocity = state.getVelocity();
	
	return utility[position.getX()][position.getY()][velocity.getX() + 5][velocity.getY() + 5];
    }

    private double expectedUtility(State state, Action action) {
	double stateUtility = 0;
	for (PotentialState potentialState : mdp.getNextStates(state, action)) {
	    stateUtility += potentialState.getProbability() * getUtility(potentialState.getState());
	}
	return stateUtility;
    }

    @Override
    public boolean finished() {
	return finished;
    }

    @Override
    public Policy getPolicy() {
	return policy;
    }

    @Override
    public int getIterationCount() {
	return iterationCount;
    }

    class ValueIterationPolicy implements Policy {
	public Action getAction(State state) {
	    Position position = state.getPosition();
	    Velocity velocity = state.getVelocity();
	    
	    return bestActions[position.getX()][position.getY()][velocity.getX() + 5][velocity.getY() + 5];
	}
    }
}
