package com.wasome.space_command.components;

import static com.wasome.space_command.util.PointUtil.rotateAbout;

import org.jbox2d.common.Vec2;

import com.wasome.space_command.Component;
import com.wasome.space_command.WorldElement;
import com.wasome.space_command.data.Point;

public abstract class Engine extends Component implements WorldElement {
	protected float maximumThrust;
	// 0.0f-1.0f
	protected float currentThrustPercent = 0f;
	protected float timeToMaximumThrust = 0f;
	protected Direction pointingTowards;
	protected Point<Float> localPosition = Point.NULL_FLOAT;
	protected Point<Float> force = Point.NULL_FLOAT;

	public static enum Direction {
		/**
		 * Forward, front, straight ahead.
		 */
		BOW,
		/**
		 * Backward, behind, aft, reverse.
		 */
		STERN,
		/**
		 * Left, CCW (Counter Clockwise)
		 */
		PORT,
		/**
		 * Right, CW (Clockwise)
		 */
		STARBOARD;

		/**
		 * @param d
		 *            the direction in which the engine is pointed -- the ship
		 *            will move in the opposite direction.
		 * @param magnitude
		 *            the size of the thrust
		 */
		public static final Point<Float> toForce(Direction d, float magnitude) {
			switch (d) {
			case BOW:
				return new Point<Float>(0f, -magnitude);
			case STERN:
				return new Point<Float>(0f, magnitude);
			case PORT:
				return new Point<Float>(magnitude, 0f);
			case STARBOARD:
				return new Point<Float>(-magnitude, 0f);
			}
			return null;
		}
	}

	public Engine() {
	}

	public Engine(Direction d) {
		setFacingDirection(d);
	}

	public void setLocalPosition(Point<Float> point) {
		localPosition = point;
	}

	@Override
	public Point<Float> getLocalPosition() {
		return localPosition;
	}

	public void setFacingDirection(Direction d) {
		pointingTowards = d;
	}

	public Direction getFacingDirection() {
		return pointingTowards;
	}

	/**
	 * The engine location, in world coordinates
	 */
	protected Point<Float> calculateWorldEngineLocation() {
		Point<Float> worldCenter = new Point<Float>(ship.getBody().getX(), ship
				.getBody().getY());
		Point<Float> worldEnginePosition = new Point<Float>(worldCenter.x
				+ localPosition.x, worldCenter.y + localPosition.y);
		Point<Float> rotatedPosition = rotateAbout(worldCenter,
				worldEnginePosition, ship.getRotation(), true);
		return rotatedPosition;
	}

	protected Point<Float> calculateLocalEngineForce() {
		Point<Float> localCenter = new Point<Float>(ship.getBody().getLocalX(),
				ship.getBody().getLocalY());
		Point<Float> localEngineForce = new Point<Float>(force.x, force.y);
		Point<Float> rotatedForce = rotateAbout(localCenter, localEngineForce,
				ship.getRotation(), true);
		return rotatedForce;
	}

	@Override
	public void update() {
		force = Direction.toForce(pointingTowards, currentThrustPercent
				* maximumThrust);
		if (force.x != 0f || force.y != 0f) {
			Point<Float> rotatedPosition = calculateWorldEngineLocation();
			Point<Float> rotatedForce = calculateLocalEngineForce();
			ship.getBody().applyForce(rotatedForce.x, rotatedForce.y,
					rotatedPosition.x, rotatedPosition.y, false);
		}
	}

	/**
	 * 
	 * @param percent
	 *            a float between 0 and 1 (1 being 100%)
	 */
	public void setOutput(float percent) {
		currentThrustPercent = percent;
	}
}
