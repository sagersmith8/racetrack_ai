package com.ai.sim;

import com.ai.Racetrack;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;

/**
 * Utility for handling common collision models and determining collision.
 */
public class Collision {
    /* The two main collision models:
     *   STOP    - stop moving when you hit a wall
     *   RESTART - stop moving and go back to the track's start when you hit a wall
     */
    public static final CollisionModel STOP = new StopCollisionModel();
    public static final CollisionModel RESTART = new RestartCollisionModel();

    /**
     * Performs collision pathing, following a move from a given position.
     *
     * Essentially follows the movement of the agent as if they were infinitely small,
     * and returns the position that the agent ends up at.
     *
     * @param racetrack the racetrack to path on
     * @param position the starting position of the agent
     * @param velocity the agent's velocity
     * @return the agent's resulting position or null if the agent reached the finish line
     */
    static Position followMove(Racetrack racetrack, Position position, Velocity velocity) {
        //using a decimal representation that starts an agent in the center of each cell
        double currentX = position.getX() + 0.5;
        double currentY = position.getY() + 0.5;

        double xRate = 1.0;
        double yRate = 1.0;

        //decide how to move the agent based on which direction the agent is moving most
        if (Math.abs(velocity.getX()) > Math.abs(velocity.getY())) {
            xRate = Math.signum(velocity.getX());
            yRate = velocity.getY() / (double)velocity.getX() * xRate;
        } else {
            yRate = Math.signum(velocity.getY());
            xRate = velocity.getX() / (double)velocity.getY() * yRate;
        }

        Position endPosition = new Position(position.getX() + velocity.getX(),
                                            position.getY() + velocity.getY());
        Position lastPosition = new Position(asWholeNumber(currentX), asWholeNumber(currentY));
        currentX += xRate;
        currentY += yRate;

        //iteratively move the agent towards towards the end position until it reaches the end
        //position, collides with an unsafe square, or goes through an ending position
        while (!lastPosition.equals(endPosition)) {
            Position currentPosition = checkPositions(racetrack, velocity, currentX, currentY);

            if (currentPosition == null) {
                return lastPosition;
            }

            //treat reaching the finish line as a special case so
            //it isn't mistaken as a collision
            if(racetrack.finishLine().contains(currentPosition)) {
                return null;
            }

            currentX += xRate;
            currentY += yRate;

            lastPosition = currentPosition;
        }
        return lastPosition;
    }

    /**
     * Returns the next position to end up at, or null if there is a collision at this position.
     *
     * @param racetrack the racetrack to check position on
     * @param velocity the agent's velocity
     * @param x the agent's x-position at this step
     * @param y the agent's y-position at this step
     */
    private static Position checkPositions(Racetrack racetrack, Velocity velocity, double x, double y) {
        if (isWholeNumber(x)) {
            Position leftPosition = new Position(asWholeNumber(x) - 1, asWholeNumber(y));
            Position rightPosition = new Position(asWholeNumber(x), asWholeNumber(y));

            if (racetrack.isSafe(leftPosition) && racetrack.isSafe(rightPosition)) {
                if (velocity.getX() < 0) {
                    return leftPosition;
                }
                return rightPosition;
            }
        } else if (isWholeNumber(y)) {
            Position bottomPosition = new Position(asWholeNumber(x), asWholeNumber(y) - 1);
            Position topPosition = new Position(asWholeNumber(x), asWholeNumber(y));

            if (racetrack.isSafe(bottomPosition) && racetrack.isSafe(topPosition)) {
                if (velocity.getY() < 0) {
                    return bottomPosition;
                }
                return topPosition;
            }
        } else {
            Position currentPosition = new Position(asWholeNumber(x), asWholeNumber(y));
            if (racetrack.isSafe(currentPosition)) {
                return currentPosition;
            }
        }
        return null;
    }

    private static final double EPSILON = 0.00001;
    /**
     * Determines whether a floating-point number is a whole number,
     * assuming any difference within a small epsilon is due to floating-point
     * error and not intended to be different from the nearest whole number.
     *
     * @param num the number to check
     * @return whether the number represents a whole number
     */
    private static boolean isWholeNumber(double num) {
        return Math.abs(Math.round(num) - num) <= EPSILON;
    }

    /**
     * Turn a given coordinate number into a whole number reprsenting a cell index.
     *
     * If the position is close to a whole number, use that whole number.
     * Otherwise, just use the number's floor as normal.
     *
     * @param num the number to round off
     * @return the whole number for this floating-point number
     */
    private static int asWholeNumber(double num) {
        if (isWholeNumber(num)) {
            return (int)Math.round(num);
        }
        return (int)Math.floor(num);
    }
}

/**
 * The collision model that stops moving when a wall is hit.
 */
class StopCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
        Position collisionPosition = Collision.followMove(racetrack, position, velocity);
        Position endPosition = new Position(position.getX() + velocity.getX(),
                                            position.getY() + velocity.getY());

        if (endPosition.equals(collisionPosition)) {
            return new State(endPosition, velocity);
        }

        if (collisionPosition == null) {
            return null;
        }

        return new State(collisionPosition, new Velocity(0, 0));
    }

    @Override
    public String toString() {
        return "stop model";
    }
}

/**
 * The collision model that returns to the start when a wall is hit.
 */
class RestartCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
        Position collisionPosition = Collision.followMove(racetrack, position, velocity);
        Position endPosition = new Position(position.getX() + velocity.getX(),
                                            position.getY() + velocity.getY());

        if (endPosition.equals(collisionPosition)) {
            return new State(endPosition, velocity);
        }

        if (collisionPosition == null) {
            return null;
        }

        return new State(racetrack.startingLine().iterator().next(), new Velocity(0, 0));
    }

    @Override
    public String toString() {
        return "restart model";
    }
}
