package com.ai;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ResultTest {
    @Test
    public void testMean() {
	Result result = new Result(Arrays.asList(10, 20));
	Assert.assertEquals(15, result.getMean(), 0.0001);

	result = new Result(Arrays.asList(0));
	Assert.assertEquals(0, result.getMean(), 0.0001);
    }

    @Test
    public void testVariance() {
	Result result = new Result(Arrays.asList(10, 20));
	Assert.assertEquals(50, result.getVariance(), 0.0001);

	result = new Result(Arrays.asList(1, 2, 3));
	Assert.assertEquals(1, result.getVariance(), 0.0001);
    }

    @Test
    public void testConfidence() {
	Result result = new Result(Arrays.asList(10, 20));
	Assert.assertEquals(5 * 1.96, result.getConfidence(), 0.0001);
	
	result = new Result(Arrays.asList(1, 2, 3));
	Assert.assertEquals(Math.sqrt(1/3f) * 1.96, result.getConfidence(), 0.0001);
    }
}
