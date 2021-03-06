package com.wasome.space_command.ships;

import static com.wasome.space_command.util.PointUtil.rotateAbout;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.newdawn.fizzy.Body;
import org.newdawn.slick.Image;

import com.esotericsoftware.kryo.Kryo;
import com.wasome.space_command.Entity;
import com.wasome.space_command.components.Engine;
import com.wasome.space_command.components.Engine.Direction;
import com.wasome.space_command.components.ShipComponent;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.flight_plan.FlightPlan;
import com.wasome.space_command.flight_plan.OrientTowardsPoint;
import com.wasome.space_command.network.SentToClient;
import com.wasome.space_command.network.TakeControl;
import com.wasome.space_command.player.Inventory;
import com.wasome.space_command.util.CollectionUtil;
import com.wasome.space_command.util.WorldElementCollection;

public abstract class Ship extends Entity implements SentToClient {
	protected FlightPlan flightPlan;
	protected Point<Float> size;
	protected Inventory inventory;
	protected int playerId = 1; // TODO this should be fixed, obviously
	protected boolean playerControlling = false;

	{
		setZIndex(0);
	}

	public void init() {
		super.init();
		subEntities = spring.getBean(WorldElementCollection.class);
	}

	public void updateComponents() {
		subEntities.update();
	}

	public void renderComponents() {
		subEntities.render();
	}

	public void performFlightPlan() {
		if (flightPlan != null && flightPlan.isValid()) {
			flightPlan.perform(this);
		}
	}

	public void addComponent(ShipComponent c) {
		c.setShip(this);
		subEntities.addElement(c);
	}

	public Point<Float> getCenter() {
		return new Point<Float>(body.getX(), body.getY());
	}

	public Point<Float> getSize() {
		return size;
	}

	/**
	 * Rotation in radians
	 * 
	 * @return rotation in radians
	 */
	public float getRotation() {
		return body.getRotation();
	}

	private class EngineFacingPredicate implements Predicate {
		private Direction direction;

		public EngineFacingPredicate(final Direction direction) {
			this.direction = direction;
		}

		@Override
		public boolean evaluate(Object object) {
			return ((Engine) object).getFacingDirection() == direction;
		}
	}

	public void turnToAngle(float angle) {
		float difference = Math.abs(body.getRotation() - angle);

		if (difference <= 0.1f) {
			return;
		}

		if (body.getRotation() < angle) {
			turnCounterClockwise();
		} else if (body.getRotation() > angle) {
			turnClockwise();
		}
	}

	private static final Engine[] EMPTY_ENGINE_ARRAY = new Engine[] {};

	protected final List<Engine> getGyroEngines() {
		return CollectionUtil.partition(getEngineComponents(), new EngineFacingPredicate(Direction.GYRO)).get(0);
	}

	protected void turnClockwise() {
		setEngineOutput(-1f, getGyroEngines().toArray(EMPTY_ENGINE_ARRAY));
	}

	protected void turnCounterClockwise() {
		setEngineOutput(1f, getGyroEngines().toArray(EMPTY_ENGINE_ARRAY));
	}

	protected void stopTurning() {
		setEngineOutput(0f, getGyroEngines().toArray(EMPTY_ENGINE_ARRAY));
	}

	protected void accelerate() {
		turnEnginesOnOff(Direction.STERN, Direction.BOW);
	}

	protected void stopAccelerating() {
		turnEnginesOnOff(null, Direction.STERN);
	}

	protected void reverse() {
		turnEnginesOnOff(Direction.BOW, Direction.STERN);
	}

	protected void stopReversing() {
		turnEnginesOnOff(null, Direction.BOW);
	}

	protected void turnEnginesOnOff(Direction turnOn, Direction turnOff) {
		List<List<Engine>> partitionedEngines = CollectionUtil.partition(getEngineComponents(), new EngineFacingPredicate(turnOn), new EngineFacingPredicate(
				turnOff));
		List<Engine> enginesToTurnOn = partitionedEngines.get(0);
		List<Engine> enginesToTurnOff = partitionedEngines.get(1);
		setEngineOutput(1f, enginesToTurnOn.toArray(EMPTY_ENGINE_ARRAY));
		setEngineOutput(0f, enginesToTurnOff.toArray(EMPTY_ENGINE_ARRAY));
	}

	protected void allEnginesOff() {
		setEngineOutput(0f, getEngineComponents().toArray(EMPTY_ENGINE_ARRAY));
	}

	/**
	 * Set the output of all the given engines to the provided value.
	 * 
	 * @param output
	 *            output at which ALL engines should be set
	 * @param engines
	 *            one ore more engines
	 */
	protected void setEngineOutput(float output, Engine... engines) {
		for (Engine engine : engines) {
			engine.setOutput(output);
		}
	}

	public List<Engine> getEngineComponents() {
		List<Engine> engines = new LinkedList<Engine>();
		for (Entity component : subEntities.updatableElements) {
			if (component instanceof Engine) {
				engines.add((Engine) component);
			}
		}
		return engines;
	}
//
//	public void enableDirectControl() {
//		directControlEnabled = true;
//	}
//
//	public void disableDirectControl() {
//		directControlEnabled = false;
//	}

	public Body<Ship> getBody() {
		return body;
	}

	public Point<Float> localToWorld(Point<Float> localPoint) {
		Point<Float> worldCenter = new Point<Float>(body.getX(), body.getY());
		Point<Float> worldEnginePosition = new Point<Float>(worldCenter.x + localPoint.x, worldCenter.y + localPoint.y);
		Point<Float> rotated = rotateAbout(worldCenter, worldEnginePosition, getRotation(), true);
		return rotated;
	}

	abstract protected Image getImage();

	public void goTo(Point<Float> worldPoint) {
		FlightPlan fp = new FlightPlan();
		fp.addStep(new OrientTowardsPoint(worldPoint));
		flightPlan = fp;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getPlayerId() {
		return playerId;
	}

	@Override
	public void writeObjectData(Kryo kryo, ByteBuffer buffer) {
		buffer.putFloat(body.getX());
		buffer.putFloat(body.getY());
		buffer.putFloat(body.getXVelocity());
		buffer.putFloat(body.getYVelocity());
		buffer.putFloat(body.getAngularVelocity());
		buffer.putFloat(body.getRotation());
	}

	@Override
	public void readObjectData(Kryo kryo, ByteBuffer buffer) {
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		if (isNew()) {
			initializeAtLocation(new Point<Float>(x, y));
		} else {
			body.setPosition(x, y);
		}
		body.setVelocity(buffer.getFloat(), buffer.getFloat());
		body.setAngularVelocity(buffer.getFloat());
		body.setRotation(buffer.getFloat());
	}

	/**
	 * Called on the client
	 * @param player
	 */
	public void takeControl(int clientId) {
		if (server != null) {
			server.enableControlOfShip(clientId, this);
		} else if (client != null) {
			client.sendToServer(new TakeControl(this));
		}
	}
	
	public void setIsPlayerControlling(boolean flag){
		playerControlling = flag;
	}
}
