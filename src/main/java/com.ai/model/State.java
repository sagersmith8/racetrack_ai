package com.ai.model;

import java.util.Objects;

/**
 * An object encapsulating an agent's state a particular time.
 *
 * This consists entirely of a position and a velocity.
 */
public class State {
    private final Position position;
    private final Velocity velocity;

    /**
     * Make a state for a given position and velocity
     *
     * @param position the state's position
     * @param velocity the state's velocity
     */
    public State(Position position, Velocity velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * Gives this state's position.
     *
     * @return the state's position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gives this state's velocity.
     *
     * @return the state's velocity
     */
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
