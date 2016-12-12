package com.ai.sim;

import com.ai.model.Action;
import com.ai.model.State;

/**
 * An interface for "simulators" which determine what happens when a particular
 * action is performed in a particular state.
 */
public interface ActionSimulator {
    public State getNextState(State state, Action action);
}
