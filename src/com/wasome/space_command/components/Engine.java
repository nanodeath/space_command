package com.wasome.space_command.components;

import static com.wasome.space_command.util.PointUtil.rotateAbout;

import java.nio.ByteBuffer;

import org.jbox2d.common.MathUtils;

import com.esotericsoftware.kryo.Kryo;
import com.wasome.space_command.Entity;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.network.SentToClient;
import com.wasome.space_command.ships.Ship;

public abstract class Engine extends ShipComponent implements SentToClient {

	protected float maximumThrust;
	// 0.0f-1.0f
	protected float currentThrustPercent = 0f;
	protected float timeToMaximumThrust = 0f;
	protected Direction pointingTowards;
	protected Point<Float> localPosition = Point.ORIGIN_FLOAT;
	protected Point<Float> force = Point.ORIGIN_FLOAT;

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
		STARBOARD,
		/**
		 * Special gyroscope type, can spin ships in place.
		 */
		GYRO;

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
		setZIndex(-1f);
	}

	public Engine(Direction d) {
		setFacingDirection(d);
	}

	public void setLocalPosition(Point<Float> point) {
		server.updateEntityOnClients(this);
		localPosition = point;
	}

	@Override
	public Point<Float> getLocalPosition() {
		return localPosition;
	}

	public void setFacingDirection(Direction d) {
		server.updateEntityOnClients(this);
		pointingTowards = d;
	}

	public Direction getFacingDirection() {
		return pointingTowards;
	}

	/**
	 * The engine location, in world coordinates
	 */
	protected Point<Float> calculateWorldEngineLocation() {
		Point<Float> worldCenter = new Point<Float>(ship.getBody().getX(), ship.getBody().getY());
		Point<Float> worldEnginePosition = new Point<Float>(worldCenter.x + localPosition.x, worldCenter.y + localPosition.y);
		Point<Float> rotatedPosition = rotateAbout(worldCenter, worldEnginePosition, ship.getRotation(), true);
		return rotatedPosition;
	}

	protected Point<Float> calculateLocalEngineForce() {
		Point<Float> localCenter = new Point<Float>(ship.getBody().getLocalX(), ship.getBody().getLocalY());
		Point<Float> localEngineForce = new Point<Float>(force.x, force.y);
		Point<Float> rotatedForce = rotateAbout(localCenter, localEngineForce, ship.getRotation(), true);
		return rotatedForce;
	}

	@Override
	public void update() {
		if (force.x != 0f || force.y != 0f) {
			Point<Float> rotatedPosition = calculateWorldEngineLocation();
			Point<Float> rotatedForce = calculateLocalEngineForce();
			ship.getBody().applyForce(rotatedForce.x, rotatedForce.y, rotatedPosition.x, rotatedPosition.y, false);
		}
	}

	/**
	 * Set the current output of the engine as a percentage.
	 * 
	 * @param percent
	 *            a float between 0 and 1 (1 being 100%)
	 */
	public void setOutput(float percent) {
		System.out.println("Updating thrusters!");
		server.updateEntityOnClients(this);
		currentThrustPercent = MathUtils.clamp(percent, 0f, 1f);
		force = Direction.toForce(pointingTowards, currentThrustPercent * maximumThrust);
	}

	@Override
	public void writeObjectData(Kryo kryo, ByteBuffer buffer) {
		kryo.writeObject(buffer, ship.getClass().getName());
		buffer.putInt(ship.getEntityId());
		buffer.putFloat(maximumThrust);
		buffer.putFloat(currentThrustPercent);
		System.out.println("Current thrust % on server: " + currentThrustPercent);
		buffer.putFloat(timeToMaximumThrust);
		kryo.writeObject(buffer, pointingTowards);
		kryo.writeObject(buffer, localPosition);
		kryo.writeObject(buffer, force);
		System.out.println("Current force on server: " + force.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readObjectData(Kryo kryo, ByteBuffer buffer) {
		String shipClass = kryo.readObject(buffer, String.class);
		int shipId = buffer.getInt();
		if (isNew()) {
			try {
				ship = (Ship) client.getOrCreateEntity(shipId, (Class<? extends Entity>) Class.forName(shipClass));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		maximumThrust = buffer.getFloat();
		currentThrustPercent = buffer.getFloat();
		System.out.println("Current thrust % on client: " + currentThrustPercent);
		timeToMaximumThrust = buffer.getFloat();
		pointingTowards = kryo.readObject(buffer, Direction.class);
		localPosition = kryo.readObject(buffer, Point.class);
		force = kryo.readObject(buffer, Point.class);
		System.out.println("Current force on client: " + force.toString());
	}
}