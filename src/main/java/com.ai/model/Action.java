package com.ai.model;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(xAcceleration, yAcceleration);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Action)) {
            return false;
        }
        Action other = (Action)o;
        return this.xAcceleration == other.xAcceleration &&
               this.yAcceleration == other.yAcceleration;
    }

    @Override
    public String toString() {
        return "{" + xAcceleration + ", " + yAcceleration + "}";
    }
}
