package com.ai.sim;

import com.ai.model.State;

/**
 * Represents a state that may occur in a Markov Decision Process.
 * 
 * Consists of the state that can be observed and the probability
 * of observing that state.
 */
public class PotentialState {
    private final State state;
    private final double probability;

    /**
     * Make a potential state from a state and it's probability of
     * being observed, given some particular state and action.
     *
     * @param state the potential state
     * @param probablity the probability of observing the potential state
     */
    public PotentialState(State state, double probability) {
        this.state = state;
        this.probability = probability;
    }

    /**
     * Gives the state representation of this potential state.
     *
     * @return the potential state
     */
    public State getState() {
        return state;
    }

    /**
     * Gives the probability of this state being observed.
     *
     * @return the probabliity observing this potential state
     */    
    public double getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return state+"@"+probability;
    }
}
