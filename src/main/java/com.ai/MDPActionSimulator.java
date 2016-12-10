package com.ai;

import java.util.List;

public class MDPActionSimulator implements ActionSimulator {
    private MDP mdp;
    
    public MDPActionSimulator(MDP mdp) {
	this.mdp = mdp;
    }

    /**
     * Determines the next state from a given state and action based on
     * a Markov Decision Process.
     *
     * This is done by choosing a random state from the potential states
     * of the MDP, weighted on each potential state's probability.
     *
     * @param state the prev state
     * @param action the prev action
     * @return the next state
     */
    public State getNextState(State state, Action action) {
	List<PotentialState> potentialStates = mdp.getNextStates(state, action);
	double decisionNum = Math.random();

	for (PotentialState potentialState : potentialStates) {
	    decisionNum -= potentialState.getProbability();
	    if (decisionNum <= 0) {
		return potentialState.getState();
	    }
	}

	//Assume floating-point error, so the random number was
	//between the sum of the probabilities of the potential
	//states (which should be one w/o floating point error) and one
	//This indicates the last state should be picked
	return potentialStates.get(potentialStates.size() - 1).getState();
    }
}
