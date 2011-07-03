package com.wasome.space_command.weapons;

import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.wasome.space_command.Camera;
import com.wasome.space_command.behavior.HasBody;
import com.wasome.space_command.behavior.Visible;
import com.wasome.space_command.data.Point;
import com.wasome.space_command.util.Timer;
import com.wasome.space_command.util.Timer.Task;

@Component
@Scope("prototype")
@Visible
@HasBody
public class BasicBullet extends Projectile {
	@Autowired
	private Camera camera;
	
	@Autowired
	private Timer timer;
	
	private static final Point<Float> size = new Point<Float>(0.1f, 0.1f); 
	private Point<Float> screenSize;
	
	@Override
	public void initializeAtLocation(Point<Float> position) {
		Shape rectangle = new Rectangle(size.x, size.y);
		body = new DynamicBody<Projectile>(rectangle, position.x, position.y);
		body.setBullet(true);
		screenSize = camera.scaleWorldToScreen(size.x, size.y);
		timer.registerOnceAlarm(1000, new SelfDestruct());
	}

	@Override
	public void render() {
		Graphics g = game.getGraphics();
		Point<Float> screen = camera.worldToScreen(body.getX(), body.getY());
		g.setColor(Color.white);
		g.fillRect(screen.x, screen.y, screenSize.x, screenSize.y);
	}

	@Override
	public void update() {
		// TODO should hit things
	}

	private boolean isDestroyed = false;
	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public float getInitialSpeed() {
		return 10f;
	}

	private class SelfDestruct implements Task {

		@Override
		public void executeAlarmTask() {
			isDestroyed = true;
			
		}
	}
}
