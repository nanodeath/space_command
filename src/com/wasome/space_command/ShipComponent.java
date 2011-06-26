package com.wasome.space_command;

import com.wasome.space_command.data.Point;

abstract public class ShipComponent extends Entity {
	protected Ship ship;

	public Point<Float> getLocalPosition(){
		return Point.ORIGIN_FLOAT;
	}

	public void setShip(Ship s){
		this.ship = s;
	}
}
