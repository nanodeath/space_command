package com.wasome.space_command.components;

import com.wasome.space_command.Entity;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.ships.Ship;

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
