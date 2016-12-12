package com.ai.sim;

import com.ai.Policy;
import com.ai.Racetrack;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;

public class RaceSimulator {
    private final ActionSimulator actionSimulator;
    private final int iterationLimit;

    public RaceSimulator(Racetrack racetrack, CollisionModel collisionModel) {
        this.actionSimulator = new MDPActionSimulator(new RacetrackMDP(racetrack, collisionModel));
        this.iterationLimit = racetrack.getWidth() * racetrack.getHeight() * 121 * 2;
    }

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
}
