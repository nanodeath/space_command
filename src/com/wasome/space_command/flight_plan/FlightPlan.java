package com.wasome.space_command.flight_plan;

import java.util.ArrayList;
import java.util.List;

import com.wasome.space_command.Ship;

public class FlightPlan {
	private List<FlightStep> steps = new ArrayList<FlightStep>();
	private int currentStep = 0;
	
	public boolean isValid() {
		return currentStep < steps.size();
	}
	
	public void addStep(FlightStep step){
		steps.add(step);
	}
	
	public void perform(Ship ship){
		FlightStep current = steps.get(currentStep);
		current.setShip(ship);
		current.perform();
		if(current.isDone()){
			currentStep++;
		}
	}
}
