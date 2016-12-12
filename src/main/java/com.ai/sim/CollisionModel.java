package com.ai.sim;

import com.ai.Racetrack;
import com.ai.model.State;
import com.ai.model.Position;
import com.ai.model.Velocity;

public interface CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity);
}
