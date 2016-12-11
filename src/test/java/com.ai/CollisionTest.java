package com.ai;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CollisionTest {
    public Racetrack readTrackOrFail(String file) {
        try {
            return Racetrack.fromFile(file);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    @Test
    public void testNoCollision() {
	Racetrack track = readTrackOrFail("all_safe.txt");

	Assert.assertEquals(new Position(3, 0), Collision.followMove(track, new Position(0, 0), new Velocity(3, 0)));
	Assert.assertEquals(new Position(3, 3), Collision.followMove(track, new Position(3, 0), new Velocity(0, 3)));
	Assert.assertEquals(new Position(0, 3), Collision.followMove(track, new Position(3, 3), new Velocity(-3, 0)));
	Assert.assertEquals(new Position(0, 0), Collision.followMove(track, new Position(0, 3), new Velocity(0, -3)));

	Assert.assertEquals(new Position(3, 3), Collision.followMove(track, new Position(0, 0), new Velocity(3, 3)));
	Assert.assertEquals(new Position(0, 3), Collision.followMove(track, new Position(3, 0), new Velocity(-3, 3)));
	Assert.assertEquals(new Position(3, 0), Collision.followMove(track, new Position(0, 3), new Velocity(3, -3)));
	Assert.assertEquals(new Position(0, 0), Collision.followMove(track, new Position(3, 3), new Velocity(-3, -3)));
    }

    @Test
    public void testFinishCollision() {
	Racetrack track = readTrackOrFail("all_safe.txt");
	
	Assert.assertNull(Collision.followMove(track, new Position(0, 0), new Velocity(4, 4)));
	Assert.assertNull(Collision.followMove(track, new Position(2, 2), new Velocity(4, 4)));
    }

    @Test
    public void testEdgeCollision() {
	Racetrack track = readTrackOrFail("all_safe.txt");

	System.out.println("Edge collision");
	Assert.assertEquals(new Position(0, 2), Collision.followMove(track, new Position(2, 2), new Velocity(-5, 0)));
	Assert.assertEquals(new Position(4, 2), Collision.followMove(track, new Position(2, 2), new Velocity(5, 0)));
	Assert.assertEquals(new Position(2, 0), Collision.followMove(track, new Position(2, 2), new Velocity(0, -5)));
	Assert.assertEquals(new Position(2, 4), Collision.followMove(track, new Position(2, 2), new Velocity(0, 5)));
    }
}
