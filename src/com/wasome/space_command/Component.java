package com.wasome.space_command;

import com.wasome.space_command.data.Point;

abstract public class Component implements WorldUpdatable {
	public Point<Float> getLocalPosition(){
		return new Point<Float>(0f, 0f);
	}
}
