package com.ai;

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
}
