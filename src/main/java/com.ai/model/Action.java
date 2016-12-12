package com.ai.model;

import java.util.Objects;

/**
 * An action that can be performed on a racetrack, consisting of
 * performing an acceleration in both the x and y direction from -1 to 1.
 */
public class Action {
    private final int xAcceleration;
    private final int yAcceleration;

    /**
     * Make an action that tries to perform the specified accelerations.
     *
     * @param xAcceleration the acceleration in the x direction
     * @param yAcceleration the acceleration in the y direction
     */
    public Action(int xAcceleration, int yAcceleration) {
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
    }

    /**
     * Gives the acceleration in the x-direction for this action.
     *
     * @return this action's x-acceleration
     */
    public int getXAcceleration() {
        return xAcceleration;
    }

    /**
     * Gives the acceleration in the y-direction for this action.
     *
     * @return this action's y-acceleration
     */
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
