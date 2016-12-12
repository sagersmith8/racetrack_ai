package com.ai.sim;

import com.ai.Racetrack;
import com.ai.model.Action;
import com.ai.model.State;
import com.ai.model.Velocity;

/**
 * A simulator for deciding what deterministically happens on a racetrack.
 */
public class DeterministicRacetrackSimulator implements ActionSimulator {
    private final Racetrack racetrack;
    private final CollisionModel collisionModel;

    /**
     * Makes a deterministic racetrack simulator for a given racetrack and collision model.
     *
     * @param racetrack the racetrack to simulate on
     * @param collisionModel the model to use for collisions
     */
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
