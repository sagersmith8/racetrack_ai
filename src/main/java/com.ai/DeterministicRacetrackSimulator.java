package com.ai;

public class DeterministicRacetrackSimulator implements ActionSimulator {
    private final Racetrack racetrack;
    private final CollisionModel collisionModel;

    public DeterministicRacetrackSimulator(Racetrack racetrack, CollisionModel collisionModel) {
        this.racetrack = racetrack;
        this.collisionModel = collisionModel;
    }

    public State getNextState(State state, Action action) {
        Velocity prevVelocity = state.getVelocity();
        Velocity nextVelocity = new Velocity(prevVelocity.getX() + action.getXAcceleration(),
                                             prevVelocity.getY() + action.getYAcceleration());

        return collisionModel.getNextState(racetrack, state.getPosition(), nextVelocity);
    }
}
