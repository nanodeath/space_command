package com.wasome.space_command.components;

import com.wasome.space_command.Entity;
import com.wasome.space_command.Ship;
import com.wasome.space_command.data.Point;

abstract public class ShipComponent extends Entity {
	protected Ship ship;

	public Point<Float> getLocalPosition(){
		return Point.ORIGIN_FLOAT;
	}

	public void setShip(Ship s){
		ship = s;
	}
	
	public Ship getShip(){
		return ship;
	}
}
