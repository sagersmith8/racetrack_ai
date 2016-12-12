package com.ai;

import com.ai.model.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Immutable object for storing basic info about a racetrack.
 */
public class Racetrack {
    private final boolean[][] isSafe;
    private final int width, height;
    private String name = "No name specified";
    
    private final Set<Position> startingLine;
    private final Set<Position> finishLine;

    private Racetrack(boolean[][] isSafe, Set<Position> startingLine, Set<Position> finishLine) {
        this.isSafe = isSafe;
        width = isSafe.length;
        height = isSafe[0].length;

        this.startingLine = Collections.unmodifiableSet(startingLine);
        this.finishLine = Collections.unmodifiableSet(finishLine);
    }

    /**
     * Set the name of this racetrack.
     *
     * @param name the name to set
     * @return the current racetrack
     */
    public Racetrack withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Construct a racetrack from a file.
     *
     * @param filename the name of the file to read
     * @return the constructed racetrack
     */
    public static Racetrack fromFile(String filename) throws IOException {
        return fromStream(Thread.currentThread().getContextClassLoader()
                          .getResourceAsStream(filename));
    }

    /**
     * Construct a racetrack from a stream.
     *
     * @param stream the stream to read from
     * @return the constructed racetrack
     */
    public static Racetrack fromStream(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        String[] size = in.readLine().split(",");
        int height = Integer.parseInt(size[0]);
        int width = Integer.parseInt(size[1]);

        boolean[][] isSafe = new boolean[width][height];
        Set<Position> startingLine = new HashSet<>();
        Set<Position> finishLine = new HashSet<>();

        for (int y = 0; y < height; y++) {
            char[] row = in.readLine().toCharArray();
            for (int x = 0; x < width; x++) {
                isSafe[x][y] = row[x] != '#';

                if (row[x] == 'S') {
                    startingLine.add(new Position(x, y));
                } else if (row[x] == 'F') {
                    finishLine.add(new Position(x, y));
                }
            }
        }

        return new Racetrack(isSafe, startingLine, finishLine);
    }

    @Override
    public String toString() {
        return "Racetrack("+name+")";
    }

    /**
     * Determines whether the given position is safe to be on.
     *
     * @param position the position to check
     * @return whether or not the position is safe
     */
    public boolean isSafe(Position position) {
        if (position.getX() < 0 || position.getX() >= width ||
            position.getY() < 0 || position.getY() >= height)
            return false;
        return isSafe[position.getX()][position.getY()];
    }

    /**
     * Gives the racetrack's width
     *
     * @return the width of the racetrack
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gives the racetrack's height
     *
     * @return the height of the racetrack
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gives all the positions that are starting positions on this racetrack.
     *
     * @return the set of starting positions
     */
    public Set<Position> startingLine() {
        return startingLine;
    }

    /**
     * Gives a random starting position; useful for avoiding dealing with getting
     * random elements from the set of starting positions.
     *
     * @return a random starting position for this racetrack
     */
    public Position randomStartingPosition() {
        Iterator<Position> iter = startingLine.iterator();
        int randomIndex = (int)(Math.random() * startingLine.size());

        for(; randomIndex != 0; randomIndex--) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * Gives all the positions that are ending positions on this racetrack.
     *
     * @return the set of ending positions
     */
    public Set<Position> finishLine() {
        return finishLine;
    }
}
