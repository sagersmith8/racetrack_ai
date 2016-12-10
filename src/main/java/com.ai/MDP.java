package com.ai;

import java.util.List;

public interface MDP {
    public List<PotentialState> getNextStates(State state, Action action);
}
