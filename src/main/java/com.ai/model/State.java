package com.ai.model;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof State)) {
            return false;
        }
        State other = (State)o;
        return Objects.equals(this.position, other.position) &&
               Objects.equals(this.velocity, other.velocity);
    }

    @Override
    public String toString() {
        return "[" + position + ", " + velocity + "]";
    }
}
