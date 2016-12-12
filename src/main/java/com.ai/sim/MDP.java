package com.ai.sim;

import java.util.List;

import com.ai.model.Action;
import com.ai.model.State;

public interface MDP {
    public List<PotentialState> getNextStates(State state, Action action);
}
