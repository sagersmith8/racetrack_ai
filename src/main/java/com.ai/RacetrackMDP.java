package com.ai;

import java.util.List;

public class RacetrackMDP implements MDP {
    private final ActionSimulator racetrackSimulator;
    
    public RacetrackMDP(Racetrack racetrack, CollisionModel collisionModel) {
	this.racetrackSimulator = new DeterministicRacetrackSimulator(racetrack, collisionModel);
    }
    
    public List<PotentialState> getNextStates(State state, Action action) {
	return null;
    }
}
