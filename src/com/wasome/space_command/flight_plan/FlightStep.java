package com.wasome.space_command.flight_plan;

import com.wasome.space_command.Ship;

public interface FlightStep {
	public boolean isDone();
	public void perform();
	public void setShip(Ship s);
}
