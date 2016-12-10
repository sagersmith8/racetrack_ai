package com.ai;

public class Collision {
    public static final CollisionModel STOP = new StopCollisionModel();
    public static final CollisionModel RESTART = new RestartCollisionModel();
}

interface CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity);
}

class StopCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
	return null;
    }
}

class RestartCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
	return null;
    }
}
