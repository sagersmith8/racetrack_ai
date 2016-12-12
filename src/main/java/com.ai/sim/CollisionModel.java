package com.ai.sim;

import com.ai.Racetrack;
import com.ai.model.State;
import com.ai.model.Position;
import com.ai.model.Velocity;

/**
 * An interface for handling what physically happens when you try to apply a velocity
 * on a given position. This completely encapsulates handling collision.
 */
public interface CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity);
}
