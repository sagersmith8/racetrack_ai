package com.ai.sim;

import com.ai.model.Action;
import com.ai.model.State;

public interface ActionSimulator {
    public State getNextState(State state, Action action);
}
