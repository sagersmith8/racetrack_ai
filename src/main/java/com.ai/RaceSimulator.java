package com.ai;

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
        return null;
    }
}
