package com.ai.alg;

import com.ai.Policy;
import com.ai.Racetrack;
import com.ai.sim.CollisionModel;

/**
 * A template for racetrack learners, which in general need a racetrack and collision model to operate on.
 *
 * Allows learners to be ran iteratively.
 * After calling `next`, calling `getPolicy` and `getIterationCount` will indicate the learner's current policy
 * and how many iterations have been performed respectively. `finished` allows for a learner to indicate
 * when they are done learning.
 */
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
