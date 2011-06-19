package com.wasome.space_command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.fizzy.Body;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.wasome.space_command.components.Engine;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.flight_plan.FlightPlan;

public abstract class Ship implements WorldElement {
	protected List<Component> allComponents = new ArrayList<Component>();
	protected List<WorldRenderable> visibleComponents = new ArrayList<WorldRenderable>();
	protected Point<Float> center;
	protected float orientation;
	protected FlightPlan flightPlan;
	protected boolean directControlEnabled;
	protected Body body;

	public void updateComponents(GameContainer container, int delta) {
		for (Component component : allComponents) {
			component.update();
		}
	}

	public void renderComponents(Graphics g) {
		for (WorldRenderable visibleComponent : visibleComponents) {
			visibleComponent.render();
		}
	}

	public void addComponent(Component c) {
		allComponents.add(c);
		if (c instanceof WorldRenderable) {
			visibleComponents.add((WorldRenderable) c);
		}
	}

	public Point<Float> getCenter() {
		return center;
	}

	public float getOrientation() {
		return orientation;
	}

	public void turnToAngle(float angle) {
		if (orientation > angle) {
			// turn CCW
		} else if (orientation < angle) {
			// turn CW
		}
	}
	
	public List<Engine> getEngineComponents(){
		List<Engine> engines = new LinkedList<Engine>();
		for(Component component : allComponents){
			if(component instanceof Engine){
				engines.add((Engine) component);
			}
		}
		return engines;
	}
	
	public void enableDirectControl(){
		directControlEnabled = true;
	}
	
	public void disableDirectControl(){
		directControlEnabled = false;
	}

	public Body getBody() {
		return body;
	}
}
