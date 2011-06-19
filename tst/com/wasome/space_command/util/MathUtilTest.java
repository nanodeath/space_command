package com.wasome.space_command.util;

import static com.wasome.space_command.util.MathUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.wasome.space_command.data.Point;

public class MathUtilTest {
	private static final double TOLERANCE = 0.01d;
	private static final float SQRT_3 = (float) Math.sqrt(3);

	@Test
	public void withinToleranceZero() {
		assertTrue(withinTolerance(0f, 0f, 1f));
	}

	@Test
	public void withinToleranceOnBound() {
		assertTrue(withinTolerance(0f, 1f, 1f));
	}

	@Test
	public void notWithinTolerance() {
		assertFalse(withinTolerance(0f, 2f, 1f));
	}

	@Test
	public void withinToleranceZeroTolerance() {
		assertTrue(withinTolerance(5f, 5f, 0f));
	}

	@Test(expected = IllegalArgumentException.class)
	public void withinToleranceNegativeTolerance() {
		withinTolerance(0f, 0f, -1f);
	}

	@Test
	public void zeroDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(1f, 0f));
		assertEquals(0, angle, 0.01d);
	}

	@Test
	public void thirtyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(SQRT_3, 1f));
		assertEquals(Math.PI * 1 / 6, angle, 0.01d);
	}

	@Test
	public void sixtyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(1f, SQRT_3));
		assertEquals(Math.PI * 2 / 6, angle, 0.01d);
	}

	@Test
	public void ninetyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(0f, 1f));
		assertEquals(Math.PI * 3 / 6, angle, 0.01d);
	}

	@Test
	public void oneHundredTwentyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(-1f, SQRT_3));
		assertEquals(Math.PI * 4 / 6, angle, 0.01d);
	}

	@Test
	public void oneHundredFiftyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(-SQRT_3, 1f));
		assertEquals(Math.PI * 5 / 6, angle, 0.01d);
	}

	@Test
	public void oneHundredEightyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(-1f, 0f));
		assertEquals(Math.PI * 6 / 6, angle, 0.01d);
	}

	@Test
	public void twoHundredTenDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(-SQRT_3, -1f));
		assertEquals(-Math.PI * 5 / 6, angle, 0.01d);
	}

	@Test
	public void twoHundredFortyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(-1f, -SQRT_3));
		assertEquals(-Math.PI * 4 / 6, angle, 0.01d);
	}

	@Test
	public void twoHundredSeventyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(0f, -1f));
		assertEquals(-Math.PI * 3 / 6, angle, TOLERANCE);
	}

	@Test
	public void threeHundredDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(1f, -SQRT_3));
		assertEquals(-Math.PI * 2 / 6, angle, TOLERANCE);
	}

	@Test
	public void threeHundredThirtyDegreeAngle() {
		float angle = angleBetweenPoints(new Point<Float>(0f, 0f),
				new Point<Float>(SQRT_3, -1f));
		assertEquals(-Math.PI * 1 / 6, angle, TOLERANCE);
	}
}
