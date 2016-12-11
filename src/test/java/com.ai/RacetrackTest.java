package com.ai;

import org.junit.Assert;
import org.junit.Test;

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
	Racetrack track = readTrackOrFail("small_test_1.txt");
    }

    @Test
    public void testSafeSquares() {
	Racetrack allSafeTrack = readTrackOrFail("all_safe.txt");
	
	for (int x = 0; x < 5; x++) {
	    for (int y = 0; y < 5; y++) {
		Assert.assertTrue("Expected safe square", allSafeTrack.isSafe(new Position(x, y)));
	    }
	}
    }
}
