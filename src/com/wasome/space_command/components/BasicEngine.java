package com.wasome.space_command.components;

public class BasicEngine extends Engine {
	public BasicEngine(Direction d) {
		super(d);
		maximumThrust = 10f;
	}
}
