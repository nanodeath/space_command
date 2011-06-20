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
	private Point<Float> localPosition;
	
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
		 * @param d the direction in which the engine is pointed -- the ship will move in the opposite direction.
		 * @param magnitude the size of the thrust
		 */
		public static final Point<Float> toForce(Direction d, float magnitude){
			switch(d){
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
	
	public Engine(Direction d){
		pointingTowards = d;
	}
	
	public void setLocalPosition(Point<Float> point){
		localPosition = point;
	}

	@Override
	public Point<Float> getLocalPosition() {
		return localPosition;
	}
	
	public Direction getFacingDirection(){
		return pointingTowards;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		Point<Float> force = Direction.toForce(pointingTowards, currentThrustPercent * maximumThrust);
		if(force.x != 0f || force.y != 0f){
			Point<Float> localCenter = new Point<Float>(ship.getBody().getLocalX(), ship.getBody().getLocalY());
//			float shipRotation = (float) Math.toRadians(ship.getRotation());
			float shipRotation = ship.getRotation();
			Point<Float> rotatedForce = rotateAbout(localCenter, force, shipRotation);
			Point<Float> rotatedPosition = rotateAbout(localCenter, localPosition, shipRotation);
			ship.getBody().applyForce(-rotatedForce.x, rotatedForce.y, rotatedPosition.x, rotatedPosition.y, true);			
		}
	}
	
	/**
	 * 
	 * @param percent a float between 0 and 1 (1 being 100%)
	 */
	public void setOutput(float percent){
		currentThrustPercent = percent;
	}
}
