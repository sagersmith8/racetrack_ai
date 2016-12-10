package com.ai;

public class Action {
    private final int xAcceleration;
    private final int yAcceleration;
    
    public Action(int xAcceleration, int yAcceleration) {
	this.xAcceleration = xAcceleration;
	this.yAcceleration = yAcceleration;
    }

    public int getXAcceleration() {
	return xAcceleration;
    }

    public int getYAcceleration() {
	return yAcceleration;
    }

    public boolean isValid() {
	return (-1 <= xAcceleration && xAcceleration <= 1) &&
	       (-1 <= yAcceleration && yAcceleration <= 1);
    }
}
