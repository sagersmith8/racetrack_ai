package com.ai.model;

import java.util.Objects;

/**
 * An immutable position on a racetrack.
 * This is fundametally meaningless without a specific racetrack.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Makes a position at the given coordinates.
     *
     * @param x the x-coordinate of on the racetrack
     * @param y the y-coordinate of on the racetrack
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gives the x-coordinate specified for this position.
     *
     * @return the x-coordinate of this position
     */
    public int getX() {
        return x;
    }

    /**
     * Gives the y-coordinate specified for this position.
     *
     * @return the y-coordinate of this position
     */
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
