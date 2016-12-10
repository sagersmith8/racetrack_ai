package com.ai;

public class State {
    private final Position position;
    private final Velocity velocity;
    
    public State(Position position, Velocity velocity) {
	this.position = position;
	this.velocity = velocity;
    }

    public Position getPosition() {
	return position;
    }

    public Velocity getVelocity() {
	return velocity;
    }
}

class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
}

class Velocity {
    private final int x;
    private final int y;

    public Velocity(int x, int y) {
	//bound the velocity between -5 and 5
	this.x = Math.max(-5, Math.min(x, 5));
	this.y = Math.max(-5, Math.min(y, 5));
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
}
