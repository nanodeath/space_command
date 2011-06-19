package com.wasome.space_command.components;

import org.newdawn.fizzy.Body;

import com.wasome.space_command.Component;
import com.wasome.space_command.Ship;
import com.wasome.space_command.WorldElement;
import com.wasome.space_command.data.Point;

public abstract class Engine extends Component implements WorldElement {
	protected float maximumThrust;
	// 0.0f-1.0f
	protected float currentThrustPercent = 0f;
	protected float timeToMaximumThrust = 0f;
	protected Direction pointingTowards;
	private Ship ship;
	
	public static enum Direction {
		BOW,		// forward
		STERN,		// backward
		PORT,		// left/CCW
		STARBOARD;	// right/CW

		/**
		 * @param d the direction in which the ship will be propelled by the engine
		 * @param magnitude the size of the thrust
		 */
		public static final Point<Float> toForce(Direction d, float magnitude){
			switch(d){
			case BOW:
				return new Point<Float>(0f, magnitude);
			case STERN:
				return new Point<Float>(0f, -magnitude);
			case PORT:
				return new Point<Float>(magnitude, 0f);
			case STARBOARD:
				return new Point<Float>(-magnitude, 0f);
			}
			return null;
		}
	}
	
	public Engine(Direction d){
		pointingTowards = d;
	}
	
	public void setShip(Ship s){
		this.ship = s;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		Body body = ship.getBody();
		Point<Float> force = Direction.toForce(pointingTowards, currentThrustPercent * maximumThrust);
		body.applyForce(force.x, force.y);
	}
	
	/**
	 * 
	 * @param percent a float between 0 and 1 (1 being 100%)
	 */
	public void setOutput(float percent){
		currentThrustPercent = percent;
	}
}
