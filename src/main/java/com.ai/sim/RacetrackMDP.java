package com.ai.sim;

import java.util.ArrayList;
import java.util.List;

import com.ai.Racetrack;
import com.ai.model.Action;
import com.ai.model.State;

/**
 * Represents the MDP for the racetrack problem.
 *
 * Essentially, this MDP consists of applying the acceleration 80% of the time,
 * and applying no acceleration otherwise.
 */
public class RacetrackMDP implements MDP {
    private final ActionSimulator racetrackSimulator;

    private final double ACTION_SUCCESS_RATE = 0.8;
    private final double ACTION_FAIL_RATE = 1 - ACTION_SUCCESS_RATE;
    private final Action FAIL_ACTION = new Action(0, 0);

    /**
     * Makes an MDP for the given racetrack and collision model.
     *
     * @param racetrack the MDP's racetrack
     * @param collisionModel the MDP's collision model
     */
    public RacetrackMDP(Racetrack racetrack, CollisionModel collisionModel) {
        this.racetrackSimulator = new DeterministicRacetrackSimulator(racetrack, collisionModel);
    }

    /**
     * Gets the potential states that follow performing the given action in the given state, and
     * the probability of each potential state.
     *
     * This is two states: either the action is applied, with probability 0.8, or no acceleration
     * is applied, with probability 0.2. The potential states are calculated using a deterministic
     * racetrack simulator.
     *
     * @param state the current state
     * @param action the current action
     * @return the states that can potentially follow the curent state after performing the action
     */
    public List<PotentialState> getNextStates(State state, Action action) {
        List<PotentialState> nextStates = new ArrayList<>();

        if (FAIL_ACTION.equals(action)) {
            nextStates.add(new PotentialState(racetrackSimulator.getNextState(state, action), 1.0));
        } else {
            nextStates.add(new PotentialState(racetrackSimulator.getNextState(state, action), ACTION_SUCCESS_RATE));
            nextStates.add(new PotentialState(racetrackSimulator.getNextState(state, FAIL_ACTION), ACTION_FAIL_RATE));
        }

        return nextStates;
    }
}
