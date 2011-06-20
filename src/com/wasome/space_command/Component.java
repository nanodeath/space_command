package com.wasome.space_command;

import com.wasome.space_command.data.Point;

abstract public class Component implements WorldUpdatable {
	protected Ship ship;

	public Point<Float> getLocalPosition(){
		return new Point<Float>(0f, 0f);
	}

	public void setShip(Ship s){
		this.ship = s;
	}
}
