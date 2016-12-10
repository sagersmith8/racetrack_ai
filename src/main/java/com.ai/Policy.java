package com.ai;

public interface Policy {
    public Action getAction(State state);
}
