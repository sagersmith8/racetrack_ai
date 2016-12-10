package com.ai;

public interface ActionSimulator {
    public State getNextState(State state, Action action);
}
