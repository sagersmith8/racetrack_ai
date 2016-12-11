package com.ai;

public abstract class RacetrackLearner {
    protected Racetrack racetrack;
    protected CollisionModel collisionModel;

    public RacetrackLearner(Racetrack racetrack, CollisionModel collisionModel) {
        this.racetrack = racetrack;
        this.collisionModel = collisionModel;
    }

    public abstract void next();    
    public abstract boolean finished();

    public abstract Policy getPolicy();
    public abstract int getIterationCount();
}
