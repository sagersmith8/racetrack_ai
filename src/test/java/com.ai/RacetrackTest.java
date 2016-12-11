package com.ai;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class RacetrackTest {
    public Racetrack readTrackOrFail(String file) {
        try {
            return Racetrack.fromFile(file);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }
    
    @Test
    public void testCanRead() {
        final Racetrack track = readTrackOrFail("small_test_1.txt");
    }

    @Test
    public void testSize() {
        final Racetrack squareTrack = readTrackOrFail("all_safe.txt");
        Assert.assertEquals(5, squareTrack.getWidth());
        Assert.assertEquals(5, squareTrack.getHeight());

        final Racetrack longTrack = readTrackOrFail("windy.txt");
        Assert.assertEquals(3, longTrack.getWidth());
        Assert.assertEquals(7, longTrack.getHeight());
    }

    @Test
    public void testAllSafeSquares() {
        final Racetrack allSafeTrack = readTrackOrFail("all_safe.txt");

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Assert.assertTrue("Expected safe square", allSafeTrack.isSafe(new Position(x, y)));
            }
        }
    }

    @Test
    public void testMixedSafeSquares() {
        final Racetrack track = readTrackOrFail("small_test_2.txt");

        //Test the safe squares
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 5; y++) {
                Assert.assertTrue("Expected safe square", track.isSafe(new Position(x, y)));
            }
        }
        Assert.assertTrue("Expected safe square", track.isSafe(new Position(2, 4)));
        for (int x = 3; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                Assert.assertTrue("Expected safe square", track.isSafe(new Position(x, y)));
            }
        }

        //Test the unsafe squares
        for (int y = 0; y < 4; y++) {
            Assert.assertFalse("Expected unsafe square", track.isSafe(new Position(2, y)));
        }
        Assert.assertFalse("Expected unsafe square", track.isSafe(new Position(3, 0)));
        Assert.assertFalse("Expected unsafe square", track.isSafe(new Position(4, 0)));
    }

    @Test
    public void testOutOfBoundsSquares() {
        final Racetrack track = readTrackOrFail("all_safe.txt");
        for (int x = -1; x < 6; x++) {
            Assert.assertFalse("Expected out-of-bounds square", track.isSafe(new Position(x, -1)));
            Assert.assertFalse("Expected out-of-bounds square", track.isSafe(new Position(x, 6)));
            Assert.assertFalse("Expected out-of-bounds square", track.isSafe(new Position(-1, x)));
            Assert.assertFalse("Expected out-of-bounds square", track.isSafe(new Position(6, x)));
        }
    }

    @Test
    public void testStartSquares() {
        final Racetrack allSafeTrack = readTrackOrFail("all_safe.txt");
        final Set<Position> startingLine = allSafeTrack.startingLine();

        Assert.assertEquals(2, startingLine.size());
        Assert.assertTrue(startingLine.contains(new Position(0, 0)));
        Assert.assertTrue(startingLine.contains(new Position(1, 0)));
    }

    @Test
    public void testFinishSquares() {
        final Racetrack track = readTrackOrFail("small_test_2.txt");
        final Set<Position> finishLine = track.finishLine();

        Assert.assertEquals(2, finishLine.size());
        Assert.assertTrue(finishLine.contains(new Position(3, 1)));
        Assert.assertTrue(finishLine.contains(new Position(4, 1)));
    }
}
