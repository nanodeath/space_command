package com.wasome.space_command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.newdawn.fizzy.Body;
import org.newdawn.slick.Image;

import com.wasome.space_command.components.Engine;
import com.wasome.space_command.components.Engine.Direction;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.flight_plan.FlightPlan;
import com.wasome.space_command.util.CollectionUtil;

public abstract class Ship implements WorldElement {
	protected List<Component> allComponents = new ArrayList<Component>();
	protected List<WorldRenderable> visibleComponents = new ArrayList<WorldRenderable>();
	protected float orientation;
	protected FlightPlan flightPlan;
	protected boolean directControlEnabled;
	protected Body body;
	protected Point<Float> size;

	public void updateComponents() {
		for (Component component : allComponents) {
			component.update();
		}
	}

	public void renderComponents() {
		for (WorldRenderable visibleComponent : visibleComponents) {
			visibleComponent.render();
		}
	}

	public void addComponent(Component c) {
		c.setShip(this);
		allComponents.add(c);
		if (c instanceof WorldRenderable) {
			visibleComponents.add((WorldRenderable) c);
		}
	}

	public Point<Float> getCenter() {
		return new Point<Float>(body.getX(), body.getY());
	}

	public Point<Float> getSize() {
		return size;
	}
	
	/**
	 * Rotation in radians
	 * @return rotation in radians
	 */
	public float getRotation(){
		return body.getRotation();
	}

	public float getOrientation() {
		return orientation;
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
		float difference = Math.abs(orientation - angle);

		if (difference <= 0.001f) {
			return;
		}

		if (orientation > angle) {
			turnCounterClockwise();
		} else if (orientation < angle) {
			turnClockwise();
		}
	}

	private static final Engine[] EMPTY_ENGINE_ARRAY = new Engine[] {};

	protected void turnClockwise() {
		turnEnginesOnOff(Direction.STARBOARD, Direction.PORT);
	}

	protected void turnCounterClockwise() {
		turnEnginesOnOff(Direction.PORT, Direction.STARBOARD);
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
	
	protected void turnEnginesOnOff(Direction turnOn, Direction turnOff){
		List<List<Engine>> partitionedEngines = CollectionUtil.partition(
				getEngineComponents(), new EngineFacingPredicate(
						turnOn), new EngineFacingPredicate(
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
		for (Component component : allComponents) {
			if (component instanceof Engine) {
				engines.add((Engine) component);
			}
		}
		return engines;
	}

	public void enableDirectControl() {
		directControlEnabled = true;
	}

	public void disableDirectControl() {
		directControlEnabled = false;
	}

	public Body getBody() {
		return body;
	}

	abstract protected Image getImage();
}
