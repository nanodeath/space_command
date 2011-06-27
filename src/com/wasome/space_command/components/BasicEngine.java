package com.wasome.space_command.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Camera;
import com.wasome.space_command.SpaceCommandGame;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;

@Component
@Scope("prototype")
@Visible
public class BasicEngine extends Engine {
	@Autowired
	private Camera camera;
	
	public BasicEngine(){
		maximumThrust = 10f;
	}
	
	public BasicEngine(Direction d) {
		this();
		setFacingDirection(d);
	}

	@Override
	public void render() {
		// Draw engine circle
		Point<Float> rotatedPosition = calculateWorldEngineLocation();
		Graphics g = SpaceCommandGame.getGraphics();
		g.setColor(Color.red);
		Point<Float> rotatedScreen = camera.worldToScreen(rotatedPosition.x, rotatedPosition.y);
		g.fill(new Circle(rotatedScreen.x, rotatedScreen.y, 10f));
		
		// Draw engine vector
		float x1 = rotatedScreen.x, y1 = rotatedScreen.y;
		Point<Float> rotatedWorldForce = calculateLocalEngineForce();
		Point<Float> rotatedScreenForce = camera.scaleWorldToScreen(rotatedWorldForce.x, rotatedWorldForce.y);
		float x2 = x1 - rotatedScreenForce.x, y2 = y1 + rotatedScreenForce.y;
		g.drawGradientLine(x1, y1, Color.red, x2, y2, Color.blue);
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
