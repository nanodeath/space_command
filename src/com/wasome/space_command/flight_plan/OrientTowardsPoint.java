package com.wasome.space_command.flight_plan;

import static com.wasome.space_command.util.MathUtil.angleBetweenPoints;
import static com.wasome.space_command.util.MathUtil.clampAngleToNormalRange;

import org.jbox2d.common.MathUtils;

import com.wasome.space_command.Ship;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.MathUtil;

public class OrientTowardsPoint implements FlightStep {
	private static final float THRESHOLD = 0.001f;

	private Ship ship;
	private Point<Float> point;

	public OrientTowardsPoint(Point<Float> point) {
		this.point = point;
	}

	@Override
	public void perform() {
		ship.turnToAngle(getDesiredAngle());
	}

	@Override
	public boolean isDone() {
		float currentAngle = ship.getRotation();
		return MathUtil.withinTolerance(currentAngle, getDesiredAngle(),
				THRESHOLD);
	}

	@Override
	public void setShip(Ship s) {
		this.ship = s;
	}

	private float getDesiredAngle() {
		return clampAngleToNormalRange(angleBetweenPoints(ship.getCenter(), point) - MathUtils.HALF_PI);
	}
}
