package com.ai;

import com.ai.model.Action;
import com.ai.model.State;

public interface Policy {
    public Action getAction(State state);
}
