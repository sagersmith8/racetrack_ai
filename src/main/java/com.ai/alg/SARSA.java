package com.ai.alg;

import com.ai.Main;
import com.ai.Policy;
import com.ai.Racetrack;
import com.ai.model.Action;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;
import com.ai.sim.CollisionModel;
import com.ai.sim.MDPActionSimulator;
import com.ai.sim.RaceSimulator;
import com.ai.sim.RacetrackMDP;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the SARSA algorithm using an off-line training algorithm.
 */
public class SARSA extends RacetrackLearner {
    private static final double LEARNING_RATE = 0.5;
    private static final double GAMMA = 0.7;
    private static final int TIMES_TO_VISIT = 30;

    private static final Logger logger = Logger.getLogger(Main.class);

    private int iterationCount = 0;
    private Map<State, Map<Action, Double>> qTable = new HashMap<>();
    private Map<State, Integer> timesVisited = new HashMap<>();
    private MDPActionSimulator aSim;
    private Policy policy;
    private int iterationLimit = Integer.MAX_VALUE;

    public SARSA(Racetrack racetrack, CollisionModel collisionModel) {
        super(racetrack, collisionModel);

        aSim = new MDPActionSimulator(new RacetrackMDP(racetrack, collisionModel));
        policy = new SARSAPolicy();

        iterationLimit = racetrack.getWidth()*racetrack.getHeight()*121;
    }

    class SARSAPolicy implements Policy {
        /**
         * Returns an action based on a given state, using epsilon greedy
         *
         * @param state the state to act within
         * @return the action to take in the given state
         */
        @Override
        public Action getAction(State state) {
            // get epsilon
            if (Math.random() > (double)timesVisited.getOrDefault(state,0)/TIMES_TO_VISIT) {
                return getRandomAction();
            }

            double bestCost = Double.NEGATIVE_INFINITY;
            Action argMax = null;
	    if (!qTable.containsKey(state)) {
		qTable.put(state, randomActionMap());
	    }

            Map<Action, Double> actions = qTable.get(state);
            for(Action action : actions.keySet()) {
                if(actions.get(action) > bestCost) {
                    argMax = action;
                    bestCost = actions.get(action);
                }
            }

            return argMax;
        }
        /**
         * Returns a random valid action
         *
         * @return The randomly chosen action to take
         */
        public Action getRandomAction() {
            return new Action((int)(Math.random()*3)-1, (int)(Math.random()*3)-1);
        }
    }

    private int count = 0;
    /**
     * Does an increment of learning, starting runs in random states until a run
     * reaches a finishing state, and then applies SARSA to the successful run.
     */
    @Override
    public void next() {
        int xPos, yPos, xVel, yVel;
        Position curPos;
        Velocity curVel;
        State curState;
        Action curAction;
        List<Action> actions = new ArrayList<>();
        List<State> states = new ArrayList<>();
	do {
	    //determine random starting location and velocity
	    do {
		xPos = (int)(Math.random()*racetrack.getWidth());
		yPos = (int)(Math.random()*racetrack.getHeight());
		curPos = new Position(xPos, yPos);
	    } while(!racetrack.isSafe(curPos) || racetrack.finishLine().contains(curPos));

	    xVel = (int)(Math.random()*12)-5;
	    yVel = (int)(Math.random()*12)-5;

            states.clear();
            actions.clear();

            curVel = new Velocity(xVel, yVel);
            curState = new State(curPos, curVel);

            //while we have not crossed the finish or reached our iteration limit
            for (int i = 0; curState != null && i<iterationLimit; i++) {
                //get next action
                curAction = policy.getAction(curState);
                actions.add(curAction);

                states.add(curState);
                curState = aSim.getNextState(curState, curAction);
            }
	} while (curState != null && count % 2 == 0);

        for (int i = 0; i < states.size(); i++) {
            State state = states.get(i);
            Action action = actions.get(i);
            double nextStateActionUtility = curState == null ? 0 : -1000.0;

            if (i < states.size() - 1) {
                State nextState = states.get(i + 1);
                Action nextAction = actions.get(i + 1);
		if (!qTable.containsKey(nextState)) {
		    qTable.put(nextState, randomActionMap());
		}

                nextStateActionUtility = qTable.get(nextState).get(nextAction);
            }

	    if (!qTable.containsKey(state)) {
		qTable.put(state, randomActionMap());
	    }


	    qTable.get(state).put(action, ((1.0 - LEARNING_RATE) * qTable.get(state).get(action) +
					   LEARNING_RATE * (-1.0 + GAMMA * nextStateActionUtility)));
            timesVisited.put(state, timesVisited.getOrDefault(state, 0) + 1);

        }
        iterationCount += states.size();


	if (count % 10 == 0) {
	    printSamplePolicy();
	    printSamplePolicy();
	}
	count++;
    }

    private Map<Action, Double> randomActionMap() {
	Map<Action, Double> randomMap = new HashMap<>();
	for (int vx = -1; vx <= 1; vx++) {
	    for (int vy = -1; vy <= 1; vy++) {
		randomMap.put(new Action(vx, vy), Math.random());
	    }
	}

	return randomMap;
    }

    private void printSamplePolicy() {
	RaceSimulator raceSimulator = new RaceSimulator(racetrack, collisionModel);
	Position startingPosition = racetrack.randomStartingPosition();

	if (logger.isDebugEnabled()) {
	    logger.debug("Policy Map at Iteration "+getIterationCount());
	    logger.debug(raceSimulator.printPolicyMap(raceSimulator.policyMap(startingPosition, getPolicy())));
	}
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public Policy getPolicy() {
        return policy;
    }

    @Override
    public int getIterationCount() {
        return iterationCount;
    }
}
