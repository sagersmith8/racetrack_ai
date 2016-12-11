package com.ai;

import java.util.HashMap;

public class SARSA implements Policy {
    double learningRate;

    Map<State,Action -> int> qTable = new HashMap<>();

    public SARSA(double learningRate) {
	this.learningRate = learningRate;
    }

    public Action getAction() {

    }
}
