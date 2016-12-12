package com.ai.sim;

import java.util.List;

import com.ai.model.Action;
import com.ai.model.State;

/**
 * An interface for representing a Markov Decision Process.
 *
 * Assumes that the state and action spaces are already well-known,
 * and gives the states that can be observed by performing a particular
 * action in a particular state and associated probabilities.
 */
public interface MDP {
    public List<PotentialState> getNextStates(State state, Action action);
}
