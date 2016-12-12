package com.ai.sim;

import com.ai.Racetrack;
import com.ai.model.Position;
import com.ai.model.State;
import com.ai.model.Velocity;

public class Collision {
    public static final CollisionModel STOP = new StopCollisionModel();
    public static final CollisionModel RESTART = new RestartCollisionModel();

    static Position followMove(Racetrack racetrack, Position position, Velocity velocity) {
        double currentX = position.getX() + 0.5;
        double currentY = position.getY() + 0.5;

        double xRate = 1.0;
        double yRate = 1.0;

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

        while (!lastPosition.equals(endPosition)) {
            Position currentPosition = checkPositions(racetrack, velocity, currentX, currentY);

            if (currentPosition == null) {
                return lastPosition;
            }

            if(racetrack.finishLine().contains(currentPosition)) {
                return null;
            }

            currentX += xRate;
            currentY += yRate;

            lastPosition = currentPosition;
        }
        return lastPosition;
    }

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
    private static boolean isWholeNumber(double num) {
        return Math.abs(Math.round(num) - num) <= EPSILON;
    }

    private static int asWholeNumber(double num) {
        if (isWholeNumber(num)) {
            return (int)Math.round(num);
        }
        return (int)Math.floor(num);
    }
}

class StopCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
        Position collisionPosition = Collision.followMove(racetrack, position, velocity);
        Position endPosition = new Position(position.getX() + velocity.getX(),
                                            position.getY() + velocity.getY());

        if (endPosition.equals(collisionPosition)) {
            return new State(endPosition, velocity);
        }

        if (endPosition == null) {
            return null;
        }

        return new State(collisionPosition, new Velocity(0, 0));
    }

    @Override
    public String toString() {
        return "CollisionModel(stop)";
    }
}

class RestartCollisionModel implements CollisionModel {
    public State getNextState(Racetrack racetrack, Position position, Velocity velocity) {
        Position collisionPosition = Collision.followMove(racetrack, position, velocity);
        Position endPosition = new Position(position.getX() + velocity.getX(),
                                            position.getY() + velocity.getY());

        if (endPosition.equals(collisionPosition)) {
            return new State(endPosition, velocity);
        }

        if (endPosition == null) {
            return null;
        }

        return new State(racetrack.startingLine().iterator().next(), new Velocity(0, 0));
    }

    @Override
    public String toString() {
        return "CollisionModel(restart)";
    }
}
