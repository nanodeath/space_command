package com.wasome.space_command.util;

import static org.junit.Assert.*;
import static com.wasome.space_command.util.MathUtil.*;

import org.junit.Test;

import com.wasome.space_command.data.Point;

public class PointUtilTest {
	private static final Point<Float> center = new Point<Float>(1f, 0f);
	private static final Point<Float> other = new Point<Float>(2f, 0f);

	@Test
	public void zeroRotation() {
		Point<Float> rotated = PointUtil.rotateAbout(center, other, 0);
		assertTrue(withinTolerance(2f, rotated.x, 0.001f));
		assertTrue(withinTolerance(0f, rotated.y, 0.001f));
	}

	@Test
	public void eigthRotation() {
		Point<Float> rotated = PointUtil.rotateAbout(center, other, MathUtil.PI / 4);
		assertTrue(withinTolerance((float) Math.sqrt(2d) / 2 + 1, rotated.x, 0.001f));
		assertTrue(withinTolerance((float) Math.sqrt(2d) / 2, rotated.y, 0.001f));
	}

	@Test
	public void quarterRotation() {
		Point<Float> rotated = PointUtil.rotateAbout(center, other, MathUtil.HALF_PI);
		assertTrue(withinTolerance(1f, rotated.x, 0.001f));
		assertTrue(withinTolerance(1f, rotated.y, 0.001f));
	}

	@Test
	public void halfRotation() {
		Point<Float> rotated = PointUtil.rotateAbout(center, other, MathUtil.PI);
		assertTrue(withinTolerance(0f, rotated.x, 0.001f));
		assertTrue(withinTolerance(0f, rotated.y, 0.001f));
	}

	@Test
	public void threeHalvesRotation() {
		Point<Float> rotated = PointUtil.rotateAbout(center, other, MathUtil.THREE_HALVES_PI);
		assertTrue(withinTolerance(1f, rotated.x, 0.001f));
		assertTrue(withinTolerance(-1f, rotated.y, 0.001f));
	}
}
