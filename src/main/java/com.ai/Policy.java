package com.ai;

import com.ai.model.Action;
import com.ai.model.State;

/**
 * Basic interface to describe a policy. `getAction` describes what action to perform
 * in the specified state.
 *
 * This could involve stocastic factors, so the result from `getAction` can't be cached,
 * as the result may not be deterministic.
 */
public interface Policy {
    public Action getAction(State state);
}
