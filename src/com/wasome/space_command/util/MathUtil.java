package com.wasome.space_command.util;

import com.wasome.space_command.data.Point;

public class MathUtil {
	public static final float TWO_PI = (float) (Math.PI * 2);
	public static final float PI = (float) Math.PI;
	public static final float HALF_PI = (float) (Math.PI / 2);
	public static final float THREE_HALVES_PI = (float) (Math.PI * 1.5);

	public static boolean withinTolerance(float float1, float float2,
			float tolerance) {
		if (tolerance < 0) {
			throw new IllegalArgumentException(String.format(
					"Tolerance must be non-negative (was %.2f)", tolerance));
		}
		return Math.abs(float2 - float1) <= tolerance;
	}

	public static float angleBetweenPoints(final Point<Float> pointA,
			final Point<Float> pointB) {
		float xDelta = pointB.x - pointA.x;
		float yDelta = pointB.y - pointA.y;
		if (xDelta == 0) {
			if (yDelta >= 0) {
				return HALF_PI;
			} else {
				return -HALF_PI;
			}
		}
		float angle = (float) Math.atan(yDelta / xDelta);
		if (xDelta >= 0) {
			return angle;
		} else if (yDelta >= 0) {
			return angle + PI;
		} else {
			return angle - PI;
		}
	}
}
