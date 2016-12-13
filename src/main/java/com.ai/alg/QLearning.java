package com.ai.alg;

import com.ai.Policy;
import com.ai.Racetrack;
import com.ai.model.Action;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;
import com.ai.sim.CollisionModel;
import com.ai.sim.MDPActionSimulator;
import com.ai.sim.RacetrackMDP;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QLearning extends RacetrackLearner {
    private static final double LEARNING_RATE = .7;
    private static final double DISCOUNT_FACTOR = .8;
    private static int ITERATION_LIMIT;
    private static final double TIMES_TO_VISIT = 10D;
    private static final Logger logger = Logger.getLogger(QLearning.class);
    public int iterationCount = 0;

    private Map<State, Map<Action, Double>> qTable = new HashMap<>();
    private Map<State, Integer> timesVisited = new HashMap<>();
    MDPActionSimulator mdpActionSimulator;
    QLearningPolicy policy;

    public QLearning(Racetrack racetrack, CollisionModel collisionModel) {
        super(racetrack, collisionModel);

        mdpActionSimulator = new MDPActionSimulator(new RacetrackMDP(racetrack, collisionModel));
        policy = new QLearningPolicy();

        ITERATION_LIMIT = racetrack.getWidth()*racetrack.getHeight()*121*9*2;
    }

    @Override
    public String toString() {
        return "QLearning";
    }

    private class QLearningPolicy implements Policy {
        /**
         * Returns an action based on a given state, using epsilon greedy
         *
         * @param state the state to act within
         * @return the action to take in the given state
         */
        @Override
        public Action getAction(State state) {
            // get epsilon
            if (!timesVisited.containsKey(state)) {
                timesVisited.put(state, 1);
            }

            if (Math.random() > timesVisited.get(state)/TIMES_TO_VISIT) {
                return getRandomAction();
            }

            double bestCost = Double.MAX_VALUE;
            Action argMax = null;
            if (!qTable.containsKey(state)) {
                qTable.put(state, new HashMap<>());
            }
            Map<Action, Double> actions = qTable.get(state);
            for(Action action : actions.keySet()) {
                if(actions.get(action) < bestCost) {
                    argMax = action;
                    bestCost = actions.get(action);
                }
            }

            if (argMax == null) {
                argMax = getRandomAction();
            }

            //logger.info("Policy... State: " + state + " Action:" +argMax + " "+ qTable.get(state).get(argMax));

            return argMax;
        }
        /**
         * Returns a random valid action
         *
         * @return The randomly chosen action to take
         */
        public Action getRandomAction() {
            return new Action(-1+ (int)(Math.random()*3), -1+ (int)(Math.random()*3));
        }
    }

    @Override
    public void next() {
        Position curPos;
        Velocity curVel;
        State currentState;
        Action currentAction = policy.getRandomAction();
        List<Action> actions;
        List<State> states;
        int xPos;
        int yPos;
        do {
            xPos = (int) (Math.random() * racetrack.getWidth());
            yPos = (int) (Math.random() * racetrack.getHeight());
            curPos = new Position(xPos, yPos);
        } while (!racetrack.isSafe(curPos) || racetrack.finishLine().contains(curPos));

        int xVel = -5 + (int)(Math.random()*12);
        int yVel = -5 + (int)(Math.random()*12);

        dowhile:
        do {
            states = new ArrayList<>();
            actions = new ArrayList<>();

            curPos = new Position(xPos, yPos);
            curVel = new Velocity(xVel, yVel);
            currentState = new State(curPos, curVel);

            for (int i = 0; i<ITERATION_LIMIT; i++) {
                states.add(currentState);
                actions.add(currentAction);
                currentAction = policy.getAction(currentState);
                currentState = mdpActionSimulator.getNextState(currentState, currentAction);
                if (currentState == null) {
                    logger.info("QLearning reached the finish line...");
                    break dowhile;
                }
            }

            logger.debug("QLearning reached iteration limit, trying again...");
        } while (true);



        for (int i = 0; i < states.size(); i++) {
            State state = states.get(i);
            Action action = actions.get(i);
            double nextStateActionUtility = currentState == null ? 0 : -1000;

            if (i < states.size() - 1) {
                State nextState = states.get(i + 1);
                Action nextAction = actions.get(i + 1);
                if (!qTable.containsKey(nextState)) {
                    qTable.put(nextState, new HashMap<>());
                }

                if (!qTable.get(nextState).containsKey(nextAction)) {
                    qTable.get(nextState).put(nextAction, Math.random());
                }

                nextStateActionUtility = qTable.get(nextState).get(nextAction);
            }

            int reward = -1;

            if (currentState == null) {
                reward = 0;
            }

            if (!qTable.containsKey(state)) {
                qTable.put(state, new HashMap<>());
            }

            if (!qTable.get(state).containsKey(action)) {
                qTable.get(state).put(action, Math.random());
            }

            //logger.info("Next... State: " + state + " Action:" +action +" " + qTable.get(state).get(action));
            qTable.get(state).put(action, (state!=null? qTable.get(state).get(action): 1) + (LEARNING_RATE * (reward + (DISCOUNT_FACTOR * nextStateActionUtility) - (state!=null ? qTable.get(state).get(action) : 0))));

            if (!timesVisited.containsKey(state)){
                timesVisited.put(state, timesVisited.get(state)+1);
            }
        }
        iterationCount += states.size();
    }

    @Override
    public int getIterationCount() {
        return iterationCount;
    }

    @Override
    public boolean finished() {
        return iterationCount >= ITERATION_LIMIT;
    }

    @Override
    public Policy getPolicy() {
        return policy;
    }
}
