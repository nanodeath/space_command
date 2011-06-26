package com.wasome.space_command.weapons;

import org.newdawn.fizzy.Body;

import com.wasome.space_command.WorldElement;
import com.wasome.space_command.data.Point;

abstract public class Projectile implements WorldElement {
	protected Body<Projectile> body;
	
	public Body<Projectile> getBody(){
		return body;
	}
	
	abstract public void initializeAtLocation(Point<Float> worldPosition);

	abstract public float getInitialSpeed();
}
