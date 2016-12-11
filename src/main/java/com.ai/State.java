package com.ai;

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

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Position)) {
            return false;
        }
        Position other = (Position)o;   
        return this.x == other.x &&
               this.y == other.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
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

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Velocity)) {
            return false;
        }
        Velocity other = (Velocity)o;
        return this.x == other.x &&
               this.y == other.y;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ">";
    }
}
