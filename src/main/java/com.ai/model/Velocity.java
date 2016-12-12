package com.ai.model;

import java.util.Objects;

public class Velocity {
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
