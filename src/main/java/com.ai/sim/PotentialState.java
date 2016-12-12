package com.ai.sim;

import com.ai.model.State;

public class PotentialState {
    private final State state;
    private final double probability;

    public PotentialState(State state, double probability) {
        this.state = state;
        this.probability = probability;
    }

    public State getState() {
        return state;
    }

    public double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return state+"@"+probability;
    }
}
